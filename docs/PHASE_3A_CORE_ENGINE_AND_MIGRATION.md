# Best Mates 2.0 — Phase 3A Core Engine and Migration

Date: 2026-09-10. Starting HEAD: `29414893ab71ef4168a778fd365840243124ac19`, clean, on `maintenance/repository-cleanup`.

Status: **AUDIT_COMPLETE / BOUNDED_IMPLEMENTATION_COMPLETE**. Build and host numerical checks passed. The user later accepted rotation-after-drag on a physical device during the Phase 3B.1 gate, so TASK-007 is **COMPLETE / CLOSED**. Phase 3B has since been implemented; this document remains the compatibility and migration contract.

Authority: repository implementation/history, [AI rules](../AI_RULES.md), [UI foundation](BEST_MATES_UI_2_SPEC.md), [Visual Asset Bible](BEST_MATES_VISUAL_ASSET_BIBLE.md), [Phase 2D closure/review](PHASE_2D_GOLDEN_SET_3_REVIEW.md), and the explicitly scoped Phase 3A request. Frozen Cute Atelier / Soft Crystal / Orbit Charm direction is preserved. This document authorizes no future implementation by itself.

## 1. Current architecture map

Paths below are relative to `app/src/main/java/com/bestmates/worldclock/` unless stated otherwise. These are traced consumers and owners, not assumptions from package names.

| Classification | Current implementation and actual call path |
| --- | --- |
| CORE / DOMAIN | `model/Models.kt`: `WidgetConfig`, `WidgetLayer`, `City`, `CustomText`, stable shape/solar enums and intrinsic new-layer defaults. `solar/SolarCalculator.kt`: coordinates + zoned time → binary daylight. |
| PERSISTENCE | `data/ProjectStore.kt` lists UUID projects and delegates config load/save/copy/delete to `data/ConfigStore.kt`. ConfigStore owns JSON and version migrations for both project and widget keys. |
| PRODUCTION PRESENTATION | `MainActivity` → `BestMatesApp`; local `showSettings` / `editingProject` state selects `SettingsScreen`, `WidgetEditor`, or `WorkspaceScreen` through `AnimatedContent`. No navigation framework or ViewModel is currently installed. |
| EDITOR ENGINE | `WidgetEditor` → private `LayoutCanvas`; canvas selection plus callbacks mutate the editor's config. Layer/city/text drag, resize, snap, guides and bounds helpers live in `MainActivity.kt`. `RotationHandleUnified` is an unrotated sibling using root coordinates and continuous angle unwrapping. |
| RENDERING | `BackgroundRenderer` owns normalized shape geometry and bitmap layers. Compose canvas uses the same shape bounds/path language; widget `RemoteViews` overlays native text/time/date. `SolarIconRenderer` loads/recolors/crops existing SVGs. |
| WIDGET LIFECYCLE | App pin action → `SizedWidgetProvider_4x4` + `PinWidgetReceiver` callback → project config snapshot saved by widget ID → `WorldClockWidgetProvider.render`. Generic and sized providers call the shared renderer. |
| DEBUG / PREVIEW ONLY (access) | `src/debug/.../BestMatesDesignLabActivity.kt` and debug manifest expose the Design Lab with no launcher intent. Local theme choices and Golden Set 3 review never save a project. |
| TEMPORARY BRIDGE / LEGACY | `ui/preview/` and `BestMatesCanvasReview` / production-tool studies are actually in the **main source set**. Their review entry is debug-only, but their compiled source is not all debug-only. `MainActivity.kt` still contains legacy chrome/theme/dialogs and preview bridges. |

```text
MainActivity
  BestMatesApp (app preferences + route)
    WorkspaceScreen ── ProjectStore ── ConfigStore
    SettingsScreen ── app_settings + import file copies
    WidgetEditor (config/history/selection) ── LayoutCanvas
      project save ── ProjectStore + refreshProjectWidgets
      widget configuration save ── ConfigStore(widgetId) + render + Activity result

Launcher / pin callback ── ConfigStore ── WorldClockWidgetProvider
  BackgroundRenderer + SolarCalculator + SolarIconRenderer + RemoteViews/TextClock

debug Activity ── Foundation Design Lab ── GoldenSet3Review
  Work Mat ── BestMatesCanvasReview ── SAME LayoutCanvas
```

## 2. Core engine and state ownership

