package hu.keszletbarat.app.model

import androidx.compose.runtime.Immutable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import java.util.UUID

@Immutable
data class InventoryItem(
    @DocumentId
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val category: String = CategoryFilter.HUTO.label,
    val quantity: Int = 1,
    val unit: String = InventoryUnits.DB,
    val minQuantity: Int = 1,
    val updatedAt: Long = System.currentTimeMillis()
) {
    @get:Exclude
    val isOutOfStock: Boolean
        get() = quantity <= 0

    @get:Exclude
    val isLowStock: Boolean
        get() = quantity == 0 || quantity <= minQuantity
}

enum class CategoryFilter(val label: String) {
    ALL("Összes"),
    LOW_STOCK("Kell venni (Kifogyott)"),
    HUTO("Hűtő"),
    SPAJZ("Spájz"),
    TISZTITOSZER("Tisztítószer"),
    HAZTARTAS("Háztartás"),
    EGYEB("Egyéb");

    companion object {
        val itemCategories = listOf(HUTO, SPAJZ, TISZTITOSZER, HAZTARTAS, EGYEB)
    }
}

object InventoryUnits {
    const val DB = "db"
    const val CSOMAG = "csomag"
    const val LITER = "liter"
    const val KG = "kg"
    const val BONTOTT = "bontott"

    val all = listOf(DB, CSOMAG, LITER, KG, BONTOTT)
}
