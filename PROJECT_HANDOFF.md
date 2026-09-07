# PROJECT HANDOFF

## 1. PROJECT OVERVIEW

Best Mates is intended to be an open, creator-friendly Android world-clock widget editor. Users create a widget inside the app, choose cities, shape, colors, text, fonts and day/night icons, preview the result, and add it to the Android home screen. The long-term product goal includes many cities, local time/date, solar day/night state and optional weather. The current repository is an Android-only v0.1 public preview; iOS is not implemented.

## 2. TECH STACK

- Android application, namespace/application id `com.bestmates.worldclock`.
- Kotlin + Jetpack Compose/Material 3 for the editor UI; no XML editor screens were found.
- Native `AppWidgetProvider`/`RemoteViews` rendering for home-screen widgets.
- Android SVG renderer dependency: `com.caverock:androidsvg-aar:1.4`.
- Gradle Android plugin 8.8.0, Kotlin 2.0.21, Compose compiler plugin 2.0.21, Compose BOM 2024.12.01.
- `compileSdk 35`, `minSdk 26`, `targetSdk 35`, Java/Kotlin toolchain 17.
- Main dependencies include core-ktx 1.15.0, activity-compose 1.10.0 and Compose UI/tooling/material3.
- Build configuration is in `build.gradle.kts`, `settings.gradle.kts`, `app/build.gradle.kts`; repository-local SDK/JDK paths are not committed in `local.properties`.

## 3. PROJECT STRUCTURE

- `app/src/main/java/com/bestmates/worldclock/MainActivity.kt` — single-activity Compose workspace, settings, editor, dialogs, canvas interactions and localization/theme UI.
- `app/src/main/java/com/bestmates/worldclock/model/Models.kt` — widget, layer, city, text, shape and icon-style data models.
- `app/src/main/java/com/bestmates/worldclock/data/ConfigStore.kt` — JSON persistence/migrations for projects and widget instances.
- `app/src/main/java/com/bestmates/worldclock/data/ProjectStore.kt` — project list/create/copy/delete persistence.
- `app/src/main/java/com/bestmates/worldclock/data/CityCatalog.kt` — bundled SQLite continent/country/city lookup.
- `app/src/main/java/com/bestmates/worldclock/data/CitySearchRepository.kt` — Open-Meteo network search fallback.
- `app/src/main/java/com/bestmates/worldclock/widget/BackgroundRenderer.kt` — normalized shape paths, fills, borders, glow/shadow and layer rendering.
- `app/src/main/java/com/bestmates/worldclock/widget/SolarIconRenderer.kt` — high-resolution SVG solar icon rendering/cropping.
- `app/src/main/java/com/bestmates/worldclock/widget/WorldClockWidgetProvider.kt` — RemoteViews widget updates, city/time/date binding and sizing.
- `app/src/main/java/com/bestmates/worldclock/widget/SizedWidgetProviders.kt`, `PinWidgetReceiver.kt` — widget receiver variants and pin flow.
- `app/src/main/java/com/bestmates/worldclock/solar/SolarCalculator.kt` — timezone-aware solar elevation day/night calculation.
- `app/src/main/res/xml/world_clock_widget_*.xml` — declared 1x1 through 4x6 widget providers; `world_clock_widget_info.xml` is the generic provider.
- `app/src/main/res/layout/widget_world.xml`, `widget_preview.xml` — RemoteViews layout/preview.
- `app/src/main/assets/solar_icons/` — bundled BM and third-party-style SVG solar assets; `THIRD_PARTY_NOTICES.md` records notices.
- `app/src/main/assets/cities.db` — city/country database.

## 4. CURRENTLY IMPLEMENTED FEATURES

### COMPLETED (confirmed in source)

- Compose single-activity workspace and editor navigation.
- Project persistence and widget-instance persistence through SharedPreferences JSON.
- Empty-scene support and layout migration through `layoutVersion = 8`.
- Model support for up to ten cities and five custom text layers.
- Multiple shape enum values, per-layer colors, border/glow/shadow parameters, rotation and lock fields.
- City catalog continent/country/city queries and English-name search against bundled SQLite data.
- Per-city IANA timezone, time/date visibility, country visibility, colors, fonts and sun/moon styles.
- Solar day/night calculation from city coordinates/timezone.
- Native Android widget receivers for multiple declared sizes; RemoteViews time zones and periodic update metadata.
- SVG solar rendering at supersampled resolution.
- App theme enum (Cloud/Dream/Midnight/Mono) and language enum/state infrastructure.

### PARTIALLY IMPLEMENTED

- Editor drag, resize, rotation, snapping and guide rendering exist, but behavior is not yet robust.
- Shape previews/rendering exist, but several paths/aspect/fill behaviors do not match the intended natural designs.
- Day/night colors and icons exist, but library content and visual quality are not verified against the reference set.
- Localization has a translation map and locale state, but only English/Chinese are broad; other languages have limited overrides.
- Font picker contains many named choices, but they are primarily Android/system aliases; no verified 100-font asset library is present.
- 1x1–4x6 widget declarations exist, but the renderer uses a normalized square design board, so launcher size behavior is not equivalent to a true per-grid editor.

