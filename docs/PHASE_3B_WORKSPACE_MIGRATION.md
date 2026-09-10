# Best Mates 2.0 — Phase 3B Workspace Migration

Date: 2026-09-10
Starting HEAD: `7bbf2638d5913105fe99a3a86070d411b4046d6c`
Status: **IMPLEMENTED / BUILD_PASS / MANUAL_VISUAL_QA_PENDING**

Phase 3B migrates only the production Workspace. Settings and Editor remain on their existing presentation and navigation paths. TASK-007 remains **IMPLEMENTED — MANUAL QA PENDING** and its code is unchanged in this phase.

## Architecture

`MainActivity` still owns app entry, the existing destination state and transitions. Its Workspace branch now maps the four legacy app themes into the Phase 2 design foundation and calls `WorkspaceRoute`:

```text
BestMatesApp
  legacy app theme/language state
  └─ Workspace-only BestMatesDesignTheme
       └─ WorkspaceRoute
            ├─ ProjectStore commands and local selection/clipboard/dialog state
            └─ WorkspaceScreen presentation
                 ├─ identity/top bar
                 ├─ contextual selection bar
                 ├─ empty state or project collection
                 ├─ project cards and existing background thumbnails
                 └─ Jelly Crystal Create action
```

The theme bridge preserves current behavior: Cloud, Dream and Mono resolve to their Light foundations; Midnight resolves to Dark. The foundation itself remains structurally capable of all identity × brightness combinations. Independent app brightness belongs to Settings 2.0 and was not invented here.

Visible Workspace strings are passed through `WorkspaceStrings`, keeping the existing app language owner in `MainActivity` until localization receives its own extraction. New Workspace copy has English and Chinese values; other existing languages follow the repository's current English-fallback policy for new keys.

## Production components introduced

`ui/components/CrystalComponents.kt` contains the first shared production UI primitives used by a real route:

- `crystalSurface`: one token-driven Pearl Glass / Milk Crystal / Jelly Crystal implementation with frozen press, highlight, refraction, border and elevation behavior.
- `OrbitButton`: primary, secondary, quiet and destructive roles; 48dp-or-larger targets; Orbit icon support and token-driven disabled state.
- `OrbitIconButton`: 48dp target with 20dp artwork, selected/destructive states and semantic content descriptions.

The Design Lab's compatibility wrapper now delegates to `crystalSurface`, so preview and production do not maintain separate material algorithms. No token values are duplicated in Workspace.

## Behavior preserved

- Loads the same saved project UUIDs with `ProjectStore.ids()` and `ProjectStore.load()`.
- Opens the same Editor route with the same project ID.
- Creates the same empty `WidgetConfig` with a trimmed name or the existing localized unnamed fallback, then opens it immediately.
- Retains tap-to-open, long-press selection, tap toggling while in selection mode, multi-selection and system Back to clear selection.
- Retains single-selection rename, copy and cut; copy/cut store the complete `WidgetConfig` in the route-local clipboard.
- Retains paste as a new UUID with the entire copied config. A paste action remains available after copy/cut clears selection.
- Retains multi-selection delete and existing persistence calls. Workspace delete now adds a count-aware confirmation because the Workspace has no undo.
- Retains project title, background/city counts and auto-save/edited-locally metadata.
- Reuses `BackgroundRenderer.renderLayers()` for the same lightweight background-only thumbnail. Rendering is remembered per layer/theme/density; no continuous render loop or new cache/persistence layer was introduced.
- Keeps Add to Home and widget configuration flows in the existing Editor/provider paths.

Project ordering remains the existing SharedPreferences string-set order. Clipboard remains in-memory and route-local, as before. These were not changed under a presentation migration.

## Presentation migrated

