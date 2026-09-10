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

- `app/src/main/java/com/bestmates/worldclock/MainActivity.kt` — single-activity entry, legacy app theme/language and destination state, Settings, Editor, canvas interactions and remaining dialogs.
- `app/src/main/java/com/bestmates/worldclock/ui/workspace/WorkspaceScreen.kt` — Phase 3B production Workspace route/state, project collection, selection actions, empty state and dialogs.
- `app/src/main/java/com/bestmates/worldclock/ui/components/CrystalComponents.kt` — first shared production Crystal surfaces and Orbit buttons.
- `app/src/main/java/com/bestmates/worldclock/model/Models.kt` — widget, layer, city, text, shape and icon-style data models.
- `app/src/main/java/com/bestmates/worldclock/data/ConfigStore.kt` — JSON persistence/migrations for projects and widget instances.
- `app/src/main/java/com/bestmates/worldclock/data/ProjectStore.kt` — project list/create/copy/delete persistence.
- `app/src/main/java/com/bestmates/worldclock/data/CityCatalog.kt` — bundled SQLite continent/country/city lookup.
- `app/src/main/java/com/bestmates/worldclock/data/CitySearchRepository.kt` — Open-Meteo network search implementation with no current production caller; the picker uses bundled CityCatalog.
- `app/src/main/java/com/bestmates/worldclock/widget/BackgroundRenderer.kt` — normalized shape paths, fills, borders, glow/shadow and layer rendering.
- `app/src/main/java/com/bestmates/worldclock/widget/SolarIconRenderer.kt` — high-resolution SVG solar icon rendering/cropping.
- `app/src/main/java/com/bestmates/worldclock/widget/WorldClockWidgetProvider.kt` — RemoteViews widget updates, city/time/date binding and sizing.
- `app/src/main/java/com/bestmates/worldclock/widget/SizedWidgetProviders.kt`, `PinWidgetReceiver.kt` — widget receiver variants and pin flow.
- `app/src/main/java/com/bestmates/worldclock/solar/SolarCalculator.kt` — timezone-aware solar elevation day/night calculation.
- `app/src/main/res/xml/world_clock_widget_*.xml` — declared 1x1 through 4x6 widget providers; `world_clock_widget_info.xml` is the generic provider.
- `app/src/main/res/layout/widget_world.xml`, `widget_preview.xml` — RemoteViews layout/preview.
- `app/src/main/assets/solar_icons/` — bundled BM and third-party-style SVG solar assets; `THIRD_PARTY_NOTICES.md` records notices.
- `app/src/main/assets/cities.db` — city/country database.
- `app/src/main/java/com/bestmates/worldclock/ui/editor/CanvasCoordinates.kt` — shared vector rotation used by body drag conversion and existing rotation-handle anchoring.
- `docs/PHASE_3A_CORE_ENGINE_AND_MIGRATION.md` — current architecture/compatibility audit, bounded TASK-007 result and production migration plan.
- `docs/PHASE_3B_WORKSPACE_MIGRATION.md` — Workspace behavior/architecture/compatibility record and exact Phase 3C gate.

## 4. CURRENTLY IMPLEMENTED FEATURES

### COMPLETED (confirmed in source)

- Compose single-activity navigation with a Phase 3B extracted production Workspace route and existing Settings/Editor destinations.
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
- Workspace 2.0 production adoption of the frozen Best Mates foundation with legacy theme mapping, shared Crystal components and Orbit actions.

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

- Drag/move: pointer-based canvas drag updates normalized positions. Phase 3A corrects graphics-layer-local deltas into canvas direction for component/city/text; host numerical checks pass and the user physically accepted production rotation-after-drag behavior on 2026-09-10.
- Resize: corner handle exists. Background layer snapping uses quarter-cell (`0.25`) increments; city/text helpers use finer subdivision in parts of the editor. Free, continuous non-proportional resizing is not consistently enforced for every shape.
- Rotation: `RotationHandleUnified` uses an unrotated sibling and root-coordinate tracking; angle unwrapping supports continuous multi-turn raw rotation. The former ±180° limitation is no longer the current source behavior. TASK-007 still needs final physical acceptance.
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
- Typography uses semantic roles. Expressive faces may later serve brand/display/collection/editorial moments; functional actions, Inspector controls, Settings, data, forms, menus and accessibility-critical text stay highly readable. Type Wardrobe must resolve these roles centrally rather than assign arbitrary fonts per screen.

