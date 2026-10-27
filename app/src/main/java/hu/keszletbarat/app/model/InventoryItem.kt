package hu.keszletbarat.app.model

import androidx.compose.runtime.Immutable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import java.text.Normalizer
import java.util.UUID
import kotlin.math.roundToInt

@Immutable
data class InventoryItem(
    @DocumentId
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val category: String = "Hűtő",
    val quantity: Double = 1.0,
    val unit: String = InventoryUnits.DB,
    val minQuantity: Double = 1.0,
    val updatedAt: Long = System.currentTimeMillis()
) {
    @get:Exclude
    val isOutOfStock: Boolean = quantity <= 0.0

    @get:Exclude
    val isLowStock: Boolean = quantity <= 0.0 || quantity <= minQuantity

    @get:Exclude
    val formattedQuantity: String = quantity.formatQuantity()

    @get:Exclude
    val normalizedName: String = name.normalizeForSearch()

    @get:Exclude
    val lowercaseUnit: String = unit.lowercase()
}

@Immutable
data class FilterItem(
    val id: String,
    val label: String
) {
    val isAllFilter: Boolean
        get() = id == ID_ALL || id == "__all__"

    val isLowStockFilter: Boolean
        get() = id == ID_LOW_STOCK || id == "__low_stock__"

    val isFallbackCategory: Boolean
        get() = id == ID_FALLBACK

    val isProductCategory: Boolean
        get() = !isAllFilter && !isLowStockFilter

    val isDeletable: Boolean
        get() = isProductCategory && !isFallbackCategory

    companion object {
        const val ID_ALL = "sys_all"
        const val ID_LOW_STOCK = "sys_low_stock"
        const val ID_FALLBACK = "egyeb"

        val defaultFilters: List<FilterItem> = listOf(
            FilterItem(id = ID_ALL, label = "Összes"),
            FilterItem(id = ID_LOW_STOCK, label = "Kell venni (Kifogyott)"),
            FilterItem(id = "huto", label = "Hűtő"),
            FilterItem(id = "spajz", label = "Spájz"),
            FilterItem(id = "tisztitoszer", label = "Tisztítószer"),
            FilterItem(id = "haztartas", label = "Háztartás"),
            FilterItem(id = ID_FALLBACK, label = "Egyéb")
        )

        val defaultCategoryLabels: Set<String> = defaultFilters
            .filter { it.isProductCategory }
            .map { it.label }
            .toSet()
    }
}

enum class SortField(val label: String) {
    NAME("ABC"),
    QUANTITY("Mennyiség"),
    UNIT("Mértékegység")
}

enum class SortDirection(val label: String) {
    ASC("Növekvő"),
    DESC("Csökkenő")
}

fun Double.cleanQuantity(): Double {
    return (this * 100.0).roundToInt() / 100.0
}

fun Double.formatQuantity(): String {
    val cleaned = (this * 100.0).roundToInt() / 100.0
    val asInt = cleaned.toInt()
    if (cleaned == asInt.toDouble()) {
        return asInt.toString()
    }
    val text = cleaned.toString()
    return if (text.endsWith("0") && text.contains('.')) {
        text.trimEnd('0').trimEnd('.')
    } else {
        text
    }
}

private val DIACRITICS_REGEX = "\\p{InCombiningDiacriticalMarks}+".toRegex()

fun String.normalizeForSearch(): String {
    val trimmed = this.trim().lowercase()
    if (trimmed.isEmpty()) return ""
    val normalized = Normalizer.normalize(trimmed, Normalizer.Form.NFD)
    return DIACRITICS_REGEX.replace(normalized, "")
}

object InventoryUnits {
    const val DB = "db"
    const val CSOMAG = "csomag"
    const val LITER = "liter"
    const val KG = "kg"
    const val BONTOTT = "bontott"

    val all = listOf(DB, CSOMAG, LITER, KG, BONTOTT)

    fun isFractionalUnit(unit: String): Boolean {
        return unit.equals(KG, ignoreCase = true) || unit.equals(LITER, ignoreCase = true)
    }
}