- Floating Creation Space with semantic atmosphere tokens and safe system insets.
- Soft identity header with a Pearl Glass Settings action; selection mode uses a selected surface and one Close action.
- Milk Crystal project collection with larger renderer-backed thumbnails, clearer title/metadata hierarchy and comfortable spacing.
- Selection uses selected fill + controlled edge + one rounded Confirm check. It never displays a dot and check together.
- Contextual horizontally scrollable action surface for Paste, Rename, Copy, Cut and restrained destructive Delete. Every action retains a 48dp minimum target.
- Calm empty personal-creation state built from local drawing primitives only; no illustration or network asset.
- Jelly Crystal Create widget action fixed at the bottom of the Workspace content region.
- Token-driven Quiet Motion for material press/release and the contextual toolbar transition; no springs, bounce or overshoot.
- Milk Crystal naming and deletion dialogs with explicit primary/destructive hierarchy.

## Compatibility boundaries

No changes were made to `ProjectStore`, `ConfigStore`, model fields, JSON, migrations, project/widget keys, widget IDs/providers, renderers, shape or asset IDs, time/solar logic, Settings, Editor, canvas, drag/rotation/resize/snap code, weather, Celestial assets, color wheel, or reference directories `1/` and `2/`.

The existing outer app theme remains around Settings and Editor. Workspace alone opts into `BestMatesDesignTheme`. This avoids a bulk production theme migration and keeps Phase 3C/3D behavior isolated.

## Remaining legacy code

- `MainActivity.kt` still owns destination state, localized dictionaries, `AppThemeStyle`, `AppLanguage`, the legacy outer `BestMatesTheme`, Settings and the Editor. A full navigation rewrite is not required for the planned route-by-route migration.
- `WorkspaceStrings` is an explicit bridge to the legacy localization owner. Move localization only under a focused app-shell or consolidation task.
- The background-only thumbnail is intentionally incomplete: city and text overlays are not shown. A full canonical thumbnail renderer would involve preview/widget parity and remains deferred.
- Workspace state still uses `remember`, matching prior behavior; process restoration and persistent clipboard are outside Phase 3B.
- Design Lab retains a small `AtelierCrystalLevel` compatibility enum/wrapper for its existing call sites, but delegates material drawing to the production component.

## Validation

- `assembleDebug --warning-mode all`: **PASS**.
- `:app:testDebugUnitTest`: **NO-SOURCE**; no test framework was added.
- Static structure: the selection actions scroll horizontally, cards can grow with text, names are limited to the existing 40 characters, interactive targets are at least 48dp, and root content applies safe-drawing insets.
- Device: no device was connected during the implementation check. No physical launch or subjective visual approval is claimed.

APK: `app/build/outputs/apk/debug/app-debug.apk`

When a phone is available, run from the repository root:

```powershell
& .\.android-sdk\platform-tools\adb.exe devices -l
& .\.android-sdk\platform-tools\adb.exe install -r .\app\build\outputs\apk\debug\app-debug.apk
& .\.android-sdk\platform-tools\adb.exe shell am force-stop com.bestmates.worldclock
& .\.android-sdk\platform-tools\adb.exe shell am start -n com.bestmates.worldclock/.MainActivity
```

## Manual acceptance

Install and open the Workspace, then inspect the first launch/empty state and existing project collection. Create and open a project; long-press one and several cards; use Back/Close to deselect; verify Rename, Copy → Paste, Cut → Paste and the Delete confirmation with disposable projects. Confirm the Jelly Create action, Settings entry, Editor return, Cloud/Dream/Midnight appearance, system insets and large-text layout. Verify that no saved project disappeared or changed.

TASK-007 testing is not part of Phase 3B acceptance and remains pending.

## Exact Phase 3C starting point

> Begin Best Mates 2.0 Phase 3C — Settings 2.0. Extract only the production Settings route and presentation into `ui/settings`, reuse the Phase 3B production Crystal components, and preserve the current app-theme, language and imported-file preference keys/values. Implement the approved Visual Theme Gallery and move global theme mode plus 24/12-hour ownership into Settings only after defining and validating versioned inheritance/fallback behavior for existing projects, standalone widget instances, linked widgets, copies and new defaults. Do not remove existing concrete `WidgetConfig.theme` or `use24Hour`, silently rewrite saved projects, change providers/renderers, enter Editor 2.0, modify TASK-007, promote assets, or reopen Celestial. Require GPT-6 review before any persistence/inheritance contract change; Sol may implement the visual extraction and already-approved behavior. Stop after build and the Settings device-review gate.

Phase 3C has not started.