## 7. WIDGET / TIME LOGIC

- Multiple cities: model/provider support up to ten city slots; city data is persisted.
- Time zones: IANA `zoneId` is stored per city; `WorldClockWidgetProvider` applies it to RemoteViews time/date fields.
- Day/night: `SolarCalculator.isDay(city)` uses longitude, latitude, date and timezone; `SolarIconRenderer` chooses sun/moon assets and colors.
- Widget rendering: `BackgroundRenderer.renderLayers()` produces a bitmap background; RemoteViews overlays title/subtitle, city fields and custom text.
- Update mechanism: native TextClock time/date advance independently of the 30-minute provider bitmap/solar refresh. The generic receiver declares time/timezone/date broadcasts but enumerates generic provider IDs; sized-receiver solar refresh coverage needs separate lifecycle validation.
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
- Golden Set 3 Wallpapers are review-only; they are not current production-editor wallpaper controls. Preview sources/bridges are in the main source set even though their Activity entry is debug-only.
- Editor toolbar contains duplicate/verbose actions and rough custom icons. Affected: `MainActivity.kt`.
- TASK-007 rotated dragging is closed: the user physically verified that production rotation-after-drag follows the finger correctly after the bounded Phase 3A coordinate fix.
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
- Settings font/icon import copies files and remembers display names; no complete imported-asset registry/resolver is currently connected to production rendering.

## 12. TECHNICAL DEBT

- `MainActivity.kt` is a very large monolithic Compose file; state, rendering helpers, dialogs and localization should eventually be separated carefully.
- Workspace is now extracted, but `MainActivity.kt` remains large because Settings, Editor, localization and the legacy app shell are still colocated. Continue by bounded route slices.
- Editor and widget renderer duplicate layout concepts and can drift.
- No automated regression suite covers persistence migrations, shape bounds, live gestures, city search or widget rendering. Phase 3A adds an opt-in debug numerical coordinate check; host compiled-helper checks pass, and TASK-007 also received the required physical gesture acceptance.
- Network city search has no explicit error/result-state abstraction in the inspected repository.
- Typeface identifiers are stringly typed and do not guarantee availability on every device.

## 13. BUILD / RUN / TEST

- Required JDK: bundled Java 17 at `.tooling/jdk/jdk-17.0.20.1+1`.
- Local SDK used for verification: `.android-sdk`.
- Build command from project root (PowerShell): set `JAVA_HOME` to the bundled JDK, set `ANDROID_HOME`/`ANDROID_SDK_ROOT` to `.android-sdk`, then run `./gradlew.bat assembleDebug`.
- Last verification: `assembleDebug --warning-mode all` completed successfully on 2026-09-10 for the Phase 3B.1 Workspace layout reconstruction. Output: `app/build/outputs/apk/debug/app-debug.apk`. Phase 3B.1 did not change debug infrastructure or manifests.
- Install/run: install the debug APK on an Android device, launch Best Mates, create/edit a project, then add a widget through the launcher. Verify both generic and resized widget receivers.
- Existing `testDebugUnitTest` task reports NO-SOURCE. Host validation of compiled coordinate-helper bytecode passed 110 affine-oracle cases; the user later physically accepted the production rotation-after-drag behavior and TASK-007 is closed.
- The user physically verified Phase 3B Workspace operations and existing-project preservation. No device was connected for the Phase 3B.1 install/launch step; see the Phase 3B document for its focused visual checklist and exact commands.

## 14. MANUAL DEVICE REVIEW POLICY

The user is the primary subjective visual reviewer on a physical Android device. Unless the user explicitly requests autonomous screenshot inspection, a rendering defect cannot reasonably be diagnosed without one, or the task requires visual-regression evidence, Codex must not repeatedly capture/pull screenshots, scroll through the app solely to gather screenshots, approve visuals on the user's behalf, or claim user acceptance.

After a visual implementation task:

1. Run the necessary build, compile, and static validation.
2. Detect an attached device with `adb`.
3. If connected, install/update the debug APK when needed and launch the exact Activity or shortest path to the highest-priority review surface.
4. Leave that review surface open, stop device automation, and give the user a concise manual checklist. Provide navigation or exact `adb` commands for any additional surfaces.
5. If no device is connected, finish all non-device work and report the real APK path, install command, launch command or shortest navigation path, and checklist. A missing device is not a blocker unless physical execution is essential.

