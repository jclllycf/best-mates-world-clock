# Best Mates World Clock — Architecture

## Overview
The application is a purely local Android app combining an interactive Compose-based widget editor with a native Android `RemoteViews` home screen widget. The architecture emphasizes declarative state handling, clear serialization boundaries, and robust backward compatibility.

## High-Level Package Map
- `com.bestmates.worldclock`: Root application and `MainActivity` shell.
- `ui.workspace`: The home screen displaying saved user projects and system settings.
- `ui.editor`: The interactive Canvas and Inspector for editing a single Widget configuration.
- `ui.settings`: The system settings flow and model definitions.
- `ui.components`: Generic, reusable `Crystal*` and `Orbit*` Compose components.
- `ui.theme`: The BestMatesDesign visual token system.
- `ui.i18n`: Static inline multi-language support mapped to `AppLanguage`.
- `widget`: Android Home Screen integration (`AppWidgetProvider`, `RemoteViews` compilation).
- `data`: SharedPreferences-based stores (`ConfigStore`, `ProjectStore`, `CityCatalog`).
- `model`: Canonical data classes for `WidgetConfig`, `City`, `WidgetLayer`, `CustomText`.
- `solar`: Deterministic solar math for daylight evaluation.

## Application Navigation
The app consists of a single `MainActivity` using state-based navigation to orchestrate three core screens:
1. **WorkspaceScreen**: Default entry point listing projects.
2. **Editor**: Interactive design surface for configuring widgets.
3. **Settings**: Theme and language configuration.

## Editor Architecture
The Editor uses a unidirectional data flow. 
- `WidgetEditor` acts as the coordinator and history orchestrator.
- `LayoutCanvas` handles rendering, gestures, and selection via `SnapEngine`.
- `Inspector` owns all picker dialogs and property configuration.
- **Rendering consistency**: `LayoutCanvas` delegates shape drawing to `BackgroundRenderer.render(...)` ensuring the preview matches the home screen exactly.

## Widget Runtime Architecture
- The widget heavily uses Android's `RemoteViews`.
- `WorldClockWidgetProvider` initiates widget updates.
- `BackgroundRenderer` renders the `WidgetLayer` elements into a composite Bitmap.
- `SolarIconRenderer` transforms deterministic solar definitions into vector configurations.
- UI elements (text, solar icons) are manually laid out into standard relative coordinate wrappers matching the Compose engine's `LayoutCanvas` percentage system.

## Persistence & Config Architecture
All state is stored locally using standard `SharedPreferences` encoded as JSON.
- `ConfigStore` and `ProjectStore` decouple the widget runtime configuration instances from user-saved templates.
- **Legacy Compatibility Rules**: Models always use default values for missing keys. No fields are aggressively deprecated without parsing fallbacks. Obsolete options like legacy Font IDs and SVG IDs (`LUCIDE_SUN`) remain within their respective Enum lists and render correctly but are omitted from the curated visual Pickers for new configurations.

## Source Sets
- `app/src/main/`: Core production runtime.
- `app/src/debug/`: Preview studies and mock configuration tools (e.g., `GoldenSet3Review`).
- `app/src/test/`: Focused JVM test suites for engine (e.g. `SnapEngineTest`) and models (e.g. `ConfigStoreTest`).
