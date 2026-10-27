package hu.keszletbarat.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.keszletbarat.app.ui.InventoryScreen
import hu.keszletbarat.app.ui.InventoryViewModel
import hu.keszletbarat.app.ui.KeszletBaratTheme

class MainActivity : ComponentActivity() {

    private val viewModel: InventoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            KeszletBaratTheme(
                themeMode = uiState.settings.themeMode,
                colorPalette = uiState.settings.colorPalette
            ) {
                InventoryScreen(viewModel = viewModel)
            }
        }
    }
}