| State / behavior | Owner and migration implication |
| --- | --- |
| Workspace projects | Workspace owns refresh revision, selection, clipboard and naming; invokes `ProjectStore` directly. Preserve command behavior before extracting route/UI. |
| Settings | App root owns persisted `app_theme` and language. Settings also stores import display names and copies files; imported files do not yet form a usable registry/resolver. |
| Editor config | `WidgetEditor` owns mutable `WidgetConfig`, selected indices/parts, clipboard, dialogs, 32-entry history, gesture-batched history and five-minute project autosave. Route callback handles explicit save. |
| Canvas | `LayoutCanvas` receives config and change callbacks, owns guides/tick/layout measurements. Layer placement uses normalized board bounds; city/text placement is relative to component bounds. Existing conversions, bounds and snap rules are not interchangeable. |
| Objects | Shapes/background layers are `WidgetLayer`; cities/text reference `componentId`. Selection is an index-based sealed canvas selection, not a persisted new object ID system. |
| Rotation | Root-coordinate handle position and object center feed angle calculation; continuous unwrap supports multi-turn raw float rotations. Handle anchoring rotates a vector; no ±180° persistence clamp is required. |
| Theme | Legacy app `AppThemeStyle` is separate from per-widget `ThemeMode`. Phase 2 `BestMatesTheme` resolves deterministic identity × brightness tokens and does not own a settings store. |
| Rendering | `BackgroundRenderer` and provider remain authoritative for production compatibility. Preview text measurement/fonts and native widget text differ; sharing the data model is not pixel-equivalence. |

No framework, dependency, weather service, storage abstraction, or global theme adapter is added in 3A. The only production extraction is a used coordinate helper.

## 3. Protected compatibility contracts