### NOT IMPLEMENTED / NOT VERIFIED

- iOS widget/app.
- Weather data/service and all-city live network weather integration.
- Verified crash-free behavior for every city search, back gesture and long-running editor session.
- Complete localization of every UI string.
- Automated UI/instrumentation test suite.

## 5. EDITOR INTERACTION SPECIFICATION

Current source behavior, not future intent:

- Drag/move: pointer-based canvas drag updates normalized positions; rotation-aware interaction is not fully reliable.
- Resize: corner handle exists. Background layer snapping uses quarter-cell (`0.25`) increments; city/text helpers use finer subdivision in parts of the editor. Free, continuous non-proportional resizing is not consistently enforced for every shape.
- Rotation: handle exists, but `MainActivity.kt` currently normalizes/clamps rotation to approximately `[-180, 180]`; unlimited rotation is not implemented.
- Snapping/guides: center/edge/grid guide logic exists; PPT-style linked guides, symmetry matching, equal-size snapping and haptic feedback are not confirmed.
- Selection: canvas selection drives a lower editor/action area; blank-canvas deselection and all gesture/back edge cases need verification.
- Bounds: renderer/editor clamp some centers and widget RemoteViews clamp content to design bounds. The requested “allow outside component” behavior is not a verified universal rule.
- Canvas: editor is a 4x4 logical canvas, while `BackgroundRenderer.designBounds()` is square and based on the shorter rendered dimension.
- Shape requirement for next work: do not force proportional resizing. Ticket/cloud/wave/etc. must support square, landscape and portrait proportions, with the visible path filling its own bounding box rather than leaving artificial margins.

## 6. UI / UX DESIGN RULES

- Best Mates visual language: soft minimal, airy/dreamy, low-saturation lavender/pink/blue-violet, restrained transparency and gentle edges.
- Canvas-first editing: preview remains the primary visual surface; selection controls and properties should stay visually connected to it.
- Use subtle selection outlines, quiet resize/rotation handles, unobtrusive toolbar icons and restrained motion.
- Avoid default placeholder branding/text in new blank projects.
- Keep editor, workspace, dialogs and widget rendering on one coherent design system.
- Color controls should be visual pickers/previews rather than requiring hex entry as the primary path.

## 7. WIDGET / TIME LOGIC

- Multiple cities: model/provider support up to ten city slots; city data is persisted.
- Time zones: IANA `zoneId` is stored per city; `WorldClockWidgetProvider` applies it to RemoteViews time/date fields.
- Day/night: `SolarCalculator.isDay(city)` uses longitude, latitude, date and timezone; `SolarIconRenderer` chooses sun/moon assets and colors.
- Widget rendering: `BackgroundRenderer.renderLayers()` produces a bitmap background; RemoteViews overlays title/subtitle, city fields and custom text.
- Update mechanism: provider reacts to time/timezone/date broadcasts and declares a 30-minute update period; exact minute-level refresh is not implemented.
- Preview: Compose canvas is a normalized design preview and is not yet a pixel/launcher-size guarantee across devices.
- Android home screen: generic and many fixed-size receivers are declared; launcher sizing/options are read where available. iOS is outside current scope.

## 8. IMPORTANT ARCHITECTURAL DECISIONS

| Decision | Reason | Relevant files |
|---|---|---|
| Compose editor in one activity | Fast stateful visual editing and dialogs | `MainActivity.kt` |
| JSON config in SharedPreferences | Simple per-project/per-widget persistence and migrations | `ConfigStore.kt`, `ProjectStore.kt` |
| Bundled SQLite city catalog | Avoid loading huge country city lists into Compose; deterministic offline lookup | `CityCatalog.kt`, `assets/cities.db` |
| Native RemoteViews widget | Works without KWGT/Pro and is installable as an Android app widget | `WorldClockWidgetProvider.kt`, `res/xml`, `res/layout` |
| Normalized design coordinates | Device-independent editor/render model | `MainActivity.kt`, `BackgroundRenderer.kt` |
| SVG solar assets | Crisp scalable day/night icons and recoloring | `SolarIconRenderer.kt`, `assets/solar_icons` |

## 9. DO NOT BREAK / REGRESSION PROTECTION

- Existing project/widget JSON loading, saving and layout migrations.
- Empty-scene support for newly created projects.
- City timezone/date/time rendering and day/night selection.
- Widget receiver declarations and launcher add/pin flows.
- SVG asset loading/cropping and transparent icon rendering.
- Per-city visibility/color/font/style fields and per-layer shape styling fields.
- Support for both project preview and widget-instance persistence keys.
- Do not reintroduce hard-coded sample titles/subtitles as defaults.

## 10. KNOWN BUGS

