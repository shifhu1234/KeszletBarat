package hu.keszletbarat.app.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
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
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.keszletbarat.app.data.AnimationSpeed
import hu.keszletbarat.app.data.AppColorPalette
import hu.keszletbarat.app.data.AppSettings
import hu.keszletbarat.app.data.FilterPlacement
import hu.keszletbarat.app.data.GestureDirection
import hu.keszletbarat.app.data.ThemeMode
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    viewModel: InventoryViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pendingWidgetAction by viewModel.pendingWidgetAction.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current

    var activeSheetItem by remember { mutableStateOf<InventoryItem?>(null) }
    var isEditorSheetVisible by rememberSaveable { mutableStateOf(false) }
    var isSettingsSheetVisible by rememberSaveable { mutableStateOf(false) }
    var isEdgeSidebarOpen by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(pendingWidgetAction) {
        if (pendingWidgetAction == KeszletWidgetManager.ACTION_OPEN_ADD_ITEM) {
            isSettingsSheetVisible = false
            activeSheetItem = null
            isEditorSheetVisible = true
            viewModel.consumeWidgetAction()
        }
    }

    val onIncrementItem = remember(viewModel) { viewModel::incrementQuantity }
    val onDecrementItem = remember(viewModel) { viewModel::decrementQuantity }
    val onToggleOpenedItem = remember(viewModel) { viewModel::toggleItemOpened }
    val onEditItem = remember {
        { item: InventoryItem ->
            activeSheetItem = item
            isEditorSheetVisible = true
        }
    }

    val placement = uiState.settings.filterPlacement
    val animSpeed = uiState.settings.animationSpeed
    val addGesture = uiState.settings.addItemGesture
    val settingsGesture = uiState.settings.openSettingsGesture
    val canTriggerMainGestures = !isEditorSheetVisible && !isSettingsSheetVisible

    val isDirectionAssigned = { dir: GestureDirection ->
        dir != GestureDirection.NONE && (addGesture == dir || settingsGesture == dir)
    }

    val triggerGestureDirection = { dir: GestureDirection ->
        if (canTriggerMainGestures && dir != GestureDirection.NONE) {
            if (addGesture == dir) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                activeSheetItem = null
                isEditorSheetVisible = true
            } else if (settingsGesture == dir) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                isSettingsSheetVisible = true
            }
        }
    }

    val nestedScrollConnection = remember(
        canTriggerMainGestures,
        addGesture,
        settingsGesture
    ) {
        object : NestedScrollConnection {
            private var accumulatedOverscrollY = 0f
            private var gestureConsumedInCurrentDrag = false
            private val thresholdPx = 115f

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if ((available.y > 0f && accumulatedOverscrollY < 0f) ||
                    (available.y < 0f && accumulatedOverscrollY > 0f)
                ) {
                    accumulatedOverscrollY = 0f
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (!canTriggerMainGestures || source != NestedScrollSource.UserInput || gestureConsumedInCurrentDrag) {
                    return Offset.Zero
                }
                if (available.y != 0f) {
                    accumulatedOverscrollY += available.y
                    if (accumulatedOverscrollY <= -thresholdPx && isDirectionAssigned(GestureDirection.SWIPE_UP)) {
                        gestureConsumedInCurrentDrag = true
                        accumulatedOverscrollY = 0f
                        triggerGestureDirection(GestureDirection.SWIPE_UP)
                    } else if (accumulatedOverscrollY >= thresholdPx && isDirectionAssigned(GestureDirection.SWIPE_DOWN)) {
                        gestureConsumedInCurrentDrag = true
                        accumulatedOverscrollY = 0f
                        triggerGestureDirection(GestureDirection.SWIPE_DOWN)
                    }
                }
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                accumulatedOverscrollY = 0f
                gestureConsumedInCurrentDrag = false
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

    var horizontalDragAccumulator by remember { mutableFloatStateOf(0f) }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    sortField = uiState.settings.sortField,
                    sortDirection = uiState.settings.sortDirection,
                    syncError = uiState.syncError,
                    onSearchQueryChange = viewModel::onSearchQueryChange,
                    onFilterSelected = viewModel::onFilterSelected,
                    onSortFieldChange = viewModel::setSortField,
                    onSortDirectionChange = viewModel::setSortDirection,
                    onOpenSettings = { isSettingsSheetVisible = true },
                    onTriggerGesture = triggerGestureDirection
                )
            },
            bottomBar = {
                if (placement == FilterPlacement.BOTTOM_BAR) {
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
                        isEditorSheetVisible = true
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = if (placement == FilterPlacement.BOTTOM_BAR) {
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
                    .pointerInput(canTriggerMainGestures, addGesture, settingsGesture) {
                        detectHorizontalDragGestures(
                            onDragStart = { horizontalDragAccumulator = 0f },
                            onDragEnd = { horizontalDragAccumulator = 0f },
                            onDragCancel = { horizontalDragAccumulator = 0f },
                            onHorizontalDrag = { _, dragAmount ->
                                if (!canTriggerMainGestures) return@detectHorizontalDragGestures
                                horizontalDragAccumulator += dragAmount
                                if (horizontalDragAccumulator <= -95f && isDirectionAssigned(GestureDirection.SWIPE_LEFT)) {
                                    horizontalDragAccumulator = 0f
                                    triggerGestureDirection(GestureDirection.SWIPE_LEFT)
                                } else if (horizontalDragAccumulator >= 95f && isDirectionAssigned(GestureDirection.SWIPE_RIGHT)) {
                                    horizontalDragAccumulator = 0f
                                    triggerGestureDirection(GestureDirection.SWIPE_RIGHT)
                                }
                            }
                        )
                    }
            ) {
                if (uiState.items.isEmpty() && !uiState.isLoading) {
                    val onlyLowStockSelected = uiState.selectedFilterIds.size == 1 &&
                        uiState.filters.firstOrNull { it.id in uiState.selectedFilterIds }?.isLowStockFilter == true
                    EmptyInventoryState(
                        isLowStockFilter = onlyLowStockSelected,
                        hasSearchQuery = uiState.searchQuery.isNotBlank(),
                        addItemGesture = addGesture,
                        onTriggerGesture = triggerGestureDirection,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 14.dp,
                            end = 14.dp,
                            top = 8.dp,
                            bottom = 104.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (uiState.settings.groupByCategory) {
                            groupedCategories.forEach { (categoryName, categoryItems) ->
                                item(
                                    key = "category_header_$categoryName",
                                    contentType = "category_header"
                                ) {
                                    CategoryGroupHeader(
                                        categoryName = categoryName,
                                        itemCount = categoryItems.size
                                    )
                                }
                                items(
                                    items = categoryItems,
                                    key = { it.id },
                                    contentType = { "inventory_item" }
                                ) { item ->
                                    InventoryItemRow(
                                        item = item,
                                        onIncrement = onIncrementItem,
                                        onDecrement = onDecrementItem,
                                        onToggleOpened = onToggleOpenedItem,
                                        onEditRequest = onEditItem
                                    )
                                }
                            }
                        } else {
                            items(
                                items = uiState.items,
                                key = { it.id },
                                contentType = { "inventory_item" }
                            ) { item ->
                                InventoryItemRow(
                                    item = item,
                                    onIncrement = onIncrementItem,
                                    onDecrement = onDecrementItem,
                                    onToggleOpened = onToggleOpenedItem,
                                    onEditRequest = onEditItem
                                )
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
                        modifier = Modifier
                            .fillMaxSize(),
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
                onSave = { id, name, category, quantity, unit, minQuantity, isOpened, note ->
                    viewModel.saveItem(
                        existingId = id,
                        name = name,
                        category = category,
                        quantity = quantity,
                        unit = unit,
                        minQuantity = minQuantity,
                        isOpened = isOpened,
                        note = note
                    )
                    isEditorSheetVisible = false
                    activeSheetItem = null
                },
                onDelete = { id ->
                    viewModel.deleteItem(id)
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

@Composable
private fun CategoryGroupHeader(
    categoryName: String,
    itemCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 2.dp, start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = categoryName,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
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
    sortField: SortField,
    sortDirection: SortDirection,
    syncError: String?,
    onSearchQueryChange: (String) -> Unit,
    onFilterSelected: (String) -> Unit,
    onSortFieldChange: (SortField) -> Unit,
    onSortDirectionChange: (SortDirection) -> Unit,
    onOpenSettings: () -> Unit,
    onTriggerGesture: (GestureDirection) -> Unit
) {
    var isSortMenuExpanded by remember { mutableStateOf(false) }
    var isFilterDropdownExpanded by remember { mutableStateOf(false) }
    var isCollapsibleOpen by rememberSaveable { mutableStateOf(false) }
    var verticalDragAcc by remember { mutableFloatStateOf(0f) }
    var horizontalDragAcc by remember { mutableFloatStateOf(0f) }

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
        tonalElevation = 2.dp,
        modifier = Modifier
            .pointerInput(onTriggerGesture) {
                detectVerticalDragGestures(
                    onDragStart = { verticalDragAcc = 0f },
                    onDragEnd = { verticalDragAcc = 0f },
                    onDragCancel = { verticalDragAcc = 0f },
                    onVerticalDrag = { _, dragAmount ->
                        verticalDragAcc += dragAmount
                        if (verticalDragAcc <= -90f) {
                            verticalDragAcc = 0f
                            onTriggerGesture(GestureDirection.SWIPE_UP)
                        } else if (verticalDragAcc >= 90f) {
                            verticalDragAcc = 0f
                            onTriggerGesture(GestureDirection.SWIPE_DOWN)
                        }
                    }
                )
            }
            .pointerInput(onTriggerGesture) {
                detectHorizontalDragGestures(
                    onDragStart = { horizontalDragAcc = 0f },
                    onDragEnd = { horizontalDragAcc = 0f },
                    onDragCancel = { horizontalDragAcc = 0f },
                    onHorizontalDrag = { _, dragAmount ->
                        horizontalDragAcc += dragAmount
                        if (horizontalDragAcc <= -90f) {
                            horizontalDragAcc = 0f
                            onTriggerGesture(GestureDirection.SWIPE_LEFT)
                        } else if (horizontalDragAcc >= 90f) {
                            horizontalDragAcc = 0f
                            onTriggerGesture(GestureDirection.SWIPE_RIGHT)
                        }
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
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
                                val arrow = if (direction == SortDirection.ASC) "↑" else "↓"
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "${field.label} (${direction.label} $arrow)",
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InventoryItemRow(
    item: InventoryItem,
    onIncrement: (InventoryItem) -> Unit,
    onDecrement: (InventoryItem) -> Unit,
    onToggleOpened: (InventoryItem) -> Unit,
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
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = if (isOutOfStock) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.alpha(if (isOutOfStock) 0.58f else 1f)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${item.category} • ${item.unit}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.alpha(if (isOutOfStock) 0.58f else 1f)
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.height(22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OpenedStatusBadge(
                        isOpened = item.isOpened,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onToggleOpened(item)
                        }
                    )

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

@Composable
private fun OpenedStatusBadge(
    isOpened: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isOpened) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.7f)
    }
    val txtColor = if (isOpened) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f)
    }

    Surface(
        color = bgColor,
        contentColor = txtColor,
        shape = RoundedCornerShape(6.dp),
        border = if (isOpened) {
            BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
        } else {
            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        },
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
                text = if (isOpened) "Bontott ✓" else "Bontatlan",
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
    onTriggerGesture: (GestureDirection) -> Unit,
    modifier: Modifier = Modifier
) {
    var verticalDragAcc by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .pointerInput(onTriggerGesture) {
                detectVerticalDragGestures(
                    onDragStart = { verticalDragAcc = 0f },
                    onDragEnd = { verticalDragAcc = 0f },
                    onDragCancel = { verticalDragAcc = 0f },
                    onVerticalDrag = { _, dragAmount ->
                        verticalDragAcc += dragAmount
                        if (verticalDragAcc <= -90f) {
                            verticalDragAcc = 0f
                            onTriggerGesture(GestureDirection.SWIPE_UP)
                        } else if (verticalDragAcc >= 90f) {
                            verticalDragAcc = 0f
                            onTriggerGesture(GestureDirection.SWIPE_DOWN)
                        }
                    }
                )
            }
            .padding(32.dp),
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
                    text = "Tipp: húzd a képernyőt (${addItemGesture.label}) új termék gyors felvételéhez!",
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
        note: String
    ) -> Unit,
    onDelete: (String) -> Unit
) {
    val defaultCategoryLabel = productCategories.firstOrNull()?.label ?: "Hűtő"

    var name by rememberSaveable(initialItem?.id) {
        mutableStateOf(initialItem?.name ?: "")
    }
    var selectedCategory by rememberSaveable(initialItem?.id) {
        mutableStateOf(initialItem?.category ?: defaultCategoryLabel)
    }
    var selectedUnit by rememberSaveable(initialItem?.id) {
        mutableStateOf(initialItem?.unit ?: InventoryUnits.DB)
    }
    var stepSize by rememberSaveable(initialItem?.id) {
        mutableDoubleStateOf(settings.effectiveStepForUnit(initialItem?.unit ?: InventoryUnits.DB))
    }
    var quantity by rememberSaveable(initialItem?.id) {
        mutableDoubleStateOf(initialItem?.quantity ?: settings.effectiveStepForUnit(InventoryUnits.DB))
    }
    var minQuantity by rememberSaveable(initialItem?.id) {
        mutableDoubleStateOf(initialItem?.minQuantity ?: settings.defaultMinQuantity)
    }
    var isOpened by rememberSaveable(initialItem?.id) {
        mutableStateOf(initialItem?.isOpened ?: false)
    }
    var note by rememberSaveable(initialItem?.id) {
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

    val quickSuggestions = remember(normalizedTypedName, allItems, initialItem?.id) {
        if (initialItem != null) {
            emptyList()
        } else {
            val combined = buildList {
                allItems.forEach { existing ->
                    add(QuickSuggestion(existing.name, existing.category, existing.unit))
                }
                addAll(CommonHouseholdSuggestions.presets)
            }.distinctBy { it.name.trim().lowercase() }

            if (normalizedTypedName.isBlank()) {
                val existingKeys = allItems.map { it.exactMatchKey }.toSet()
                combined.filter { it.name.trim().lowercase() !in existingKeys }.take(8)
            } else {
                combined.filter { suggestion ->
                    val key = suggestion.name.trim().lowercase()
                    key != normalizedTypedName && key.contains(normalizedTypedName)
                }.take(8)
            }
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
                    onClick = { onDelete(initialItem.id) },
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
                                val matchingCat = productCategories.firstOrNull {
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

        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isOpened = !isOpened }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                    Text(
                        text = "Megbontott termék (Bontott jelvény)",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "Jelöld be, ha a csomagolás már fel van bontva",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isOpened,
                    onCheckedChange = { isOpened = it }
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Kategória",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            CompactChipFlowRow {
                productCategories.forEach { category ->
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
    var newFilterName by rememberSaveable { mutableStateOf("") }
    var filterBeingRenamed by remember { mutableStateOf<FilterItem?>(null) }
    var renameText by remember { mutableStateOf("") }

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
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text(
            text = "Beállítások",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )

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

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Gesztusok és nézet",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )

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
                        text = "A készletlistában kategóriánként külön fejléc alatt jelennek meg a termékek",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = settings.groupByCategory,
                    onCheckedChange = onGroupByCategoryChange
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Új termék felvétele gesztussal",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
                Text(
                    text = "Válaszd ki, merre húzva a főképernyőt nyíljon meg az új termék felvétele:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    text = "Beállítások megnyitása gesztussal",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
                Text(
                    text = "Válaszd ki, merre húzva a főképernyőt nyíljanak meg a beállítások:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
        }

        HorizontalDivider()

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Kezdőképernyő Widgetek",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = "A KészletBarát két natív widgetet kínál, amelyeket közvetlenül a telefonod főképernyőjére tűzhetsz ki (vagy a főképernyőn hosszan nyomva a Widgetek menüből is hozzáadhatod őket):",
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
                        text = "Megjeleníti a főképernyőn az elfogyott és kifogyóban lévő tételek számát, valamint a legfontosabb beszerzendő termékeket.",
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
                        Text("Kell venni widget kitűzése a főképernyőre")
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
                        text = "Kompakt sáv a főképernyőre, amellyel egyetlen érintéssel megnyithatod az Új termék felvételét vagy a Kell venni listát.",
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
                        Text("Gyorsgombok widget kitűzése a főképernyőre")
                    }
                }
            }
        }

        HorizontalDivider()

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Mennyiség lépésköze",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = "Meghatározza, hogy a + és - gombok milyen értékkel növeljék vagy csökkentsék a mennyiséget.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                            text = "Ha be van kapcsolva, db és csomag esetén továbbra is 1-gyel léptet",
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

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                Text(
                    text = "Alapértelmezett minimum küszöbérték",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = "Új termék felvételekor ez az érték lesz előre beírva.",
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

        HorizontalDivider()

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
