package hu.keszletbarat.app.data

import android.content.Context
import androidx.compose.runtime.Immutable
import hu.keszletbarat.app.model.InventoryUnits
import hu.keszletbarat.app.model.SortDirection
import hu.keszletbarat.app.model.SortField
import hu.keszletbarat.app.model.cleanQuantity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemeMode(val label: String) {
    SYSTEM("Rendszer"),
    LIGHT("Világos"),
    DARK("Sötét")
}

enum class AppColorPalette(val label: String, val previewHex: Long) {
    DYNAMIC("Dinamikus", 0xFF6750A4),
    EMERALD("Smaragd", 0xFF006C4C),
    OCEAN("Óceán", 0xFF0061A4),
    SUNSET("Naplemente", 0xFF9C4146),
    LAVENDER("Levendula", 0xFF6B538C),
    ROSE("Rózsa", 0xFF984061),
    GOLD("Borostyán", 0xFF785900),
    TEAL("Türkiz", 0xFF006874),
    INDIGO("Indigó", 0xFF4355B9),
    FOREST("Erdő", 0xFF2E6C00),
    COFFEE("Kávé", 0xFF705845),
    GRAPHITE("Grafit", 0xFF45474A),
    CRIMSON("Bíbor", 0xFFB3261E),
    CORAL("Korall", 0xFFB84327),
    MINT("Menta", 0xFF006B58),
    SKY("Égszínkék", 0xFF006493),
    PLUM("Szilva", 0xFF7B2D7E),
    OLIVE("Olíva", 0xFF556500),
    CHOCOLATE("Csokoládé", 0xFF834C24),
    SLATE("Palaszürke", 0xFF3E5F73)
}

enum class FilterPlacement(val label: String) {
    TOP_BAR("Felül"),
    BOTTOM_BAR("Alul"),
    LEFT_EDGE("Bal oldalsáv"),
    RIGHT_EDGE("Jobb oldalsáv"),
    COLLAPSIBLE_PANEL("Kinyithatós menü"),
    TOP_DROPDOWN("Felső gomb"),
    HIDDEN("Kikapcsolva")
}

enum class AnimationSpeed(val label: String, val durationMillis: Int) {
    NORMAL("Normál", 230),
    BOUNCY("Rugós", 280),
    ZOOM("Nagyító", 220),
    FLOAT("Lebegő", 360),
    NONE("Kikapcsolva", 0)
}

enum class GestureDirection(val label: String) {
    NONE("Kikapcsolva"),
    SWIPE_UP("Felfelé ↑"),
    SWIPE_DOWN("Lefelé ↓"),
    SWIPE_LEFT("Balra ←"),
    SWIPE_RIGHT("Jobbra →")
}

enum class ItemLayoutMode(val label: String) {
    LIST("Listás"),
    GRID_CARDS("Kártyás (2 oszlop)"),
    COMPACT("Kompakt lista")
}

enum class UiScale(val label: String, val scaleFactor: Float) {
    SMALL("Kicsi", 0.90f),
    NORMAL("Normál", 1.0f),
    LARGE("Nagy", 1.12f)
}

enum class ItemSwipeAction(val label: String) {
    NONE("Kikapcsolva"),
    ZERO_QUANTITY("Lenullázás (0)"),
    TOGGLE_OPENED("Bontott váltás")
}

enum class BackupFormat(val label: String, val extension: String, val mimeType: String) {
    JSON("JSON (.json)", "json", "application/json"),
    CSV("CSV (.csv)", "csv", "text/csv")
}

@Immutable
data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val colorPalette: AppColorPalette = AppColorPalette.DYNAMIC,
    val filterPlacement: FilterPlacement = FilterPlacement.TOP_BAR,
    val animationSpeed: AnimationSpeed = AnimationSpeed.BOUNCY,
    val itemLayoutMode: ItemLayoutMode = ItemLayoutMode.LIST,
    val uiScale: UiScale = UiScale.NORMAL,
    val hideUnopenedBadge: Boolean = false,
    val autoResetOpenedOnZero: Boolean = true,
    val autoHideTopBarOnScroll: Boolean = false,
    val itemSwipeRightAction: ItemSwipeAction = ItemSwipeAction.NONE,
    val itemSwipeLeftAction: ItemSwipeAction = ItemSwipeAction.NONE,
    val backupFormat: BackupFormat = BackupFormat.JSON,
    val addItemGesture: GestureDirection = GestureDirection.SWIPE_UP,
    val openSettingsGesture: GestureDirection = GestureDirection.SWIPE_DOWN,
    val groupByCategory: Boolean = false,
    val defaultMinQuantity: Double = 1.0,
    val quantityStep: Double = 1.0,
    val fractionalOnlyForWeightAndVolume: Boolean = false,
    val sortField: SortField = SortField.NAME,
    val sortDirection: SortDirection = SortDirection.ASC
) {
    fun effectiveStepForUnit(unit: String): Double {
        return if (fractionalOnlyForWeightAndVolume && !InventoryUnits.isFractionalUnit(unit)) {
            1.0
        } else {
            quantityStep
        }
    }

    companion object {
        val availableSteps = listOf(1.0, 0.5, 0.25, 0.1)
    }
}

