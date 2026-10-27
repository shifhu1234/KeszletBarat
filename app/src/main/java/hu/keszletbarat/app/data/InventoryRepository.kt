package hu.keszletbarat.app.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.PersistentCacheSettings
import com.google.firebase.firestore.SetOptions
import hu.keszletbarat.app.model.InventoryItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class InventoryRepository(
    private val firestore: FirebaseFirestore = createConfiguredFirestore()
) {
    private val collection = firestore.collection(COLLECTION_NAME)

    fun observeItems(): Flow<List<InventoryItem>> = callbackFlow {
        val registration = collection.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val items = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(InventoryItem::class.java)?.copy(id = doc.id)
                }
                trySend(items)
            }
        }
        awaitClose { registration.remove() }
    }

    suspend fun upsertItem(item: InventoryItem) {
        val updatedItem = item.copy(updatedAt = System.currentTimeMillis())
        val payload = mapOf(
            "name" to updatedItem.name.trim(),
            "category" to updatedItem.category,
            "quantity" to updatedItem.quantity.coerceAtLeast(0),
            "unit" to updatedItem.unit,
            "minQuantity" to updatedItem.minQuantity.coerceAtLeast(0),
            "updatedAt" to updatedItem.updatedAt
        )
        collection.document(updatedItem.id).set(payload, SetOptions.merge()).await()
    }

    suspend fun updateQuantity(itemId: String, newQuantity: Int) {
        val safeQuantity = newQuantity.coerceAtLeast(0)
        val updates = mapOf(
            "quantity" to safeQuantity,
            "updatedAt" to System.currentTimeMillis()
        )
        collection.document(itemId).update(updates).await()
    }

    suspend fun deleteItem(itemId: String) {
        collection.document(itemId).delete().await()
    }

    companion object {
        private const val COLLECTION_NAME = "household_items"

        private fun createConfiguredFirestore(): FirebaseFirestore {
            val instance = FirebaseFirestore.getInstance()
            return try {
                val cacheSettings = PersistentCacheSettings.newBuilder()
                    .setSizeBytes( FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
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
