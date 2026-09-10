# Phase 3 — Production Migration Baseline

Status: **COMPLETE / BUILD PASS / INTERNAL BASELINE / PUBLIC v0.2.0 PREPARATION**
Date: 2026-09-10

Phase 3 moved the proven application into one Best Mates 2.0 production foundation. It deliberately preserved engines, persistence and navigation contracts. Phase 4 owns substantial information-architecture and layout reconstruction.

## Production structure

| Area | Production owner after Phase 3 | Result |
|---|---|---|
| Root theme/navigation | `MainActivity.kt` | All routes now run inside `BestMatesDesignTheme`; MainActivity retains destination and root state ownership. |
| Workspace | `ui/workspace/WorkspaceScreen.kt` | Phase 3B/3B.1 production Workspace, Crystal project cards and Orbit selection actions. |
| Settings | `ui/settings/SettingsScreen.kt`, `SettingsModels.kt` | Extracted production Settings with theme identity, app brightness, language, import and About surfaces. |
| Editor chrome | `ui/editor/EditorChrome.kt` | Extracted top bar, creation shelf and bottom actions. The fixed canvas plus scrolling inspector contract remains intact. |
| Editor engine | `MainActivity.kt`, `ui/editor/CanvasCoordinates.kt` | Existing LayoutCanvas and TASK-007 coordinate behavior preserved. Only token-driven Work Mat/selection colors changed. |
| Shared production UI | `ui/components/CrystalComponents.kt`, `CrystalDialogs.kt` | Shared Pearl/Milk/Jelly actions, fields, switches, sliders, picker rows, selected tiles and dialogs. |
| Secondary surfaces | existing picker logic in `MainActivity.kt` using shared components | City, shape, font, color, palette and Sun/Moon presentation migrated without changing their engines or IDs. |

## Phase 3C — Settings

- Preserved the existing `app_settings` keys `app_theme`, `language`, `font_name` and `icon_name` and their stored values.
- Added the app-only `appearance_mode` key with `SYSTEM`, `LIGHT` and `DARK`. When the key is absent, the resolver keeps the historical behavior exactly: Midnight is dark; Cloud, Dream and Mono are light.
- The new key does not alter `WidgetConfig.theme` or `WidgetConfig.use24Hour`. Existing and new widgets continue to own authored appearance and time format independently.
- Imported resources still copy into `filesDir/imports/` and keep their existing display-name preferences. No imported-resource registry or renderer contract was invented.
- Removed the raw theme-description-key failure by supplying complete English fallback strings and Chinese strings.

## Phase 3D — Editor

- Moved production toolbar and bottom action presentation to `EditorChrome.kt` using Orbit actions and Crystal surfaces.
- Preserved Undo/Redo history, auto-save, add component/city/text, save, Add to Home and all existing callbacks.
- Kept the approved production Pin drawing and Future Canvas Rotate icon.
- Retained LayoutCanvas size, normalized coordinates, drag/resize/rotate/snap calculations, guides, boundary-aware rotation handle and TASK-007 correction.
- Migrated Inspector containers, fields, switches, sliders, chips and selection actions to shared production components while retaining fixed canvas plus vertically scrolling inspector structure.
- Kept widget-specific `ThemeMode` and `use24Hour` controls because they are concrete persisted authoring fields, not global app preferences.

## Phase 3E — Pickers and dialogs

- All production editor dialogs now use the common Milk Crystal dialog shell.
- Font, shape, palette, color, city, Sun and Moon entry rows use shared Crystal picker surfaces.
- Selected tiles use selected fill, controlled edge and one semantic check; no dot-plus-check state was introduced.
- City lookup remains offline through `CityCatalog`. Color editing retains the existing continuous HSV/value/opacity calculation and ARGB output.
- Sun/Moon enum names and bundled asset paths remain unchanged. Celestial Atelier Final Pass remains deferred.

## Verified compatibility contracts

| Contract | Verified source behavior |
|---|---|
| Project identity | `ProjectStore.create()` still creates UUID strings and stores them in `projects/ids`. No existing ID rewrite exists. |
| Project copy/cut/paste | Workspace still copies a complete `WidgetConfig`; cut deletes only after the copied value is held. Persistence calls are unchanged. |
| Config keys | `ConfigStore` still uses `widget_<id>` and `project_<uuid>` JSON entries in `widget_configs`. |
| Schema fallback | `layoutVersion` remains 8; existing migrations and default/fallback reads are unchanged. |
| Theme/time fields | Concrete `theme` and `use24Hour` JSON values still load and save unchanged. App brightness is separate. |
| Fonts/colors | Existing font strings, ARGB `Long` values, layer/text/city fields and renderer fallback behavior are unchanged. |
| City/timezone | City `geoId`, coordinates and IANA `zoneId` persist unchanged. Native TextClock and provider timezone assignment remain unchanged. |
| Component/asset IDs | `WidgetLayer.id`, `WidgetShape` names, `SunIconStyle` names and `MoonIconStyle` names remain stable. |
| Save/reopen | Editor still calls `ProjectStore.save()` for projects and `ConfigStore.save()` for widget configuration. |
| Add to Home | Existing `pinWidget`, `PinWidgetReceiver`, `projectId` option and provider paths remain unchanged. |
| Widget rendering | `BackgroundRenderer`, `SolarIconRenderer`, `SolarCalculator` and `WorldClockWidgetProvider` were not changed in Phase 3C–3F. |
| Imported resources | Existing local copy and display-name preferences remain compatible; full production resolution remains later work. |

## Intentional remaining legacy boundary

`MainActivity.kt` still owns localization, Editor state, LayoutCanvas, renderer-backed previews and picker behavior. Moving the pointer engine or the tightly coupled editor state solely to reduce file size would add risk without improving the production migration baseline. Phase 4 should plan state and layout boundaries before further extraction.

Debug Design Lab activities remain debug-manifest-only. Golden candidates and visual references were not promoted into production resources. Reference directories `1/`, `2/` and ignored `design_refs/` are outside the application package.

## Validation gate

- `assembleDebug --warning-mode all`: PASS on 2026-09-10.
- `testDebugUnitTest`: `NO-SOURCE` (no unit-test sources exist).
- Release manifest processing: PASS; no debug Design Lab activity is present.
- Built APK inspection: no `design_refs/`, `1/` or `2/` reference path is packaged.
- APK: `app/build/outputs/apk/debug/app-debug.apk`.
- Device: no connected Android device at final validation, so install/launch remains pending.
- The v0.2.0 manual checklist in `CURRENT_SESSION.md` remains the release acceptance step; deterministic build success is not a claim of physical-device approval.