class SettingsRepository(context: Context) {

    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
        _settings.value = _settings.value.copy(themeMode = mode)
    }

    fun setColorPalette(palette: AppColorPalette) {
        prefs.edit().putString(KEY_COLOR_PALETTE, palette.name).apply()
        _settings.value = _settings.value.copy(colorPalette = palette)
    }

    fun setFilterPlacement(placement: FilterPlacement) {
        prefs.edit().putString(KEY_FILTER_PLACEMENT, placement.name).apply()
        _settings.value = _settings.value.copy(filterPlacement = placement)
    }

    fun setAnimationSpeed(speed: AnimationSpeed) {
        prefs.edit().putString(KEY_ANIMATION_SPEED, speed.name).apply()
        _settings.value = _settings.value.copy(animationSpeed = speed)
    }

    fun setItemLayoutMode(mode: ItemLayoutMode) {
        prefs.edit().putString(KEY_ITEM_LAYOUT_MODE, mode.name).apply()
        _settings.value = _settings.value.copy(itemLayoutMode = mode)
    }

    fun setUiScale(scale: UiScale) {
        prefs.edit().putString(KEY_UI_SCALE, scale.name).apply()
        _settings.value = _settings.value.copy(uiScale = scale)
    }

    fun setHideUnopenedBadge(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_HIDE_UNOPENED_BADGE, enabled).apply()
        _settings.value = _settings.value.copy(hideUnopenedBadge = enabled)
    }

    fun setAutoResetOpenedOnZero(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_RESET_OPENED_ON_ZERO, enabled).apply()
        _settings.value = _settings.value.copy(autoResetOpenedOnZero = enabled)
    }

    fun setAutoHideTopBarOnScroll(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_HIDE_TOP_BAR_SCROLL, enabled).apply()
        _settings.value = _settings.value.copy(autoHideTopBarOnScroll = enabled)
    }

    fun setItemSwipeRightAction(action: ItemSwipeAction) {
        prefs.edit().putString(KEY_ITEM_SWIPE_RIGHT, action.name).apply()
        _settings.value = _settings.value.copy(itemSwipeRightAction = action)
    }

    fun setItemSwipeLeftAction(action: ItemSwipeAction) {
        prefs.edit().putString(KEY_ITEM_SWIPE_LEFT, action.name).apply()
        _settings.value = _settings.value.copy(itemSwipeLeftAction = action)
    }

    fun setBackupFormat(format: BackupFormat) {
        prefs.edit().putString(KEY_BACKUP_FORMAT, format.name).apply()
        _settings.value = _settings.value.copy(backupFormat = format)
    }

    fun setAddItemGesture(direction: GestureDirection) {
        val current = _settings.value
        val updatedSettingsGesture = if (direction != GestureDirection.NONE && current.openSettingsGesture == direction) {
            GestureDirection.NONE
        } else {
            current.openSettingsGesture
        }
        prefs.edit()
            .putString(KEY_ADD_ITEM_GESTURE, direction.name)
            .putString(KEY_OPEN_SETTINGS_GESTURE, updatedSettingsGesture.name)
            .apply()
        _settings.value = current.copy(
            addItemGesture = direction,
            openSettingsGesture = updatedSettingsGesture
        )
    }

    fun setOpenSettingsGesture(direction: GestureDirection) {
        val current = _settings.value
        val updatedAddGesture = if (direction != GestureDirection.NONE && current.addItemGesture == direction) {
            GestureDirection.NONE
        } else {
            current.addItemGesture
        }
        prefs.edit()
            .putString(KEY_OPEN_SETTINGS_GESTURE, direction.name)
            .putString(KEY_ADD_ITEM_GESTURE, updatedAddGesture.name)
            .apply()
        _settings.value = current.copy(
            openSettingsGesture = direction,
            addItemGesture = updatedAddGesture
        )
    }

    fun setGroupByCategory(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_GROUP_BY_CATEGORY, enabled).apply()
        _settings.value = _settings.value.copy(groupByCategory = enabled)
    }

    fun setDefaultMinQuantity(value: Double) {
        val safeValue = value.coerceAtLeast(0.0).cleanQuantity()
        prefs.edit().putString(KEY_DEFAULT_MIN_QUANTITY, safeValue.toString()).apply()
        _settings.value = _settings.value.copy(defaultMinQuantity = safeValue)
    }

    fun setQuantityStep(step: Double) {
        val safeStep = step.coerceAtLeast(0.05).cleanQuantity()
        prefs.edit().putString(KEY_QUANTITY_STEP, safeStep.toString()).apply()
        _settings.value = _settings.value.copy(quantityStep = safeStep)
    }

    fun setFractionalOnlyForWeightAndVolume(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_FRACTIONAL_ONLY_WEIGHT_VOLUME, enabled).apply()
        _settings.value = _settings.value.copy(fractionalOnlyForWeightAndVolume = enabled)
    }

    fun setSortField(field: SortField) {
        prefs.edit().putString(KEY_SORT_FIELD, field.name).apply()
        _settings.value = _settings.value.copy(sortField = field)
    }

    fun setSortDirection(direction: SortDirection) {
        prefs.edit().putString(KEY_SORT_DIRECTION, direction.name).apply()
        _settings.value = _settings.value.copy(sortDirection = direction)
    }

    private fun loadSettings(): AppSettings {
        val themeName = prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
        val themeMode = runCatching { ThemeMode.valueOf(themeName) }.getOrDefault(ThemeMode.SYSTEM)

        val paletteName = prefs.getString(KEY_COLOR_PALETTE, AppColorPalette.DYNAMIC.name) ?: AppColorPalette.DYNAMIC.name
        val colorPalette = runCatching { AppColorPalette.valueOf(paletteName) }.getOrDefault(AppColorPalette.DYNAMIC)

        val placementName = prefs.getString(KEY_FILTER_PLACEMENT, FilterPlacement.TOP_BAR.name) ?: FilterPlacement.TOP_BAR.name
        val filterPlacement = runCatching { FilterPlacement.valueOf(placementName) }.getOrDefault(FilterPlacement.TOP_BAR)

        val animName = prefs.getString(KEY_ANIMATION_SPEED, AnimationSpeed.BOUNCY.name) ?: AnimationSpeed.BOUNCY.name
        val animationSpeed = runCatching { AnimationSpeed.valueOf(animName) }.getOrDefault(AnimationSpeed.BOUNCY)

        val layoutName = prefs.getString(KEY_ITEM_LAYOUT_MODE, ItemLayoutMode.LIST.name) ?: ItemLayoutMode.LIST.name
        val itemLayoutMode = runCatching { ItemLayoutMode.valueOf(layoutName) }.getOrDefault(ItemLayoutMode.LIST)

        val scaleName = prefs.getString(KEY_UI_SCALE, UiScale.NORMAL.name) ?: UiScale.NORMAL.name
        val uiScale = runCatching { UiScale.valueOf(scaleName) }.getOrDefault(UiScale.NORMAL)

        val hideUnopenedBadge = prefs.getBoolean(KEY_HIDE_UNOPENED_BADGE, false)
        val autoResetOpenedOnZero = prefs.getBoolean(KEY_AUTO_RESET_OPENED_ON_ZERO, true)
        val autoHideTopBarOnScroll = prefs.getBoolean(KEY_AUTO_HIDE_TOP_BAR_SCROLL, false)

        val swipeRightName = prefs.getString(KEY_ITEM_SWIPE_RIGHT, ItemSwipeAction.NONE.name) ?: ItemSwipeAction.NONE.name
        val itemSwipeRightAction = runCatching { ItemSwipeAction.valueOf(swipeRightName) }.getOrDefault(ItemSwipeAction.NONE)

        val swipeLeftName = prefs.getString(KEY_ITEM_SWIPE_LEFT, ItemSwipeAction.NONE.name) ?: ItemSwipeAction.NONE.name
        val itemSwipeLeftAction = runCatching { ItemSwipeAction.valueOf(swipeLeftName) }.getOrDefault(ItemSwipeAction.NONE)

        val backupFormatName = prefs.getString(KEY_BACKUP_FORMAT, BackupFormat.JSON.name) ?: BackupFormat.JSON.name
        val backupFormat = runCatching { BackupFormat.valueOf(backupFormatName) }.getOrDefault(BackupFormat.JSON)

        val addGestureName = prefs.getString(KEY_ADD_ITEM_GESTURE, GestureDirection.SWIPE_UP.name) ?: GestureDirection.SWIPE_UP.name
        val addItemGesture = runCatching { GestureDirection.valueOf(addGestureName) }.getOrDefault(GestureDirection.SWIPE_UP)

        val settingsGestureName = prefs.getString(KEY_OPEN_SETTINGS_GESTURE, GestureDirection.SWIPE_DOWN.name) ?: GestureDirection.SWIPE_DOWN.name
        val openSettingsGesture = runCatching { GestureDirection.valueOf(settingsGestureName) }.getOrDefault(GestureDirection.SWIPE_DOWN)

        val groupByCategory = prefs.getBoolean(KEY_GROUP_BY_CATEGORY, false)

        val defaultMinQuantity = prefs.getString(KEY_DEFAULT_MIN_QUANTITY, "1.0")
            ?.toDoubleOrNull()
            ?.coerceAtLeast(0.0)
            ?.cleanQuantity() ?: 1.0

        val quantityStep = prefs.getString(KEY_QUANTITY_STEP, "1.0")
            ?.toDoubleOrNull()
            ?.coerceAtLeast(0.05)
            ?.cleanQuantity() ?: 1.0

        val fractionalOnly = prefs.getBoolean(KEY_FRACTIONAL_ONLY_WEIGHT_VOLUME, false)

        val sortFieldName = prefs.getString(KEY_SORT_FIELD, SortField.NAME.name) ?: SortField.NAME.name
        val sortField = runCatching { SortField.valueOf(sortFieldName) }.getOrDefault(SortField.NAME)

        val sortDirectionName = prefs.getString(KEY_SORT_DIRECTION, SortDirection.ASC.name) ?: SortDirection.ASC.name
        val sortDirection = runCatching { SortDirection.valueOf(sortDirectionName) }.getOrDefault(SortDirection.ASC)

        return AppSettings(
            themeMode = themeMode,
            colorPalette = colorPalette,
            filterPlacement = filterPlacement,
            animationSpeed = animationSpeed,
            itemLayoutMode = itemLayoutMode,
            uiScale = uiScale,
            hideUnopenedBadge = hideUnopenedBadge,
            autoResetOpenedOnZero = autoResetOpenedOnZero,
            autoHideTopBarOnScroll = autoHideTopBarOnScroll,
            itemSwipeRightAction = itemSwipeRightAction,
            itemSwipeLeftAction = itemSwipeLeftAction,
            backupFormat = backupFormat,
            addItemGesture = addItemGesture,
            openSettingsGesture = openSettingsGesture,
            groupByCategory = groupByCategory,
            defaultMinQuantity = defaultMinQuantity,
            quantityStep = quantityStep,
            fractionalOnlyForWeightAndVolume = fractionalOnly,
            sortField = sortField,
            sortDirection = sortDirection
        )
    }

    companion object {
        private const val PREFS_NAME = "keszletbarat_settings"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_COLOR_PALETTE = "color_palette"
        private const val KEY_FILTER_PLACEMENT = "filter_placement"
        private const val KEY_ANIMATION_SPEED = "animation_speed"
        private const val KEY_ITEM_LAYOUT_MODE = "item_layout_mode"
        private const val KEY_UI_SCALE = "ui_scale"
        private const val KEY_HIDE_UNOPENED_BADGE = "hide_unopened_badge"
        private const val KEY_AUTO_RESET_OPENED_ON_ZERO = "auto_reset_opened_on_zero"
        private const val KEY_AUTO_HIDE_TOP_BAR_SCROLL = "auto_hide_top_bar_scroll"
        private const val KEY_ITEM_SWIPE_RIGHT = "item_swipe_right"
        private const val KEY_ITEM_SWIPE_LEFT = "item_swipe_left"
        private const val KEY_BACKUP_FORMAT = "backup_format"
        private const val KEY_ADD_ITEM_GESTURE = "add_item_gesture"
        private const val KEY_OPEN_SETTINGS_GESTURE = "open_settings_gesture"
        private const val KEY_GROUP_BY_CATEGORY = "group_by_category"
        private const val KEY_DEFAULT_MIN_QUANTITY = "default_min_quantity"
        private const val KEY_QUANTITY_STEP = "quantity_step"
        private const val KEY_FRACTIONAL_ONLY_WEIGHT_VOLUME = "fractional_only_weight_volume"
        private const val KEY_SORT_FIELD = "sort_field"
        private const val KEY_SORT_DIRECTION = "sort_direction"
    }
}
