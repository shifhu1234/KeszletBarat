package hu.keszletbarat.app.ui

import android.os.SystemClock
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.keszletbarat.app.data.AnimationSpeed
import hu.keszletbarat.app.data.AppColorPalette
import hu.keszletbarat.app.data.AppSettings
import hu.keszletbarat.app.data.BackupFormat
import hu.keszletbarat.app.data.FilterPlacement
import hu.keszletbarat.app.data.GestureDirection
import hu.keszletbarat.app.data.ItemLayoutMode
import hu.keszletbarat.app.data.ItemSwipeAction
import hu.keszletbarat.app.data.ThemeMode
import hu.keszletbarat.app.data.UiScale
import hu.keszletbarat.app.model.CommonHouseholdSuggestions
import hu.keszletbarat.app.model.FilterItem
import hu.keszletbarat.app.model.InventoryItem
import hu.keszletbarat.app.model.InventoryUnits
import hu.keszletbarat.app.model.QuickSuggestion
import hu.keszletbarat.app.model.SortDirection
import hu.keszletbarat.app.model.SortField
import hu.keszletbarat.app.model.cleanQuantity
import hu.keszletbarat.app.model.formatQuantity
import hu.keszletbarat.app.widget.KeszletWidgetManager
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.roundToInt

@Immutable
private data class UndoBannerData(
    val itemSnapshot: InventoryItem,
    val message: String,
    val timestamp: Long = SystemClock.uptimeMillis()
)

private enum class SettingsTab(val label: String) {
    APPEARANCE("Megjelenés"),
    BEHAVIOR("Működés"),
    CATEGORIES("Szűrők"),
    BACKUP_EXTRAS("Mentés & Extrák")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    viewModel: InventoryViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pendingWidgetAction by viewModel.pendingWidgetAction.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val baseDensity = LocalDensity.current
    val scaleFactor = uiState.settings.uiScale.scaleFactor
    val scaledDensity = remember(baseDensity, scaleFactor) {
        Density(
            density = baseDensity.density * scaleFactor,
            fontScale = baseDensity.fontScale * scaleFactor
        )
    }

