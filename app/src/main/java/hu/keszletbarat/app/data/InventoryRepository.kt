package hu.keszletbarat.app.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings
import com.google.firebase.firestore.SetOptions
import hu.keszletbarat.app.model.FilterItem
import hu.keszletbarat.app.model.InventoryItem
import hu.keszletbarat.app.model.InventoryUnits
import hu.keszletbarat.app.model.cleanQuantity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.Executors

data class FirestoreSyncResult(
    val items: List<InventoryItem> = emptyList(),
    val filters: List<FilterItem>? = null,
    val filtersUpdatedAt: Long = 0L,
    val errorMessage: String? = null
)

class InventoryRepository(
    private val firestore: FirebaseFirestore = createConfiguredFirestore()
) {
    private val collection = firestore.collection(COLLECTION_NAME)
    private val snapshotExecutor = Executors.newSingleThreadExecutor()

    fun observeItems(): Flow<FirestoreSyncResult> = callbackFlow {
        trySend(FirestoreSyncResult())

        val registration = collection.addSnapshotListener(snapshotExecutor) { snapshot, error ->
            if (error != null) {
                val message = if (error.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                    "Firestore hozzáférés megtagadva (PERMISSION_DENIED). Engedélyezd a Rules fülön az olvasást/írást a Firebase Console-ban!"
                } else {
                    error.localizedMessage ?: "Szinkronizációs hiba történt."
                }
                trySend(FirestoreSyncResult(errorMessage = message))
                return@addSnapshotListener
            }
            if (snapshot != null) {
                var parsedFilters: List<FilterItem>? = null
                var filtersTimestamp = 0L
                val items = ArrayList<InventoryItem>(snapshot.documents.size)

                for (doc in snapshot.documents) {
                    if (doc.id == FILTER_CONFIG_DOC_ID) {
                        parsedFilters = doc.toFilterListOrNull()
                        filtersTimestamp = (doc.get("updatedAt") as? Number)?.toLong() ?: 0L
                    } else {
                        val item = doc.toInventoryItemOrNull()
                        if (item != null) {
                            items.add(item)
                        }
                    }
                }

                trySend(
                    FirestoreSyncResult(
                        items = items,
                        filters = parsedFilters,
                        filtersUpdatedAt = filtersTimestamp,
                        errorMessage = null
                    )
                )
            }
        }
        awaitClose { registration.remove() }
    }

    suspend fun upsertItem(item: InventoryItem) {
        val updatedItem = item.copy(updatedAt = System.currentTimeMillis())
        val payload = mapOf(
            "name" to updatedItem.name.trim(),
            "category" to updatedItem.category,
            "quantity" to updatedItem.quantity.coerceAtLeast(0.0).cleanQuantity(),
            "unit" to updatedItem.unit,
            "minQuantity" to updatedItem.minQuantity.coerceAtLeast(0.0).cleanQuantity(),
            "updatedAt" to updatedItem.updatedAt
        )
        collection.document(updatedItem.id).set(payload, SetOptions.merge()).await()
    }

    suspend fun updateQuantity(itemId: String, newQuantity: Double) {
        val safeQuantity = newQuantity.coerceAtLeast(0.0).cleanQuantity()
        val updates = mapOf(
            "quantity" to safeQuantity,
            "updatedAt" to System.currentTimeMillis()
        )
        collection.document(itemId).set(updates, SetOptions.merge()).await()
    }

    suspend fun deleteItem(itemId: String) {
        collection.document(itemId).delete().await()
    }

    suspend fun saveFiltersAndMigrateItems(
        filters: List<FilterItem>,
        updatedAt: Long,
        categoryMigrations: Map<String, String>,
        affectedItems: List<InventoryItem>
    ) {
        val batch = firestore.batch()
        val serializedFilters = filters.map {
            val safeId = when (it.id) {
                "__all__" -> FilterItem.ID_ALL
                "__low_stock__" -> FilterItem.ID_LOW_STOCK
                else -> it.id
            }
            mapOf("id" to safeId, "label" to it.label)
        }
        val configPayload = mapOf(
            "filters" to serializedFilters,
            "updatedAt" to updatedAt
        )
        batch.set(collection.document(FILTER_CONFIG_DOC_ID), configPayload)

        if (categoryMigrations.isNotEmpty()) {
            for (item in affectedItems) {
                val targetCategory = categoryMigrations.entries
                    .firstOrNull { it.key.equals(item.category, ignoreCase = true) }
                    ?.value
                if (targetCategory != null && targetCategory != item.category) {
                    val itemUpdates = mapOf(
                        "category" to targetCategory,
                        "updatedAt" to updatedAt
                    )
                    batch.set(collection.document(item.id), itemUpdates, SetOptions.merge())
                }
            }
        }

        batch.commit().await()
    }

    private fun DocumentSnapshot.toFilterListOrNull(): List<FilterItem>? {
        return runCatching {
            val rawList = get("filters") as? List<*> ?: return null
            val parsed = rawList.mapNotNull { entry ->
                val map = entry as? Map<*, *> ?: return@mapNotNull null
                val rawId = map["id"] as? String ?: return@mapNotNull null
                val id = when (rawId) {
                    "__all__" -> FilterItem.ID_ALL
                    "__low_stock__" -> FilterItem.ID_LOW_STOCK
                    else -> rawId
                }
                val label = map["label"] as? String ?: return@mapNotNull null
                if (id.isBlank() || label.isBlank()) null else FilterItem(id = id, label = label)
            }
            if (parsed.isEmpty()) null else ensureRequiredFilters(parsed)
        }.getOrNull()
    }

    private fun ensureRequiredFilters(list: List<FilterItem>): List<FilterItem> {
        val result = list.toMutableList()
        if (result.none { it.isAllFilter }) {
            result.add(0, FilterItem(FilterItem.ID_ALL, "Összes"))
        }
        if (result.none { it.isLowStockFilter }) {
            val insertIdx = if (result.size >= 1) 1 else 0
            result.add(insertIdx, FilterItem(FilterItem.ID_LOW_STOCK, "Kell venni (Kifogyott)"))
        }
        if (result.none { it.isFallbackCategory }) {
            result.add(FilterItem(FilterItem.ID_FALLBACK, "Egyéb"))
        }
        return result.distinctBy { it.id }
    }

    private fun DocumentSnapshot.toInventoryItemOrNull(): InventoryItem? {
        return runCatching {
            val name = getString("name") ?: return null
            val category = getString("category") ?: "Egyéb"
            val unit = getString("unit") ?: InventoryUnits.DB
            val quantity = (get("quantity") as? Number)?.toDouble()?.cleanQuantity() ?: 0.0
            val minQuantity = (get("minQuantity") as? Number)?.toDouble()?.cleanQuantity() ?: 1.0
            val updatedAt = (get("updatedAt") as? Number)?.toLong() ?: System.currentTimeMillis()

            InventoryItem(
                id = id,
                name = name,
                category = category,
                quantity = quantity,
                unit = unit,
                minQuantity = minQuantity,
                updatedAt = updatedAt
            )
        }.getOrNull()
    }

    companion object {
        private const val COLLECTION_NAME = "household_items"
        private const val FILTER_CONFIG_DOC_ID = "sys_filter_config"

        private fun createConfiguredFirestore(): FirebaseFirestore {
            val instance = FirebaseFirestore.getInstance()
            return try {
                val cacheSettings = PersistentCacheSettings.newBuilder()
                    .setSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                    .build()
                val settings = FirebaseFirestoreSettings.Builder()
                    .setLocalCacheSettings(cacheSettings)
                    .build()
                instance.firestoreSettings = settings
                instance
            } catch (_: IllegalStateException) {
                instance
            }
        }
    }
}