- Localization is incomplete: non-Chinese locales fall back to many English strings. Affected: `MainActivity.kt` translation map.
- Wallpaper preview controls and related code still exist although the latest product direction asked to remove them. Affected: `MainActivity.kt`.
- Editor toolbar contains duplicate/verbose actions and rough custom icons. Affected: `MainActivity.kt`.
- Rotation is limited to `[-180,180]`; rotated dragging can use inconsistent coordinate transforms. Affected: `MainActivity.kt`.
- Background canvas/design bounds remain square and can mismatch selected 1x1–4x6 launcher proportions. Affected: `MainActivity.kt`, `BackgroundRenderer.kt`, widget XML/provider sizing.
- Some shape paths (notably capsule/cloud/petal/wave/ticket) are placeholders or visually rough and do not always fill the selected bounds naturally. Affected: `Models.kt`, `BackgroundRenderer.kt`.
- Background snapping is quarter-cell rather than the requested finer/linked grid behavior. Affected: `MainActivity.kt`.
- Custom text line-height/rendering is heuristic and may overlap for decorative fonts or multiline content. Affected: `MainActivity.kt`, `WorldClockWidgetProvider.kt`.
- Search/network failure and back-gesture crash reports from device use are not reproduced or confirmed by source inspection; treat as unresolved until instrumented.

## 11. PARTIALLY IMPLEMENTED FEATURES

- Complete language packs (German, Russian, Turkish, Arabic, French).
- Curated high-quality sun/moon library matching all reference screenshots, including Islamic crescent variants.
- Broad distinct font library with visual samples; current choices are mostly system typeface aliases.
- Linked alignment/symmetry/equal-size snapping, haptic feedback and smooth gesture animation.
- Natural shape geometry and shape-specific aspect recommendations.
- Fully device-faithful preview and launcher cell mapping across manufacturers.
- Weather fields and future extensible city data.

## 12. TECHNICAL DEBT

- `MainActivity.kt` is a very large monolithic Compose file; state, rendering helpers, dialogs and localization should eventually be separated carefully.
- Editor and widget renderer duplicate layout concepts and can drift.
- No automated regression tests cover persistence migrations, shape bounds, rotation, city search or widget rendering.
- Network city search has no explicit error/result-state abstraction in the inspected repository.
- Typeface identifiers are stringly typed and do not guarantee availability on every device.

## 13. BUILD / RUN / TEST

- Required JDK: bundled Java 17 at `.tooling/jdk/jdk-17.0.20.1+1`.
- Local SDK used for verification: `.android-sdk`.
- Build command from project root (PowerShell): set `JAVA_HOME` to the bundled JDK, set `ANDROID_HOME`/`ANDROID_SDK_ROOT` to `.android-sdk`, then run `./gradlew.bat assembleDebug`.
- Last verification: `assembleDebug` completed successfully on 2026-09-05. Output: `app/build/outputs/apk/debug/app-debug.apk`.
- Install/run: install the debug APK on an Android device, launch Best Mates, create/edit a project, then add a widget through the launcher. Verify both generic and resized widget receivers.
- No automated tests were found or run.

## 14. NEXT DEVELOPMENT STAGE

### P0 — Must prioritize

1. Reproduce and fix editor stability/gesture issues (back navigation, long-session crashes, rotated drag, unlimited rotation) without changing persistence contracts.
2. Make the preview/render coordinate system and all shape bounds deterministic; verify every declared launcher size and free/non-proportional resize.
3. Complete localization coverage and add a testable locale string audit.

### P1 — Important

1. Replace rough shape paths and finish a curated vector sun/moon library with crisp previews.
2. Implement linked alignment guides, equal-size snapping, haptic/smooth motion and unobtrusive controls.
3. Improve city search error handling and verify bundled catalog coverage.

### P2 — Later

1. Expand verified font assets and preview samples.
2. Add weather integration and update policy.
3. Add automated persistence, renderer and Compose interaction tests.

## 15. FIRST FILES TO READ

1. `app/src/main/java/com/bestmates/worldclock/MainActivity.kt` — current UI/state/editor behavior.
2. `app/src/main/java/com/bestmates/worldclock/model/Models.kt` — persisted data contracts and shape/icon enums.
3. `app/src/main/java/com/bestmates/worldclock/widget/BackgroundRenderer.kt` — actual shape geometry and rendering bounds.
4. `app/src/main/java/com/bestmates/worldclock/widget/WorldClockWidgetProvider.kt` — launcher widget rendering/update behavior.
5. `app/src/main/java/com/bestmates/worldclock/data/ConfigStore.kt` — persistence schema and migrations.
6. `app/src/main/java/com/bestmates/worldclock/data/CityCatalog.kt` and `solar/SolarCalculator.kt` — city data and day/night logic.

## 16. HANDOFF CHECKLIST

- [ ] Read this handoff.
- [ ] Verify the listed key files exist and inspect their current implementation.
- [ ] Confirm `assembleDebug` with the bundled JDK/SDK.
- [ ] Re-check completed features against source, not chat history.
- [ ] Review incomplete features and known bugs before editing.
- [ ] Understand the DO NOT BREAK list.
- [ ] Select one scoped P0 task before beginning changes.