Capture only the minimum necessary screenshots when one of the exceptions above applies. Future visual-task reports use this format:

- Build: PASS / FAIL
- Device: connected / not connected
- Review surface prepared: screen or Activity
- User should inspect: concise visual checklist
- Functional checks: verified non-subjective behavior
- Known deferred items: explicit remaining scope

Do not continue subjective refinement after preparing the review surface until the user provides feedback.

## 15. PHASE 3 PRODUCTION UX RECONSTRUCTION RULE

Before implementing each remaining production-screen migration, write a short layout plan that identifies:

1. Behaviors that must survive.
2. Data and controls that must remain accessible.
3. Legacy layout decisions that can be discarded.
4. The new Best Mates 2.0 information hierarchy.
5. Primary and secondary actions.
6. Fixed and scrolling regions.
7. Safe-area and compact-width behavior.

Preserve proven behavior, engines, persistence, IDs, saved data and navigation semantics. Treat legacy UI only as a functional reference. Reconstruct obsolete hierarchy, spacing, grouping, toolbar placement, density and rhythm instead of applying the new visual language to the old composition. This gate is mandatory for Settings 2.0, Editor 2.0, asset pickers, appearance controls and Font/Color/Celestial/Shape selection surfaces.

## 16. NEXT DEVELOPMENT STAGE

Best Mates 2.0 Phase 3B Workspace operations and existing-project compatibility are physically verified. Phase 3B.1 reconstructs the Workspace hierarchy, compact cards and icon-only selection toolbar while preserving those operations. See [the Phase 3B handoff](docs/PHASE_3B_WORKSPACE_MIGRATION.md) for architecture, compatibility boundaries, the focused visual gate and the Phase 3C starting task.

Phase 3C Settings 2.0 is the next separately authorized slice. Extract Settings presentation and reuse production Crystal components while preserving existing preference keys/values. GPT-6 must review theme/time inheritance and fallback contracts before any schema or saved-project behavior changes; do not silently rewrite existing concrete widget settings.

TASK-007 is **COMPLETE / CLOSED / PHYSICALLY_VERIFIED**. Phase 2D visual direction remains established; Golden Set 3 candidates are not automatically production assets. `Celestial Atelier Final Pass` remains separate and deferred. Do not begin Phase 3C, reopen TASK-007, or begin Celestial work without explicit scope.

### P0 — Must prioritize

The legacy backlog below is context, not authorization to reopen TASK-007 or Celestial. Future phases require their own scope and the gates in the Phase 3A handoff.

1. Make the preview/render coordinate system and all shape bounds deterministic; verify every declared launcher size and free/non-proportional resize.
2. Complete localization coverage and add a testable locale string audit.

### P1 — Important

1. Replace rough shape paths and finish a curated vector sun/moon library with crisp previews.
2. Implement linked alignment guides, equal-size snapping, haptic/smooth motion and unobtrusive controls.
3. Improve city search error handling and verify bundled catalog coverage.

### P2 — Later

1. Expand verified font assets and preview samples.
2. Add weather integration and update policy.
3. Add automated persistence, renderer and Compose interaction tests.

## 17. FIRST FILES TO READ

1. `app/src/main/java/com/bestmates/worldclock/MainActivity.kt` — current UI/state/editor behavior.
2. `app/src/main/java/com/bestmates/worldclock/model/Models.kt` — persisted data contracts and shape/icon enums.
3. `app/src/main/java/com/bestmates/worldclock/widget/BackgroundRenderer.kt` — actual shape geometry and rendering bounds.
4. `app/src/main/java/com/bestmates/worldclock/widget/WorldClockWidgetProvider.kt` — launcher widget rendering/update behavior.
5. `app/src/main/java/com/bestmates/worldclock/data/ConfigStore.kt` — persistence schema and migrations.
6. `app/src/main/java/com/bestmates/worldclock/data/CityCatalog.kt` and `solar/SolarCalculator.kt` — city data and day/night logic.

## 18. HANDOFF CHECKLIST

- [ ] Read this handoff.
- [ ] Verify the listed key files exist and inspect their current implementation.
- [ ] Confirm `assembleDebug` with the bundled JDK/SDK.
- [ ] Re-check completed features against source, not chat history.
- [ ] Review incomplete features and known bugs before editing.
- [ ] Understand the DO NOT BREAK list.
- [ ] Follow the Manual Device Review Policy for visual work.
- [ ] Select one explicitly authorized scoped task before beginning changes.