- Preserve SharedPreferences `projects` / `ids`, `widget_configs` / `project_<UUID>` and `widget_<ID>`, and `app_settings` / `app_theme`, `language`, `font_name`, `icon_name`.
- Preserve existing project UUIDs, Android widget IDs, receiver class names, XML metadata, pin callback/result behavior and the existing `projectId` options convention.
- Preserve `layoutVersion = 8`, root and nested JSON fields, pre-v2/v3/v7/v8 compatibility logic, empty scenes, raw float rotations, ARGB longs/alpha, component associations, saved theme and `use24Hour` values.
- Preserve every persisted `WidgetShape`, `SunIconStyle`, `MoonIconStyle`, legacy `SolarIconStyle`, and enum-name fallback behavior. Golden review IDs do not replace production IDs.
- Preserve font identity strings, including system aliases and `BM|...` encodings, imported filenames/references and copied files even though import consumption is incomplete.
- Preserve city names, coordinates, `zoneId`, geo identifiers and overrides. Never silently substitute UTC or coordinates `(0,0)` for invalid data.
- Preserve current renderers, SVG paths/recoloring, transparent components, widget configuration entry and save, existing project copy semantics and saved content colors/fonts.
- Theme/time ownership will move to Settings according to [UI spec §12](BEST_MATES_UI_2_SPEC.md#12-themetime-ownership-and-compatibility-contract). Existing concrete values survive. The current schema does **not** encode independent “follow Settings” policies; this requires a separately reviewed compatibility design.

No protected storage, ID, renderer, provider or asset file changed in 3A. Reference directories `1/` and `2/` were neither inspected nor modified.

## 4. Time engine audit — preserve correct behavior

`LayoutCanvas` refreshes a Compose tick every 30 seconds. For each city it calls `ZonedDateTime.now(ZoneId.of(city.zoneId))` and formats time (`HH:mm` or `h:mm a`) and date (`EEE, MMM d`) using the app locale. `CityPartEditor` has a remembered illustrative font specimen, not a live clock.

Production widget time/date use native `TextClock` views. The provider sets each city's zone and sets both system-format branches to the saved `use24Hour` pattern. Native clocks advance without re-rendering the widget bitmap every minute. The 30-minute provider schedule concerns background/solar refresh, not a 30-minute-frozen time label. [Android TextClock contract](https://developer.android.com/reference/android/widget/TextClock).

| Audit question | Finding / decision |
| --- | --- |
| Timezone and DST | IANA `ZoneId` / platform rules are used. No per-city fixed UTC-offset clock approximation was found. Leave this correct mechanism alone. |
| Date rollover | Zoned dates follow the selected city's midnight. Compose may be up to one tick late. Separate `now()` calls for time/date/solar can straddle midnight; future shared instant can address that without changing timezone rules. |
| 12/24-hour ownership | Concrete `WidgetConfig.use24Hour` currently controls editor and widget. Keep it until Settings migration preserves old projects and standalone widgets. |
| Refresh | Provider metadata is 30 minutes; generic receiver also declares time/timezone/date broadcasts. Its receiver path enumerates generic provider IDs, while sized receivers only declare widget update. Sized solar refresh on clock/zone change needs separate lifecycle validation. |
| Duplication / work | Formatters and zoned calls are constructed in canvas recompositions; preview and widget have distinct presentation paths. Defer shared formatting/instant extraction to the editor slice; no measured performance claim. |
| Locale | Compose uses app language; native widget clock uses host/platform locale behavior. Defer explicit parity to Settings/localization acceptance. |
| Invalid persisted zone | Preview formatting catches failures; solar/provider zone construction does not consistently do so. No bad stored example was found. A recovery policy belongs with data validation, not a silent fallback here. |

**Classification:** correct timezone engine LEFT ALONE; shared instant/formatter cleanup **DEFER (P2)**. Replacing `TextClock` with minute bitmap updates or fixed offsets is **REJECT**.

## 5. Sunrise / sunset feasibility — already location-aware, expansion deferred

`City` already contains latitude, longitude and IANA timezone, and ConfigStore persists them. `CityPicker` currently queries bundled `CityCatalog`; `CitySearchRepository` contains Open-Meteo geocoding but has no current production caller. The read-only catalog audit found 235,640 city rows and 394 distinct timezone strings, no blank/missing zones, and no missing/out-of-range coordinates. This is coverage evidence, not proof of every historical timezone rule or imported value.

`SolarCalculator.isDay` already calculates offline solar elevation from date, local hour, longitude, latitude and the actual zoned offset using a NOAA-inspired approximation. Zenith below 90.833° selects the existing day state. Editor and provider already call it; switching Sun/Moon is not currently based on a fixed 06:00–18:00 interval.

The current function returns only a Boolean at an instant. It has no sunrise/sunset event times, twilight state, explicit polar-day/polar-night result, unavailable-coordinate result, event cache or boundary fixtures. The approximation uses a 365-day divisor and does not clamp the `acos` argument. A high-latitude Boolean result is not an explicit polar-event contract.

**Classification: DEFER (P1 for future event correctness).** Adding full events/twilight is larger than a safe Boolean-helper patch and is unnecessary for 3A. No city database migration is needed, but event semantics and widget scheduling must be decided together. No astronomy dependency or artwork change is introduced.

Future bounded work should provide a deterministic `DaylightState` / daily-event result with optional sunrise/sunset instants and explicit polar/unavailable cases; accept a shared `Instant` and city; test equator, DST transitions, leap day, Tromsø/Longyearbyen, both hemispheres and no-event days. Cache daily events by coordinates + local date + zone/rules version as appropriate, then classify the current instant. Keep existing assets and define how coarse widget updates cross a daylight boundary. Do not invent event times when the sun never rises/sets.

## 6. Weather feasibility — DEFER

Weather is feasible, but it introduces an external service, recurrent network work, freshness/error state and an update policy. That does not meet SAFE NOW. No weather code, dependency, permission change or fake production data is added.

Open-Meteo is one plausible future provider: its coordinate-based forecast API can use existing city coordinates, with weather codes and timestamps. Provider choice remains open. [Official API](https://open-meteo.com/en/docs).

As checked on 2026-09-10, Open-Meteo lists a no-key free non-commercial API with rate limits (600/minute, 5,000/hour, 10,000/day; pricing also lists a monthly allowance). Commercial use requires a paid plan/key; free service has no uptime guarantee. Recheck plan cost and quota before integration; no subscription is selected here. [Pricing](https://open-meteo.com/en/pricing), [terms](https://open-meteo.com/en/terms).

| Requirement | Future decision / scope |
| --- | --- |
| Permission / network | INTERNET already exists. Add explicit failure state, timeout/retry/backoff and connectivity constraints; current geocoding code is not a weather subsystem. |
| Credentials / budget | Decide product eligibility, provider terms/attribution, cost ceiling and credential distribution before implementation. A shared paid secret must not simply be embedded in the APK. |
| Mapping / privacy | Send selected city coordinates, not device GPS; no location permission is needed for manual cities. Provider receives coordinates/IP; document that behavior and minimize payloads. |
| Cache | Provider-neutral timestamped snapshot per coordinate/unit key; deduplicate cities, retain last good data and distinguish fresh/stale/unavailable. Keep cache outside widget/project JSON. |
| Offline / failure | Show last good snapshot with age or unavailable; never invent sunny weather or replace a user project on request failure. |
| Scheduling | Start with an approximately hourly freshness budget and demand-triggered refresh, not per-frame/per-minute requests. WorkManager would add a dependency and lifecycle policy; execution is not exact. |
| Widget | Render cached state only; network must not block `AppWidgetProvider`. Update affected IDs after successful refresh and preserve time clocks independently. |

Android widget periodic updates cannot be scheduled below 30 minutes through `updatePeriodMillis`; different background scheduling requires a deliberate policy. [Android widget guidance](https://developer.android.com/develop/ui/views/appwidgets/advanced).

Potential later API names, **documentation only**: `WeatherRepository`, `WeatherSnapshot(observedAt, fetchedAt, coordinates, units, condition, values, source)`, `WeatherCondition.Unknown`, and a separate fresh/stale/unavailable result. Weather is optional and must not block the production UI migration.

## 7. Work Mat versus production Editor

The Golden Set 3 Work Mat calls `BestMatesCanvasReview`, which calls the **same private `LayoutCanvas`** as `WidgetEditor`. It does not have an alternative pointer engine, geometry cache, draw layer or better edge-handle algorithm to transplant.

| Concern | Evidence / classification |
| --- | --- |
| Pointer architecture and coordinates | Same body pointer handlers, graphics layers, gesture consumption and normalized conversions. Shared engine, not a Work Mat improvement. |
| Rotation and edge placement | Same `RotationHandleUnified`, root-coordinate tracking, anchor placement, bounds and touch handling. The reviewed apparent advantage is not separate geometry. |
| Draw / geometry | Same production Heart, `BackgroundRenderer` conventions and Compose canvas. No independent accelerated renderer. |
| Local state | Work Mat remembers a disposable config with one large Heart and one Tokyo city. Production owns history, autosave, clipboard, more inspector/dialog state and variable object counts. **DEBUG-ONLY SIMPLIFICATION**. |
| Snap / guides | Work Mat passes enabled snap/grid into the same engine. Its large heart limits available travel; compare smaller objects in production with snap off first. |
| Visual context | Foundation theme/neutral mat, toolbar study and small local scrolling inspector. **VISUAL ONLY** migration basis. |
| Interaction / geometry improvements | None unique to Work Mat were found. The 3A local-to-canvas correction benefits both because they share the engine. |
| Perceived smoothness | Simpler scene/state workload is a plausible explanation, **not measured proof**. No frame benchmark or recomposition trace was collected. Do not claim a performance improvement from this audit. |

Safe migration path: extract production route/state boundaries first; adopt Work Mat chrome around existing canvas callbacks; use identical scenes when investigating performance; profile only if a reproducible problem remains. Do not replace the engine or remove history to imitate a simpler demo.

## 8. TASK-007 bounded result

**One authorized investigation/repair attempt consumed.** Status: **BOUNDED_FIX_IMPLEMENTED / HOST_NUMERICAL_PASS / AWAITING_MANUAL_DEVICE_VERIFICATION**. Prior unlimited-rotation work is preserved. Do not mark the task DONE until actual production gestures pass.

Root cause: the three body drag handlers are downstream of `graphicsLayer(rotationZ)`. Compose maps current and previous pointer positions into that local coordinate frame. Existing code incorrectly treated their difference as a canvas-space vector (one old comment explicitly claimed rotation could not affect input coordinates).

For fixed rotation during a body drag:

```text
localDelta = inverseRotation(objectRotation) × canvasDelta
canvasDelta = rotation(objectRotation) × localDelta
```

Compose UI 1.7.6 source inspection confirms local pointer mapping in `HitPathTracker` / `NodeCoordinator`; current and previous positions use the same current transform, so translation/pivot cancel in their difference. [Versioned official sources](https://dl.google.com/dl/android/maven2/androidx/compose/ui/ui/1.7.6/ui-1.7.6-sources.jar).

Fix: accumulate local deltas, rotate once by the gesture-start object's positive rotation, then perform the existing pixel→dp→normalized conversion and existing snap/bounds behavior. Apply exactly the same helper to layer, city and text body dragging. Gesture start already captures the latest object through `rememberUpdatedState`; pointer keys remain unchanged.

The existing vector helper was moved unchanged into `ui/editor/CanvasCoordinates.kt` and reused for drag plus its previous rotation-handle anchor call. Rotation, resize, snapping, guides, object dimensions, renderer geometry, persistence, haptics, history and gesture-start/end callbacks are unchanged.

Validation:

- Debug build succeeds. A host JDK affine-transform oracle invoked the **compiled production Kotlin helper**, passing 110 cases spanning 0°, 45°, 90°, 180°, 270°, 360°, 450°, 720° and negative rotations, cardinal/diagonal/zero moves, moving origins and three densities. A 90° fixture distinguishes the former incorrect assumption. Local runner is ignored under `.tooling/phase3a/CanvasDragCheck.java`.
- Committed debug-only `EditorDragRegressionChecks` provides analogous Android Matrix checks through `runEngineChecks=true`; no new test framework, project writes or visual automation. It compiled but has **not executed on Android in this session** because no device was detected.
- Arithmetic/source checks do not prove live Compose gesture dispatch or subjective tracking. Manual production tests below remain required.

If the manual test exposes a broader issue, record angle/object/snap state and re-defer for a separate GPT-6 gate. Do not silently perform repeated alternative repairs in Phase 3A.

## 9. Safe upgrades implemented

| Decision | Scope | Why it meets SAFE NOW |
| --- | --- | --- |
| SAFE NOW / P1 | Correct local-to-canvas delta in three body drag consumers | Isolated common root cause; no saved-data or renderer change; independent numerical oracle and manual acceptance path. |
| SAFE NOW / supporting extraction | `CanvasCoordinates.kt` | Reuses the existing formula and existing anchor behavior; avoids three copied matrices. No package shuffle or speculative kernel. |
| SAFE NOW / debug validation | Opt-in Matrix regression check on existing debug Activity | Debug source only, no dependency, no production navigation change or test data. Release manifest checked. |

No other engine opportunity justified a production code change in 3A.

## 10. Deferred and rejected opportunities

Priorities describe potential impact, not a claim that a reproduced failure occurred in this session.

| Priority / classification | Evidence and limitation | Why defer / next bounded gate |
| --- | --- | --- |
| P0 conditional / DEFER | ConfigStore catches parse failures and returns default config; later save can overwrite the original failed payload. No corrupt example observed. | Requires load-result/recovery/quarantine semantics and historical fixtures. A catch or silent fallback change alone is unsafe. |
| P1 / DEFER | `PinWidgetReceiver` copies a project snapshot but does not write `AppWidgetOptions.projectId`; `refreshProjectWidgets` searches that option. New pins therefore are not established live links. | Decide snapshot vs live linkage, existing pins and project-deletion fallback. Naively adding the option can make missing-project default config overwrite a valid widget snapshot. |
| P1 / DEFER | Generic time/zone broadcast handler enumerates generic IDs; sized receivers only subscribe to widget update. | Dedicated widget lifecycle test for all provider classes and solar refresh; preserve native TextClock behavior. |
| P1 / DEFER | Editor config/history/routes rely heavily on `remember`; process recreation can lose unsaved state. | Define restored draft/history ownership before introducing a state holder; exercise both editor entry paths and autosave. |
| P1 / DEFER | Full sunrise/sunset/twilight/polar events | Event semantics, fixtures/cache and refresh policy described in §5; no astronomy expansion now. |
| P1/P2 / DEFER | Imported font/icon files and names exist without a production registry/consumer | Asset identity, missing-file recovery, format/license validation and shared renderer resolver belong to 3E. Preserve current imports. |
| P2 / DEFER | Time/formatter allocations, duplicate font resolver and preview/widget layout differences | Extract when touched by an approved production slice; establish parity fixtures, do not promise performance without measurement. |
| P2 / DEFER | Workspace previews are incomplete representations and legacy chrome is monolithic | 3B rebuilds presentation; a new thumbnail renderer is a separate scoped follow-up. |
| P2 / DEFER | Settings global preference inheritance is not represented by current JSON | GPT-6 compatibility gate before 3C behavior migration; retain concrete legacy values, standalone widgets and copies. |
| Optional / DEFER | Weather | External service/cache/background/terms/credential scope exceeds SAFE NOW. |
| REJECT | Whole-editor rewrite, bulk renderer unification, mechanical domain/data package shuffle, new navigation/DI framework just for organization | No isolated benefit that justifies broad regressions. |
| REJECT | Fixed offsets, fake weather, material-specific duplicate shape IDs, mass Golden asset promotion, renaming saved IDs | Conflicts with correctness, approved architecture or compatibility. |

## 11. Production architecture target and design-system adoption

Use incremental extraction around actual owners. Keep `model/`, `data/`, `widget/`, `solar/` in place initially; do not create unused `domain/` layers. MainActivity should ultimately host entry/theme/navigation composition, with routes owning screen state and screens receiving state/actions.

```text
ui/
  theme/       existing BestMates tokens + later LegacyAppearanceAdapter
  icons/       existing OrbitIcons; curated approved replacements only when scoped
  components/  CrystalSurface, CrystalButton, CrystalIconButton, SelectionCard
  workspace/   WorkspaceRoute, WorkspaceScreen, ProjectCard, WorkspaceState
  settings/    SettingsRoute/Screen + preference sections (3C)
  editor/      CanvasCoordinates (now); EditorRoute/Screen/State (3D)
  dialogs/     extract used shared dialogs when a second consumer exists
  pickers/     unified picker shells when actual asset integration begins
data/
  existing ConfigStore / ProjectStore / CityCatalog retained
  assets/      future registry/resolver only after 3E contract gate
solar/         existing Boolean engine retained
widget/        existing renderers/providers retained
model/         current persisted identities retained
```

| Treatment | Concrete migration |
| --- | --- |
| EXTRACTED | Now: used coordinate helper. Later: Workspace route/state first, then Settings, then Editor orchestration and pure screen content. Preserve callbacks and store commands at each extraction. |
| WRAPPED | Route-local `BestMatesTheme`; explicit legacy identity/brightness mapping. Current Cloud/Dream/Mono remain light and Midnight dark until independent settings mode is approved. No arbitrary project's theme drives app chrome. |
| MIGRATED | Frozen preview surface recipes into reusable production components as first needed; then workspace/dialogs, Settings sections, editor inspector/pickers and canvas chrome. |
| LEFT ALONE | JSON/migrations/IDs, IANA time, native TextClock, renderers/providers, city catalog, existing solar assets and gesture geometry unless a separate bug gate authorizes work. |
| REMOVED LATER | Legacy screen/helper duplicates and preview bridges only after callers migrate and regression gates pass. Keep Golden Set 3 Celestial/Sources available for the deferred artwork review. |

`BestMatesTheme`, colors, typography, dimensions, elevation, states and motion become the only chrome-token source in each migrated route. Extract existing surface drawing/press recipes from `BestMatesFoundationPreview.kt`, rather than copying preview screens into production. No adapter is required until the first real route consumer exists.

- **Pearl Glass:** icon/compact buttons, chips, field shells and small selectors; light depth, semantic targets and controlled edges.
- **Milk Crystal:** project cards, picker/inspector panels and rows, selection/theme cards; medium depth and readable content.
- **Jelly Crystal:** primary Create/Apply actions; strongest restrained depth, sufficiently opaque label band.
- Keep frozen layered tone, pearl highlight, lower refraction, light/dark differences and pressed-state recipe. Quiet Motion remains 80ms press / 120ms release / 160ms state; no bounce or new springs.
- Production components accept labels/state/callbacks and provide semantic selected/disabled/error states, accessible touch targets, and content descriptions. Use one check **or** one semantic dot; Soft Halo is secondary decoration. Generic Apply actions get no decorative checkmark.
- `CrystalButton` / `CrystalIconButton` / `SelectionCard` come first for 3B. Chips, fields, toggles/sliders, dialogs and picker shells follow their first real consumers, without inventing a large unused component catalog.
- Work Mat informs visual canvas surroundings only. Editor remains **fixed canvas at the top + vertically scrolling soft inspector below**, preserving existing gesture dispatch and content coordinates.
- Orbit uses accepted/provisional Phase 2D choices (global Settings gear, local Adjust, approved Undo/Redo, production Pin, user-selected Rotate). No icon/artwork reset.

Future widget objects keep **Shape × Variant × Material × Appearance** separate. Production `WidgetShape` identity remains stable; variant metadata must not rename saved shapes. Transparent/legacy, Mist Glass, Milk Crystal, Dream Frost and Jelly Veil are possible content materials, distinct from app-chrome recipes even when names overlap. Material/appearance metadata and resolver integration require the 3E compatibility gate; transparent legacy components stay valid.

### Phase 3 production UX reconstruction rule

Every remaining screen migration begins with a short layout plan before Compose implementation. The legacy screen is a behavioral reference, not a layout specification. Preserve proven engines, behavior, navigation semantics, persistence contracts, stable IDs and saved data; reconstruct obsolete hierarchy, grouping, spacing, toolbar placement, density, fixed/scrolling regions and visual rhythm when the Best Mates 2.0 product model needs it.

For each screen, record:

1. Behaviors that must survive.
2. Data and controls that must remain accessible.
3. Legacy layout decisions that can be discarded.
4. The new Best Mates 2.0 information hierarchy.
5. Primary and secondary actions.
6. Fixed and scrolling regions.
7. Safe-area and compact-width behavior.

This planning gate applies to Settings, Editor, asset pickers, appearance controls, and Font, Color, Celestial and Shape selection surfaces. Do not migrate them as legacy composition with new tokens.

Typography follows semantic roles. Expressive typography may later support brand moments, display headings, collection titles and selected empty/editorial moments. Functional typography remains highly readable for actions, Inspector controls, Settings, data labels, fields, menus and accessibility-critical text. Future Type Wardrobe integration assigns fonts through these roles; it does not assign arbitrary fonts screen by screen or alter persisted font behavior without its own compatibility gate.

## 12. Phase 3 migration sequence

| Phase | Goal / likely files | Protected behavior / main risk | Model and manual gate |
| --- | --- | --- | --- |
| 3A | Engine map; isolated drag correction; this migration contract | Protected IDs/stores/renderers; live finger tracking required a device gate | GPT-6 judgment and host checks completed; user later passed TASK-007 on the physical production Editor. |
| 3B / 3B.1 | Workspace 2.0; extract `WorkspaceScreen`, introduce first production components, then reconstruct hierarchy and compact selection/card layout | Project create/open/rename/selection/copy/cut/paste/delete, UUIDs, editor/settings entry. Risk: callback/state or saved-value loss. | Core operations physically verified; reconstructed layout awaits the focused 3B.1 visual gate. |
| 3C | Settings 2.0; `ui/settings`, settings store/resolver only after contract approval | Language/import references and retained project/standalone-widget theme/time values. Risk: silent global overwrite or incomplete editor-control relocation. | Sol for UI; GPT-6 before inheritance/schema policy. Check old 12/24-hour projects, standalone/linked instances, copies, new defaults and both editor routes. |
| 3D | Editor 2.0; route/state/inspector/tool/picker extraction, Work Mat chrome around existing LayoutCanvas | Drag/resize/rotate/snap/guides, history, locks, autosave/save/back and fixed-canvas structure. Risk: coordinate frame/lifecycle/recomposition regressions. | Sol for small presentation slices; mandatory GPT-6 gate before moving gesture ownership or geometry. User tests all three object types, edge/multi-turn/snap/Undo cases. |
| 3E | Approved asset/material/appearance integration; registry/resolvers, picker models, explicit compatibility metadata | Stable IDs/imports/transparency, thumbnail/canonical/widget agreement and licences. Risk: render drift or broken old project assets. | GPT-6 contracts, Sol bounded integration; user checks old/new projects + actual pinned widgets, missing imports and selection fidelity. Celestial final pass remains separate. |
| 3F | Consolidate proven paths, compatibility regression, remove unused bridges/duplicates | Historical JSON, project/widget lifecycle, locales, all declared sizes and fallback behavior. Risk: cleanup removing reachable compatibility. | Sol for proven cleanup/tests; GPT-6 for unresolved storage/lifecycle/render questions. User verifies upgrade/reopen/pin/resize/save/restart. |

These phases can contain small reviewable slices. No bulk package move is a prerequisite. Settings inheritance and asset contracts must be resolved before their dependent UI controls are removed or promoted.

## 13. Regression risks and gates

| Risk | Required protection |
| --- | --- |
| Correct matrix but different live pointer behavior | Physical layer/city/text tests with fresh drag after rotation; no success claim from compilation/numerical checks alone. |
| Changed modifier order during 3D | Explicitly review whether pointer nodes are inside/outside rotation; applying the new conversion twice would be wrong. |
| Snap/bounds mistaken for drag failure | Compare small objects with snap off, then on; separately record edge limits and guide attraction. |
| Index selection/history during extraction | Preserve gesture batching and check Undo/Redo, deleting/reordering objects, cancel and back. |
| Appearance inheritance mutates user content | Old stored fields retained; copy/standalone/linked/deleted-project fixtures before preference migration. |
| Canvas/widget drift | Retain renderers and normalized conventions; verify on the home screen whenever rendering or contracts actually change. |
| Debug bridge in release | Debug Activity/checker stay under `src/debug`; merged release manifest must not expose Design Lab. Move preview source only in a later proven cleanup. |
| Visual direction drift | Use frozen tokens/artwork decisions; subjective acceptance belongs to user. No repeated automated screenshot sweep. |

## 14. GPT-6 → Sol handoff

Model assignments below follow the user's requested split and the risk of each repository change. They are engineering recommendations, not measured Best Mates model benchmarks or a guarantee of outcomes. The official comparison describes GPT-5.6 Sol for complex professional work and GPT-6 Astra for the hardest end-to-end work; no model setting is changed by this document. [Official OpenAI model comparison](https://developers.openai.com/api/docs/models/compare).

**Locked:** compatibility contracts in §3; incremental route-first architecture; existing engine/renderers; fixed canvas + scrolling inspector; Settings ownership and preservation rules; frozen Phase 2 materials and accepted Orbit choices; Shape/Variant/Material/Appearance separation; no automatic candidate promotion.

**Sol may implement after a separately authorized phase:** Workspace presentation extraction and first shared components; Settings visual structure using current behavior; routine Editor visual slices preserving engine callbacks; agreed registry/picker integration after the contract gate. Use existing stores without adding frameworks for their own sake.

**Requires GPT-6 review:** moving pointer nodes/coordinate spaces, canvas bounds/snap/resize engine changes, new editor history/draft ownership, preference inheritance/storage migrations, corrupt-data recovery, pin snapshot/live-link semantics, provider scheduling, material identity/render contracts and any repeated TASK-007 repair after this bounded attempt.

**Deferred:** full solar events/twilight, weather, final Celestial artwork, asset production promotion/import registry, lifecycle/recovery improvements and measured performance work.

**Must not be touched without explicit scope:** reference `1/` / `2/`, persisted IDs/legacy migrations, widget receiver semantics, existing project files/imports, production celestial artwork or frozen icon direction. Do not treat the backlog as automatic authorization.

Exact recommended **Phase 3B starting task**:

> Extract the existing Workspace route and rebuild only its project list/empty state, Create action, selection controls and rename dialog with the frozen Best Mates tokens and production Crystal components. Use existing ProjectStore commands and UUIDs unchanged. Adopt the new theme only within the Workspace route using legacy identity/brightness mapping. Preserve entry into the existing Editor and Settings, project creation/name fallback, open, long-press selection, back-deselect, copy/cut/paste/delete and copy preservation of saved widget fields. Do not add theme-mode/time inheritance, a new thumbnail renderer, live widget links, asset promotion or editor extraction. Stop after build, device preparation and the Workspace review gate.

This is a handoff, not the start of 3B.

## 15. Validation and manual device-review checklist

Build: **PASS**, `assembleDebug --warning-mode all`, 2026-09-10. Resumed verification completed successfully; all 43 tasks were up to date and no warnings were emitted in that invocation. `:app:testDebugUnitTest` reports **NO-SOURCE**, not a passing UI/unit suite. Host compiled-helper numerical checks: **PASS**, as detailed in §8.

Release safety: **PASS**, `:app:processReleaseMainManifest`; merged release manifest contains no `BestMatesDesignLabActivity` or debug entry. Debug manifest still has no launcher intent. No dependency/provider/permission/resource change.

Device: **NOT CONNECTED** in the Phase 3A verification session. APK is ready at `app/build/outputs/apk/debug/app-debug.apk`. No install, Android Matrix execution, launch or physical acceptance is claimed. No screenshots captured.

Run from the repository root after connecting the phone:

```powershell
& .\.android-sdk\platform-tools\adb.exe devices -l
& .\.android-sdk\platform-tools\adb.exe install -r .\app\build\outputs\apk\debug\app-debug.apk
# Optional deterministic Android check; opens the existing Design Lab.
& .\.android-sdk\platform-tools\adb.exe shell am force-stop com.bestmates.worldclock
& .\.android-sdk\platform-tools\adb.exe shell am start -n com.bestmates.worldclock/com.bestmates.worldclock.debug.BestMatesDesignLabActivity --ez runEngineChecks true
& .\.android-sdk\platform-tools\adb.exe logcat -d -s BestMatesEngineChecks:I AndroidRuntime:E '*:S'
# Highest-priority manual review is the actual production editor:
& .\.android-sdk\platform-tools\adb.exe shell am start -n com.bestmates.worldclock/.MainActivity
```

Shortest path: Best Mates → open a disposable/copy project → Editor. Use a small component, one city and one custom text; a very large Heart near the edge is a poor first direction fixture. Do not use an important project for destructive checks.

1. Snap off: for component, city and text separately, rotate to 0°, 45°, 90°, 180° and 270°; start a fresh body drag right/left/up/down. Movement should follow the finger in screen direction with no sudden axis swap.
2. Repeat after 360°, 450°, 720° and negative rotation, including drag → rotate → drag. Confirm the initial movement does not use an old rotation value.
3. Snap on: check grid/edge guides, edge placement and free movement between snaps. Resize and use the rotation handle after moving; compare existing bounds behavior.
4. Check lock/Pin, blank deselection, Undo/Redo after a complete drag, Save → reopen, and a copied project retaining its settings. Existing widget time/rendering should remain unchanged.
5. Optional comparison: Design Lab → Golden Set 3 → Work Mat; same engine, disposable scene. Celestial and Sources remain available but are outside this acceptance task.

This checklist was later completed successfully on the production Editor: the user accepted rotation-after-drag and TASK-007 is closed. Any future coordinate change requires a separately scoped, evidenced regression. Celestial artwork remains separate.
