package hu.keszletbarat.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.keszletbarat.app.data.AnimationSpeed
import hu.keszletbarat.app.data.AppColorPalette
import hu.keszletbarat.app.data.AppSettings
import hu.keszletbarat.app.data.FilterPlacement
import hu.keszletbarat.app.data.ThemeMode
import hu.keszletbarat.app.model.FilterItem
import hu.keszletbarat.app.model.InventoryItem
import hu.keszletbarat.app.model.InventoryUnits
import hu.keszletbarat.app.model.SortDirection
import hu.keszletbarat.app.model.SortField
import hu.keszletbarat.app.model.cleanQuantity
import hu.keszletbarat.app.model.formatQuantity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    viewModel: InventoryViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var activeSheetItem by remember { mutableStateOf<InventoryItem?>(null) }
    var isEditorSheetVisible by rememberSaveable { mutableStateOf(false) }
    var isSettingsSheetVisible by rememberSaveable { mutableStateOf(false) }

    val onIncrementItem = remember(viewModel) { viewModel::incrementQuantity }
    val onDecrementItem = remember(viewModel) { viewModel::decrementQuantity }
    val onEditItem = remember {
        { item: InventoryItem ->
            activeSheetItem = item
            isEditorSheetVisible = true
        }
    }

    val placement = uiState.settings.filterPlacement
    val animSpeed = uiState.settings.animationSpeed

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                InventoryTopBar(
                    searchQuery = uiState.searchQuery,
                    selectedFilterId = uiState.selectedFilterId,
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
                    onOpenSettings = { isSettingsSheetVisible = true }
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
                                selectedFilterId = uiState.selectedFilterId,
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
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (placement == FilterPlacement.LEFT_EDGE) {
                    EdgeFilterSidebar(
                        filters = uiState.filters,
                        selectedFilterId = uiState.selectedFilterId,
                        onFilterSelected = viewModel::onFilterSelected
                    )
                    VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    if (uiState.items.isEmpty() && !uiState.isLoading) {
                        val activeFilter = uiState.filters.firstOrNull { it.id == uiState.selectedFilterId }
                        EmptyInventoryState(
                            isLowStockFilter = activeFilter?.isLowStockFilter == true,
                            hasSearchQuery = uiState.searchQuery.isNotBlank(),
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
                            items(
                                items = uiState.items,
                                key = { it.id },
                                contentType = { "inventory_item" }
                            ) { item ->
                                InventoryItemRow(
                                    item = item,
                                    onIncrement = onIncrementItem,
                                    onDecrement = onDecrementItem,
                                    onEditRequest = onEditItem
                                )
                            }
                        }
                    }
                }

                if (placement == FilterPlacement.RIGHT_EDGE) {
                    VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    EdgeFilterSidebar(
                        filters = uiState.filters,
                        selectedFilterId = uiState.selectedFilterId,
                        onFilterSelected = viewModel::onFilterSelected
                    )
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
                productCategories = uiState.productCategories,
                settings = uiState.settings,
                onSave = { id, name, category, quantity, unit, minQuantity ->
                    viewModel.saveItem(
                        existingId = id,
                        name = name,
                        category = category,
                        quantity = quantity,
                        unit = unit,
                        minQuantity = minQuantity
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
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(duration)
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(duration)
                    )
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
    selectedFilterId: String,
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
    onOpenSettings: () -> Unit
) {
    var isSortMenuExpanded by remember { mutableStateOf(false) }
    var isFilterDropdownExpanded by remember { mutableStateOf(false) }
    var isCollapsibleOpen by rememberSaveable { mutableStateOf(false) }

    val activeFilter = filters.firstOrNull { it.id == selectedFilterId } ?: filters.firstOrNull()

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
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
                                containerColor = if (activeFilter?.isAllFilter == false) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surfaceContainerHigh
                                },
                                contentColor = if (activeFilter?.isAllFilter == false) {
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
                                val isSelected = filter.id == selectedFilterId
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
                                        isFilterDropdownExpanded = false
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
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalFilterRow(
                        filters = filters,
                        selectedFilterId = selectedFilterId,
                        onFilterSelected = onFilterSelected
                    )
                }
                FilterPlacement.TOP_DROPDOWN -> {
                    if (activeFilter != null && !activeFilter.isAllFilter) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Aktív szűrő: ${activeFilter.label}",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            TextButton(
                                onClick = { onFilterSelected(FilterItem.ID_ALL) },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("Összes mutatása", style = MaterialTheme.typography.labelMedium)
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
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { isCollapsibleOpen = !isCollapsibleOpen }
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
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
                                        text = "Szűrő: ${activeFilter?.label ?: "Összes"}",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
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
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        filters.forEach { filter ->
                                            StableFilterChip(
                                                filter = filter,
                                                isSelected = filter.id == selectedFilterId,
                                                onClick = {
                                                    onFilterSelected(filter.id)
                                                    isCollapsibleOpen = false
                                                }
                                            )
                                        }
                                    }
                                }
                            } else {
                                AnimatedVisibility(
                                    visible = isCollapsibleOpen,
                                    enter = expandVertically(tween(animationSpeed.durationMillis)),
                                    exit = shrinkVertically(tween(animationSpeed.durationMillis))
                                ) {
                                    Column {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        FlowRow(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            filters.forEach { filter ->
                                                StableFilterChip(
                                                    filter = filter,
                                                    isSelected = filter.id == selectedFilterId,
                                                    onClick = {
                                                        onFilterSelected(filter.id)
                                                        isCollapsibleOpen = false
                                                    }
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

@Composable
private fun HorizontalFilterRow(
    filters: List<FilterItem>,
    selectedFilterId: String,
    onFilterSelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = filters,
            key = { it.id }
        ) { filter ->
            StableFilterChip(
                filter = filter,
                isSelected = selectedFilterId == filter.id,
                onClick = { onFilterSelected(filter.id) }
            )
        }
    }
}

@Composable
private fun EdgeFilterSidebar(
    filters: List<FilterItem>,
    selectedFilterId: String,
    onFilterSelected: (String) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier
            .width(104.dp)
            .fillMaxHeight()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = filters,
                key = { it.id }
            ) { filter ->
                val isSelected = selectedFilterId == filter.id
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
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onFilterSelected(filter.id) }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    ) {
                        Text(
                            text = filter.label,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
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

@Composable
private fun StableFilterChip(
    filter: FilterItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isLowStockTab = filter.isLowStockFilter
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        modifier = Modifier.height(36.dp),
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
                    modifier = Modifier.size(16.dp)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InventoryItemRow(
    item: InventoryItem,
    onIncrement: (InventoryItem) -> Unit,
    onDecrement: (InventoryItem) -> Unit,
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
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
                    .alpha(if (isOutOfStock) 0.55f else 1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = if (isOutOfStock) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.height(22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${item.category} • ${item.unit}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
                    modifier = Modifier.width(56.dp)
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
            modifier = Modifier.padding(horizontal = 6.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun EmptyInventoryState(
    isLowStockFilter: Boolean,
    hasSearchQuery: Boolean,
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
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ItemEditorSheetContent(
    initialItem: InventoryItem?,
    productCategories: List<FilterItem>,
    settings: AppSettings,
    onSave: (
        id: String?,
        name: String,
        category: String,
        quantity: Double,
        unit: String,
        minQuantity: Double
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

    LaunchedEffect(selectedUnit) {
        if (initialItem == null) {
            stepSize = settings.effectiveStepForUnit(selectedUnit)
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
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

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Termék neve") },
            placeholder = { Text("pl. Alufólia, Tej, Zsíroldó") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            )
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Kategória",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                productCategories.forEach { category ->
                    val isSelected = selectedCategory.equals(category.label, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category.label },
                        label = {
                            Text(
                                text = category.label,
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

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Mértékegység",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                InventoryUnits.all.forEach { unit ->
                    val isSelected = selectedUnit == unit
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedUnit = unit },
                        label = {
                            Text(
                                text = unit,
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

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Lépésköz (érték-köz)",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AppSettings.availableSteps.forEach { stepOption ->
                    val isSelected = stepSize == stepOption
                    FilterChip(
                        selected = isSelected,
                        onClick = { stepSize = stepOption },
                        label = {
                            Text(
                                text = "±${stepOption.formatQuantity()} $selectedUnit",
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
                    minQuantity
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SettingsSheetContent(
    settings: AppSettings,
    filters: List<FilterItem>,
    onDismiss: () -> Unit,
    onThemeModeChange: (ThemeMode) -> Unit,
    onColorPaletteChange: (AppColorPalette) -> Unit,
    onFilterPlacementChange: (FilterPlacement) -> Unit,
    onAnimationSpeedChange: (AnimationSpeed) -> Unit,
    onMoveFilter: (String, Int) -> Unit,
    onAddFilter: (String) -> Unit,
    onRenameFilter: (String, String) -> Unit,
    onDeleteFilter: (String) -> Unit,
    onResetFilterOrder: () -> Unit,
    onDefaultMinQuantityChange: (Double) -> Unit,
    onQuantityStepChange: (Double) -> Unit,
    onFractionalOnlyForWeightAndVolumeChange: (Boolean) -> Unit
) {
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
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Beállítások",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Megjelenés módja (saját készülék)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ThemeMode.entries.forEach { mode ->
                    val isSelected = settings.themeMode == mode
                    FilterChip(
                        selected = isSelected,
                        onClick = { onThemeModeChange(mode) },
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
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Színpaletta (saját készülék)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                AppColorPalette.entries.forEach { palette ->
                    val selected = settings.colorPalette == palette
                    FilterChip(
                        selected = selected,
                        onClick = { onColorPaletteChange(palette) },
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
                        },
                        label = {
                            Text(
                                text = palette.label,
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
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Animációk sebessége (ablakok, menük)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AnimationSpeed.entries.forEach { speed ->
                    val isSelected = settings.animationSpeed == speed
                    FilterChip(
                        selected = isSelected,
                        onClick = { onAnimationSpeedChange(speed) },
                        label = {
                            Text(
                                text = speed.label,
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
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Szűrők elhelyezkedése a képernyőn",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = "Válaszd ki, hol legyenek elérhetőek a kategória-szűrők a főképernyőn.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterPlacement.entries.forEach { placement ->
                    val isSelected = settings.filterPlacement == placement
                    FilterChip(
                        selected = isSelected,
                        onClick = { onFilterPlacementChange(placement) },
                        label = {
                            Text(
                                text = placement.label,
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

        HorizontalDivider()

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Mennyiség lépésköze (érték-köz)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = "Meghatározza, hogy a + és - gombok milyen értékkel növeljék vagy csökkentsék a mennyiséget (pl. 0.5 fél kg-hoz).",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                AppSettings.availableSteps.forEach { step ->
                    val isSelected = settings.quantityStep == step
                    FilterChip(
                        selected = isSelected,
                        onClick = { onQuantityStepChange(step) },
                        label = {
                            val labelText = when (step) {
                                1.0 -> "1 (Egész)"
                                0.5 -> "0.5 (Fél)"
                                0.25 -> "0.25 (Negyed)"
                                0.1 -> "0.1 (Tized)"
                                else -> step.formatQuantity()
                            }
                            Text(
                                text = labelText,
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
                    text = "Új termék felvételekor ez az érték lesz előre beírva (0 esetén csak akkor jelez kifogyottat, ha 0-ra csökken).",
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

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Szűrők és kategóriák (közös)",
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
