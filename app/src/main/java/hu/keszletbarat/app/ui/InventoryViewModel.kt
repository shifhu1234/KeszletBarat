package hu.keszletbarat.app.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import hu.keszletbarat.app.data.InventoryRepository
import hu.keszletbarat.app.model.CategoryFilter
import hu.keszletbarat.app.model.InventoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.Normalizer
import java.util.UUID

@Immutable
data class InventoryUiState(
    val items: List<InventoryItem> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: CategoryFilter = CategoryFilter.ALL,
    val lowStockCount: Int = 0,
    val isLoading: Boolean = true,
    val syncError: String? = null
)

class InventoryViewModel(
    private val repository: InventoryRepository = InventoryRepository()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(CategoryFilter.ALL)
    val selectedCategory: StateFlow<CategoryFilter> = _selectedCategory.asStateFlow()

    private val _optimisticOverrides = MutableStateFlow<Map<String, InventoryItem>>(emptyMap())
    private val _optimisticDeletedIds = MutableStateFlow<Set<String>>(emptySet())
    private val _writeError = MutableStateFlow<String?>(null)

    private val mergedStateFlow = combine(
        repository.observeItems(),
        _optimisticOverrides,
        _optimisticDeletedIds,
        _writeError
    ) { syncResult, overrides, deletedIds, writeError ->
        val remoteItems = syncResult.items
        val itemMap = LinkedHashMap<String, InventoryItem>(remoteItems.size + overrides.size)
        for (item in remoteItems) {
            if (item.id !in deletedIds) {
                itemMap[item.id] = item
            }
        }
        for ((id, overrideItem) in overrides) {
            if (id !in deletedIds) {
                val existing = itemMap[id]
                if (existing == null || overrideItem.updatedAt >= existing.updatedAt) {
                    itemMap[id] = overrideItem
                }
            }
        }
        val sorted = itemMap.values.sortedBy { it.name.lowercase() }
        Pair(sorted, syncResult.errorMessage ?: writeError)
    }.flowOn(Dispatchers.Default)

    val uiState: StateFlow<InventoryUiState> = combine(
        mergedStateFlow,
        _searchQuery,
        _selectedCategory
    ) { (allItems, errorMsg), query, category ->
        val normalizedQuery = query.normalizeSearch()
        val lowStockTotal = allItems.count { it.quantity == 0 || it.quantity <= it.minQuantity }

        val filtered = allItems.filter { item ->
            val matchesCategory = when (category) {
                CategoryFilter.ALL -> true
                CategoryFilter.LOW_STOCK -> item.quantity == 0 || item.quantity <= item.minQuantity
                else -> item.category.equals(category.label, ignoreCase = true)
            }
            val matchesQuery = if (normalizedQuery.isEmpty()) {
                true
            } else {
                item.name.normalizeSearch().contains(normalizedQuery)
            }
            matchesCategory && matchesQuery
        }

        InventoryUiState(
            items = filtered,
            searchQuery = query,
            selectedCategory = category,
            lowStockCount = lowStockTotal,
            isLoading = false,
            syncError = errorMsg
        )
    }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InventoryUiState()
        )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(category: CategoryFilter) {
        _selectedCategory.value = category
    }

    fun incrementQuantity(item: InventoryItem) {
        updateItemQuantity(item, item.quantity + 1)
    }

    fun decrementQuantity(item: InventoryItem) {
        if (item.quantity <= 0) return
        updateItemQuantity(item, (item.quantity - 1).coerceAtLeast(0))
    }

    private fun updateItemQuantity(item: InventoryItem, newQuantity: Int) {
        val updated = item.copy(
            quantity = newQuantity,
            updatedAt = System.currentTimeMillis()
        )
        _optimisticOverrides.update { current -> current + (item.id to updated) }
        viewModelScope.launch {
            runCatching {
                repository.updateQuantity(item.id, newQuantity)
                _writeError.value = null
            }.onFailure { e ->
                handleFirestoreError(e)
            }
        }
    }

    fun saveItem(
        existingId: String?,
        name: String,
        category: String,
        quantity: Int,
        unit: String,
        minQuantity: Int
    ) {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) return

        val itemId = existingId ?: UUID.randomUUID().toString()
        val item = InventoryItem(
            id = itemId,
            name = trimmedName,
            category = category,
            quantity = quantity.coerceAtLeast(0),
            unit = unit,
            minQuantity = minQuantity.coerceAtLeast(0),
            updatedAt = System.currentTimeMillis()
        )

        _optimisticDeletedIds.update { current -> current - itemId }
        _optimisticOverrides.update { current -> current + (item.id to item) }
        viewModelScope.launch {
            runCatching {
                repository.upsertItem(item)
                _writeError.value = null
            }.onFailure { e ->
                handleFirestoreError(e)
            }
        }
    }

    fun deleteItem(itemId: String) {
        _optimisticDeletedIds.update { current -> current + itemId }
        _optimisticOverrides.update { current -> current - itemId }
        viewModelScope.launch {
            runCatching {
                repository.deleteItem(itemId)
                _writeError.value = null
            }.onFailure { e ->
                handleFirestoreError(e)
            }
        }
    }

    private fun handleFirestoreError(throwable: Throwable) {
        val firestoreException = throwable as? FirebaseFirestoreException
        _writeError.value = if (firestoreException?.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
            "Firestore hozzáférés megtagadva (PERMISSION_DENIED). Engedélyezd a Rules fülön az olvasást/írást a Firebase Console-ban!"
        } else {
            throwable.localizedMessage ?: "Mentési hiba történt."
        }
    }

    private fun String.normalizeSearch(): String {
        val trimmed = this.trim().lowercase()
        if (trimmed.isEmpty()) return ""
        val normalized = Normalizer.normalize(trimmed, Normalizer.Form.NFD)
        return DIACRITICS_REGEX.replace(normalized, "")
    }

    companion object {
        private val DIACRITICS_REGEX = "\\p{InCombiningDiacriticalMarks}+".toRegex()
    }
}
