package hu.keszletbarat.app.ui

import android.app.Application
import androidx.compose.runtime.Immutable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import hu.keszletbarat.app.data.AnimationSpeed
import hu.keszletbarat.app.data.AppColorPalette
import hu.keszletbarat.app.data.AppSettings
import hu.keszletbarat.app.data.BackupFormat
import hu.keszletbarat.app.data.FilterPlacement
import hu.keszletbarat.app.data.GestureDirection
import hu.keszletbarat.app.data.InventoryRepository
import hu.keszletbarat.app.data.ItemLayoutMode
import hu.keszletbarat.app.data.ItemSwipeAction
import hu.keszletbarat.app.data.SettingsRepository
import hu.keszletbarat.app.data.ThemeMode
import hu.keszletbarat.app.data.UiScale
import hu.keszletbarat.app.model.FilterItem
import hu.keszletbarat.app.model.InventoryItem
import hu.keszletbarat.app.model.InventoryUnits
import hu.keszletbarat.app.model.SortDirection
import hu.keszletbarat.app.model.SortField
import hu.keszletbarat.app.model.cleanQuantity
import hu.keszletbarat.app.model.normalizeForSearch
import hu.keszletbarat.app.widget.KeszletWidgetManager
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
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

@Immutable
data class InventoryUiState(
    val items: List<InventoryItem> = emptyList(),
    val allItems: List<InventoryItem> = emptyList(),
    val filters: List<FilterItem> = FilterItem.defaultFilters,
    val productCategories: List<FilterItem> = FilterItem.defaultFilters.filter { it.isProductCategory },
    val selectedFilterIds: Set<String> = setOf(FilterItem.ID_ALL),
    val searchQuery: String = "",
    val lowStockCount: Int = 0,
    val isLoading: Boolean = true,
    val syncError: String? = null,
    val settings: AppSettings = AppSettings()
)

private data class MergedDataState(
    val allItems: List<InventoryItem>,
    val filters: List<FilterItem>,
    val errorMessage: String?
)

class InventoryViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = InventoryRepository()
    private val settingsRepository = SettingsRepository(application)

    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilterIds = MutableStateFlow<Set<String>>(setOf(FilterItem.ID_ALL))

    private val _optimisticOverrides = MutableStateFlow<Map<String, InventoryItem>>(emptyMap())
    private val _optimisticDeletedIds = MutableStateFlow<Set<String>>(emptySet())
    private val _optimisticFilters = MutableStateFlow<Pair<Long, List<FilterItem>>?>(null)
    private val _writeError = MutableStateFlow<String?>(null)
    private val _latestAllItems = MutableStateFlow<List<InventoryItem>>(emptyList())

    private val _pendingWidgetAction = MutableStateFlow<String?>(null)
    val pendingWidgetAction: StateFlow<String?> = _pendingWidgetAction.asStateFlow()

    private val mergedStateFlow = combine(
        repository.observeItems(),
        _optimisticOverrides,
        _optimisticDeletedIds,
        _optimisticFilters,
        _writeError
    ) { syncResult, overrides, deletedIds, optimisticFiltersPair, writeError ->
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

        val activeFilters = when {
            optimisticFiltersPair != null && optimisticFiltersPair.first >= syncResult.filtersUpdatedAt -> {
                optimisticFiltersPair.second
            }
            syncResult.filters != null -> {
                syncResult.filters
            }
            optimisticFiltersPair != null -> {
                optimisticFiltersPair.second
            }
            else -> {
                FilterItem.defaultFilters
            }
        }

        val allItemsList = itemMap.values.toList()
        _latestAllItems.value = allItemsList
        KeszletWidgetManager.syncWidgets(getApplication(), allItemsList)

        MergedDataState(
            allItems = allItemsList,
            filters = activeFilters,
            errorMessage = syncResult.errorMessage ?: writeError
        )
    }.flowOn(Dispatchers.Default)

    val uiState: StateFlow<InventoryUiState> = combine(
        mergedStateFlow,
        _searchQuery,
        _selectedFilterIds,
        settingsRepository.settings
    ) { merged, query, selectedIds, currentSettings ->
        val allItems = merged.allItems
        val filters = merged.filters
        val allFilterItem = filters.firstOrNull { it.isAllFilter } ?: FilterItem.defaultFilters.first()

        val filtersDisabled = currentSettings.filterPlacement == FilterPlacement.HIDDEN
        val matchedActiveFilters = if (filtersDisabled) {
            emptyList()
        } else {
            filters.filter { it.id in selectedIds && !it.isAllFilter }
        }

        val effectiveSelectedIds = if (filtersDisabled || matchedActiveFilters.isEmpty() || selectedIds.any { id -> filters.firstOrNull { it.id == id }?.isAllFilter == true }) {
            setOf(allFilterItem.id)
        } else {
            matchedActiveFilters.map { it.id }.toSet()
        }

        val isAllSelected = effectiveSelectedIds.contains(allFilterItem.id)
        val normalizedQuery = query.normalizeForSearch()
        val lowStockTotal = allItems.count { it.isLowStock }

        val filtered = allItems.filter { item ->
            val matchesCategory = if (isAllSelected) {
                true
            } else {
                matchedActiveFilters.any { filter ->
                    if (filter.isLowStockFilter) {
                        item.isLowStock
                    } else {
                        item.category.equals(filter.label, ignoreCase = true)
                    }
                }
            }
            val matchesQuery = if (normalizedQuery.isEmpty()) {
                true
            } else {
                item.normalizedName.contains(normalizedQuery)
            }
            matchesCategory && matchesQuery
        }

        val sorted = sortItems(
            items = filtered,
            field = currentSettings.sortField,
            direction = currentSettings.sortDirection
        )

        InventoryUiState(
            items = sorted,
            allItems = allItems,
            filters = filters,
            productCategories = filters.filter { it.isProductCategory },
            selectedFilterIds = effectiveSelectedIds,
            searchQuery = query,
            lowStockCount = lowStockTotal,
            isLoading = false,
            syncError = merged.errorMessage,
            settings = currentSettings
        )
    }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InventoryUiState(settings = settingsRepository.settings.value)
        )

    private fun sortItems(
        items: List<InventoryItem>,
        field: SortField,
        direction: SortDirection
    ): List<InventoryItem> {
        val favoriteFirst = Comparator<InventoryItem> { a, b ->
            b.isFavorite.compareTo(a.isFavorite)
        }
        val nameAsc = Comparator<InventoryItem> { a, b ->
            a.normalizedName.compareTo(b.normalizedName)
        }
        val fieldComparator: Comparator<InventoryItem> = when (field) {
            SortField.NAME -> if (direction == SortDirection.ASC) {
                nameAsc
            } else {
                Comparator<InventoryItem> { a, b -> b.normalizedName.compareTo(a.normalizedName) }
            }
            SortField.QUANTITY -> if (direction == SortDirection.ASC) {
                Comparator<InventoryItem> { a, b -> a.quantity.compareTo(b.quantity) }
                    .thenComparing(nameAsc)
            } else {
                Comparator<InventoryItem> { a, b -> b.quantity.compareTo(a.quantity) }
                    .thenComparing(nameAsc)
            }
            SortField.UNIT -> if (direction == SortDirection.ASC) {
                Comparator<InventoryItem> { a, b -> a.lowercaseUnit.compareTo(b.lowercaseUnit) }
                    .thenComparing(nameAsc)
            } else {
                Comparator<InventoryItem> { a, b -> b.lowercaseUnit.compareTo(a.lowercaseUnit) }
                    .thenComparing(nameAsc)
            }
            SortField.OPENED -> if (direction == SortDirection.ASC) {
                Comparator<InventoryItem> { a, b -> b.isOpened.compareTo(a.isOpened) }
                    .thenComparing(nameAsc)
            } else {
                Comparator<InventoryItem> { a, b -> a.isOpened.compareTo(b.isOpened) }
                    .thenComparing(nameAsc)
            }
        }
        return items.sortedWith(favoriteFirst.thenComparing(fieldComparator))
    }

    fun handleWidgetAction(action: String?) {
        if (action.isNullOrBlank()) return
        when (action) {
            KeszletWidgetManager.ACTION_OPEN_ALL -> {
                _selectedFilterIds.value = setOf(FilterItem.ID_ALL)
            }
            KeszletWidgetManager.ACTION_OPEN_LOW_STOCK -> {
                _selectedFilterIds.value = setOf(FilterItem.ID_LOW_STOCK)
            }
            KeszletWidgetManager.ACTION_OPEN_ADD_ITEM -> {
                _pendingWidgetAction.value = action
            }
        }
    }

    fun consumeWidgetAction() {
        _pendingWidgetAction.value = null
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onFilterSelected(filterId: String) {
        val filters = uiState.value.filters
        val clickedFilter = filters.firstOrNull { it.id == filterId }
        val allFilterId = filters.firstOrNull { it.isAllFilter }?.id ?: FilterItem.ID_ALL

        if (clickedFilter == null || clickedFilter.isAllFilter || filterId == FilterItem.ID_ALL) {
            _selectedFilterIds.value = setOf(allFilterId)
            return
        }

        _selectedFilterIds.update { current ->
            val withoutAll = current.filterNot { id ->
                id == FilterItem.ID_ALL || id == "__all__" || filters.firstOrNull { it.id == id }?.isAllFilter == true
            }.toMutableSet()

            if (filterId in withoutAll) {
                withoutAll.remove(filterId)
            } else {
                withoutAll.add(filterId)
            }

            if (withoutAll.isEmpty()) {
                setOf(allFilterId)
            } else {
                withoutAll
            }
        }
    }

    fun incrementQuantity(item: InventoryItem) {
        val step = settingsRepository.settings.value.effectiveStepForUnit(item.unit)
        val newQuantity = (item.quantity + step).cleanQuantity()
        updateItemQuantity(item, newQuantity)
    }

    fun decrementQuantity(item: InventoryItem) {
        if (item.quantity <= 0.0) return
        val step = settingsRepository.settings.value.effectiveStepForUnit(item.unit)
        val newQuantity = (item.quantity - step).coerceAtLeast(0.0).cleanQuantity()
        updateItemQuantity(item, newQuantity)
    }

    fun zeroItemQuantity(item: InventoryItem) {
        if (item.quantity <= 0.0) return
        updateItemQuantity(item, 0.0)
    }

    fun toggleItemOpened(item: InventoryItem) {
        val newOpenedState = !item.isOpened
        val updated = item.copy(
            isOpened = newOpenedState,
            updatedAt = System.currentTimeMillis()
        )
        _optimisticOverrides.update { current -> current + (item.id to updated) }
        viewModelScope.launch {
            runCatching {
                repository.updateOpenedState(item.id, newOpenedState)
                _writeError.value = null
            }.onFailure { e ->
                handleFirestoreError(e)
            }
        }
    }

    fun toggleItemFavorite(item: InventoryItem) {
        val newFavoriteState = !item.isFavorite
        val updated = item.copy(
            isFavorite = newFavoriteState,
            updatedAt = System.currentTimeMillis()
        )
        _optimisticOverrides.update { current -> current + (item.id to updated) }
        viewModelScope.launch {
            runCatching {
                repository.updateFavoriteState(item.id, newFavoriteState)
                _writeError.value = null
            }.onFailure { e ->
                handleFirestoreError(e)
            }
        }
    }

    private fun updateItemQuantity(item: InventoryItem, newQuantity: Double) {
        val safeQuantity = newQuantity.coerceAtLeast(0.0).cleanQuantity()
        val shouldResetOpened = safeQuantity <= 0.0 && settingsRepository.settings.value.autoResetOpenedOnZero && item.isOpened
        val newOpenedState = if (shouldResetOpened) false else item.isOpened

        val updated = item.copy(
            quantity = safeQuantity,
            isOpened = newOpenedState,
            updatedAt = System.currentTimeMillis()
        )
        _optimisticOverrides.update { current -> current + (item.id to updated) }
        viewModelScope.launch {
            runCatching {
                repository.updateQuantity(
                    itemId = item.id,
                    newQuantity = safeQuantity,
                    isOpened = if (shouldResetOpened) false else null
                )
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
        quantity: Double,
        unit: String,
        minQuantity: Double,
        isOpened: Boolean,
        isFavorite: Boolean,
        note: String
    ) {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) return

        val safeQty = quantity.coerceAtLeast(0.0).cleanQuantity()
        val effectiveOpened = if (safeQty <= 0.0 && settingsRepository.settings.value.autoResetOpenedOnZero) {
            false
        } else {
            isOpened
        }

        val itemId = existingId ?: UUID.randomUUID().toString()
        val item = InventoryItem(
            id = itemId,
            name = trimmedName,
            category = category,
            quantity = safeQty,
            unit = unit,
            minQuantity = minQuantity.coerceAtLeast(0.0).cleanQuantity(),
            isOpened = effectiveOpened,
            isFavorite = isFavorite,
            note = note.trim(),
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

    fun restoreItem(item: InventoryItem) {
        val restored = item.copy(updatedAt = System.currentTimeMillis())
        _optimisticDeletedIds.update { current -> current - restored.id }
        _optimisticOverrides.update { current -> current + (restored.id to restored) }
        viewModelScope.launch {
            runCatching {
                repository.upsertItem(restored)
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

    fun exportBackupContent(format: BackupFormat): String {
        val items = _latestAllItems.value
        return when (format) {
            BackupFormat.JSON -> {
                val arr = JSONArray()
                for (item in items) {
                    val obj = JSONObject()
                    obj.put("id", item.id)
                    obj.put("name", item.name)
                    obj.put("category", item.category)
                    obj.put("quantity", item.quantity)
                    obj.put("unit", item.unit)
                    obj.put("minQuantity", item.minQuantity)
                    obj.put("isOpened", item.isOpened)
                    obj.put("isFavorite", item.isFavorite)
                    obj.put("note", item.note)
                    arr.put(obj)
                }
                arr.toString(2)
            }
            BackupFormat.CSV -> {
                val sb = StringBuilder()
                sb.appendLine("id,name,category,quantity,unit,minQuantity,isOpened,isFavorite,note")
                for (item in items) {
                    sb.append(escapeCsv(item.id)).append(',')
                    sb.append(escapeCsv(item.name)).append(',')
                    sb.append(escapeCsv(item.category)).append(',')
                    sb.append(item.quantity).append(',')
                    sb.append(escapeCsv(item.unit)).append(',')
                    sb.append(item.minQuantity).append(',')
                    sb.append(item.isOpened).append(',')
                    sb.append(item.isFavorite).append(',')
                    sb.appendLine(escapeCsv(item.note))
                }
                sb.toString()
            }
        }
    }

    fun importBackupContent(rawContent: String): Int {
        val trimmed = rawContent.trim()
        if (trimmed.isEmpty()) return 0

        val parsedItems = if (trimmed.startsWith("[")) {
            parseJsonBackup(trimmed)
        } else {
            parseCsvBackup(trimmed)
        }

        if (parsedItems.isEmpty()) return 0

        val now = System.currentTimeMillis()
        val existingByName = _latestAllItems.value.associateBy { it.exactMatchKey }

        val finalItems = parsedItems.map { imported ->
            val match = existingByName[imported.exactMatchKey]
            imported.copy(
                id = match?.id ?: imported.id,
                updatedAt = now
            )
        }

        _optimisticDeletedIds.update { current ->
            current - finalItems.map { it.id }.toSet()
        }
        _optimisticOverrides.update { current ->
            val map = LinkedHashMap(current)
            for (item in finalItems) {
                map[item.id] = item
            }
            map
        }

        viewModelScope.launch {
            runCatching {
                repository.upsertItemsBatch(finalItems)
                _writeError.value = null
            }.onFailure { e ->
                handleFirestoreError(e)
            }
        }

        return finalItems.size
    }

    private fun parseJsonBackup(jsonText: String): List<InventoryItem> {
        return runCatching {
            val arr = JSONArray(jsonText)
            val result = ArrayList<InventoryItem>(arr.length())
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                val name = obj.optString("name", "").trim()
                if (name.isEmpty()) continue
                val id = obj.optString("id", "").ifBlank { UUID.randomUUID().toString() }
                val category = obj.optString("category", "Hűtő").ifBlank { "Hűtő" }
                val quantity = obj.optDouble("quantity", 1.0).coerceAtLeast(0.0).cleanQuantity()
                val rawUnit = obj.optString("unit", InventoryUnits.DB)
                val unit = if (rawUnit in InventoryUnits.all) rawUnit else InventoryUnits.DB
                val minQuantity = obj.optDouble("minQuantity", 1.0).coerceAtLeast(0.0).cleanQuantity()
                val isOpened = obj.optBoolean("isOpened", false)
                val isFavorite = obj.optBoolean("isFavorite", false)
                val note = obj.optString("note", "").trim()

                result.add(
                    InventoryItem(
                        id = id,
                        name = name,
                        category = category,
                        quantity = quantity,
                        unit = unit,
                        minQuantity = minQuantity,
                        isOpened = isOpened,
                        isFavorite = isFavorite,
                        note = note
                    )
                )
            }
            result
        }.getOrDefault(emptyList())
    }

    private fun parseCsvBackup(csvText: String): List<InventoryItem> {
        return runCatching {
            val lines = csvText.lines().map { it.trim() }.filter { it.isNotEmpty() }
            if (lines.isEmpty()) return emptyList()
            val startIndex = if (lines.first().lowercase().startsWith("id,") || lines.first().lowercase().startsWith("name,")) 1 else 0
            val result = ArrayList<InventoryItem>()
            for (i in startIndex until lines.size) {
                val cols = parseCsvLine(lines[i])
                if (cols.size < 2) continue
                val hasIdCol = cols.size >= 6
                val id = if (hasIdCol) cols[0].ifBlank { UUID.randomUUID().toString() } else UUID.randomUUID().toString()
                val name = (if (hasIdCol) cols.getOrNull(1) else cols.getOrNull(0))?.trim().orEmpty()
                if (name.isEmpty()) continue
                val category = (if (hasIdCol) cols.getOrNull(2) else cols.getOrNull(1))?.trim()?.ifBlank { "Hűtő" } ?: "Hűtő"
                val quantity = (if (hasIdCol) cols.getOrNull(3) else cols.getOrNull(2))?.toDoubleOrNull()?.coerceAtLeast(0.0)?.cleanQuantity() ?: 1.0
                val rawUnit = (if (hasIdCol) cols.getOrNull(4) else cols.getOrNull(3))?.trim() ?: InventoryUnits.DB
                val unit = if (rawUnit in InventoryUnits.all) rawUnit else InventoryUnits.DB
                val minQuantity = (if (hasIdCol) cols.getOrNull(5) else cols.getOrNull(4))?.toDoubleOrNull()?.coerceAtLeast(0.0)?.cleanQuantity() ?: 1.0
                val isOpened = (if (hasIdCol) cols.getOrNull(6) else null)?.toBooleanStrictOrNull() ?: false
                val isFavorite = (if (hasIdCol) cols.getOrNull(7) else null)?.toBooleanStrictOrNull() ?: false
                val note = (if (hasIdCol) cols.getOrNull(8) else null)?.trim().orEmpty()

                result.add(
                    InventoryItem(
                        id = id,
                        name = name,
                        category = category,
                        quantity = quantity,
                        unit = unit,
                        minQuantity = minQuantity,
                        isOpened = isOpened,
                        isFavorite = isFavorite,
                        note = note
                    )
                )
            }
            result
        }.getOrDefault(emptyList())
    }

    private fun escapeCsv(value: String): String {
        val clean = value.replace("\"", "\"\"")
        return if (clean.contains(',') || clean.contains('"') || clean.contains('\n')) {
            "\"$clean\""
        } else {
            clean
        }
    }

    private fun parseCsvLine(line: String): List<String> {
        val result = ArrayList<String>()
        val current = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < line.length) {
            val c = line[i]
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length && line[i + 1] == '"') {
                        current.append('"')
                        i++
                    } else {
                        inQuotes = false
                    }
                } else {
                    current.append(c)
                }
            } else {
                when (c) {
                    '"' -> inQuotes = true
                    ',' -> {
                        result.add(current.toString())
                        current.clear()
                    }
                    else -> current.append(c)
                }
            }
            i++
        }
        result.add(current.toString())
        return result
    }

    fun moveFilterOrder(filterId: String, direction: Int) {
        val current = uiState.value.filters.toMutableList()
        val index = current.indexOfFirst { it.id == filterId }
        if (index == -1) return
        val targetIndex = (index + direction).coerceIn(0, current.lastIndex)
        if (index == targetIndex) return
        val moved = current.removeAt(index)
        current.add(targetIndex, moved)
        persistFilterChanges(newFilters = current, categoryMigrations = emptyMap())
    }

    fun addFilter(label: String) {
        val trimmed = label.trim()
        if (trimmed.isEmpty()) return
        val current = uiState.value.filters.toMutableList()
        if (current.any { it.label.equals(trimmed, ignoreCase = true) }) return

        val newFilter = FilterItem(
            id = "custom_${UUID.randomUUID().toString().take(8)}",
            label = trimmed
        )
        val fallbackIndex = current.indexOfFirst { it.isFallbackCategory }
        if (fallbackIndex != -1) {
            current.add(fallbackIndex, newFilter)
        } else {
            current.add(newFilter)
        }
        persistFilterChanges(newFilters = current, categoryMigrations = emptyMap())
    }

    fun renameFilter(filterId: String, newLabel: String) {
        val trimmed = newLabel.trim()
        if (trimmed.isEmpty()) return
        val current = uiState.value.filters.toMutableList()
        val index = current.indexOfFirst { it.id == filterId }
        if (index == -1) return

        val existingFilter = current[index]
        if (existingFilter.label == trimmed) return
        if (current.any { it.id != filterId && it.label.equals(trimmed, ignoreCase = true) }) return

        val oldLabel = existingFilter.label
        current[index] = existingFilter.copy(label = trimmed)

        val migrations = if (existingFilter.isProductCategory) {
            mapOf(oldLabel to trimmed)
        } else {
            emptyMap()
        }
        persistFilterChanges(newFilters = current, categoryMigrations = migrations)
    }

    fun deleteFilter(filterId: String) {
        val current = uiState.value.filters.toMutableList()
        val target = current.firstOrNull { it.id == filterId } ?: return
        if (!target.isDeletable) return

        current.removeAll { it.id == filterId }
        val fallbackLabel = current.firstOrNull { it.isFallbackCategory }?.label ?: "Egyéb"

        _selectedFilterIds.update { selected ->
            val updated = selected - filterId
            if (updated.isEmpty()) setOf(FilterItem.ID_ALL) else updated
        }

        val migrations = mapOf(target.label to fallbackLabel)
        persistFilterChanges(newFilters = current, categoryMigrations = migrations)
    }

    fun resetFilters() {
        val defaultFilters = FilterItem.defaultFilters
        val validCategoryLabels = FilterItem.defaultCategoryLabels
        val fallbackLabel = "Egyéb"

        val currentAllItems = _latestAllItems.value
        val migrations = LinkedHashMap<String, String>()
        for (item in currentAllItems) {
            if (validCategoryLabels.none { it.equals(item.category, ignoreCase = true) }) {
                migrations[item.category] = fallbackLabel
            }
        }

        val defaultIds = defaultFilters.map { it.id }.toSet()
        _selectedFilterIds.update { selected ->
            val validSelected = selected.intersect(defaultIds)
            if (validSelected.isEmpty()) setOf(FilterItem.ID_ALL) else validSelected
        }

        persistFilterChanges(newFilters = defaultFilters, categoryMigrations = migrations)
    }

    private fun persistFilterChanges(
        newFilters: List<FilterItem>,
        categoryMigrations: Map<String, String>
    ) {
        val now = System.currentTimeMillis()
        _optimisticFilters.value = Pair(now, newFilters)

        val allCurrentItems = _latestAllItems.value
        if (categoryMigrations.isNotEmpty()) {
            _optimisticOverrides.update { currentOverrides ->
                val updatedMap = LinkedHashMap(currentOverrides)
                for (item in allCurrentItems) {
                    val targetCategory = categoryMigrations.entries
                        .firstOrNull { it.key.equals(item.category, ignoreCase = true) }
                        ?.value
                    if (targetCategory != null && targetCategory != item.category) {
                        updatedMap[item.id] = item.copy(
                            category = targetCategory,
                            updatedAt = now
                        )
                    }
                }
                updatedMap
            }
        }

        viewModelScope.launch {
            runCatching {
                repository.saveFiltersAndMigrateItems(
                    filters = newFilters,
                    updatedAt = now,
                    categoryMigrations = categoryMigrations,
                    affectedItems = allCurrentItems
                )
                _writeError.value = null
            }.onFailure { e ->
                handleFirestoreError(e)
            }
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        settingsRepository.setThemeMode(mode)
    }

    fun setColorPalette(palette: AppColorPalette) {
        settingsRepository.setColorPalette(palette)
    }

    fun setFilterPlacement(placement: FilterPlacement) {
        settingsRepository.setFilterPlacement(placement)
    }

    fun setAnimationSpeed(speed: AnimationSpeed) {
        settingsRepository.setAnimationSpeed(speed)
    }

    fun setItemLayoutMode(mode: ItemLayoutMode) {
        settingsRepository.setItemLayoutMode(mode)
    }

    fun setUiScale(scale: UiScale) {
        settingsRepository.setUiScale(scale)
    }

    fun setHideUnopenedBadge(enabled: Boolean) {
        settingsRepository.setHideUnopenedBadge(enabled)
    }

    fun setAutoResetOpenedOnZero(enabled: Boolean) {
        settingsRepository.setAutoResetOpenedOnZero(enabled)
    }

    fun setAutoHideTopBarOnScroll(enabled: Boolean) {
        settingsRepository.setAutoHideTopBarOnScroll(enabled)
    }

    fun setItemSwipeRightAction(action: ItemSwipeAction) {
        settingsRepository.setItemSwipeRightAction(action)
    }

    fun setItemSwipeLeftAction(action: ItemSwipeAction) {
        settingsRepository.setItemSwipeLeftAction(action)
    }

    fun setBackupFormat(format: BackupFormat) {
        settingsRepository.setBackupFormat(format)
    }

    fun setAddItemGesture(direction: GestureDirection) {
        settingsRepository.setAddItemGesture(direction)
    }

    fun setOpenSettingsGesture(direction: GestureDirection) {
        settingsRepository.setOpenSettingsGesture(direction)
    }

    fun setGroupByCategory(enabled: Boolean) {
        settingsRepository.setGroupByCategory(enabled)
    }

    fun setDefaultMinQuantity(value: Double) {
        settingsRepository.setDefaultMinQuantity(value)
    }

    fun setQuantityStep(step: Double) {
        settingsRepository.setQuantityStep(step)
    }

    fun setFractionalOnlyForWeightAndVolume(enabled: Boolean) {
        settingsRepository.setFractionalOnlyForWeightAndVolume(enabled)
    }

    fun setSortField(field: SortField) {
        settingsRepository.setSortField(field)
    }

    fun setSortDirection(direction: SortDirection) {
        settingsRepository.setSortDirection(direction)
    }

    private fun handleFirestoreError(throwable: Throwable) {
        val firestoreException = throwable as? FirebaseFirestoreException
        _writeError.value = if (firestoreException?.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
            "Firestore hozzáférés megtagadva (PERMISSION_DENIED). Engedélyezd a Rules fülön az olvasást/írást a Firebase Console-ban!"
        } else {
            throwable.localizedMessage ?: "Mentési hiba történt."
        }
    }
}
