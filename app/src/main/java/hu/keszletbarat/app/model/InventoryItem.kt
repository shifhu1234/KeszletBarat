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
    val isOpened: Boolean = false,
    val isFavorite: Boolean = false,
    val note: String = "",
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
    val exactMatchKey: String = name.trim().lowercase()

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
    UNIT("Mértékegység"),
    OPENED("Bontottság")
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

    val all = listOf(DB, CSOMAG, LITER, KG)

    fun isFractionalUnit(unit: String): Boolean {
        return unit.equals(KG, ignoreCase = true) || unit.equals(LITER, ignoreCase = true)
    }
}

@Immutable
data class QuickSuggestion(
    val name: String,
    val category: String,
    val unit: String
)

object CommonHouseholdSuggestions {
    val presets: List<QuickSuggestion> = listOf(
        QuickSuggestion("Tej 2.8%", "Hűtő", InventoryUnits.LITER),
        QuickSuggestion("Tej 1.5%", "Hűtő", InventoryUnits.LITER),
        QuickSuggestion("Tej 3.5%", "Hűtő", InventoryUnits.LITER),
        QuickSuggestion("Laktózmentes tej", "Hűtő", InventoryUnits.LITER),
        QuickSuggestion("Zabtej", "Hűtő", InventoryUnits.LITER),
        QuickSuggestion("Mandulatej", "Hűtő", InventoryUnits.LITER),
        QuickSuggestion("Tojás", "Hűtő", InventoryUnits.DB),
        QuickSuggestion("Vaj", "Hűtő", InventoryUnits.DB),
        QuickSuggestion("Margarin", "Hűtő", InventoryUnits.DB),
        QuickSuggestion("Trappista sajt", "Hűtő", InventoryUnits.KG),
        QuickSuggestion("Mozzarella", "Hűtő", InventoryUnits.CSOMAG),
        QuickSuggestion("Szeletelt sajt", "Hűtő", InventoryUnits.CSOMAG),
        QuickSuggestion("Krémsajt", "Hűtő", InventoryUnits.DB),
        QuickSuggestion("Túró", "Hűtő", InventoryUnits.CSOMAG),
        QuickSuggestion("Tejföl", "Hűtő", InventoryUnits.DB),
        QuickSuggestion("Natúr joghurt", "Hűtő", InventoryUnits.DB),
        QuickSuggestion("Gyümölcsjoghurt", "Hűtő", InventoryUnits.DB),
        QuickSuggestion("Kefir", "Hűtő", InventoryUnits.DB),
        QuickSuggestion("Főzőtejszín", "Hűtő", InventoryUnits.DB),
        QuickSuggestion("Habtejszín", "Hűtő", InventoryUnits.DB),
        QuickSuggestion("Csirkemell sonka", "Hűtő", InventoryUnits.CSOMAG),
        QuickSuggestion("Pulykamell sonka", "Hűtő", InventoryUnits.CSOMAG),
        QuickSuggestion("Téliszalámi", "Hűtő", InventoryUnits.CSOMAG),
        QuickSuggestion("Virsli", "Hűtő", InventoryUnits.CSOMAG),
        QuickSuggestion("Bacon", "Hűtő", InventoryUnits.CSOMAG),
        QuickSuggestion("Kolbász", "Hűtő", InventoryUnits.KG),
        QuickSuggestion("Csirkemell filé", "Hűtő", InventoryUnits.KG),
        QuickSuggestion("Darált hús", "Hűtő", InventoryUnits.KG),
        QuickSuggestion("Mustár", "Hűtő", InventoryUnits.DB),
        QuickSuggestion("Ketchup", "Hűtő", InventoryUnits.DB),
        QuickSuggestion("Majonéz", "Hűtő", InventoryUnits.DB),
        QuickSuggestion("Piros Arany", "Hűtő", InventoryUnits.DB),
        QuickSuggestion("Leveles tészta", "Hűtő", InventoryUnits.CSOMAG),
        QuickSuggestion("Narancslé", "Hűtő", InventoryUnits.LITER),
        QuickSuggestion("Almalé", "Hűtő", InventoryUnits.LITER),
        QuickSuggestion("Kenyér", "Spájz", InventoryUnits.DB),
        QuickSuggestion("Toast kenyér", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Zsemle", "Spájz", InventoryUnits.DB),
        QuickSuggestion("Kifli", "Spájz", InventoryUnits.DB),
        QuickSuggestion("Finomliszt", "Spájz", InventoryUnits.KG),
        QuickSuggestion("Rétesliszt", "Spájz", InventoryUnits.KG),
        QuickSuggestion("Kristálycukor", "Spájz", InventoryUnits.KG),
        QuickSuggestion("Porcukor", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Nádcukor", "Spájz", InventoryUnits.KG),
        QuickSuggestion("Só", "Spájz", InventoryUnits.KG),
        QuickSuggestion("Napraforgó étolaj", "Spájz", InventoryUnits.LITER),
        QuickSuggestion("Olívaolaj", "Spájz", InventoryUnits.LITER),
        QuickSuggestion("Ecet", "Spájz", InventoryUnits.LITER),
        QuickSuggestion("Jázmin rizs", "Spájz", InventoryUnits.KG),
        QuickSuggestion("Basmati rizs", "Spájz", InventoryUnits.KG),
        QuickSuggestion("Spagetti tészta", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Penne tészta", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Fodros nagykocka", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Tarhonya", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Zsemlemorzsa", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Búzadara", "Spájz", InventoryUnits.KG),
        QuickSuggestion("Zabpehely", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Müzli", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Burgonya", "Spájz", InventoryUnits.KG),
        QuickSuggestion("Vöröshagyma", "Spájz", InventoryUnits.KG),
        QuickSuggestion("Fokhagyma", "Spájz", InventoryUnits.DB),
        QuickSuggestion("Szemes kávé", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Őrölt kávé", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Kávékapszula", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Filteres tea", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Kakaópor", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Méz", "Spájz", InventoryUnits.DB),
        QuickSuggestion("Lekvár", "Spájz", InventoryUnits.DB),
        QuickSuggestion("Mogyorókrém", "Spájz", InventoryUnits.DB),
        QuickSuggestion("Sütőpor", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Vaníliás cukor", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Feketebors", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Fűszerpaprika", "Spájz", InventoryUnits.CSOMAG),
        QuickSuggestion("Csemegekukorica konzerv", "Spájz", InventoryUnits.DB),
        QuickSuggestion("Vörösbab konzerv", "Spájz", InventoryUnits.DB),
        QuickSuggestion("Tonhal konzerv", "Spájz", InventoryUnits.DB),
        QuickSuggestion("Paradicsomszósz", "Spájz", InventoryUnits.DB),
        QuickSuggestion("Csemegeuborka", "Spájz", InventoryUnits.DB),
        QuickSuggestion("Ásványvíz (mentes)", "Spájz", InventoryUnits.LITER),
        QuickSuggestion("Ásványvíz (dús)", "Spájz", InventoryUnits.LITER),
        QuickSuggestion("Mosogatószer", "Tisztítószer", InventoryUnits.DB),
        QuickSuggestion("Mosogatógép tabletta", "Tisztítószer", InventoryUnits.CSOMAG),
        QuickSuggestion("Mosogatógép öblítő", "Tisztítószer", InventoryUnits.LITER),
        QuickSuggestion("Mosogatógép só", "Tisztítószer", InventoryUnits.CSOMAG),
        QuickSuggestion("Zsíroldó", "Tisztítószer", InventoryUnits.DB),
        QuickSuggestion("Vízkőoldó", "Tisztítószer", InventoryUnits.DB),
        QuickSuggestion("Ablaktisztító", "Tisztítószer", InventoryUnits.DB),
        QuickSuggestion("Általános felülettisztító", "Tisztítószer", InventoryUnits.LITER),
        QuickSuggestion("Felmosószer", "Tisztítószer", InventoryUnits.LITER),
        QuickSuggestion("WC tisztító gél", "Tisztítószer", InventoryUnits.DB),
        QuickSuggestion("WC illatosító", "Tisztítószer", InventoryUnits.CSOMAG),
        QuickSuggestion("Domestos / Fertőtlenítő", "Tisztítószer", InventoryUnits.LITER),
        QuickSuggestion("Színes mosógél", "Tisztítószer", InventoryUnits.LITER),
        QuickSuggestion("Fehér mosógél", "Tisztítószer", InventoryUnits.LITER),
        QuickSuggestion("Mosókapszula", "Tisztítószer", InventoryUnits.CSOMAG),
        QuickSuggestion("Öblítő", "Tisztítószer", InventoryUnits.LITER),
        QuickSuggestion("Folteltávolító", "Tisztítószer", InventoryUnits.DB),
        QuickSuggestion("Színfogó kendő", "Tisztítószer", InventoryUnits.CSOMAG),
        QuickSuggestion("WC papír", "Háztartás", InventoryUnits.CSOMAG),
        QuickSuggestion("Papírtörlő", "Háztartás", InventoryUnits.CSOMAG),
        QuickSuggestion("Papírzsebkendő", "Háztartás", InventoryUnits.CSOMAG),
        QuickSuggestion("Szalvéta", "Háztartás", InventoryUnits.CSOMAG),
        QuickSuggestion("Alufólia", "Háztartás", InventoryUnits.DB),
        QuickSuggestion("Frissentartó fólia", "Háztartás", InventoryUnits.DB),
        QuickSuggestion("Sütőpapír", "Háztartás", InventoryUnits.DB),
        QuickSuggestion("Szemeteszsák (30L)", "Háztartás", InventoryUnits.CSOMAG),
        QuickSuggestion("Szemeteszsák (60L)", "Háztartás", InventoryUnits.CSOMAG),
        QuickSuggestion("Fagyasztó tasak", "Háztartás", InventoryUnits.CSOMAG),
        QuickSuggestion("Mosogatószivacs", "Háztartás", InventoryUnits.CSOMAG),
        QuickSuggestion("Mikroszálas törlőkendő", "Háztartás", InventoryUnits.CSOMAG),
        QuickSuggestion("Gumikesztyű", "Háztartás", InventoryUnits.CSOMAG),
        QuickSuggestion("Folyékony szappan", "Háztartás", InventoryUnits.DB),
        QuickSuggestion("Tusfürdő", "Háztartás", InventoryUnits.DB),
        QuickSuggestion("Sampon", "Háztartás", InventoryUnits.DB),
        QuickSuggestion("Hajbalzsam", "Háztartás", InventoryUnits.DB),
        QuickSuggestion("Fogkrém", "Háztartás", InventoryUnits.DB),
        QuickSuggestion("Fogkefe", "Háztartás", InventoryUnits.DB),
        QuickSuggestion("Szájvíz", "Háztartás", InventoryUnits.DB),
        QuickSuggestion("Dezodor", "Háztartás", InventoryUnits.DB),
        QuickSuggestion("Borotvahab", "Háztartás", InventoryUnits.DB),
        QuickSuggestion("Nedves törlőkendő", "Háztartás", InventoryUnits.CSOMAG),
        QuickSuggestion("Kézkrém", "Háztartás", InventoryUnits.DB),
        QuickSuggestion("AA ceruzaelem", "Egyéb", InventoryUnits.CSOMAG),
        QuickSuggestion("AAA mikroelem", "Egyéb", InventoryUnits.CSOMAG),
        QuickSuggestion("Teamécses", "Egyéb", InventoryUnits.CSOMAG)
    )
}
