# Best Mates — Project status (0.4.0 reconstruction)

Branch `claude/best-mates-reconstruction-2026-10-10`, started from `b2a2e2c`.
Last updated 2026-10-14. This file supersedes the phase gates in `CURRENT_SESSION.md`,
`PROJECT_HANDOFF.md` and `TASKS.md`; those remain the historical record.

Aesthetic approval belongs to the user. Everything below marked **verified** was checked
on a 1080 × 2340 / 450 dpi Android 15 emulator (the same screen as the Samsung SM-A5760)
and by JVM unit tests. Nothing has been installed on the user's phone.

## What changed

### 1. Design language — "Daybook"
- One warm palette: paper and ink, with day (sun) and night (moon) as the only motifs.
  Light and dark appearances; the four stored identities (Cloud, Dream, Midnight, Mono)
  now pick an accent (Marigold, Rose, Iris, Graphite), so old preferences still resolve.
- Bundled typography (SIL OFL 1.1, licences in `app/src/main/assets/licenses`):
  Fraunces for display, Figtree for the interface.
- A new component kit (`ui/design`): buttons, custom switch, slider, segmented control,
  chips, fields, sheets and dialogs. The old "Crystal" glass components and theme moved to
  the debug source set, where the archived Golden Set review gallery still runs.
- A consistent action-icon family (`BmIcons`), keeping the approved Orbit Undo, Redo and
  Settings glyphs.
- A new adaptive launcher icon with a monochrome (themed) layer and a branded splash: a
  marigold sun cradled by a crescent moon — the two "mates".

### 2. Workspace
- A staggered gallery of **live widget previews** (real times, sun/moon state) on each
  project's backdrop, instead of grey thumbnails that showed only the background shape.
- Stable order (most recently edited first). The old order came from an unordered set and
  reshuffled between launches.
- Long-press selection with rename, duplicate and delete. Cut/paste of whole projects
  is gone; Duplicate covers it.
- A creation sheet with a name and the widget's real home-screen size (2×1 … 4×5).
- An "On home" marker for projects that are placed as widgets.

### 3. Editor
- Canvas-first layout: a dotted desk, a backdrop that stands in for the wallpaper (the
  default uses the phone's own wallpaper colours via `WallpaperColors`, no permission),
  the widget's bounds and cell grid, and a floating toolbar (day/night preview, snapping,
  grid, backdrop).
- A rewritten gesture layer (`EditorCanvas`). One detector hit-tests rotated frames in
  board coordinates, so rotated drags need no per-element correction. It adds right and
  bottom edge handles, two-finger pinch and rotate, magnetic guides to edges, centres,
  quarters and other elements with haptic ticks, and live size and angle readouts.
- A contextual inspector with tabs per element: shape, colour, edge and depth; city,
  type, colour and sky; text, font and colour. Plus a font gallery, a colour sheet
  (swatches plus a saturation/value field, hue and opacity), shape gallery, sun/moon
  library, layers and widget-settings sheets.
- Autosave (debounced, plus on pause and exit). Each gesture or slider drag is one undo
  step; changing the selection commits pending typing.
- City search accepts any script (東京, 东京, Москва). The old picker rejected non-ASCII
  input.
- New cities match existing city sizes. A single row of cities re-spaces evenly to
  include a newcomer.

### 4. One scene engine for editor, previews and the widget
- `scene/` (SceneBuilder, SceneRenderer, SceneGeometry, ShapePaths) measures and draws the
  design once. The editor canvas, the Workspace previews, the pin preview and the launcher
  bitmap all use it, so they cannot drift apart.
- Shapes are normalised to fill their own bounds exactly. Cloud, four-petal, ticket,
  banner, arch and soft-square were redrawn as clean constructive geometry. The approved
  heart is unchanged.

### 5. Home-screen widget
- **Bug fixed:** the project name appeared as a stray header above every widget.
- **Bug fixed:** pinned widgets never received later edits, because the pin flow never
  recorded which project they came from. Widgets are now bound to their project
  (`WidgetBindings`) and refresh on save.
- **Bug fixed:** rotation of cities and text was ignored on the home screen. Rotated text
  now renders rotated, including the live time and date (`View.setRotation` is
  remotable; verified on Pixel Launcher).
- **Bug fixed:** providers had no `initialLayout`.
- Layouts are responsive: one per size the launcher reports (portrait and landscape).
- The pin confirmation dialog shows the real design, at the project's own cell size,
  instead of a generic grey "WORLD TIME" placeholder that was always 4×4.
- Tapping a widget opens its project. Sun/moon icons refresh shortly after the next
  sunrise or sunset of any city, not only every 30 minutes.
- Launcher cell size is learned from placed widgets, so new projects use this phone's
  real proportions.

### 6. Persistence and migration
- Schema 9. `ConfigCodec` is a pure codec containing the whole migration ladder
  (v1 → v9), tested on real historical JSON payloads.
- Before an older payload migrates, its raw JSON is copied once to `backup_v<n>_<key>` in
  the same preferences file (a recovery path). An unreadable payload is also backed up
  instead of being silently replaced.