    var activeSheetItem by remember { mutableStateOf<InventoryItem?>(null) }
    var isEditorSheetVisible by rememberSaveable { mutableStateOf(false) }
    var isSettingsSheetVisible by rememberSaveable { mutableStateOf(false) }
    var isEdgeSidebarOpen by rememberSaveable { mutableStateOf(false) }
    var editorOpenSessionCounter by remember { mutableLongStateOf(0L) }
    var collapsedCategories by rememberSaveable { mutableStateOf(emptySet<String>()) }
    var isTopBarHiddenByScroll by remember { mutableStateOf(false) }
    var undoBannerData by remember { mutableStateOf<UndoBannerData?>(null) }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*")
    ) { uri ->
        if (uri != null) {
            runCatching {
                val content = viewModel.exportBackupContent(uiState.settings.backupFormat)
                context.contentResolver.openOutputStream(uri)?.use { out ->
                    out.write(content.toByteArray(Charsets.UTF_8))
                }
                Toast.makeText(
                    context,
                    "Biztonsági mentés sikeresen elmentve!",
                    Toast.LENGTH_SHORT
                ).show()
            }.onFailure {
                Toast.makeText(
                    context,
                    "Hiba a biztonsági mentés írásakor.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            runCatching {
                val content = context.contentResolver.openInputStream(uri)?.use { input ->
                    input.bufferedReader(Charsets.UTF_8).readText()
                }.orEmpty()
                val importedCount = viewModel.importBackupContent(content)
                if (importedCount > 0) {
                    Toast.makeText(
                        context,
                        "$importedCount termék sikeresen importálva!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Nem található érvényes termék a kiválasztott fájlban.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }.onFailure {
                Toast.makeText(
                    context,
                    "Hiba a fájl beolvasásakor.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    LaunchedEffect(undoBannerData?.timestamp) {
        if (undoBannerData != null) {
            delay(4500L)
            undoBannerData = null
        }
    }

    LaunchedEffect(uiState.settings.autoHideTopBarOnScroll, uiState.searchQuery, uiState.items.isEmpty()) {
        if (!uiState.settings.autoHideTopBarOnScroll || uiState.searchQuery.isNotBlank() || uiState.items.isEmpty()) {
            isTopBarHiddenByScroll = false
        }
    }

    LaunchedEffect(pendingWidgetAction) {
        if (pendingWidgetAction == KeszletWidgetManager.ACTION_OPEN_ADD_ITEM) {
            isSettingsSheetVisible = false
            activeSheetItem = null
            editorOpenSessionCounter = SystemClock.uptimeMillis()
            isEditorSheetVisible = true
            viewModel.consumeWidgetAction()
        }
    }

    val onIncrementItem = remember(viewModel) { viewModel::incrementQuantity }
    val onDecrementItem = remember(viewModel) { viewModel::decrementQuantity }
    val onToggleOpenedItem = remember(viewModel) { viewModel::toggleItemOpened }
    val onToggleFavoriteItem = remember(viewModel) { viewModel::toggleItemFavorite }
    val onEditItem = remember {
        { item: InventoryItem ->
            activeSheetItem = item
            isEditorSheetVisible = true
        }
    }

    val onExecuteItemSwipeAction = remember(viewModel) {
        { item: InventoryItem, action: ItemSwipeAction ->
            when (action) {
                ItemSwipeAction.ZERO_QUANTITY -> {
                    if (item.quantity > 0.0) {
                        undoBannerData = UndoBannerData(
                            itemSnapshot = item,
                            message = "„${item.name}” lenullázva"
                        )
                        viewModel.zeroItemQuantity(item)
                    }
                }
                ItemSwipeAction.TOGGLE_OPENED -> {
                    viewModel.toggleItemOpened(item)
                }
                ItemSwipeAction.NONE -> {}
            }
        }
    }

    val placement = uiState.settings.filterPlacement
    val animSpeed = uiState.settings.animationSpeed
    val layoutMode = uiState.settings.itemLayoutMode
    val addGesture = uiState.settings.addItemGesture
    val settingsGesture = uiState.settings.openSettingsGesture
    val hasItemHorizontalSwipe = uiState.settings.itemSwipeRightAction != ItemSwipeAction.NONE ||
        uiState.settings.itemSwipeLeftAction != ItemSwipeAction.NONE
    val canTriggerMainGestures = !isEditorSheetVisible && !isSettingsSheetVisible && !isEdgeSidebarOpen

    var lastGestureTriggerTime by remember { mutableLongStateOf(0L) }
    var lastTouchDownTime by remember { mutableLongStateOf(0L) }
    val densityVal = scaledDensity.density

    val isDirectionAssigned = { dir: GestureDirection ->
        dir != GestureDirection.NONE && (addGesture == dir || settingsGesture == dir)
    }

    val triggerGestureDirection = { dir: GestureDirection ->
        val now = SystemClock.uptimeMillis()
        if (canTriggerMainGestures && dir != GestureDirection.NONE && now - lastGestureTriggerTime > 400L) {
            if (addGesture == dir) {
                lastGestureTriggerTime = now
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                activeSheetItem = null
                editorOpenSessionCounter = now
                isEditorSheetVisible = true
            } else if (settingsGesture == dir) {
                lastGestureTriggerTime = now
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                isSettingsSheetVisible = true
            }
        }
    }

    val nestedScrollConnection = remember(
        canTriggerMainGestures,
        addGesture,
        settingsGesture,
        densityVal,
        uiState.settings.autoHideTopBarOnScroll,
        uiState.searchQuery
    ) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (uiState.settings.autoHideTopBarOnScroll &&
                    source == NestedScrollSource.UserInput &&
                    uiState.searchQuery.isBlank()
                ) {
                    if (available.y < -18f) {
                        isTopBarHiddenByScroll = true
                    } else if (available.y > 14f) {
                        isTopBarHiddenByScroll = false
                    }
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                if (!canTriggerMainGestures) return Velocity.Zero
                val now = SystemClock.uptimeMillis()
                if (now - lastGestureTriggerTime < 350L) {
                    return available
                }
                val touchDuration = now - lastTouchDownTime
                val vyDpPerSec = available.y / densityVal
                if (touchDuration in 15L..165L && abs(vyDpPerSec) >= 2500f) {
                    val dir = if (vyDpPerSec < 0f) GestureDirection.SWIPE_UP else GestureDirection.SWIPE_DOWN
                    if (isDirectionAssigned(dir)) {
                        triggerGestureDirection(dir)
                        return available
                    }
                }
                return Velocity.Zero
            }
        }
    }

    val groupedCategories = remember(
        uiState.items,
        uiState.filters,
        uiState.settings.groupByCategory
    ) {
        if (!uiState.settings.groupByCategory) {
            emptyList()
        } else {
            val filterOrderMap = uiState.filters
                .filter { it.isProductCategory }
                .mapIndexed { index, filterItem -> filterItem.label.lowercase() to index }
                .toMap()

            uiState.items
                .groupBy { it.category }
                .entries
                .sortedWith(
                    compareBy<Map.Entry<String, List<InventoryItem>>> { entry ->
                        filterOrderMap[entry.key.lowercase()] ?: Int.MAX_VALUE
                    }.thenBy { it.key.lowercase() }
                )
                .map { it.key to it.value }
        }
    }

    CompositionLocalProvider(LocalDensity provides scaledDensity) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(
                    canTriggerMainGestures,
                    addGesture,
                    settingsGesture,
                    hasItemHorizontalSwipe,
                    densityVal
                ) {
                    if (!canTriggerMainGestures) return@pointerInput
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
                        val startX = down.position.x
                        val startY = down.position.y
                        val startTime = down.uptimeMillis
                        lastTouchDownTime = startTime
                        var firedInThisGesture = false

                        while (true) {
                            val event = awaitPointerEvent(PointerEventPass.Initial)
                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                            if (!change.pressed) break

                            if (!firedInThisGesture) {
                                val dt = (change.uptimeMillis - startTime).coerceAtLeast(1L)
                                val dxDp = (change.position.x - startX) / densityVal
                                val dyDp = (change.position.y - startY) / densityVal
                                val absDx = abs(dxDp)
                                val absDy = abs(dyDp)

                                if (dt in 12L..135L && absDy >= 68f && absDy > absDx * 1.6f) {
                                    val vyDpPerSec = (absDy * 1000f) / dt
                                    if (vyDpPerSec >= 1450f) {
                                        val dir = if (dyDp < 0f) GestureDirection.SWIPE_UP else GestureDirection.SWIPE_DOWN
                                        if (isDirectionAssigned(dir)) {
                                            firedInThisGesture = true
                                            change.consume()
                                            triggerGestureDirection(dir)
                                        }
                                    }
                                } else if (!hasItemHorizontalSwipe && dt in 12L..240L && absDx >= 62f && absDx > absDy * 1.5f) {
                                    val vxDpPerSec = (absDx * 1000f) / dt
                                    if (vxDpPerSec >= 650f) {
                                        val dir = if (dxDp < 0f) GestureDirection.SWIPE_LEFT else GestureDirection.SWIPE_RIGHT
                                        if (isDirectionAssigned(dir)) {
                                            firedInThisGesture = true
                                            change.consume()
                                            triggerGestureDirection(dir)
                                        }
                                    }
                                }
                            } else {
                                change.consume()
                            }
                        }
                    }
                }
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                topBar = {
                    InventoryTopBar(
                        searchQuery = uiState.searchQuery,
                        selectedFilterIds = uiState.selectedFilterIds,
                        filters = uiState.filters,
                        filterPlacement = placement,
                        animationSpeed = animSpeed,
                        isHiddenByScroll = isTopBarHiddenByScroll,
                        sortField = uiState.settings.sortField,
                        sortDirection = uiState.settings.sortDirection,
                        syncError = uiState.syncError,
                        onSearchQueryChange = viewModel::onSearchQueryChange,
                        onFilterSelected = viewModel::onFilterSelected,
                        onSortFieldChange = viewModel::setSortField,
                        onSortDirectionChange = viewModel::setSortDirection,
                        onOpenSettings = { isSettingsSheetVisible = true }
                    )
                },
                bottomBar = {
                    if (placement == FilterPlacement.BOTTOM_BAR && !isTopBarHiddenByScroll) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            tonalElevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .navigationBarsPadding()
                                    .padding(vertical = 8.dp)
                            ) {
                                HorizontalFilterRow(
                                    filters = uiState.filters,
                                    selectedFilterIds = uiState.selectedFilterIds,
                                    onFilterSelected = viewModel::onFilterSelected
                                )
                            }
                        }
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            activeSheetItem = null
                            editorOpenSessionCounter = SystemClock.uptimeMillis()
                            isEditorSheetVisible = true
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = if (placement == FilterPlacement.BOTTOM_BAR && !isTopBarHiddenByScroll) {
                            Modifier
                        } else {
                            Modifier.navigationBarsPadding()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Új tétel hozzáadása"
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .nestedScroll(nestedScrollConnection)
                ) {
                    if (uiState.items.isEmpty() && !uiState.isLoading) {
                        val onlyLowStockSelected = uiState.selectedFilterIds.size == 1 &&
                            uiState.filters.firstOrNull { it.id in uiState.selectedFilterIds }?.isLowStockFilter == true
                        EmptyInventoryState(
                            isLowStockFilter = onlyLowStockSelected,
                            hasSearchQuery = uiState.searchQuery.isNotBlank(),
                            addItemGesture = addGesture,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        val toggleCategoryCollapse = { catName: String ->
                            collapsedCategories = if (catName in collapsedCategories) {
                                collapsedCategories - catName
                            } else {
                                collapsedCategories + catName
                            }
                        }

                        when (layoutMode) {
                            ItemLayoutMode.GRID_CARDS -> {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(
                                        start = 12.dp,
                                        end = 12.dp,
                                        top = 8.dp,
                                        bottom = 112.dp
                                    ),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    if (uiState.settings.groupByCategory) {
                                        groupedCategories.forEach { (categoryName, categoryItems) ->
                                            val isCollapsed = categoryName in collapsedCategories
                                            item(
                                                key = "grid_category_header_$categoryName",
                                                span = { GridItemSpan(maxLineSpan) },
                                                contentType = "category_header"
                                            ) {
                                                CategoryGroupHeader(
                                                    categoryName = categoryName,
                                                    itemCount = categoryItems.size,
                                                    isCollapsed = isCollapsed,
                                                    onToggleCollapse = { toggleCategoryCollapse(categoryName) }
                                                )
                                            }
                                            if (!isCollapsed) {
                                                items(
                                                    items = categoryItems,
                                                    key = { it.id },
                                                    contentType = { "inventory_grid_item" }
                                                ) { item ->
                                                    SwipeableItemContainer(
                                                        item = item,
                                                        rightAction = uiState.settings.itemSwipeRightAction,
                                                        leftAction = uiState.settings.itemSwipeLeftAction,
                                                        onExecuteAction = onExecuteItemSwipeAction
                                                    ) {
                                                        InventoryGridCard(
                                                            item = item,
                                                            hideUnopenedBadge = uiState.settings.hideUnopenedBadge,
                                                            onIncrement = onIncrementItem,
                                                            onDecrement = onDecrementItem,
                                                            onToggleOpened = onToggleOpenedItem,
                                                            onToggleFavorite = onToggleFavoriteItem,
                                                            onEditRequest = onEditItem
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        items(
                                            items = uiState.items,
                                            key = { it.id },
                                            contentType = { "inventory_grid_item" }
                                        ) { item ->
                                            SwipeableItemContainer(
                                                item = item,
                                                rightAction = uiState.settings.itemSwipeRightAction,
                                                leftAction = uiState.settings.itemSwipeLeftAction,
                                                onExecuteAction = onExecuteItemSwipeAction
                                            ) {
                                                InventoryGridCard(
                                                    item = item,
                                                    hideUnopenedBadge = uiState.settings.hideUnopenedBadge,
                                                    onIncrement = onIncrementItem,
                                                    onDecrement = onDecrementItem,
                                                    onToggleOpened = onToggleOpenedItem,
                                                    onToggleFavorite = onToggleFavoriteItem,
                                                    onEditRequest = onEditItem
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            ItemLayoutMode.COMPACT -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(
                                        start = 14.dp,
                                        end = 14.dp,
                                        top = 8.dp,
                                        bottom = 112.dp
                                    ),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (uiState.settings.groupByCategory) {
                                        groupedCategories.forEach { (categoryName, categoryItems) ->
                                            val isCollapsed = categoryName in collapsedCategories
                                            item(
                                                key = "compact_category_header_$categoryName",
                                                contentType = "category_header"
                                            ) {
                                                CategoryGroupHeader(
                                                    categoryName = categoryName,
                                                    itemCount = categoryItems.size,
                                                    isCollapsed = isCollapsed,
                                                    onToggleCollapse = { toggleCategoryCollapse(categoryName) }
                                                )
                                            }
                                            if (!isCollapsed) {
                                                items(
                                                    items = categoryItems,
                                                    key = { it.id },
                                                    contentType = { "inventory_compact_item" }
                                                ) { item ->
                                                    SwipeableItemContainer(
                                                        item = item,
                                                        rightAction = uiState.settings.itemSwipeRightAction,
                                                        leftAction = uiState.settings.itemSwipeLeftAction,
                                                        onExecuteAction = onExecuteItemSwipeAction
                                                    ) {
                                                        InventoryCompactRow(
                                                            item = item,
                                                            hideUnopenedBadge = uiState.settings.hideUnopenedBadge,
                                                            onIncrement = onIncrementItem,
                                                            onDecrement = onDecrementItem,
                                                            onToggleOpened = onToggleOpenedItem,
                                                            onToggleFavorite = onToggleFavoriteItem,
                                                            onEditRequest = onEditItem
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        items(
                                            items = uiState.items,
                                            key = { it.id },
                                            contentType = { "inventory_compact_item" }
                                        ) { item ->
                                            SwipeableItemContainer(
                                                item = item,
                                                rightAction = uiState.settings.itemSwipeRightAction,
                                                leftAction = uiState.settings.itemSwipeLeftAction,
                                                onExecuteAction = onExecuteItemSwipeAction
                                            ) {
                                                InventoryCompactRow(
                                                    item = item,
                                                    hideUnopenedBadge = uiState.settings.hideUnopenedBadge,
                                                    onIncrement = onIncrementItem,
                                                    onDecrement = onDecrementItem,
                                                    onToggleOpened = onToggleOpenedItem,
                                                    onToggleFavorite = onToggleFavoriteItem,
                                                    onEditRequest = onEditItem
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            ItemLayoutMode.LIST -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(
                                        start = 14.dp,
                                        end = 14.dp,
                                        top = 8.dp,
                                        bottom = 112.dp
                                    ),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    if (uiState.settings.groupByCategory) {
                                        groupedCategories.forEach { (categoryName, categoryItems) ->
                                            val isCollapsed = categoryName in collapsedCategories
                                            item(
                                                key = "category_header_$categoryName",
                                                contentType = "category_header"
                                            ) {
                                                CategoryGroupHeader(
                                                    categoryName = categoryName,
                                                    itemCount = categoryItems.size,
                                                    isCollapsed = isCollapsed,
                                                    onToggleCollapse = { toggleCategoryCollapse(categoryName) }
                                                )
                                            }
                                            if (!isCollapsed) {
                                                items(
                                                    items = categoryItems,
                                                    key = { it.id },
                                                    contentType = { "inventory_item" }
                                                ) { item ->
                                                    SwipeableItemContainer(
                                                        item = item,
                                                        rightAction = uiState.settings.itemSwipeRightAction,
                                                        leftAction = uiState.settings.itemSwipeLeftAction,
                                                        onExecuteAction = onExecuteItemSwipeAction
                                                    ) {
                                                        InventoryItemRow(
                                                            item = item,
                                                            hideUnopenedBadge = uiState.settings.hideUnopenedBadge,
                                                            onIncrement = onIncrementItem,
                                                            onDecrement = onDecrementItem,
                                                            onToggleOpened = onToggleOpenedItem,
                                                            onToggleFavorite = onToggleFavoriteItem,
                                                            onEditRequest = onEditItem
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        items(
                                            items = uiState.items,
                                            key = { it.id },
                                            contentType = { "inventory_item" }
                                        ) { item ->
                                            SwipeableItemContainer(
                                                item = item,
                                                rightAction = uiState.settings.itemSwipeRightAction,
                                                leftAction = uiState.settings.itemSwipeLeftAction,
                                                onExecuteAction = onExecuteItemSwipeAction
                                            ) {
                                                InventoryItemRow(
                                                    item = item,
                                                    hideUnopenedBadge = uiState.settings.hideUnopenedBadge,
                                                    onIncrement = onIncrementItem,
                                                    onDecrement = onDecrementItem,
                                                    onToggleOpened = onToggleOpenedItem,
                                                    onToggleFavorite = onToggleFavoriteItem,
                                                    onEditRequest = onEditItem
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if ((placement == FilterPlacement.LEFT_EDGE || placement == FilterPlacement.RIGHT_EDGE) && isEdgeSidebarOpen) {
                        val sidebarScrimInteraction = remember { MutableInteractionSource() }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.22f))
                                .clickable(
                                    interactionSource = sidebarScrimInteraction,
                                    indication = null,
                                    onClick = { isEdgeSidebarOpen = false }
                                )
                        )
                    }

                    if (placement == FilterPlacement.LEFT_EDGE) {
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            CollapsibleEdgeSidebar(
                                isLeft = true,
                                isOpen = isEdgeSidebarOpen,
                                animationSpeed = animSpeed,
                                filters = uiState.filters,
                                selectedFilterIds = uiState.selectedFilterIds,
                                onToggleOpen = { isEdgeSidebarOpen = !isEdgeSidebarOpen },
                                onFilterSelected = viewModel::onFilterSelected
                            )
                        }
                    }

                    if (placement == FilterPlacement.RIGHT_EDGE) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            CollapsibleEdgeSidebar(
                                isLeft = false,
                                isOpen = isEdgeSidebarOpen,
                                animationSpeed = animSpeed,
                                filters = uiState.filters,
                                selectedFilterIds = uiState.selectedFilterIds,
                                onToggleOpen = { isEdgeSidebarOpen = !isEdgeSidebarOpen },
                                onFilterSelected = viewModel::onFilterSelected
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = undoBannerData != null,
                        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .navigationBarsPadding()
                            .padding(start = 16.dp, end = 88.dp, bottom = 16.dp)
                    ) {
                        val currentUndo = undoBannerData
                        if (currentUndo != null) {
                            Surface(
                                color = MaterialTheme.colorScheme.inverseSurface,
                                contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                                shape = RoundedCornerShape(14.dp),
                                tonalElevation = 8.dp,
                                shadowElevation = 6.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = currentUndo.message,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    TextButton(
                                        onClick = {
                                            viewModel.restoreItem(currentUndo.itemSnapshot)
                                            undoBannerData = null
                                        }
                                    ) {
                                        Text(
                                            text = "Visszavonás",
                                            color = MaterialTheme.colorScheme.inversePrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            AdaptiveBottomSheet(
                visible = isEditorSheetVisible,
                animationSpeed = animSpeed,
                onDismiss = {
                    isEditorSheetVisible = false
                    activeSheetItem = null
                }
            ) {
                ItemEditorSheetContent(
                    initialItem = activeSheetItem,
                    sessionSeed = editorOpenSessionCounter,
                    allItems = uiState.allItems,
                    productCategories = uiState.productCategories,
                    settings = uiState.settings,
                    onSelectExistingItem = { existingItem ->
                        activeSheetItem = existingItem
                    },
                    onQuickIncrementExisting = { existingItem ->
                        viewModel.incrementQuantity(existingItem)
                        isEditorSheetVisible = false
                        activeSheetItem = null
                    },
                    onSave = { id, name, category, quantity, unit, minQuantity, isOpened, isFavorite, note ->
                        viewModel.saveItem(
                            existingId = id,
                            name = name,
                            category = category,
                            quantity = quantity,
                            unit = unit,
                            minQuantity = minQuantity,
                            isOpened = isOpened,
                            isFavorite = isFavorite,
                            note = note
                        )
                        isEditorSheetVisible = false
                        activeSheetItem = null
                    },
                    onDelete = { itemToDelete ->
                        undoBannerData = UndoBannerData(
                            itemSnapshot = itemToDelete,
                            message = "„${itemToDelete.name}” törölve"
                        )
                        viewModel.deleteItem(itemToDelete.id)
                        isEditorSheetVisible = false
                        activeSheetItem = null
                    }
                )
            }

            AdaptiveBottomSheet(
                visible = isSettingsSheetVisible,
                animationSpeed = animSpeed,
                onDismiss = { isSettingsSheetVisible = false }
            ) {
                SettingsSheetContent(
                    settings = uiState.settings,
                    filters = uiState.filters,
                    onDismiss = { isSettingsSheetVisible = false },
                    onThemeModeChange = viewModel::setThemeMode,
                    onColorPaletteChange = viewModel::setColorPalette,
                    onFilterPlacementChange = viewModel::setFilterPlacement,
                    onAnimationSpeedChange = viewModel::setAnimationSpeed,
                    onItemLayoutModeChange = viewModel::setItemLayoutMode,
                    onUiScaleChange = viewModel::setUiScale,
                    onHideUnopenedBadgeChange = viewModel::setHideUnopenedBadge,
                    onAutoResetOpenedOnZeroChange = viewModel::setAutoResetOpenedOnZero,
                    onAutoHideTopBarOnScrollChange = viewModel::setAutoHideTopBarOnScroll,
                    onItemSwipeRightActionChange = viewModel::setItemSwipeRightAction,
                    onItemSwipeLeftActionChange = viewModel::setItemSwipeLeftAction,
                    onBackupFormatChange = viewModel::setBackupFormat,
                    onExportBackupClick = {
                        val ext = uiState.settings.backupFormat.extension
                        exportLauncher.launch("keszletbarat_mentes.$ext")
                    },
                    onImportBackupClick = {
                        importLauncher.launch(arrayOf("*/*"))
                    },
                    onAddItemGestureChange = viewModel::setAddItemGesture,
                    onOpenSettingsGestureChange = viewModel::setOpenSettingsGesture,
                    onGroupByCategoryChange = viewModel::setGroupByCategory,
                    onMoveFilter = viewModel::moveFilterOrder,
                    onAddFilter = viewModel::addFilter,
                    onRenameFilter = viewModel::renameFilter,
                    onDeleteFilter = viewModel::deleteFilter,
                    onResetFilterOrder = viewModel::resetFilters,
                    onDefaultMinQuantityChange = viewModel::setDefaultMinQuantity,
                    onQuantityStepChange = viewModel::setQuantityStep,
                    onFractionalOnlyForWeightAndVolumeChange = viewModel::setFractionalOnlyForWeightAndVolume
                )
            }
        }
    }
}

@Composable
private fun SwipeableItemContainer(
    item: InventoryItem,
    rightAction: ItemSwipeAction,
    leftAction: ItemSwipeAction,
    onExecuteAction: (InventoryItem, ItemSwipeAction) -> Unit,
    content: @Composable () -> Unit
) {
    if (rightAction == ItemSwipeAction.NONE && leftAction == ItemSwipeAction.NONE) {
        content()
        return
    }

    val haptic = LocalHapticFeedback.current
    val density = LocalDensity.current.density
    val thresholdPx = 78f * density
    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(
        targetValue = dragOffsetX,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "itemSwipeOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(item.id, item.quantity, item.isOpened, rightAction, leftAction) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (dragOffsetX >= thresholdPx && rightAction != ItemSwipeAction.NONE) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onExecuteAction(item, rightAction)
                        } else if (dragOffsetX <= -thresholdPx && leftAction != ItemSwipeAction.NONE) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onExecuteAction(item, leftAction)
                        }
                        dragOffsetX = 0f
                    },
                    onDragCancel = {
                        dragOffsetX = 0f
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        val next = dragOffsetX + dragAmount
                        val allowed = when {
                            next > 0f && rightAction == ItemSwipeAction.NONE -> 0f
                            next < 0f && leftAction == ItemSwipeAction.NONE -> 0f
                            else -> next.coerceIn(-thresholdPx * 1.35f, thresholdPx * 1.35f)
                        }
                        if (allowed != 0f) {
                            change.consume()
                        }
                        dragOffsetX = allowed
                    }
                )
            }
    ) {
        if (abs(animatedOffsetX) > 8f) {
            val isRight = animatedOffsetX > 0f
            val activeAction = if (isRight) rightAction else leftAction
            val bgColor = when (activeAction) {
                ItemSwipeAction.ZERO_QUANTITY -> MaterialTheme.colorScheme.errorContainer
                ItemSwipeAction.TOGGLE_OPENED -> MaterialTheme.colorScheme.primaryContainer
                ItemSwipeAction.NONE -> Color.Transparent
            }
            val txtColor = when (activeAction) {
                ItemSwipeAction.ZERO_QUANTITY -> MaterialTheme.colorScheme.onErrorContainer
                ItemSwipeAction.TOGGLE_OPENED -> MaterialTheme.colorScheme.onPrimaryContainer
                ItemSwipeAction.NONE -> Color.Transparent
            }

            Surface(
                color = bgColor,
                contentColor = txtColor,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.matchParentSize()
            ) {
                Box(
                    contentAlignment = if (isRight) Alignment.CenterStart else Alignment.CenterEnd,
                    modifier = Modifier.padding(horizontal = 18.dp)
                ) {
                    Text(
                        text = activeAction.label,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        Box(
            modifier = Modifier.offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
        ) {
            content()
        }
    }
}

@Composable
private fun CategoryGroupHeader(
    categoryName: String,
    itemCount: Int,
    isCollapsed: Boolean,
    onToggleCollapse: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.7f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onToggleCollapse)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = if (isCollapsed) {
                        Icons.Default.KeyboardArrowDown
                    } else {
                        Icons.Default.KeyboardArrowUp
                    },
                    contentDescription = if (isCollapsed) "Kategória kinyitása" else "Kategória összecsukása",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "$itemCount db",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AdaptiveBottomSheet(
    visible: Boolean,
    animationSpeed: AnimationSpeed,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    BackHandler(enabled = visible, onBack = onDismiss)

    val maxSheetHeight = (LocalConfiguration.current.screenHeightDp * 0.88f).dp
    val scrimInteraction = remember { MutableInteractionSource() }
    val sheetInteraction = remember { MutableInteractionSource() }

    if (animationSpeed == AnimationSpeed.NONE) {
        if (visible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
                    .clickable(
                        interactionSource = scrimInteraction,
                        indication = null,
                        onClick = onDismiss
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    tonalElevation = 6.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = maxSheetHeight)
                        .clickable(
                            interactionSource = sheetInteraction,
                            indication = null,
                            onClick = {}
                        )
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SheetDragHandle()
                        content()
                    }
                }
            }
        }
    } else {
        val duration = animationSpeed.durationMillis

        val sheetEnterTransition = when (animationSpeed) {
            AnimationSpeed.BOUNCY -> {
                scaleIn(
                    initialScale = 0.80f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ) + slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
            }
            AnimationSpeed.ZOOM -> {
                scaleIn(
                    initialScale = 0.45f,
                    animationSpec = tween(duration, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(duration))
            }
            AnimationSpeed.FLOAT -> {
                fadeIn(animationSpec = tween(duration, easing = FastOutSlowInEasing)) +
                    slideInVertically(
                        initialOffsetY = { it / 3 },
                        animationSpec = tween(duration, easing = FastOutSlowInEasing)
                    ) +
                    scaleIn(
                        initialScale = 0.94f,
                        animationSpec = tween(duration, easing = FastOutSlowInEasing)
                    )
            }
            else -> {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(duration, easing = FastOutSlowInEasing)
                )
            }
        }

        val sheetExitTransition = when (animationSpeed) {
            AnimationSpeed.BOUNCY -> {
                scaleOut(
                    targetScale = 0.84f,
                    animationSpec = tween(190)
                ) + slideOutVertically(
                    targetOffsetY = { it / 2 },
                    animationSpec = tween(190)
                )
            }
            AnimationSpeed.ZOOM -> {
                scaleOut(
                    targetScale = 0.45f,
                    animationSpec = tween(duration, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(duration))
            }
            AnimationSpeed.FLOAT -> {
                fadeOut(animationSpec = tween(duration, easing = FastOutSlowInEasing)) +
                    slideOutVertically(
                        targetOffsetY = { it / 3 },
                        animationSpec = tween(duration, easing = FastOutSlowInEasing)
                    ) +
                    scaleOut(
                        targetScale = 0.94f,
                        animationSpec = tween(duration, easing = FastOutSlowInEasing)
                    )
            }
            else -> {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(duration, easing = FastOutSlowInEasing)
                )
            }
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(duration)),
            exit = fadeOut(animationSpec = tween(duration))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
                    .clickable(
                        interactionSource = scrimInteraction,
                        indication = null,
                        onClick = onDismiss
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    tonalElevation = 6.dp,
                    modifier = Modifier
                        .animateEnterExit(
                            enter = sheetEnterTransition,
                            exit = sheetExitTransition
                        )
                        .fillMaxWidth()
                        .heightIn(max = maxSheetHeight)
                        .clickable(
                            interactionSource = sheetInteraction,
                            indication = null,
                            onClick = {}
                        )
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SheetDragHandle()
                        content()
                    }
                }
            }
        }
    }
}

@Composable
private fun SheetDragHandle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(4.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InventoryTopBar(
    searchQuery: String,
    selectedFilterIds: Set<String>,
    filters: List<FilterItem>,
    filterPlacement: FilterPlacement,
    animationSpeed: AnimationSpeed,
    isHiddenByScroll: Boolean,
    sortField: SortField,
    sortDirection: SortDirection,
    syncError: String?,
    onSearchQueryChange: (String) -> Unit,
    onFilterSelected: (String) -> Unit,
    onSortFieldChange: (SortField) -> Unit,
    onSortDirectionChange: (SortDirection) -> Unit,
    onOpenSettings: () -> Unit
) {
    var isSortMenuExpanded by remember { mutableStateOf(false) }
    var isFilterDropdownExpanded by remember { mutableStateOf(false) }
    var isCollapsibleOpen by rememberSaveable { mutableStateOf(false) }

    val selectedFilters = remember(filters, selectedFilterIds) {
        filters.filter { it.id in selectedFilterIds }
    }
    val isOnlyAllSelected = selectedFilters.isEmpty() || selectedFilters.all { it.isAllFilter }
    val selectedFiltersSummary = remember(selectedFilters, isOnlyAllSelected) {
        if (isOnlyAllSelected) {
            "Összes"
        } else {
            selectedFilters.joinToString(", ") { it.label }
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = if (isHiddenByScroll) 0.dp else 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            AnimatedVisibility(
                visible = !isHiddenByScroll,
                enter = expandVertically(tween(200)) + fadeIn(tween(180)),
                exit = shrinkVertically(tween(200)) + fadeOut(tween(150))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            modifier = Modifier.weight(1f),
                            placeholder = {
                                Text(
                                    text = "Keresés a készletben...",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Keresés"
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { onSearchQueryChange("") }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Törlés"
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(28.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            ),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Search
                            )
                        )

                        if (filterPlacement == FilterPlacement.TOP_DROPDOWN) {
                            Box {
                                FilledTonalIconButton(
                                    onClick = { isFilterDropdownExpanded = true },
                                    modifier = Modifier.size(48.dp),
                                    shape = CircleShape,
                                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                                        containerColor = if (!isOnlyAllSelected) {
                                            MaterialTheme.colorScheme.primaryContainer
                                        } else {
                                            MaterialTheme.colorScheme.surfaceContainerHigh
                                        },
                                        contentColor = if (!isOnlyAllSelected) {
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        }
                                    )
                                ) {
                                    Icon(
                                        imageVector = AppIcons.FilterList,
                                        contentDescription = "Szűrők"
                                    )
                                }

                                DropdownMenu(
                                    expanded = isFilterDropdownExpanded,
                                    onDismissRequest = { isFilterDropdownExpanded = false }
                                ) {
                                    filters.forEach { filter ->
                                        val isSelected = filter.id in selectedFilterIds
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = filter.label,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                )
                                            },
                                            trailingIcon = if (isSelected) {
                                                {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            } else null,
                                            onClick = {
                                                onFilterSelected(filter.id)
                                                if (filter.isAllFilter) {
                                                    isFilterDropdownExpanded = false
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Box {
                            FilledTonalIconButton(
                                onClick = { isSortMenuExpanded = true },
                                modifier = Modifier.size(48.dp),
                                shape = CircleShape,
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                Icon(
                                    imageVector = AppIcons.SwapVert,
                                    contentDescription = "Rendezés"
                                )
                            }

                            DropdownMenu(
                                expanded = isSortMenuExpanded,
                                onDismissRequest = { isSortMenuExpanded = false }
                            ) {
                                SortField.entries.forEach { field ->
                                    SortDirection.entries.forEach { direction ->
                                        val isCurrent = sortField == field && sortDirection == direction
                                        val optionLabel = if (field == SortField.OPENED) {
                                            if (direction == SortDirection.ASC) {
                                                "Bontott termékek elöl"
                                            } else {
                                                "Bontatlan termékek elöl"
                                            }
                                        } else {
                                            val arrow = if (direction == SortDirection.ASC) "↑" else "↓"
                                            "${field.label} (${direction.label} $arrow)"
                                        }
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = optionLabel,
                                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                                                )
                                            },
                                            trailingIcon = if (isCurrent) {
                                                {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            } else null,
                                            onClick = {
                                                onSortFieldChange(field)
                                                onSortDirectionChange(direction)
                                                isSortMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        FilledTonalIconButton(
                            onClick = onOpenSettings,
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Beállítások"
                            )
                        }
                    }

                    when (filterPlacement) {
                        FilterPlacement.TOP_BAR -> {
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalFilterRow(
                                filters = filters,
                                selectedFilterIds = selectedFilterIds,
                                onFilterSelected = onFilterSelected
                            )
                        }
                        FilterPlacement.TOP_DROPDOWN -> {
                            if (!isOnlyAllSelected) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Szűrők: $selectedFiltersSummary",
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.weight(1f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    TextButton(
                                        onClick = { onFilterSelected(FilterItem.ID_ALL) },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Text("Összes", style = MaterialTheme.typography.labelMedium)
                                    }
                                }
                            }
                        }
                        FilterPlacement.COLLAPSIBLE_PANEL -> {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { isCollapsibleOpen = !isCollapsibleOpen }
                                            .padding(vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = AppIcons.FilterList,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                text = "Szűrők: $selectedFiltersSummary",
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Icon(
                                            imageVector = if (isCollapsibleOpen) {
                                                Icons.Default.KeyboardArrowUp
                                            } else {
                                                Icons.Default.KeyboardArrowDown
                                            },
                                            contentDescription = null
                                        )
                                    }

                                    if (animationSpeed == AnimationSpeed.NONE) {
                                        if (isCollapsibleOpen) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            CompactChipFlowRow {
                                                filters.forEach { filter ->
                                                    StableFilterChip(
                                                        filter = filter,
                                                        isSelected = filter.id in selectedFilterIds,
                                                        onClick = { onFilterSelected(filter.id) }
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        val enterTransition = if (animationSpeed == AnimationSpeed.BOUNCY) {
                                            expandVertically(
                                                animationSpec = spring(
                                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                                    stiffness = Spring.StiffnessMediumLow
                                                )
                                            )
                                        } else {
                                            expandVertically(tween(animationSpeed.durationMillis))
                                        }
                                        AnimatedVisibility(
                                            visible = isCollapsibleOpen,
                                            enter = enterTransition,
                                            exit = shrinkVertically(tween(animationSpeed.durationMillis))
                                        ) {
                                            Column {
                                                Spacer(modifier = Modifier.height(8.dp))
                                                CompactChipFlowRow {
                                                    filters.forEach { filter ->
                                                        StableFilterChip(
                                                            filter = filter,
                                                            isSelected = filter.id in selectedFilterIds,
                                                            onClick = { onFilterSelected(filter.id) }
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else -> {}
                    }

                    if (syncError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = syncError,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun CompactChipFlowRow(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompactFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    leadingIcon: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        FilterChip(
            selected = selected,
            onClick = onClick,
            modifier = modifier.height(32.dp),
            leadingIcon = leadingIcon,
            label = {
                Text(
                    text = label,
                    fontWeight = FontWeight.Medium
                )
            },
            border = FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = selected,
                borderWidth = 1.dp,
                selectedBorderWidth = 1.dp,
                borderColor = MaterialTheme.colorScheme.outlineVariant,
                selectedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun HorizontalFilterRow(
    filters: List<FilterItem>,
    selectedFilterIds: Set<String>,
    onFilterSelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(
            items = filters,
            key = { it.id }
        ) { filter ->
            StableFilterChip(
                filter = filter,
                isSelected = filter.id in selectedFilterIds,
                onClick = { onFilterSelected(filter.id) }
            )
        }
    }
}

@Composable
private fun CollapsibleEdgeSidebar(
    isLeft: Boolean,
    isOpen: Boolean,
    animationSpeed: AnimationSpeed,
    filters: List<FilterItem>,
    selectedFilterIds: Set<String>,
    onToggleOpen: () -> Unit,
    onFilterSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isLeft) {
            EdgeToggleTab(
                isLeft = false,
                isOpen = isOpen,
                onClick = onToggleOpen
            )
        }

        if (animationSpeed == AnimationSpeed.NONE) {
            if (isOpen) {
                if (!isLeft) {
                    VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                }
                EdgeFilterListPanel(
                    filters = filters,
                    selectedFilterIds = selectedFilterIds,
                    onFilterSelected = onFilterSelected
                )
                if (isLeft) {
                    VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                }
            }
        } else {
            val duration = animationSpeed.durationMillis
            val enterSpec = if (animationSpeed == AnimationSpeed.BOUNCY) {
                expandHorizontally(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
            } else {
                expandHorizontally(animationSpec = tween(duration))
            }
            AnimatedVisibility(
                visible = isOpen,
                enter = enterSpec,
                exit = shrinkHorizontally(animationSpec = tween(duration))
            ) {
                Row(modifier = Modifier.fillMaxHeight()) {
                    if (!isLeft) {
                        VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    }
                    EdgeFilterListPanel(
                        filters = filters,
                        selectedFilterIds = selectedFilterIds,
                        onFilterSelected = onFilterSelected
                    )
                    if (isLeft) {
                        VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    }
                }
            }
        }

        if (isLeft) {
            EdgeToggleTab(
                isLeft = true,
                isOpen = isOpen,
                onClick = onToggleOpen
            )
        }
    }
}

@Composable
private fun EdgeToggleTab(
    isLeft: Boolean,
    isOpen: Boolean,
    onClick: () -> Unit
) {
    val shape = if (isLeft) {
        RoundedCornerShape(topEnd = 14.dp, bottomEnd = 14.dp)
    } else {
        RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp)
    }

    val icon = when {
        isLeft && isOpen -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
        isLeft && !isOpen -> Icons.AutoMirrored.Filled.KeyboardArrowRight
        !isLeft && isOpen -> Icons.AutoMirrored.Filled.KeyboardArrowRight
        else -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
    }

    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = shape,
        tonalElevation = 6.dp,
        shadowElevation = 4.dp,
        modifier = Modifier
            .width(26.dp)
            .height(68.dp)
            .clip(shape)
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = "Oldalsáv nyitása/zárása",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun EdgeFilterListPanel(
    filters: List<FilterItem>,
    selectedFilterIds: Set<String>,
    onFilterSelected: (String) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        modifier = Modifier
            .width(124.dp)
            .fillMaxHeight()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(
                items = filters,
                key = { it.id }
            ) { filter ->
                val isSelected = filter.id in selectedFilterIds
                val isLowStockTab = filter.isLowStockFilter

                val bgColor = when {
                    isSelected && isLowStockTab -> MaterialTheme.colorScheme.errorContainer
                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surfaceContainerHigh
                }
                val txtColor = when {
                    isSelected && isLowStockTab -> MaterialTheme.colorScheme.onErrorContainer
                    isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }

                Surface(
                    color = bgColor,
                    contentColor = txtColor,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onFilterSelected(filter.id) }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    ) {
                        Text(
                            text = filter.label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StableFilterChip(
    filter: FilterItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isLowStockTab = filter.isLowStockFilter
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        FilterChip(
            selected = isSelected,
            onClick = onClick,
            modifier = Modifier.height(32.dp),
            label = {
                Text(
                    text = filter.label,
                    fontWeight = FontWeight.Medium
                )
            },
            leadingIcon = if (isLowStockTab) {
                {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                }
            } else null,
            border = FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = isSelected,
                borderWidth = 1.dp,
                selectedBorderWidth = 1.dp,
                borderColor = MaterialTheme.colorScheme.outlineVariant,
                selectedBorderColor = Color.Transparent
            ),
            colors = if (isLowStockTab) {
                FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onErrorContainer,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.onErrorContainer
                )
            } else {
                FilterChipDefaults.filterChipColors()
            }
        )
    }
}

@Composable
private fun FavoriteStarButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = if (isFavorite) Icons.Default.Star else AppIcons.StarOutline,
        contentDescription = if (isFavorite) "Kedvenc eltávolítása" else "Megjelölés kedvencként",
        tint = if (isFavorite) {
            Color(0xFFF5B400)
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f)
        },
        modifier = modifier
            .size(22.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(2.dp)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InventoryItemRow(
    item: InventoryItem,
    hideUnopenedBadge: Boolean,
    onIncrement: (InventoryItem) -> Unit,
    onDecrement: (InventoryItem) -> Unit,
    onToggleOpened: (InventoryItem) -> Unit,
    onToggleFavorite: (InventoryItem) -> Unit,
    onEditRequest: (InventoryItem) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val isOutOfStock = item.isOutOfStock
    val isLowStock = item.isLowStock

    val containerColor = when {
        isOutOfStock -> MaterialTheme.colorScheme.surfaceContainerLowest
        isLowStock -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.28f)
        else -> MaterialTheme.colorScheme.surfaceContainerLow
    }

    val borderColor = when {
        isOutOfStock -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)
        isLowStock -> MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
        else -> Color.Transparent
    }

    val showOpenedBadge = item.isOpened || !hideUnopenedBadge

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .combinedClickable(
                onClick = { onEditRequest(item) },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onEditRequest(item)
                }
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FavoriteStarButton(
                        isFavorite = item.isFavorite,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onToggleFavorite(item)
                        }
                    )
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = if (isOutOfStock) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .alpha(if (isOutOfStock) 0.58f else 1f)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${item.category} • ${item.unit}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.alpha(if (isOutOfStock) 0.58f else 1f)
                )

                if (showOpenedBadge || isOutOfStock || isLowStock) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        modifier = Modifier.height(22.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (showOpenedBadge) {
                            OpenedStatusBadge(
                                isOpened = item.isOpened,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    onToggleOpened(item)
                                }
                            )
                        }

                        if (isOutOfStock) {
                            StatusBadge(
                                text = "Elfogyott",
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            )
                        } else if (isLowStock) {
                            StatusBadge(
                                text = "Kifogyóban",
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }

                if (item.note.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.note,
                        style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilledTonalIconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onDecrement(item)
                    },
                    enabled = item.quantity > 0.0,
                    shape = CircleShape,
                    modifier = Modifier.size(42.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(
                        imageVector = AppIcons.Remove,
                        contentDescription = "Csökkentés"
                    )
                }

                Text(
                    text = item.formattedQuantity,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = when {
                        isOutOfStock -> MaterialTheme.colorScheme.error
                        isLowStock -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(52.dp)
                )

                FilledIconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onIncrement(item)
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(42.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Növelés"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InventoryGridCard(
    item: InventoryItem,
    hideUnopenedBadge: Boolean,
    onIncrement: (InventoryItem) -> Unit,
    onDecrement: (InventoryItem) -> Unit,
    onToggleOpened: (InventoryItem) -> Unit,
    onToggleFavorite: (InventoryItem) -> Unit,
    onEditRequest: (InventoryItem) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val isOutOfStock = item.isOutOfStock
    val isLowStock = item.isLowStock

    val containerColor = when {
        isOutOfStock -> MaterialTheme.colorScheme.surfaceContainerLowest
        isLowStock -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.28f)
        else -> MaterialTheme.colorScheme.surfaceContainerLow
    }

    val borderColor = when {
        isOutOfStock -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)
        isLowStock -> MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
        else -> Color.Transparent
    }

    val showOpenedBadge = item.isOpened || !hideUnopenedBadge

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .combinedClickable(
                onClick = { onEditRequest(item) },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onEditRequest(item)
                }
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                FavoriteStarButton(
                    isFavorite = item.isFavorite,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onToggleFavorite(item)
                    }
                )
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (isOutOfStock) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .alpha(if (isOutOfStock) 0.58f else 1f)
                )
            }

            Text(
                text = "${item.category} • ${item.unit}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.alpha(if (isOutOfStock) 0.58f else 1f)
            )

            if (showOpenedBadge || isOutOfStock || isLowStock) {
                Row(
                    modifier = Modifier.height(22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    if (showOpenedBadge) {
                        OpenedStatusBadge(
                            isOpened = item.isOpened,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onToggleOpened(item)
                            }
                        )
                    }

                    if (isOutOfStock) {
                        StatusBadge(
                            text = "Elfogyott",
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    } else if (isLowStock) {
                        StatusBadge(
                            text = "Kifogyóban",
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            if (item.note.isNotBlank()) {
                Text(
                    text = item.note,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = FontStyle.Italic,
                        fontSize = 11.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FilledTonalIconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onDecrement(item)
                    },
                    enabled = item.quantity > 0.0,
                    shape = CircleShape,
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(
                        imageVector = AppIcons.Remove,
                        contentDescription = "Csökkentés",
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = item.formattedQuantity,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = when {
                        isOutOfStock -> MaterialTheme.colorScheme.error
                        isLowStock -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    textAlign = TextAlign.Center
                )

                FilledIconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onIncrement(item)
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Növelés",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InventoryCompactRow(
    item: InventoryItem,
    hideUnopenedBadge: Boolean,
    onIncrement: (InventoryItem) -> Unit,
    onDecrement: (InventoryItem) -> Unit,
    onToggleOpened: (InventoryItem) -> Unit,
    onToggleFavorite: (InventoryItem) -> Unit,
    onEditRequest: (InventoryItem) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val isOutOfStock = item.isOutOfStock
    val isLowStock = item.isLowStock

    val containerColor = when {
        isOutOfStock -> MaterialTheme.colorScheme.surfaceContainerLowest
        isLowStock -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.28f)
        else -> MaterialTheme.colorScheme.surfaceContainerLow
    }

    val borderColor = when {
        isOutOfStock -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)
        isLowStock -> MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
        else -> Color.Transparent
    }

    val showOpenedBadge = item.isOpened || !hideUnopenedBadge

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .combinedClickable(
                onClick = { onEditRequest(item) },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onEditRequest(item)
                }
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    FavoriteStarButton(
                        isFavorite = item.isFavorite,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onToggleFavorite(item)
                        }
                    )
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = if (isOutOfStock) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .alpha(if (isOutOfStock) 0.58f else 1f)
                    )
                    Text(
                        text = "(${item.category})",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }

                if (showOpenedBadge || isOutOfStock || isLowStock) {
                    Row(
                        modifier = Modifier.height(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (showOpenedBadge) {
                            OpenedStatusBadge(
                                isOpened = item.isOpened,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    onToggleOpened(item)
                                }
                            )
                        }

                        if (isOutOfStock) {
                            StatusBadge(
                                text = "Elfogyott",
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            )
                        } else if (isLowStock) {
                            StatusBadge(
                                text = "Kifogyóban",
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                FilledTonalIconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onDecrement(item)
                    },
                    enabled = item.quantity > 0.0,
                    shape = CircleShape,
                    modifier = Modifier.size(34.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(
                        imageVector = AppIcons.Remove,
                        contentDescription = "Csökkentés",
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = "${item.formattedQuantity} ${item.unit}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = when {
                        isOutOfStock -> MaterialTheme.colorScheme.error
                        isLowStock -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(64.dp),
                    maxLines = 1
                )

                FilledIconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onIncrement(item)
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(34.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Növelés",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun OpenedStatusBadge(
    isOpened: Boolean,
    onClick: () -> Unit
) {
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    val bgColor = if (isOpened) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        if (isDark) {
            Color(0xFFD48B4B).copy(alpha = 0.22f)
        } else {
            Color(0xFFE6A467).copy(alpha = 0.26f)
        }
    }

    val txtColor = if (isOpened) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        if (isDark) {
            Color(0xFFF2B67F)
        } else {
            Color(0xFF7A4414)
        }
    }

    val borderStroke = if (isOpened) {
        BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    } else {
        BorderStroke(
            1.dp,
            if (isDark) Color(0xFFD48B4B).copy(alpha = 0.55f) else Color(0xFFB87030).copy(alpha = 0.45f)
        )
    }

    Surface(
        color = bgColor,
        contentColor = txtColor,
        shape = RoundedCornerShape(6.dp),
        border = borderStroke,
        modifier = Modifier
            .height(20.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 7.dp)
        ) {
            Text(
                text = if (isOpened) "Bontott" else "Bontatlan",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isOpened) FontWeight.Bold else FontWeight.Medium
                ),
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

@Composable
private fun StatusBadge(
    text: String,
    containerColor: Color,
    contentColor: Color
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.height(20.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 7.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

@Composable
private fun EmptyInventoryState(
    isLowStockFilter: Boolean,
    hasSearchQuery: Boolean,
    addItemGesture: GestureDirection,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = if (isLowStockFilter) {
                    Icons.Default.Warning
                } else {
                    Icons.Default.ShoppingCart
                },
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Text(
                text = when {
                    hasSearchQuery -> "Nincs találat a keresésre"
                    isLowStockFilter -> "Mindenből van elég készleten!"
                    else -> "Még nincs felvéve tétel ebben a kategóriában"
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            if (addItemGesture != GestureDirection.NONE) {
                Text(
                    text = "Tipp: rántsd meg gyorsan a képernyőt (${addItemGesture.label}) új termék felvételéhez!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ItemEditorSheetContent(
    initialItem: InventoryItem?,
    sessionSeed: Long,
    allItems: List<InventoryItem>,
    productCategories: List<FilterItem>,
    settings: AppSettings,
    onSelectExistingItem: (InventoryItem) -> Unit,
    onQuickIncrementExisting: (InventoryItem) -> Unit,
    onSave: (
        id: String?,
        name: String,
        category: String,
        quantity: Double,
        unit: String,
        minQuantity: Double,
        isOpened: Boolean,
        isFavorite: Boolean,
        note: String
    ) -> Unit,
    onDelete: (InventoryItem) -> Unit
) {
    val selectableCategories = remember(productCategories) {
        productCategories.filterNot {
            it.label.equals("Bontatlan", ignoreCase = true) ||
                it.label.equals("Bontott", ignoreCase = true)
        }
    }
    val defaultCategoryLabel = selectableCategories.firstOrNull()?.label ?: "Hűtő"

    var name by rememberSaveable(initialItem?.id, sessionSeed) {
        mutableStateOf(initialItem?.name ?: "")
    }
    var selectedCategory by rememberSaveable(initialItem?.id, sessionSeed) {
        mutableStateOf(initialItem?.category ?: defaultCategoryLabel)
    }
    var selectedUnit by rememberSaveable(initialItem?.id, sessionSeed) {
        val initUnit = initialItem?.unit ?: InventoryUnits.DB
        mutableStateOf(if (initUnit in InventoryUnits.all) initUnit else InventoryUnits.DB)
    }
    var stepSize by rememberSaveable(initialItem?.id, sessionSeed) {
        mutableDoubleStateOf(settings.effectiveStepForUnit(initialItem?.unit ?: InventoryUnits.DB))
    }
    var quantity by rememberSaveable(initialItem?.id, sessionSeed) {
        mutableDoubleStateOf(initialItem?.quantity ?: settings.effectiveStepForUnit(InventoryUnits.DB))
    }
    var minQuantity by rememberSaveable(initialItem?.id, sessionSeed) {
        mutableDoubleStateOf(initialItem?.minQuantity ?: settings.defaultMinQuantity)
    }
    var isOpened by rememberSaveable(initialItem?.id, sessionSeed) {
        mutableStateOf(initialItem?.isOpened ?: false)
    }
    var isFavorite by rememberSaveable(initialItem?.id, sessionSeed) {
        mutableStateOf(initialItem?.isFavorite ?: false)
    }
    var note by rememberSaveable(initialItem?.id, sessionSeed) {
        mutableStateOf(initialItem?.note ?: "")
    }

    LaunchedEffect(selectedUnit) {
        if (initialItem == null) {
            stepSize = settings.effectiveStepForUnit(selectedUnit)
        }
    }

    val normalizedTypedName = name.trim().lowercase()
    val duplicateItem = remember(normalizedTypedName, allItems, initialItem?.id) {
        if (normalizedTypedName.isBlank()) {
            null
        } else {
            allItems.firstOrNull { item ->
                item.id != initialItem?.id && item.exactMatchKey == normalizedTypedName
            }
        }
    }

    val randomizedPool = remember(sessionSeed, initialItem?.id, allItems.size) {
        val combined = buildList {
            allItems.forEach { existing ->
                val validUnit = if (existing.unit in InventoryUnits.all) existing.unit else InventoryUnits.DB
                add(QuickSuggestion(existing.name, existing.category, validUnit))
            }
            addAll(CommonHouseholdSuggestions.presets)
        }.distinctBy { it.name.trim().lowercase() }
        combined.shuffled()
    }

    val quickSuggestions = remember(normalizedTypedName, randomizedPool, allItems, initialItem?.id) {
        if (initialItem != null) {
            emptyList()
        } else if (normalizedTypedName.isBlank()) {
            val existingKeys = allItems.map { it.exactMatchKey }.toSet()
            val notYetInStock = randomizedPool.filter { it.name.trim().lowercase() !in existingKeys }
            if (notYetInStock.size >= 6) {
                notYetInStock.take(6)
            } else {
                randomizedPool.take(6)
            }
        } else {
            randomizedPool.filter { suggestion ->
                val key = suggestion.name.trim().lowercase()
                key != normalizedTypedName && key.contains(normalizedTypedName)
            }.take(6)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (initialItem == null) "Új termék felvétele" else "Termék szerkesztése",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            if (initialItem != null) {
                OutlinedButton(
                    onClick = { onDelete(initialItem) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Törlés",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Törlés")
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Termék neve") },
                placeholder = { Text("pl. Tej 2.8%, Alufólia, Zsíroldó") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Megjegyzés (opcionális)") },
                placeholder = { Text("pl. laktózmentes, kedvenc márka, lejárat...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                )
            )
        }

        if (duplicateItem != null) {
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Ez a termék már szerepel a készletben!",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Text(
                        text = "${duplicateItem.name}: jelenleg ${duplicateItem.formattedQuantity} ${duplicateItem.unit} (${duplicateItem.category})",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val dupStep = settings.effectiveStepForUnit(duplicateItem.unit)
                        FilledTonalButton(
                            onClick = { onQuickIncrementExisting(duplicateItem) },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "+${dupStep.formatQuantity()} ${duplicateItem.unit} hozzáadása",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        OutlinedButton(
                            onClick = { onSelectExistingItem(duplicateItem) },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Meglévő szerkesztése",
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        if (quickSuggestions.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = if (normalizedTypedName.isBlank()) "Gyors javaslatok" else "Találatok és javaslatok",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                CompactChipFlowRow {
                    quickSuggestions.forEach { suggestion ->
                        CompactFilterChip(
                            selected = false,
                            onClick = {
                                name = suggestion.name
                                val matchingCat = selectableCategories.firstOrNull {
                                    it.label.equals(suggestion.category, ignoreCase = true)
                                }
                                if (matchingCat != null) {
                                    selectedCategory = matchingCat.label
                                }
                                selectedUnit = suggestion.unit
                            },
                            label = "${suggestion.name} (${suggestion.unit})"
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                color = if (isOpened) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = if (isOpened) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { isOpened = !isOpened }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Bontott",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Switch(
                        checked = isOpened,
                        onCheckedChange = { isOpened = it }
                    )
                }
            }

            Surface(
                color = if (isFavorite) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = if (isFavorite) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { isFavorite = !isFavorite }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Star else AppIcons.StarOutline,
                            contentDescription = null,
                            tint = if (isFavorite) Color(0xFFF5B400) else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Kedvenc",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                    Switch(
                        checked = isFavorite,
                        onCheckedChange = { isFavorite = it }
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Kategória",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            CompactChipFlowRow {
                selectableCategories.forEach { category ->
                    CompactFilterChip(
                        selected = selectedCategory.equals(category.label, ignoreCase = true),
                        onClick = { selectedCategory = category.label },
                        label = category.label
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Mértékegység",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            CompactChipFlowRow {
                InventoryUnits.all.forEach { unit ->
                    CompactFilterChip(
                        selected = selectedUnit == unit,
                        onClick = { selectedUnit = unit },
                        label = unit
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Lépésköz",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            CompactChipFlowRow {
                AppSettings.availableSteps.forEach { stepOption ->
                    CompactFilterChip(
                        selected = stepSize == stepOption,
                        onClick = { stepSize = stepOption },
                        label = "±${stepOption.formatQuantity()} $selectedUnit"
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Aktuális mennyiség",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                )
                Text(
                    text = "Egység: $selectedUnit",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalIconButton(
                    onClick = {
                        quantity = (quantity - stepSize).coerceAtLeast(0.0).cleanQuantity()
                    },
                    enabled = quantity > 0.0
                ) {
                    Icon(AppIcons.Remove, contentDescription = "Csökkentés")
                }
                Text(
                    text = quantity.formatQuantity(),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.width(56.dp),
                    textAlign = TextAlign.Center
                )
                FilledIconButton(
                    onClick = {
                        quantity = (quantity + stepSize).cleanQuantity()
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Növelés")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Minimum küszöbérték",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                )
                Text(
                    text = "Kifogyóban jelzés küszöbe",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalIconButton(
                    onClick = {
                        minQuantity = (minQuantity - stepSize).coerceAtLeast(0.0).cleanQuantity()
                    },
                    enabled = minQuantity > 0.0
                ) {
                    Icon(AppIcons.Remove, contentDescription = "Minimum csökkentése")
                }
                Text(
                    text = minQuantity.formatQuantity(),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.width(56.dp),
                    textAlign = TextAlign.Center
                )
                FilledTonalIconButton(
                    onClick = {
                        minQuantity = (minQuantity + stepSize).cleanQuantity()
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Minimum növelése")
                }
            }
        }

        Button(
            onClick = {
                onSave(
                    initialItem?.id,
                    name,
                    selectedCategory,
                    quantity,
                    selectedUnit,
                    minQuantity,
                    isOpened,
                    isFavorite,
                    note
                )
            },
            enabled = name.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = if (initialItem == null) "Hozzáadás a készlethez" else "Módosítások mentése",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsSheetContent(
    settings: AppSettings,
    filters: List<FilterItem>,
    onDismiss: () -> Unit,
    onThemeModeChange: (ThemeMode) -> Unit,
    onColorPaletteChange: (AppColorPalette) -> Unit,
    onFilterPlacementChange: (FilterPlacement) -> Unit,
    onAnimationSpeedChange: (AnimationSpeed) -> Unit,
    onItemLayoutModeChange: (ItemLayoutMode) -> Unit,
    onUiScaleChange: (UiScale) -> Unit,
    onHideUnopenedBadgeChange: (Boolean) -> Unit,
    onAutoResetOpenedOnZeroChange: (Boolean) -> Unit,
    onAutoHideTopBarOnScrollChange: (Boolean) -> Unit,
    onItemSwipeRightActionChange: (ItemSwipeAction) -> Unit,
    onItemSwipeLeftActionChange: (ItemSwipeAction) -> Unit,
    onBackupFormatChange: (BackupFormat) -> Unit,
    onExportBackupClick: () -> Unit,
    onImportBackupClick: () -> Unit,
    onAddItemGestureChange: (GestureDirection) -> Unit,
    onOpenSettingsGestureChange: (GestureDirection) -> Unit,
    onGroupByCategoryChange: (Boolean) -> Unit,
    onMoveFilter: (String, Int) -> Unit,
    onAddFilter: (String) -> Unit,
    onRenameFilter: (String, String) -> Unit,
    onDeleteFilter: (String) -> Unit,
    onResetFilterOrder: () -> Unit,
    onDefaultMinQuantityChange: (Double) -> Unit,
    onQuantityStepChange: (Double) -> Unit,
    onFractionalOnlyForWeightAndVolumeChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var newFilterName by rememberSaveable { mutableStateOf("") }
    var filterBeingRenamed by remember { mutableStateOf<FilterItem?>(null) }
    var renameText by remember { mutableStateOf("") }

    val activeTab = SettingsTab.entries.getOrElse(selectedTabIndex) { SettingsTab.APPEARANCE }

    if (filterBeingRenamed != null) {
        AlertDialog(
            onDismissRequest = { filterBeingRenamed = null },
            title = { Text("Szűrő átnevezése") },
            text = {
                OutlinedTextField(
                    value = renameText,
                    onValueChange = { renameText = it },
                    label = { Text("Új megnevezés") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val target = filterBeingRenamed
                        if (target != null && renameText.isNotBlank()) {
                            onRenameFilter(target.id, renameText)
                        }
                        filterBeingRenamed = null
                    },
                    enabled = renameText.isNotBlank()
                ) {
                    Text("Mentés")
                }
            },
            dismissButton = {
                TextButton(onClick = { filterBeingRenamed = null }) {
                    Text("Mégse")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 20.dp)
            .padding(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Beállítások",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )

        CompactChipFlowRow {
            SettingsTab.entries.forEachIndexed { idx, tab ->
                CompactFilterChip(
                    selected = selectedTabIndex == idx,
                    onClick = { selectedTabIndex = idx },
                    label = tab.label
                )
            }
        }

        HorizontalDivider()

        when (activeTab) {
            SettingsTab.APPEARANCE -> {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Megjelenés módja",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                ThemeMode.entries.forEach { mode ->
                                    val isSelected = settings.themeMode == mode
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { onThemeModeChange(mode) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(34.dp),
                                        label = {
                                            Text(
                                                text = mode.label,
                                                modifier = Modifier.fillMaxWidth(),
                                                textAlign = TextAlign.Center,
                                                fontWeight = FontWeight.Medium
                                            )
                                        },
                                        border = FilterChipDefaults.filterChipBorder(
                                            enabled = true,
                                            selected = isSelected,
                                            borderWidth = 1.dp,
                                            selectedBorderWidth = 1.dp,
                                            borderColor = MaterialTheme.colorScheme.outlineVariant,
                                            selectedBorderColor = Color.Transparent
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Színpaletta",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        CompactChipFlowRow {
                            AppColorPalette.entries.forEach { palette ->
                                CompactFilterChip(
                                    selected = settings.colorPalette == palette,
                                    onClick = { onColorPaletteChange(palette) },
                                    label = palette.label,
                                    leadingIcon = {
                                        Box(
                                            modifier = Modifier
                                                .size(14.dp)
                                                .clip(CircleShape)
                                                .background(Color(palette.previewHex))
                                                .border(
                                                    width = 1.dp,
                                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                                    shape = CircleShape
                                                )
                                        )
                                    }
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Betűméret és kártyaméret skálázás",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        CompactChipFlowRow {
                            UiScale.entries.forEach { scale ->
                                CompactFilterChip(
                                    selected = settings.uiScale == scale,
                                    onClick = { onUiScaleChange(scale) },
                                    label = scale.label
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Felsorolás formátuma",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        CompactChipFlowRow {
                            ItemLayoutMode.entries.forEach { mode ->
                                CompactFilterChip(
                                    selected = settings.itemLayoutMode == mode,
                                    onClick = { onItemLayoutModeChange(mode) },
                                    label = mode.label
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Animáció stílusa",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        CompactChipFlowRow {
                            AnimationSpeed.entries.forEach { speed ->
                                CompactFilterChip(
                                    selected = settings.animationSpeed == speed,
                                    onClick = { onAnimationSpeedChange(speed) },
                                    label = speed.label
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                            Text(
                                text = "„Bontatlan” címke elrejtése a kártyákon",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                            )
                            Text(
                                text = "Ha be van kapcsolva, csak a megbontott termékeken jelenik meg a „Bontott” jelvény",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = settings.hideUnopenedBadge,
                            onCheckedChange = onHideUnopenedBadgeChange
                        )
                    }
                }
            }

            SettingsTab.BEHAVIOR -> {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                            Text(
                                text = "Csoportosítás kategória szerint",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                            )
                            Text(
                                text = "A termékek összecsukható kategória-fejlécek alatt jelennek meg",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = settings.groupByCategory,
                            onCheckedChange = onGroupByCategoryChange
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                            Text(
                                text = "Keresősáv és szűrők elrejtése görgetéskor",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                            )
                            Text(
                                text = "Lefelé görgetéskor automatikusan becsukódik a felső sáv, felfelé görgetve visszanyílik",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = settings.autoHideTopBarOnScroll,
                            onCheckedChange = onAutoHideTopBarOnScrollChange
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                            Text(
                                text = "Automatikus „Bontatlan”-ra állítás 0 mennyiségnél",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                            )
                            Text(
                                text = "Ha egy bontott termék elfogy (0-ra csökken), automatikusan visszaáll Bontatlanra",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = settings.autoResetOpenedOnZero,
                            onCheckedChange = onAutoResetOpenedOnZeroChange
                        )
                    }

                    HorizontalDivider()

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Listaelem jobbra húzása (Swipe →)",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        CompactChipFlowRow {
                            ItemSwipeAction.entries.forEach { action ->
                                CompactFilterChip(
                                    selected = settings.itemSwipeRightAction == action,
                                    onClick = { onItemSwipeRightActionChange(action) },
                                    label = action.label
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Listaelem balra húzása (← Swipe)",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        CompactChipFlowRow {
                            ItemSwipeAction.entries.forEach { action ->
                                CompactFilterChip(
                                    selected = settings.itemSwipeLeftAction == action,
                                    onClick = { onItemSwipeLeftActionChange(action) },
                                    label = action.label
                                )
                            }
                        }
                    }

                    HorizontalDivider()

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Új termék felvétele gyors rántással",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        CompactChipFlowRow {
                            GestureDirection.entries.forEach { direction ->
                                CompactFilterChip(
                                    selected = settings.addItemGesture == direction,
                                    onClick = { onAddItemGestureChange(direction) },
                                    label = direction.label
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Beállítások megnyitása gyors rántással",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        CompactChipFlowRow {
                            GestureDirection.entries.forEach { direction ->
                                CompactFilterChip(
                                    selected = settings.openSettingsGesture == direction,
                                    onClick = { onOpenSettingsGestureChange(direction) },
                                    label = direction.label
                                )
                            }
                        }
                    }

                    HorizontalDivider()

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Mennyiség lépésköze",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        CompactChipFlowRow {
                            AppSettings.availableSteps.forEach { step ->
                                CompactFilterChip(
                                    selected = settings.quantityStep == step,
                                    onClick = { onQuantityStepChange(step) },
                                    label = step.formatQuantity()
                                )
                            }
                        }

                        if (settings.quantityStep < 1.0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                                    Text(
                                        text = "Tört lépésköz csak kg és liter esetén",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                    )
                                    Text(
                                        text = "db és csomag esetén továbbra is 1-gyel léptet",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Switch(
                                    checked = settings.fractionalOnlyForWeightAndVolume,
                                    onCheckedChange = onFractionalOnlyForWeightAndVolumeChange
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                            Text(
                                text = "Alapértelmezett minimum küszöbérték",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = "Új termék felvételekor ez az érték lesz előre beírva",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilledTonalIconButton(
                                onClick = {
                                    onDefaultMinQuantityChange(
                                        (settings.defaultMinQuantity - settings.quantityStep).coerceAtLeast(0.0)
                                    )
                                },
                                enabled = settings.defaultMinQuantity > 0.0,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(AppIcons.Remove, contentDescription = "Csökkentés")
                            }
                            Text(
                                text = settings.defaultMinQuantity.formatQuantity(),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.width(48.dp),
                                textAlign = TextAlign.Center
                            )
                            FilledTonalIconButton(
                                onClick = {
                                    onDefaultMinQuantityChange(settings.defaultMinQuantity + settings.quantityStep)
                                },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Növelés")
                            }
                        }
                    }
                }
            }

            SettingsTab.CATEGORIES -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Szűrők elhelyezkedése",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        CompactChipFlowRow {
                            FilterPlacement.entries.forEach { placement ->
                                CompactFilterChip(
                                    selected = settings.filterPlacement == placement,
                                    onClick = { onFilterPlacementChange(placement) },
                                    label = placement.label
                                )
                            }
                        }
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Szűrők és kategóriák",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = "Minden eszközön szinkronizálódik. Törölt kategória termékei az Egyéb alá kerülnek.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        TextButton(onClick = onResetFilterOrder) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Alaphelyzet")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = newFilterName,
                            onValueChange = { newFilterName = it },
                            placeholder = { Text("Új szűrő / kategória neve...") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Done
                            )
                        )
                        FilledIconButton(
                            onClick = {
                                if (newFilterName.isNotBlank()) {
                                    onAddFilter(newFilterName)
                                    newFilterName = ""
                                }
                            },
                            enabled = newFilterName.isNotBlank(),
                            modifier = Modifier.size(48.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Szűrő hozzáadása"
                            )
                        }
                    }

                    filters.forEachIndexed { index, filter ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 14.dp, end = 6.dp, top = 4.dp, bottom = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${index + 1}. ${filter.label}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { onMoveFilter(filter.id, -1) },
                                        enabled = index > 0,
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowUp,
                                            contentDescription = "Mozgatás fel"
                                        )
                                    }
                                    IconButton(
                                        onClick = { onMoveFilter(filter.id, 1) },
                                        enabled = index < filters.lastIndex,
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Mozgatás le"
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            renameText = filter.label
                                            filterBeingRenamed = filter
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Átnevezés",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    if (filter.isDeletable) {
                                        IconButton(
                                            onClick = { onDeleteFilter(filter.id) },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Törlés",
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.width(36.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            SettingsTab.BACKUP_EXTRAS -> {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Kezdőképernyő Widgetek",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "Tűzd ki a KészletBarát widgeteket közvetlenül a telefonod főképernyőjére:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "1. Kell venni (Kifogyóban) lista widget",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Megjeleníti a főképernyőn az elfogyott és kifogyóban lévő tételek számát és listáját.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            FilledTonalButton(
                                onClick = {
                                    val pinned = KeszletWidgetManager.requestPinLowStockWidget(context)
                                    if (!pinned) {
                                        Toast.makeText(
                                            context,
                                            "A főképernyőn hosszan nyomva a Widgetek között tudod hozzáadni!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Kell venni widget kitűzése")
                            }
                        }
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "2. Gyorsgombok widget",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Kompakt sáv a főképernyőre az Új termék felvétele és a Kell venni lista gyors megnyitásához.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            FilledTonalButton(
                                onClick = {
                                    val pinned = KeszletWidgetManager.requestPinQuickActionWidget(context)
                                    if (!pinned) {
                                        Toast.makeText(
                                            context,
                                            "A főképernyőn hosszan nyomva a Widgetek között tudod hozzáadni!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Gyorsgombok widget kitűzése")
                            }
                        }
                    }
                }
            }
        }

        HorizontalDivider()

        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Biztonsági mentés (JSON / CSV)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Mentsd le a teljes készletet fájlba, vagy tölts be tételeket egy korábbi JSON / CSV mentésből:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                CompactChipFlowRow {
                    BackupFormat.entries.forEach { format ->
                        CompactFilterChip(
                            selected = settings.backupFormat == format,
                            onClick = { onBackupFormatChange(format) },
                            label = format.label
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(
                        onClick = onExportBackupClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Mentés fájlba")
                    }
                    OutlinedButton(
                        onClick = onImportBackupClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Fájl importálása")
                    }
                }
            }
        }

        Button(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Kész",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}
