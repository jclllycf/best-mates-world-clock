package com.bestmates.worldclock

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.bestmates.worldclock.data.ProjectStore
import com.bestmates.worldclock.data.WidgetBindings
import com.bestmates.worldclock.ui.design.BmTheme
import com.bestmates.worldclock.ui.editor.EditorScreen
import com.bestmates.worldclock.ui.i18n.LocalBestMatesLanguage
import com.bestmates.worldclock.ui.i18n.translated
import com.bestmates.worldclock.ui.settings.AppAppearanceMode
import com.bestmates.worldclock.ui.settings.AppLanguage
import com.bestmates.worldclock.ui.settings.AppThemeStyle
import com.bestmates.worldclock.ui.settings.SettingsScreen
import com.bestmates.worldclock.ui.settings.legacyAppearanceFor
import com.bestmates.worldclock.ui.workspace.WorkspaceRoute
import com.bestmates.worldclock.widget.WidgetPinning
import com.bestmates.worldclock.widget.WidgetViews
import com.bestmates.worldclock.widget.WorldClockWidgetProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private sealed interface Screen {
    data object Workspace : Screen
    data object Settings : Screen
    data class Editor(val projectId: String) : Screen
}

class MainActivity : ComponentActivity() {
    private val openRequest = mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
        )
        super.onCreate(savedInstanceState)
        val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) setResult(Activity.RESULT_CANCELED)
        openRequest.value = intent.getStringExtra(WidgetViews.EXTRA_OPEN_PROJECT)
        setContent { BestMatesApp(widgetId, openRequest.value) { openRequest.value = null } }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.getStringExtra(WidgetViews.EXTRA_OPEN_PROJECT)?.let { openRequest.value = it }
    }
}

@Composable
private fun BestMatesApp(configureWidgetId: Int, openProject: String?, onOpenHandled: () -> Unit) {
    val activity = androidx.activity.compose.LocalActivity.current!!
    val preferences = remember { activity.getSharedPreferences("app_settings", Context.MODE_PRIVATE) }
    var theme by remember { mutableStateOf(AppThemeStyle.from(preferences.getString("app_theme", null))) }
    var appearance by remember { mutableStateOf(AppAppearanceMode.from(preferences.getString("appearance_mode", null)) ?: legacyAppearanceFor(theme)) }
    var language by remember { mutableStateOf(AppLanguage.from(preferences.getString("language", "en"))) }
    val dark = when (appearance) {
        AppAppearanceMode.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        AppAppearanceMode.LIGHT -> false
        AppAppearanceMode.DARK -> true
    }
    // System-bar icon contrast follows the app's own appearance, not only the system theme.
    val view = androidx.compose.ui.platform.LocalView.current
    androidx.compose.runtime.SideEffect {
        androidx.core.view.WindowCompat.getInsetsController(activity.window, view).apply {
            isAppearanceLightStatusBars = !dark
            isAppearanceLightNavigationBars = !dark
        }
    }
    val store = remember { ProjectStore(activity) }
    val scope = rememberCoroutineScope()
    val configuring = configureWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID
    var screen by remember { mutableStateOf<Screen>(Screen.Workspace) }

    LaunchedEffect(openProject) {
        if (openProject != null) {
            if (store.exists(openProject)) screen = Screen.Editor(openProject)
            onOpenHandled()
        }
    }

    fun persist(projectId: String, config: com.bestmates.worldclock.model.WidgetConfig) {
        store.save(projectId, config)
        scope.launch(Dispatchers.Default) { WorldClockWidgetProvider.refreshProject(activity.applicationContext, projectId) }
    }

    /** Launcher "add widget" flow: link the new widget to the chosen design and finish. */
    fun finishConfigure(projectId: String) {
        WidgetBindings(activity).bind(configureWidgetId, projectId)
        WorldClockWidgetProvider.render(activity, AppWidgetManager.getInstance(activity), configureWidgetId)
        activity.setResult(Activity.RESULT_OK, Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, configureWidgetId))
        activity.finish()
    }

    CompositionLocalProvider(LocalBestMatesLanguage provides language) {
        BmTheme(dark = dark, accent = theme.accent) {
            AnimatedContent(
                targetState = screen,
                transitionSpec = {
                    (fadeIn(tween(220)) + scaleIn(tween(260), initialScale = .97f)) togetherWith (fadeOut(tween(160)) + scaleOut(tween(200), targetScale = 1.02f))
                },
                label = "navigation",
            ) { target ->
                when (target) {
                    Screen.Workspace -> WorkspaceRoute(
                        onOpenProject = { id -> if (configuring) finishConfigure(id) else screen = Screen.Editor(id) },
                        onCreated = { id -> screen = Screen.Editor(id) },
                        onSettings = { screen = Screen.Settings },
                        pickMode = configuring,
                    )
                    Screen.Settings -> SettingsScreen(
                        versionName = BuildConfig.VERSION_NAME,
                        currentTheme = theme,
                        appearance = appearance,
                        onThemeChange = { theme = it; preferences.edit().putString("app_theme", it.name).apply() },
                        onAppearanceChange = { appearance = it; preferences.edit().putString("appearance_mode", it.stored).apply() },
                        currentLanguage = language,
                        onLanguageChange = { language = it; preferences.edit().putString("language", it.stored).apply() },
                        onBack = { screen = Screen.Workspace },
                    )
                    is Screen.Editor -> {
                        val initial = remember(target.projectId) { store.load(target.projectId) }
                        EditorScreen(
                            initial = initial,
                            isProject = true,
                            onPersist = { persist(target.projectId, it) },
                            onClose = { if (configuring) finishConfigure(target.projectId) else screen = Screen.Workspace },
                            onAddToHome = if (configuring) null else { cfg ->
                                if (!WidgetPinning.supported(activity) || !WidgetPinning.request(activity, target.projectId, cfg)) {
                                    Toast.makeText(activity, translated(language, "pin_unsupported"), Toast.LENGTH_LONG).show()
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}
