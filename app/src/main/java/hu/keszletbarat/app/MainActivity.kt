package hu.keszletbarat.app

import android.content.Intent
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
import hu.keszletbarat.app.widget.KeszletWidgetManager

class MainActivity : ComponentActivity() {

    private val viewModel: InventoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        processWidgetIntent(intent)
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        processWidgetIntent(intent)
    }

    private fun processWidgetIntent(intent: Intent?) {
        val action = intent?.getStringExtra(KeszletWidgetManager.EXTRA_WIDGET_ACTION)
        if (!action.isNullOrBlank()) {
            viewModel.handleWidgetAction(action)
            intent.removeExtra(KeszletWidgetManager.EXTRA_WIDGET_ACTION)
        }
    }
}