- A malformed city no longer discards the whole project.
- Legacy font identifiers map to current styles that keep their old look; for example
  `SANS_ROUNDED` (the old default) maps to bold system sans.
- Verified: the three seeded v8 projects upgraded in place from the baseline APK, kept
  their content, and their pre-existing (unbound) home-screen widget re-rendered through
  the new engine.

## Before and after
Screenshots are on the emulator with identical seeded data; full set in `docs/review/2026-10-14/`.

| Surface | Before | After |
|---|---|---|
| Workspace | ![](docs/review/2026-10-14/before-workspace.jpg) | ![](docs/review/2026-10-14/after-workspace.jpg) |
| Editor | ![](docs/review/2026-10-14/before-editor.jpg) | ![](docs/review/2026-10-14/after-editor.jpg) |
| Inspector | ![](docs/review/2026-10-14/before-inspector.jpg) | ![](docs/review/2026-10-14/after-inspector.jpg) |
| Pin dialog | ![](docs/review/2026-10-14/before-pin-dialog.jpg) | ![](docs/review/2026-10-14/after-pin-dialog.jpg) |
| Home screen | ![](docs/review/2026-10-14/before-home-widget.jpg) | ![](docs/review/2026-10-14/after-home-widget.jpg) |
| Settings | ![](docs/review/2026-10-14/before-settings.jpg) | ![](docs/review/2026-10-14/after-settings.jpg) |

## Verification
- `assembleDebug` passes. **54 JVM unit tests pass**: migration ladder, codec round trip,
  geometry, snapping, rotation snapping, font mapping, solar schedule, city placement,
  editor history, plus the earlier suites.
- Emulator, **verified**: upgrade over 0.2/0.3 data; Workspace ordering, creation,
  selection, rename, duplicate and delete; editor select, drag, rotate, add
  shape/city/text, inspector tabs, font gallery, city search, autosave, undo; pin flow to
  the home screen with the real preview; bound widget refresh after an edit; rotated live
  time; dark mode; Chinese.
- **Not yet verified:** the user's Samsung SM-A5760 and One UI launcher (cell sizes, font
  substitution); landscape; Android 8–11 positioning; long-running sunrise/sunset alarms;
  accessibility with TalkBack and large font scales.

## Known limitations
- **Live time and date use system typefaces.** Launchers inflate widgets in a restricted
  context that ignores app-bundled fonts, so a ticking `TextClock` cannot use Fraunces or
  Figtree. Time and date offer 14 system styles (thin through black, narrow, serif, mono,
  script), and the editor draws the same system faces, so the preview still matches.
  City names and custom text use all 21 bundled fonts. A future "precise" mode could draw
  time in any font and redraw every minute (with exact alarms, at a battery and
  reliability cost) — see next steps.
- Live date text follows the device locale, as the launcher's `TextClock` does.
- German, French, Russian, Turkish and Arabic cover the most visible strings only; the
  rest falls back to English. Arabic right-to-left layout is unreviewed.
- The sun/moon artwork library is the existing BM set. The deferred Celestial final pass
  is still open.
- Snapping thresholds and handle sizes need checking by hand on the real phone.

## Installing on the Samsung (not done — needs your go-ahead)
The phone has `com.bestmates.worldclock` 0.3.0 (versionCode 3) with real projects. This
build is 0.4.0 (versionCode 10), so it installs as an upgrade **only if it is signed with
the same key** as the installed app.

Known signing certificates (SHA-256):
- This build and the September builds in the original `Android Development` folder:
  `c7cef145e25856271b04b2281e443e58f1ac945a8a1ea2c2024b97478bf42241` (this PC's debug key).
- The public `BestMates-v0.1.0-debug.apk` release asset:
  `cd8d1b432ff677abf5f219f35f2e6155da5e738a4c92eed583b45b3bdca194f8` (a different key).

Read-only check of the phone (copies the installed APK to the PC; changes nothing):

```powershell
$apk = (C:\ADB\adb.exe shell pm path com.bestmates.worldclock).Replace("package:", "").Trim()
C:\ADB\adb.exe pull $apk "$env:TEMP\bm-installed.apk"
& "C:\Users\JCLXJ\AppData\Local\Android\Sdk\build-tools\35.0.0\apksigner.bat" verify --print-certs "$env:TEMP\bm-installed.apk"
```

If it prints `c7cef145…`, `adb install -r app\build\outputs\apk\debug\app-debug.apk`
upgrades in place and migrates the projects; each raw pre-v9 payload is backed up first.
If it prints anything else, do **not** uninstall, because that would erase the projects.
Export them first (a next step), or build with the matching key.

## Next steps
1. Phone acceptance on the Samsung: cells, fonts, snapping feel, widget sizes 2×1 to 4×5.
2. A precise-typography option for live time (bitmap time with exact minute alarms),
   off by default.
3. An analog clock element. `AnalogClock` with custom dial and hand icons is remotable
   on API 31+, so it would stay live with artwork in the Best Mates style.
4. Templates in the creation sheet (Duo, Family, Postcard).
5. Celestial final pass: a redrawn sun/moon family in the mark's language.
6. Project export/import (JSON) as a user-facing backup.
7. Complete translations and an RTL review; TalkBack and font-scale pass.
