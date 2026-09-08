# Best Mates 2.0 — UI Design Foundation

Status: **PHASE 2C.3 SOFT CRYSTAL ATELIER INTERACTION FOUNDATION — VISUALLY FROZEN**
Date: 2026-09-09
Implementation baseline: `ed86e9c` (`feat(ui): finalize Soft Crystal Atelier foundation`), with the repository-maintenance ignore rules retained. Phase 2C.3 keeps the approved Cute Atelier / Cozy Orbit palette, typography, geometry, layout, hierarchy, production isolation, and debug harness while closing interaction-surface, selection-indicator, icon-geometry, and system-inset defects.

Scope: design-foundation tokens, Orbit Charm vectors, the isolated Design Lab, and its debug-only device host. Production Workspace, Settings, Editor, app-root theme, persistence, widgets, rendering, gestures, canvas behavior, and TASK-007 remain unchanged.

## 0. Phase 2C authoritative refinement

This section supersedes earlier Phase 1 numeric palette, typography, radius, depth, state, icon, and component-recipe values wherever they conflict. Sections 1–15 retain the foundation rationale, ownership rules, accessibility contract, and production boundaries. The Phase 1 contrast table in §15 is historical evidence for the former palette and must not be used to approve Phase 2C colors.

The refined personality is **Cute Atelier + Soft Crystal + Orbit Charm**: milk-pearl and blush environments, plum hierarchy, scarce charm details, clear working surfaces, and a lavender-neutral canvas. Crystal treatment is a local material cue, not a full-screen glass effect. It is warm and collectible without becoming childish, candy-saturated, glass-heavy, or a stock Material theme. One card normally has zero or one decorative signature motif.

### 0.0 Phase 2C.3 frozen foundation contract

Phase 2C.3 freezes the three typed crystal families and their approved hierarchy:

| Family | Intended use | Required construction |
| --- | --- | --- |
| Level 1 — Pearl Glass | Chips, toolbar/icon actions, snap/guides tags, compact controls | Translucent pearl body; quiet vertical tone; theme contamination; 1dp translucent edge; short internal highlight; faint ambient shadow; lower refraction |
| Level 2 — Milk Crystal | Project/theme cards, font/shape/city tiles, inspector, picker, settings and selection tiles | Milk-translucent body; diffuse pearl veil; identity tint; defined tonal edge/refraction; medium ambient depth; optional short warm reflection |
| Level 3 — Jelly Crystal | One primary action per local decision area | Opaque semantic body for contrast; restrained vertical tone; local pearl light; translucent colored rim; strongest lower refraction and ambient depth |

The implementation uses cached Compose drawing, layered translucent fills, a low-alpha identity diagonal, a diffuse internal pearl veil, local top/bottom lines, hairlines, and restrained shadows. It does not require backdrop blur. The effect must remain readable when rendered over every resolved identity surface, and direct color samples must keep their authored color inside a separate neutral crystal frame.

### 0.1 Resolved neutral environments

Each identity now owns its neutral environment as well as its accent and atmosphere. Identity and Light/Dark remain independent.

| Identity / brightness | background | base | raised | inset | overlay | elevated | canvasMat |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Cloud / Light | `#FFF8FB` | `#FFFDFE` | `#FFFFFF` | `#F9F0F6` | `#FFFBFD` | `#F8EDF5` | `#F2EEF4` |
| Cloud / Dark | `#19131D` | `#211925` | `#2C2131` | `#151018` | `#35283B` | `#403048` | `#29222D` |
| Dream / Light | `#FFF5FA` | `#FFFBFD` | `#FFFFFF` | `#F9EAF2` | `#FFF9FC` | `#FCECF4` | `#F3EDF2` |
| Dream / Dark | `#1D121A` | `#271821` | `#33212C` | `#170E15` | `#3D2934` | `#49303E` | `#2C222B` |
| Midnight / Light | `#FAF7FC` | `#FEFCFF` | `#FFFFFF` | `#F1ECF7` | `#FCF9FF` | `#F5EFFA` | `#F0EDF4` |
| Midnight / Dark | `#120E17` | `#1B1521` | `#261D2D` | `#0E0B12` | `#30243A` | `#3A2B46` | `#241E2A` |
| Mono / Light | `#FBF8F8` | `#FEFCFC` | `#FFFFFF` | `#F2EDEC` | `#FDFBFB` | `#F7F1F3` | `#F0ECEE` |
| Mono / Dark | `#191719` | `#211E21` | `#2B272B` | `#141214` | `#342F34` | `#3D373D` | `#272327` |

| Identity / brightness | textPrimary | textSecondary | textMuted | borderSubtle | borderControl |
| --- | --- | --- | --- | --- | --- |
| Cloud / Light | `#322A35` | `#6C5E6C` | `#776879` | `#EBDCE6` | `#957F91` |
| Cloud / Dark | `#F8EDF5` | `#D4C1CF` | `#B09DAD` | `#4C394F` | `#937997` |
| Dream / Light | `#382A34` | `#765F6E` | `#7A6673` | `#EFD7E3` | `#96798C` |
| Dream / Dark | `#FFEEF5` | `#DCC1CE` | `#B49AA8` | `#583846` | `#A4768D` |
| Midnight / Light | `#302A38` | `#665D70` | `#716979` | `#E3DCEB` | `#897C92` |
| Midnight / Dark | `#F4EDF8` | `#CDBFD4` | `#A697AD` | `#44354E` | `#887695` |
| Mono / Light | `#312D30` | `#686166` | `#70696E` | `#E4DDDF` | `#8C8287` |
| Mono / Dark | `#F3EFF2` | `#CBC3C8` | `#AAA2A7` | `#474047` | `#898087` |

### 0.2 Identity accents and atmosphere

| Identity / brightness | accent / onAccent | accentContainer / onAccentContainer | atmosphereA / atmosphereB |
| --- | --- | --- | --- |
| Cloud / Light | `#8F60AD` / `#FFFFFF` | `#F1DDF6` / `#533461` | `#F7CFDF` / `#DDD0F3` |
| Cloud / Dark | `#DABCEA` / `#3D2348` | `#4D3157` / `#F7DFFF` | `#D88EAA` / `#9A7AC5` |
| Dream / Light | `#A15D83` / `#FFFFFF` | `#F6DDE9` / `#62344C` | `#F3BFD3` / `#DFC6F2` |
| Dream / Dark | `#ECB2CF` / `#4C2643` | `#5C3048` / `#FFDDEA` | `#C66F91` / `#9C70BC` |
| Midnight / Light | `#745B91` / `#FFFFFF` | `#E8DDF2` / `#49385E` | `#D8CBE8` / `#E7D3DC` |
| Midnight / Dark | `#D3B8E8` / `#352242` | `#493357` / `#F2DBFF` | `#76568E` / `#8C526C` |
| Mono / Light | `#786871` / `#FFFFFF` | `#EEE4E8` / `#494047` | `#E8DADD` / `#DFD8E2` |
| Mono / Dark | `#CDBFC7` / `#342C31` | `#443B41` / `#F1E8ED` | `#8D737F` / `#796F82` |

Supporting light families are rose `#B97891` / `#F8E3EC`, peach `#C98A72` / `#F9E5DC`, berry-red `#A84762` / `#FBE5EB`, sage `#5F7D6C` / `#E5F0E8`, and honey `#9A6D31` / `#F8EBD2`. Dark counterparts are `#E1ACC0` / `#553140`, `#E7B29D` / `#5B372B`, `#F1A2B5` / `#552532`, `#A8CEB5` / `#294335`, and `#E8C58F` / `#4C371D`.

Cloud Light keeps `#9367B2`, `#958897`, and `#B8A3B4` as visual north-star swatches. The resolved functional accent is deepened to `#8F60AD`, raising white-label contrast from 4.35:1 to 4.72:1. Functional muted-text and control-edge roles are also deepened just enough to remain at or above 4.5:1 and 3:1 respectively across their resolved surfaces.

Atmosphere uses two large static radial blooms behind opaque surfaces, peaking at `.28` in light and `.10` in dark. A single faint orbit arc is allowed in a branded region. The canvas receives no atmosphere gradient.

### 0.3 Refined type, geometry, state, and depth

The interface scale is editorial and compact: display roles are `36/44`, `32/40`, `28/36`; headline roles are `26/34`, `24/32`, `21/28`; titles are `20/26`, `16/22`, `14/20`; bodies are `16/24`, `14/21`, `12/18`; labels are `14/18`, `12/16`, `12/16`; `valueTime` is `24/30`. Display/headline use the rounded system family; UI text uses system sans. No widget-authored font is affected.

Radius tokens are `6, 10, 14, 18, 20, 24, 28dp`, plus full. Full is reserved for compact chips, segmented selections, badges, circles, and swatches. Primary buttons use 20dp, ordinary cards/inspectors 18–24dp, fields 18dp, and large overlay trays 28dp.

Selected, focus, and error outlines are `1.5dp`, `1.5dp`, and `1dp`; focus keeps a 2dp clear offset. Hover/pressed layers are `.06/.08`. Disabled content/container remain `.38/.08`. Semantic selection-marker tokens are a 6dp dot, an 18dp ordinary check, and a 16dp compact check. A selected surface resolves in this order: selected fill, controlled edge, exactly one semantic check or dot, then an optional Soft Halo. A check-bearing state never also displays a dot. The dot is allowed for a chip or compact selector only when no check is present. Soft Halo 2.0 is `accent` at `.20` light / `.24` dark with a 12dp diffuse falloff and maximum 4dp spread; it remains secondary decoration.

Depth fallbacks are 0dp, 2dp, 4dp, and 8dp. Reference blur values are 0, 10, 18, and 30dp. Light/dark shadow alphas are depth1 `.08/.18`, depth2 `.10/.22`, and depth3 `.12/.20`. Surface tone and tinted 1dp hairlines carry most hierarchy; nested children normally remain depth0.

The frozen `pearlGlass`, `milkCrystal`, and `jellyCrystal` recipes own fill alpha, resting elevation, shadow alpha, top/bottom tonal mixes, theme-contamination alpha, edge alpha, upper highlight, lower refraction, optional warm reflection, and highlight length.

| Family / brightness | Fill | Elevation | Shadow | Top / bottom mix | Theme tint | Edge | Highlight | Refraction | Warm reflection | Highlight length |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| Pearl Glass / Light | `.88` | 1.5dp | `.065` | `.045 / .055` | `.045` | `.46` | `.30` | `.14` | `.045` | `.68` |
| Pearl Glass / Dark | `.80` | 1dp | `.018` | `.07 / .09` | `.055` | `.62` | `.15` | `.22` | `.025` | `.68` |
| Milk Crystal / Light | `.93` | 3dp | `.085` | `.04 / .055` | `.055` | `.40` | `.26` | `.15` | `.05` | `.72` |
| Milk Crystal / Dark | `.86` | 2dp | `.026` | `.06 / .085` | `.07` | `.58` | `.14` | `.24` | `.025` | `.72` |
| Jelly Crystal / Light | `1.00` | 4dp | `.12` | `.02 / .075` | `.035` | `.48` | `.27` | `.19` | `.06` | `.62` |
| Jelly Crystal / Dark | `1.00` | 2.5dp | `.035` | `.04 / .11` | `.05` | `.64` | `.14` | `.25` | `.03` | `.62` |

Jelly Crystal keeps the action body opaque so label contrast cannot vary with the surface beneath it; its translucent colored rim, local light, and refraction still carry the crystal character. Dark mode uses weaker external shadow and stronger tonal edge/refraction instead of a black drop-shadow stack.

Pressed feedback resolves to 0.5dp elevation, 0.75dp downward translation, `.987` visual scale, and `3.5%` light / `4.5%` dark tonal deepening. Shadow strength falls to 22% of resting, highlight alpha to 48%, highlight length contracts to 46%, and lower refraction rises to 112% so the compressed object retains a defined lower edge. The toggle thumb grows by 1.25dp while pressed. Press/release use `motionPress` 80ms and `motionRelease` 120ms with `easeStandard`; `motionState` remains 160ms for state transitions. There is no spring, overshoot, or bounce, and the layout/hit region does not shrink. Text buttons remain nearly flat and use only a tonal pressed fill.

The Jelly Crystal label band retains the opaque `accent` / `onAccent` pair, whose eight resolved contrast values remain at least 4.5:1. Pearl highlights and identity contamination stay outside the primary label's central band; visual review must still inspect antialiased glyphs and actual device compositing.

### 0.4 Component and icon recipes

- Primary actions are 54dp soft rounded rectangles with plum fill, subtle depth, and high-readability content. Secondary uses pearl/rose tint. Outlined uses a quiet filled surface plus hairline. Text actions keep only an invisible 48dp touch region. Destructive resting uses berry text/icon on a light berry container, not a large red pill.
- The accessory toggle uses a 44×26dp Pearl Glass track inside a 48dp target, a 17dp resting crystal thumb, a fine semantic edge, and a 1.25dp pressed-thumb expansion. The slider uses a 5dp rounded tonal track, an 18dp Pearl Glass thumb, a local highlight/refraction rim, and a compact crystal value tag.
- Stationery fields use `surfaceInset`, 18dp radius, and a 1dp resting hairline. Focus shifts the surface and uses a 1.5dp accent edge with a small halo. Error uses the soft berry container, 1dp edge, and persistent helper text.
- Compact chips may remain pills, but use a 34dp visual body inside a 48dp target, thinner outlines, tighter text, and a 6dp dot marker for selection when no check is present. Avoid rows of large outlined capsules.
- Overlay/picker surfaces use `surfaceOverlay`, 28dp radius, clear title hierarchy, tinted hairline, restrained depth, and selected pearl cards. The future color picker uses a representative continuous hue/saturation disc, separate brightness and opacity controls, a distinct recommended-palette area, faithful color samples inside neutral frames, and one clear Apply action. Recommendations are shortcuts, never the complete color model.
- The future canvas study uses the resolved lavender-neutral `canvasMat`, grid at `.09`, precise guide at `.62`, a small measurement tag, Soft Halo selection, and the Orbit Charm Rotate/Resize pair. This remains a static Design Lab mockup.

**Orbit Charm** keeps a 24×24 viewport, 20dp default artwork, 48dp target, round caps/joins, and 1.8 viewport-unit stroke (about 1.7dp rendered). Normal utility commands use clean functional silhouettes without appended decorative dots. Dots, beads, petals, or sparkles remain limited to Theme, Sun, and Moon; semantic geometry such as a location centre, scissors loop, or settings hub is not decoration. Confirm is a plain semantic check with rounded terminals and bends, balanced at 16, 20, and 24dp, with no built-in ornament. Undo and Redo are exact mirrored relatives with separate readable heads and flowing curved bodies. The Font mark keeps comfortable negative space between its vector `A` and lowercase `a`. Pin remains a compact soft tack, optically distinct from the closed-shackle Lock. These corrections are provisional hygiene, not the Phase 2D Orbit Charm redesign, and production call sites are not migrated in this phase.

### 0.5 Design Lab presentation and review boundary

The Design Lab now has two layers. **Experience Showcase** comes first with a branded hero, mini Workspace, theme card, toolbar, inspector, picker tray, full icon family, Halo comparison, neutral canvas study, color atelier, and custom controls. **Token Diagnostics** follows with compact surfaces, colors, typography, spacing/radii, states, and motion. The selector still exercises all eight identity × brightness combinations without storage.

The debug Activity alone synchronizes status/navigation bar color and icon brightness with the selected Lab appearance. Its atmosphere intentionally draws edge to edge while the scroll viewport consumes `safeDrawing` insets before scrolling, keeping readable and interactive content below status icons and above navigation chrome. Production window behavior and production navigation are unchanged. Phase 2C is visually frozen after physical-device review unless a serious regression is found; no production screen migration is authorized by this specification.

### 0.6 Production Pin and Lock semantics audit

The production editor currently exposes only `EditorAction.PIN`. It toggles the selected layer/city/text model's existing `locked` flag. That flag prevents canvas movement and suppresses resize/rotation handles, while the selected item's inspector and property callbacks remain available. Current behavior therefore matches the intended **Pin = prevent positional canvas manipulation while allowing property edits** meaning.

There is no separate production Lock command or independent full-edit lock state. The foundation `OrbitIcons.Lock` artwork is reserved for a later **Lock = prevent all manipulation and editing** feature contract; it must not be surfaced as though this behavior already exists, and it must not alias Pin without an explicit product/data migration. Phase 2C.3 changes no production action, model, persistence field, gesture gate, or inspector behavior.

### 0.7 Production color capability audit

The production `VisualColorPickerDialog` converts existing ARGB to HSV, resolves hue continuously over 0–360° from pointer angle, resolves saturation continuously over radius 0–1, and exposes independent brightness and opacity sliders over 0–1. It writes the result through `HSVToColor(alpha, hsv)` and therefore retains continuous sRGB HSV selection plus alpha; saved authored colors are not limited to theme swatches.

The Design Lab color study must communicate that full capability. Its continuous hue/saturation disc is representative visual validation rather than a replacement picker implementation, and the displayed theme colors are explicitly a recommended palette. Any later production migration must preserve continuous hue, saturation, brightness, opacity, existing ARGB values, Apply/Cancel draft semantics, and color fidelity.

## 1. Direction and boundaries

| System | Name | Design intent |
| --- | --- | --- |
| Product | Best Mates 2.0 | A calm, approachable space for making personal world-clock widgets |
| Design language | Cute Atelier / Cozy Orbit | Cute atmosphere around a professional creative-tool skeleton |
| Atmosphere | Powder Blush + Milk Pearl + Lavender Veil | Emotional ambient color behind clearly separated working surfaces |
| Icons | Orbit Charm | A rounded outline family with clean utility silhouettes and scarce identity ornament |
| Motion | Quiet Motion | Short, purposeful feedback with no bounce or ambient animation |
| Selection | Soft Halo | A crisp state outline supported by a restrained halo and a non-color indicator |
| Workspace | Floating Creation Space | Breathing room around project previews, with one clear creation action |
| Editor | Fixed Canvas + Scrolling Soft Inspector | The canvas remains fixed above an independently scrolling editing area |
| Settings | Visual Theme Gallery | Themes are understandable through real miniature interface previews |
| Branding | Mates Orbit | Two companion forms sharing an orbit; warm, original, legible at small sizes |

The visual hierarchy is **created content → current editing task → navigation → atmosphere**. The conceptual blend is Japanese soft-tech, quiet editorial design, accessory/charm detail, and modern creative-tool clarity. Cute character belongs in chrome, cards, controls, pickers, selection, empty states, and scarce micro-details. The canvas remains a quiet lavender-neutral stage and never becomes pink. A surface normally carries zero or one decorative motif.

Non-negotiable boundaries:

- TASK-007 remains `PARTIALLY_COMPLETE / DEFERRED`. Rotation sensitivity is acceptable; post-rotation drag does not consistently follow the finger on a physical device. This document neither fixes nor resumes that issue.
- Preserve the editor's fixed canvas at the top and vertically scrolling inspector/editing area below. Do not replace it with a fully scrolling page, draggable inspector, or side inspector.
- Do not change gesture recognition, pointer coordinates, rotation/resize/drag calculations, snapping, hit testing, history, autosave, or rendering geometry under the label of visual polish.
- Global app theme mode and 24-hour / 12-hour time format belong in **Settings**. The final editor has no global theme or time-format controls.
- App chrome tokens do not replace user-authored colors, fonts, opacity, shape styling, or day/night palettes in saved widgets.
- Preserve JSON contracts, layout migrations, project and widget-instance storage, native providers, city timezones, solar calculations, and launcher pin flows.
- Do not read, edit, move, or delete reference folders `1/` and `2/` for this phase. Do not expand the repository cleanup scope.
- No production-screen redesign, font import, icon call-site migration, or broad architectural refactor occurs in Phase 2C. Implementation is limited to the foundation tokens, Orbit Charm vectors, Design Lab, and debug-only host.

## 2. Repository evidence and pre-foundation audit

This section records the repository state that motivated Phase 1 and the later isolated foundation work. It is historical evidence rather than a description of the Phase 2C files now present; the implemented target is defined in §0 and the current file inventory in §14.

Read before authoring: [AI_RULES.md](../AI_RULES.md), [PROJECT_HANDOFF.md](../PROJECT_HANDOFF.md), [TASKS.md](../TASKS.md), [CURRENT_SESSION.md](../CURRENT_SESSION.md), and [cleanup audit](REPOSITORY_CLEANUP_AUDIT.md), plus recent git history and the relevant source.

History resolves stale handoff statements: `31c3ebb` introduced continuous angle tracking; `60a9bb5` corrected the rotation control's coordinate frame; `cce5d13` explicitly deferred the remaining movement defect; `f9beacd` unified action icons. The older handoff's statement that rotation remains clamped to ±180° is not the current implementation. TASK-004/005 are implemented and awaiting visual review, not approved as finished.

During document preparation, a separate recovery checkpoint (`bacd712`) captured the draft, followed by audit commits `49c0853` and `3417f42`. These commits and [PROJECT_AUDIT.md](../PROJECT_AUDIT.md) are preserved. A comparison from `f9beacd` through `3417f42` confirmed that production application files and the required handoff/rules files were unchanged. The audit's cleanup recommendations are outside Phase 1.

Line references below are to the baseline above; function names remain the primary search anchors.

| Area | Evidence in repository | Foundation implication |
| --- | --- | --- |
| Compose theme | [MainActivity.kt](../app/src/main/java/com/bestmates/worldclock/MainActivity.kt), `AppThemeStyle` around L93 and `BestMatesTheme` L281–320 | CLOUD, DREAM, MONO select light schemes; MIDNIGHT selects dark. Palette identity and brightness are coupled. Only a subset of Material color roles is explicit; other roles inherit library defaults. |
| Typography | `BestMatesTypography`, L108–114 | Five roles customize system font aliases/weights, without a complete explicit size/line-height scale. Call sites add their own sizes and bold weights. |
| Navigation | `BestMatesApp`, L333–382 | Six inline navigation durations (170–260ms), offsets, and scale factors combine fade, slide, and scale. Theme preferences live in `app_settings`. |
| Workspace | `WorkspaceScreen` / `WorkspaceThumbnail`, L384–444 | Existing `ElevatedCard`, 1dp card elevation, 14/16dp padding, 72dp thumbnails, 18dp thumbnail radii, text/emoji marks. Thumbnail currently renders background layers, not a complete city/text preview. |
| Settings | `SettingsScreen`, L447–475 | Four color-swatch theme rows, horizontally scrolling language chips, font/icon imports, and about text. Root content is a non-scrolling `Column`; theme swatch hex values are repeated separately. |
| Editor structure | `WidgetEditor`, L492–648 | `LayoutCanvas` is outside the weighted `verticalScroll` column. Selected-element controls and city chips are inside it. Preserve this separation. |
| Settings ownership gap | `WidgetEditor`, L616–624; `ThemePicker`, L1945 | The editor's theme and 24-hour controls mutate `WidgetConfig.theme` and `WidgetConfig.use24Hour`, not the app's visual theme preference. Moving them requires an explicit settings/persistence design, not simply reparenting UI. |
| Shared controls | `EditorActionRow`, `EditorActionButton`, `EditableDisplayRow`, `SectionTitle`, `SliderRow`, `SwitchRow`, `ChoiceButton` | Reusable functions exist, but are private inside the activity. Icon buttons use 20dp artwork / 1.6dp stroke / 40dp layout boxes; destructive tint is hard-coded `#9B4D64`; disabled icon alpha is `.35`. |
| Libraries and dialogs | `FontField`, `CityPartDialog`, `ShapePickerDialog`, sun/moon library fields, city dialogs, `DayNightPaletteField`, `VisualColorPickerDialog` | Repeated elevated selection cards, `primaryContainer` selection, separate cell sizes, 9–20sp inline preview/label sizes, default `AlertDialog` shells. Some choices apply immediately; the color picker has a draft and Apply/Cancel. |
| Canvas decoration | `LayoutCanvas`, L1048 onward | Fixed `#E9E7EC` background, `#716A79` grid, several lavender guide/selection colors and alpha values, multiple 5/7/8dp selection radii. Canvas chrome does not fully follow the app theme. |
| Handles | `ResizeHandle`, `RotationHandleUnified`, legacy `RotationHandle`, L1509–1626 | Separate white/lavender paints and 1.25dp strokes. Unified rotation artwork is 17dp in a 42dp target. Paint may eventually use tokens; pointer-related code remains outside this phase. |
| Native window theme | [themes.xml](../app/src/main/res/values/themes.xml) | A separate platform light theme and `#6750A4` accent; system bar colors/icon brightness are also set in `BestMatesTheme`. Future window integration must follow resolved brightness. |
| Authored widget appearance | [Models.kt](../app/src/main/java/com/bestmates/worldclock/model/Models.kt), [ConfigStore.kt](../app/src/main/java/com/bestmates/worldclock/data/ConfigStore.kt), [widget_styles.xml](../app/src/main/res/values/widget_styles.xml), renderer/provider files | Widget colors, geometry, font sizes, shadow parameters, `theme`, and `use24Hour` are content/rendering contracts. Keep them separate from app UI tokens. |

An occurrence audit of the 1,975-line `MainActivity.kt` found **57 `Color(0x…)` literals, 131 numeric `.dp` literals, 12 numeric `.sp` literals, 20 `RoundedCornerShape(...)` calls, and 9 `tween(...)` calls**. These are search counts, not unique tokens or a list of safe replacements. They include preview/content values; classify each use before extracting it. No dedicated `ui/theme` or shared `ui/components` package exists in the inspected production source.

## 3. Token architecture

Use three layers with a single resolved theme at the application root:

1. **Reference values:** named palette swatches, spacing, radii, type metrics, motion curves, icon metrics. Raw hex values and dimension literals live here.
2. **Semantic tokens:** purpose-based roles such as `surfaceRaised`, `textSecondary`, `selectionOutline`, `canvasMat`, and `destructive`. Resolve these from visual theme identity plus independent Light/Dark/System mode.
3. **Component recipes:** buttons, inspector sections, gallery cards, sliders, overlays, and selection decoration consume semantic roles and scales. They do not contain their own palettes or arbitrary opacity values.

Proposed access contract, descriptive rather than implementation code:

```text
AppSettings → Theme identity + App theme mode + device brightness
            → BestMatesTheme
                → MaterialTheme.colorScheme / typography / shapes
                → BestMatesDesign.colors / spacing / radii / elevation
                → BestMatesDesign.motion / icons / states
                    → Shared components → Screens

WidgetConfig + approved display-preference resolver → preview / native widget
    (separate content boundary; never serialized from app chrome tokens)
```

Rules:

- Use immutable typed token groups and read-only composition locals. Components receive state and callbacks; tokens never read stores, trigger navigation, or mutate `WidgetConfig`.
- Use `Dp` for layout/strokes, `Sp` for interface text, milliseconds for durations, and explicit unitless alpha values. Convert dp to pixels at drawing boundaries only. Existing model units and gesture math remain unchanged.
- Prefer semantic aliases at call sites: `inspectorSurface = surfaceRaised`, `pickerSurface = surfaceOverlay`, `selectionOutline = accent`, `disabledContent = textPrimary at state.disabledContentAlpha`.
- A theme identity changes its approved neutral environment, accent family, and atmosphere values. It does not change layout, touch targets, typography sizes, motion speed, or content defaults. Identity and brightness remain independent axes.
- Explicitly map every color/typography role used by the bundled Material 3 version. Do not depend on incidental stock purple values. Do not upgrade Compose for this extraction.
- Keep existing `AppThemeStyle` stored identifiers readable. Do not reuse the persisted widget `ThemeMode` enum as the app's settings model merely because the labels look similar.
- No Android dynamic wallpaper colors in the initial foundation: they would make the reviewed palette non-deterministic. Device brightness is still followed in System mode.

### Material 3 bridge

| Material role | Semantic source |
| --- | --- |
| `primary`, `onPrimary`, `primaryContainer`, `onPrimaryContainer` | `accent`, `onAccent`, `accentContainer`, `onAccentContainer` |
| `secondary` and its on/container roles | `secondary` family |
| `tertiary` and its on/container roles | `tertiary` family |
| `background`, `onBackground` | `background`, `textPrimary` |
| `surface`, `onSurface`, `onSurfaceVariant` | `surfaceBase`, `textPrimary`, `textSecondary` |
| `surfaceContainerLowest`, `surfaceContainerLow`, `surfaceContainer` | `surfaceRaised`, `surfaceBase`, `surfaceInset` in light; `surfaceInset`, `surfaceBase`, `surfaceRaised` in dark |
| `surfaceContainerHigh`, `surfaceContainerHighest`, legacy `surfaceVariant` | `surfaceOverlay`, `surfaceElevated`, `surfaceInset` |
| `surfaceBright`, `surfaceDim` | `surfaceRaised`, `surfaceInset` in light; `surfaceElevated`, `surfaceInset` in dark |
| `outline`, `outlineVariant`, `surfaceTint` | `borderControl`, `borderSubtle`, `accent` |
| Error roles | `destructive` family |
| `inverseSurface`, `inverseOnSurface`, `inversePrimary` | Opposite-brightness `surfaceRaised`, `textPrimary`, `accent` |
| `scrim` | Black; opacity supplied once by overlay recipe |

Custom recipes name their surface explicitly instead of inferring it from elevation. `surfaceTint` is available for Material compatibility, but approved surfaces use zero automatic tonal elevation to avoid adding an unreviewed tint over the resolved color.

## 4. Colors and surface hierarchy

All hex values below are opaque sRGB (`#RRGGBB`) unless an alpha is explicitly stated. Kotlin may encode these as `0xFFRRGGBB`. These are the implemented Phase 2C foundation targets; production screens have not adopted them.

### 4.1 Default Cloud palette: Powder Blush + Milk Pearl

| Semantic token | Light | Dark | Use |
| --- | --- | --- | --- |
| `background` | `#FFF8FB` | `#19131D` | App backdrop, exposed workspace |
| `surfaceBase` | `#FFFDFE` | `#211925` | Navigation and resting page groups |
| `surfaceRaised` | `#FFFFFF` | `#2C2131` | Project cards, inspector sections |
| `surfaceInset` | `#F9F0F6` | `#151018` | Input wells, quiet grouped controls |
| `surfaceOverlay` | `#FFFBFD` | `#35283B` | Pickers, sheets, dialogs, menus |
| `surfaceElevated` | `#F8EDF5` | `#403048` | Small floating controls within an overlay |
| `textPrimary` | `#322A35` | `#F8EDF5` | Main labels, titles, content |
| `textSecondary` | `#6C5E6C` | `#D4C1CF` | Supporting labels, neutral action icons |
| `textMuted` | `#776879` | `#B09DAD` | Active metadata and hints; always opaque |
| `borderSubtle` | `#EBDCE6` | `#4C394F` | Decorative separators and card edges |
| `borderControl` | `#957F91` | `#937997` | Required input/control boundaries |
| `accent` | `#8F60AD` | `#DABCEA` | Primary action, selected outline, focus |
| `onAccent` | `#FFFFFF` | `#3D2348` | Text/icon on a filled primary control |
| `accentContainer` | `#F1DDF6` | `#4D3157` | Selected card/chip and tonal button fill |
| `onAccentContainer` | `#533461` | `#F7DFFF` | Content on selected/tonal fills |
| `secondary` / `onSecondary` | `#B97891` / `#302027` | `#E1ACC0` / `#4C2736` | Supporting rose accent, sparingly |
| `secondaryContainer` / `onSecondaryContainer` | `#F8E3EC` / `#593342` | `#553140` / `#FFDDEA` | Soft rose support |
| `tertiary` / `onTertiary` | `#C98A72` / `#3E2922` | `#E7B29D` / `#4C2A1F` | Warm peach accent |
| `tertiaryContainer` / `onTertiaryContainer` | `#F9E5DC` / `#623C2E` | `#5B372B` / `#FFDED0` | Peach support |
| `destructive` / `onDestructive` | `#A84762` / `#FFFFFF` | `#F1A2B5` / `#541729` | Delete and error actions |
| `destructiveContainer` / `onDestructiveContainer` | `#FBE5EB` / `#6C263A` | `#552532` / `#FFDCE5` | Error message/quiet destructive state |
| `success` / `onSuccess` | `#5F7D6C` / `#FFFFFF` | `#A8CEB5` / `#1C3928` | Confirmed saved/completed status |
| `successContainer` / `onSuccessContainer` | `#E5F0E8` / `#304C3B` | `#294335` / `#D8F0DF` | Supporting success message |
| `warning` / `onWarning` | `#9A6D31` / `#FFFFFF` | `#E8C58F` / `#412E12` | Attention that is not destructive |
| `warningContainer` / `onWarningContainer` | `#F8EBD2` / `#5D411D` | `#4C371D` / `#FFE6BC` | Warning message |
| `shadowColor` | `#553D57` | `#08050A` | Neutral depth only |
| `canvasMat` | `#F2EEF4` | `#29222D` | Neutral editing stage behind authored content |
| `onCanvasMat` | `#554B59` | `#E5D9E4` | Canvas chrome/empty-state text |
| `checkerLight` / `checkerDark` | `#F5F0F4` / `#E2D9E1` | `#352A39` / `#211A25` | Transparency preview, not actual widget content |

Surface order is conceptual depth, not a requirement to make every successive light-mode layer darker. Raised light surfaces are pearl white; dark raised surfaces become lighter. Surface boundaries must still work with shadows disabled.

### 4.2 Theme identities and brightness

Retain **Cloud, Dream, Midnight, Mono** as gallery names and stable identities. Cloud is the default expression of Powder Blush + Milk Pearl. Each identity must support **System / Light / Dark** independently; Midnight is a twilight palette identity, not a forced mode.

Use the complete neutral environments in §0.1 and accent/atmosphere values in §0.2. Supporting status families remain shared by brightness. This keeps all eight variants deterministic while allowing Dream, Midnight, and Mono to feel materially distinct from Cloud.

| Theme / brightness | `accent` | `onAccent` | `accentContainer` | `onAccentContainer` |
| --- | --- | --- | --- | --- |
| Cloud / Light | `#8F60AD` | `#FFFFFF` | `#F1DDF6` | `#533461` |
| Cloud / Dark | `#DABCEA` | `#3D2348` | `#4D3157` | `#F7DFFF` |
| Dream / Light | `#A15D83` | `#FFFFFF` | `#F6DDE9` | `#62344C` |
| Dream / Dark | `#ECB2CF` | `#4C2643` | `#5C3048` | `#FFDDEA` |
| Midnight / Light | `#745B91` | `#FFFFFF` | `#E8DDF2` | `#49385E` |
| Midnight / Dark | `#D3B8E8` | `#352242` | `#493357` | `#F2DBFF` |
| Mono / Light | `#786871` | `#FFFFFF` | `#EEE4E8` | `#494047` |
| Mono / Dark | `#CDBFC7` | `#342C31` | `#443B41` | `#F1E8ED` |

| Theme | `atmosphereA` / `atmosphereB` | Character |
| --- | --- | --- |
| Cloud | blush + lavender | Milk pearl, blush mist, and lavender veil |
| Dream | rose + muted lilac | Pinker and more romantic without candy saturation |
| Midnight | violet + muted rose | Deep plum-black studio atmosphere |
| Mono | dusty mauve + blush neutral | Ivory/graphite editorial restraint |

Atmosphere recipe: at most two static radial gradients, A near the upper end edge and B near the lower start edge, each fading to transparent over roughly 60% of the page's shorter dimension. Peak alpha is **0.28 light / 0.10 dark**, composited once over `background`. Keep gradients behind opaque content groups and away from reading/control surfaces. No animated particles, glass blur, noise, or gradients inside the canvas. The palette remains usable with both gradients absent.

### 4.3 Contrast contract

- Product requirement: all active interface text, including small metadata, targets at least **4.5:1** against its actual composited background. Meaningful control boundaries, focus/selection outlines, and action icons target **3:1**. Decorative separators and atmosphere are not the only evidence of a state. These thresholds use [W3C text contrast](https://www.w3.org/WAI/WCAG22/Understanding/contrast-minimum.html) and [non-text contrast](https://www.w3.org/WAI/WCAG22/Understanding/non-text-contrast.html) as measurable design criteria.
- Never reuse `borderSubtle` as the sole boundary of an otherwise invisible input. Use `borderControl`, or a contrasting filled control with its own clear outline/state mark.
- Do not lower opacity on active helper text to make it quiet. Use `textMuted`. Check overlaid pressed/selected states separately.
- Authored widget content may contain any user-selected color. A dual-tone canvas selection outline provides visibility over arbitrary art; app accessibility checks do not justify recoloring saved content.
- Numerical palette checks are recorded in §15. They are not a claim that the current app passes accessibility or that all rendered states have been verified.

## 5. Typography

UI typography is separate from the user's widget font library. Use Android system sans for UI body/labels, with a rounded system sans for brand/display/headline roles where available. Fall back to system sans without changing hierarchy; use platform fallback for Chinese, Arabic, and other scripts. Do not bundle or rename fonts in this phase.

All size/line-height pairs are in sp; weight is the nominal numeric weight; letter spacing is 0sp for every role. Avoid all-caps navigation and synthetic decorative text effects.

| Material role / intended use | Size / line height | Weight |
| --- | --- | --- |
| `displayLarge` / rare brand presentation | 36 / 44 | 600 |
| `displayMedium` / brand presentation | 32 / 40 | 600 |
| `displaySmall` / workspace introduction | 28 / 36 | 600 |
| `headlineLarge` / major page heading | 26 / 34 | 600 |
| `headlineMedium` / page heading | 24 / 32 | 600 |
| `headlineSmall` / compact page heading | 21 / 28 | 600 |
| `titleLarge` / app bar, dialog title | 20 / 26 | 600 |
| `titleMedium` / project/section title | 16 / 22 | 600 |
| `titleSmall` / property group | 14 / 20 | 600 |
| `bodyLarge` / setting row, input | 16 / 24 | 400 |
| `bodyMedium` / inspector label, description | 14 / 21 | 400 |
| `bodySmall` / helper text | 12 / 18 | 400 |
| `labelLarge` / button, tab, chip | 14 / 18 | 500 |
| `labelMedium` / metadata, value | 12 / 16 | 500 |
| `labelSmall` / secondary badge | 12 / 16 | 500 |
| `valueTime` / Settings format example | 24 / 30 | 500 |

No functional UI label below 12sp. Font-library specimens are content previews and may use the selected font; their names and selection indicators use UI typography. Use tabular digits for changing numeric values where supported, without imposing a monospace font on city/text artwork.

Use minimum heights, not fixed text containers. Allow multiline labels and growing rows at large font scale; do not compress glyphs or remove font padding globally. Ellipsize only nonessential summaries with an accessible full label. Do not modify multiline widget rendering metrics or resume TASK-010 as a typography cleanup.

## 6. Spacing, radii, borders, and depth

### 6.1 Spacing and layout scale

| Token | dp | Typical use |
| --- | --- | --- |
| `space0` | 0 | Flush content |
| `space1` | 2 | Optical micro-adjustment, never a touch gap |
| `space2` | 4 | Label/value micro-gap |
| `space3` | 8 | Related controls, icon/label gap, minimum gap between distinct touch boxes |
| `space4` | 12 | Related rows, compact card grid |
| `space5` | 16 | Inspector/card padding, compact screen gutter |
| `space6` | 20 | Standard screen gutter and card padding |
| `space7` | 24 | Section separation, dialog padding |
| `space8` | 32 | Major groups |
| `space9` | 40 | Spacious workspace sections |
| `space10` | 48 | Empty-state separation; also a separate minimum-touch token |
| `space11` | 64 | Large composition spacing |

Compact screens use 16dp side gutters; wider screens use 24dp. Existing canvas margins remain unchanged until a separately reviewed layout task. Use 16dp inspector internal padding, 12dp row spacing, and 24dp between major groups as target recipes. A single-line Settings row is at least 56dp tall; supporting text increases it naturally. Inputs are at least 56dp tall.

### 6.2 Corner radius scale

| Token | dp | Use |
| --- | --- | --- |
| `radiusNone` | 0 | Drawing primitives |
| `radiusXs` | 6 | Tiny status/selection detail |
| `radiusSm` | 10 | Canvas bounding decoration, small preview well |
| `radiusMd` | 14 | Small controls and compact menus |
| `radiusLg` | 18 | Text fields and compact controls |
| `radiusXl` | 20 | Project cards, inspector sections |
| `radius2xl` | 24 | Canvas frame target, large gallery card, dialog |
| `radius3xl` | 28 | Modal sheet top corners |
| `radiusFull` | 50% of shortest side | Circular icon button/swatch; capsule only for compact badges |

Material shape bridge: extraSmall → 6, small → 10, medium → 14, large → 24, extraLarge → 28dp; component recipes explicitly select 18/20/24dp where required. Clip content to the inner container shape, never a user's authored widget to a newly invented decorative outline. The canvas frame's proposed radius is a future paint/layout review, not a Phase 2C change to its existing 18dp clip.

### 6.3 Border recipes

- `borderDecorative = 1dp borderSubtle`: card edges, separators, preview wells.
- `borderInteractive = 1dp borderControl`: resting outlined buttons and inputs.
- `borderSelected = 1.5dp selectionOutline`: selected card/chip, preserving its total measured size.
- `borderFocus = 1.5dp focusRing` with 2dp clear offset: keyboard/accessibility focus; reserve space to avoid clipping.
- `borderError = 1dp destructive`: invalid field plus explanatory text/icon.
- Draw borders as overlays or reserved strokes so switching state never shifts content. Separate adjacent groups with space before adding more lines. Do not put a border and shadow around every nested row.

### 6.4 Elevation, shadow, and glow

These are visual reference recipes. Blur/spread values describe the target appearance; they are not a claim that an arbitrary shadow API is available in the current Compose BOM. Prefer the existing supported elevation API and compare visually before adding a custom renderer.

| Token | Elevation fallback | Reference offset Y / blur / spread (dp) | Shadow alpha Light / Dark | Use |
| --- | --- | --- | --- | --- |
| `depth0` | 0dp | 0 / 0 / 0 | 0 / 0 | Page, inset controls, inspector rows |
| `depth1` | 2dp | 2 / 10 / 0 | .08 / .18 | Resting project card, canvas host |
| `depth2` | 4dp | 4 / 18 / 0 | .10 / .22 | Floating toolbar, popup menu |
| `depth3` | 8dp | 10 / 30 / 0 | .12 / .20 | Modal sheet or dialog |

All offsets X are 0. Use one shadow per floating group; nested children remain `depth0`. Dark mode relies primarily on surface value and edge contrast, not stronger colored shadows. Do not blur the app background under overlays.

Soft Halo is decoration, not depth: outside an eligible selected card/control, use `accent` at **.20 light / .24 dark**, 12dp soft falloff, 0 offset and no more than 4dp spread. Pair it with the solid selected outline and bead/check state indicator. Omit the glow when clipped, costly, or visually busy; retain the outline. Keyboard focus uses the crisp ring. No pulsing, neon, multi-shadow stacks, or permanent glowing buttons. UI glow tokens must never replace `WidgetLayer.softness`, `glowOpacity`, or shadow fields.

## 7. State system: Soft Halo

Semantic aliases: `selectionOutline = accent`, `selectionFill = accentContainer`, `selectionContent = onAccentContainer`, `focusRing = accent`, `guide = accent`. Use the current resolved theme, not hard-coded lavender.

| State | Visual recipe | Meaning and behavior |
| --- | --- | --- |
| Resting | Component's normal surface/content | No selection glow |
| Hover | Current foreground at `.06` over current fill | Pointer feedback only; no geometry change |
| Pressed | Current foreground at `.08` over current fill | Bounded state layer; instant input response |
| Selected | `selectionFill` + 1.5dp `selectionOutline` + `selectionContent` | Exactly one semantic 6dp dot or 16/18dp check plus selected semantics; optional halo on the outer card |
| Focused | Separate 1.5dp focus ring at 2dp offset | Can coexist with selection; does not select or activate by itself |
| Disabled | `textPrimary` at `.38`; filled control base `textPrimary` at `.08` over its parent | No click, ripple, hover or halo; expose disabled semantics and an accessible reason when useful |
| Destructive resting | `destructive` icon/label on normal surface | Reserved for delete/cut/data-loss actions, never for ordinary deselection |
| Destructive filled | `destructive` fill + `onDestructive` | Confirmation action after explicit consequence text |
| Error | `destructive` outline + message + icon | Keep the entered value and explain how to recover |
| Loading | Keep measured control size; labeled progress indicator | Prevent duplicate submission; do not imply success |
| Locked element | Selected decoration plus lock indicator | Selectable/readable; disable only unavailable mutations, not the entire inspector |

State precedence: disabled removes all interactive feedback; otherwise error/destructive supplies the semantic color, selected supplies persistent state, and pressed/hover supplies one temporary layer. Do not stack hover and press alpha. Focus remains separately visible. For a selected destructive item, use destructive outline/container and a check; avoid competing purple and red glows.

Filled-button state layers use `onAccent` (or `onDestructive`); neutral and selected surfaces use their corresponding content color. Apply alpha once at the overlay level. Press feedback must not reduce the final text contrast below §4.3.

Disabled is not a substitute for unavailable data, a loading state, or an empty library. Show an explanation for a reached item limit. Auto-save being enabled is a toggle state, not evidence that the latest changes have been saved; only confirmed save results use success feedback. Do not promise an autosave timing change.

### Canvas selection exception

Do not tint, dim, or fill the selected artwork. Draw an overlay around its existing computed bounds: inner 1dp accent line plus outer 1dp contrasting keyline. Use a light keyline around a dark/light-mode accent and a dark keyline around a light/dark-mode accent, so at least one edge remains visible over arbitrary art. Preserve the existing object's rotation and bounding computation. Do not animate the bounds between objects.

Draw handles on small opaque `surfaceOverlay` discs with `accent` glyphs and a `borderControl` edge when required for contrast. The target visual family uses 16–20dp artwork; the touch-area target is 48dp. **Handle placement, measured size, clipping, and touch-area changes require a separately scoped interaction/accessibility review; they are not token-only changes.** The current 42dp rotation target and 28dp resize artwork are documented gaps, not authorization to alter TASK-007 code.

Canvas grid is decorative: `onCanvasMat` at `.08`, 1 physical pixel. Active guides use opaque `guide`, 1dp plus a contrasting keyline when over artwork; guide labels use `onAccent` on opaque `accent` at `labelMedium`. Do not change guide detection, snap increments, haptics, or pointer math. A geometry-free color substitution is the only possible later foundation integration here.

## 8. Icon system: Orbit Charm

Orbit Charm is the reusable Best Mates 2.0 outline family. Phase 2C establishes it without replacing production call sites.

| Token/rule | Definition |
| --- | --- |
| `iconViewport` | 24 × 24 design units |
| `iconSizeDefault` | 20dp rendered artwork, centered in a 48 × 48dp target |
| `iconSizeProminent` | 24dp for navigation/standalone actions |
| `iconSizeInline` | 16dp for non-interactive metadata; never a standalone touch box |
| `iconStrokeDefault` | 1.8 design units in the 24-unit viewport: about 1.7dp at 20dp rendering |
| `iconStrokeInline` | Optically adjust to at least 1.4dp for 16dp artwork; do not shrink every detail proportionally |
| Caps and joins | Round; no sharp miter spikes |
| Live area | Normally 3–21 on each viewport axis, retaining 3-unit outside breathing space |
| Detail clearance | At least 2 design units between unrelated strokes; simplify rather than adding tiny interior marks |
| Optical compensation | Curves/diagonals may overshoot the normal live area by up to .5 design units; manually center visual mass within ±.5 unit |
| Tint | `textSecondary` resting, semantic selected/destructive/disabled colors for states |

Use clear silhouettes at actual 20dp size: copy uses two sheets; paste has a clipboard; cut uses scissors; pin/lock states remain distinct. Normal utility icons and the semantic Confirm check carry no appended dot, bead, petal, or sparkle. Reserve restrained ornament for Theme, Sun, and Moon; do not mistake functional geometry such as location centres, scissors loops, or settings hubs for decoration. Rotate uses a partial orbit with a directional terminal instead of a generic circular arrow; Resize uses fine diagonal expansion geometry. Avoid emoji, Unicode arrows, heavy fills, multiple decorations on one command, and per-icon background bubbles in a grouped toolbar.

Single-icon buttons need localized action descriptions and a tooltip/accessible label; toggle controls announce their state. Decorative icons beside a fully labeled control do not repeat the label to TalkBack. Mirror directional navigation in RTL; do not blindly mirror text glyphs, clocks, or artwork. Review undo/redo direction consistently with navigation conventions.

Orbit Charm is the app command family. Sun/moon assets chosen as widget content remain in their current libraries and preserve their asset IDs and notices. The Mates Orbit brand mark is a separate identity asset, not an extra action icon.

## 9. Motion system: Quiet Motion

| Duration token | ms | Use |
| --- | --- | --- |
| `motionInstant` | 0 | Direct manipulation, focus acquisition, reduced-motion geometry |
| `motionPress` | 80 | Press-state fade in |
| `motionRelease` | 120 | Press release, small state-color change |
| `motionState` | 160 | Selection decoration or switch/chip state |
| `motionExit` | 180 | Modal/content exit |
| `motionEnter` | 220 | Small surface or inspector content entrance |
| `motionNavigate` | 260 | Forward/back destination transition |
| `motionTheme` | 300 | App chrome palette crossfade |

| Easing token | Cubic Bézier control points | Use |
| --- | --- | --- |
| `easeStandard` | `(0.2, 0, 0, 1)` | State and destination changes |
| `easeEnter` | `(0, 0, 0.2, 1)` | Surfaces arriving |
| `easeExit` | `(0.4, 0, 1, 1)` | Surfaces leaving |
| `easeLinear` | `(0, 0, 1, 1)` | Alpha-only fades and palette interpolation |

Recipes:

- Buttons and crystal controls: use the frozen Phase 2C.3 surface recipe above, 80ms in / 120ms out, with 160ms for state transitions; no shrinking hit target, overshoot, bounce, spring, or delayed callbacks.
- Selection: show the solid outline/check immediately; fade optional halo over 160ms. Never spring or interpolate selected-object geometry.
- Navigation: fade plus at most 16dp horizontal travel, 260ms `easeStandard`; back reverses direction, RTL mirrors directional movement. Do not combine routine page scale, slide, and multiple mismatched fade durations.
- Sheet: platform sheet translation with 220ms enter / 180ms exit target behavior; dialog: fade plus at most 8dp vertical travel. Do not animate dialog text independently. Focus follows the visible surface.
- Inspector: stable container and scroll ownership. A new selection can replace its property content with a 120ms fade; avoid animated height changes that push the canvas or move a control under the finger. Honor the current selection immediately.
- Theme: 300ms alpha/color transition for app chrome only. Do not interpolate widget content colors or recreate editor state when theme changes.
- Sliders and canvas gestures: value, thumb, and authored content follow input immediately; no tween on drag/rotate/resize or history recording. Existing snap feedback remains outside the motion migration.
- No autoplay orbiting marks, breathing glows, long staggered entrances, or endless decorative animation. Loading motion exists only while work is in progress and has a static textual status.

Respect system animation duration settings, including disabled animations. A future **Reduce motion** setting can only further reduce movement: remove translation/scale/glow animation, and use an optional fade of at most 100ms; if system animations are disabled, all transitions are immediate. Neither setting changes input response or state semantics. Implement this once in the motion resolver; do not add ad hoc delayed coroutines to controls.

## 10. Component hierarchy and behavior contracts

### 10.1 Buttons and action bars

| Hierarchy | Recipe | Typical use |
| --- | --- | --- |
| Primary filled | `accent` / `onAccent`, 54dp height, radius20, 3dp soft depth, no hard border | Create widget; Save; Apply in a draft picker |
| Secondary tonal | `secondaryContainer` / `onSecondaryContainer`, radius18, 2dp porcelain depth | Important supporting action |
| Secondary outlined | `surfaceRaised`, `borderControl`, `textPrimary`, radius18, 1dp nearly-flat depth | Add to Home; import; change city |
| Tertiary text | No persistent fill/border, `accent` | Cancel, Close, supporting navigation |
| Icon action | 20dp art, 48dp target, bounded state layer, 1.5dp shallow base | Undo/redo and frequent toolbar commands |
| Destructive | Quiet `destructive` text/icon; filled only for confirmation | Delete project, irreversible removal |

At most one filled primary action per local decision area. Buttons have minimum 48dp height, 20dp horizontal padding and 8dp icon/text gap; let labels wrap or stack adjacent actions at large text sizes. Preserve current actions and enablement during token extraction. Toolbars that cannot fit real 48dp targets need a later layout review (wrapping or labeled overflow), not squeezed targets or overlapping invisible hit regions.

Workspace project deletion should eventually show a named/count-aware confirmation because no workspace undo is currently present. Editor element deletion may remain a direct action where the existing undo path is valid; show clear undo feedback in later UX work. Do not add confirmation/state/history changes as part of a paint-only conversion.

### 10.2 Sliders, toggles, chips, and fields

- **Slider:** label and compact crystal value tag above the control; 5dp inactive rounded track in `borderSubtle`, a secondary-to-accent active track, and an 18dp Pearl Glass thumb with an accent rim and local highlight/refraction; minimum 48dp interaction height. Use existing ranges/steps and callbacks. Continuous values remain continuous; ticks only where values have a meaningful discrete interpretation. Provide accessible range/value semantics and a later numeric entry alternative for precise values. Do not alter normalized model units.
- **Toggle:** use the Best Mates accessory recipe: 44 × 26dp Pearl Glass visual track inside at least a 48dp target, pearl-white 17dp resting thumb, 1.25dp pressed expansion, fine semantic edge, `accentContainer` lavender-resin active track, and neutral milk-glass off track. Text says what is enabled. A simple setting makes the row one semantic toggle; a row with a separate Edit action must expose two distinct, non-overlapping controls.
- **Chip:** full radius, 12dp horizontal padding, 8dp icon gap; visual body is 34dp with a real 48dp interactive box. Selected = selected fill/1.5dp outline plus one 6dp dot when no check is used; unselected = `surfaceRaised` + `borderSubtle`. Single-choice sets announce radio-like selection; multi-select sets announce independent choices. Wrap chips instead of truncating localized labels or relying on hidden horizontal choices.
- **Text field:** `surfaceInset`, radius18, resting 1dp hairline, focused 1.5dp `accent`, error 1dp `destructive`; persistent label, 16sp input, helper/error below. At least 56dp height; grows for multiline input. Hex input is an optional precise path in a color picker, not the primary color-selection interface.
- **Color swatch:** 36dp visual sample inside a 48dp target, 16dp check in a contrasting badge outside the authored-color inset, neutral checkerboard for alpha, label/alpha announced. App theme must not tint the sampled color, and the check badge carries no dot.
- **Status chip:** non-interactive if informational; do not style it like a clickable choice. Save state includes text/icon, not a color-only dot.

### 10.3 Pickers, sheets, dialogs, and feedback

| Layer | Surface/shape/depth | Purpose and dismissal |
| --- | --- | --- |
| Inline field / preview | `surfaceInset`, radius18, depth0 | Current value and direct control |
| Anchored menu | `surfaceOverlay`, radius14, depth2 | Short action list; dismiss outside/Back; restore trigger focus |
| Library picker sheet | `surfaceOverlay`, radius28 top corners, depth3 | Shapes/fonts/sun/moon/cities/palettes; searchable or scrollable choices |
| Full-height picker on constrained screens | Same surface, safe-area layout | Long results, keyboard, or large-font content; title and close remain reachable |
| Dialog | `surfaceOverlay`, radius28, depth3, 24dp padding | Rename, concise confirmation, short focused decision |
| Snackbar/status | `inverseSurface` / `inverseOnSurface`, radius14 | Result plus optional action; accessible announcement, no canvas obstruction |

Modal scrim: black at `.32` in light and `.52` in dark, applied once. Only one modal is active at a time. A subpicker replaces the parent modal content or navigates within its shell; Back returns to the previous picker level before closing. Keep an explicit Close/Cancel control, contain modal focus, and return focus to the opening field.

Single-choice libraries may commit on selection and close; document this in their component contract and show the current item with a check. Multi-step editing such as color/opacity uses a local draft, Original/New samples, **Apply** and **Cancel**. Back/outside dismissal cancels the draft. Never mutate persisted values while presenting a Cancel action that cannot roll them back. Current `CityPartDialog` contains immediate edits; converting it to draft semantics is later behavioral work, not an extraction detail.

Library cells use `surfaceRaised`, selected outline/check, 12dp spacing and at least 96dp preferred width for labeled previews; reduce columns before clipping names. Long city results are readable list rows, not narrow gallery tiles. Theme preview cards use the selected state's outer frame so their miniature example is not recolored. Empty, loading, no-result, and failure states use the same shell and an explicit next action where available; do not claim import/search reliability improvements without implementation.

Future standard maximum widths: dialogs 560dp, library sheets 640dp, Settings content 720dp. On phones use available width minus safe gutters; all modal content can scroll with actions reachable above the keyboard. These are layout targets for later approval, not changes to current dialog bounds.

## 11. App-level composition

### 11.1 Workspace: Floating Creation Space

The workspace is a pearl/charcoal atmosphere with opaque project cards floating quietly above it. Keep a clear title, Settings entry, and primary Create widget action. Cards show the user's preview, project name, and concise real state. Avoid dashboard statistics, promotional banners, stock sample widgets, or an extra brand panel that crowds creation.

Use `surfaceRaised`, radius20, depth1, 16–20dp padding, and 12dp card gaps. Selected projects receive Soft Halo plus a check/count; selection actions remain separate from normal project navigation. Long-press selection needs an equivalent discoverable accessible action in later UX work. Empty workspace: Mates Orbit mark, concise guidance, and Create widget. Empty newly created projects remain empty.

Preview wells use neutral `canvasMat` and contain actual content colors. The existing thumbnail only renders background layers; a complete faithful thumbnail is a separate renderer-integration task. Do not invent city/time preview content to simulate a completed feature.

### 11.2 Editor: Fixed Canvas + Scrolling Soft Inspector

The following is the invariant, not a new screen implementation:

```text
Safe area / editor navigation and actions
Canvas tools (Snap, Guides)
┌──────────────────────────────────────┐
│ Fixed canvas / neutral creation mat │  Outside inspector scroll
│ Authored widget + selection overlay │
└──────────────────────────────────────┘
             12–16dp visual separation
┌──────────────────────────────────────┐
│ Selected element / local actions   │
│ Property groups                    │  Vertical scroll only here
│ Selected cities / related controls │
└──────────────────────────────────────┘
Save / Add to Home + bottom safe area
```

Canvas rules:

- Preserve the current top canvas and its current normalized board/renderer relationship. Surface tokens style its surrounding mat and decoration; user artwork stays unchanged.
- Neutral opaque mat in both brightness modes; optional checkerboard only when useful for transparency inspection, never a wallpaper photograph or atmosphere gradient.
- Grid is subordinate to artwork. Selection decoration and guides are overlays excluded from saved and launcher-rendered content.
- No permanent shadow/glow around every object. Only user-authored effects belong inside widget artwork.
- App theme changes update canvas chrome without modifying the widget's saved day/night choice or per-city sun/moon calculation.

Inspector rules:

- One independent vertical scroll owner below the canvas. Use `surfaceRaised` radius20 sections on the app background, 16dp inner padding; use spacing/subheadings instead of deeply nested elevated cards.
- Start with selected element identity/state and local actions, then properties in task order: content/shape → typography or palette → border/glow/depth. Selected city shortcuts remain in the scrolling area.
- No selection: a short prompt and relevant creation affordance, not a full disabled property form. Locked selection remains identifiable, with unavailable mutations explained.
- Preserve scroll position during ordinary property changes; make a newly selected element's section discoverable without animated jumps during a gesture.
- Global theme mode/time format are absent. Shape colors, per-layer day/night palettes, text fonts, city visibility, and local editing tools remain editor properties.

Constrained-height target for a later layout task: retain a top canvas region and give the inspector usable height (prefer at least 144dp where space allows). On keyboard display or landscape, a smaller canvas presentation may be designed only with a separate coordinate/measurement review; it must never enter the inspector scroll or become a side panel. The current square canvas can constrain short windows, and this phase makes no claim to solve that. Do not add viewport rescaling while TASK-007 is deferred.

### 11.3 Settings: Visual Theme Gallery

Settings is a vertically scrolling page with a stable title/Back action. Keep labels and current values visible; avoid horizontal-only settings navigation. The target information architecture is:

| Order / group | Rows and controls | Scope |
| --- | --- | --- |
| 1. Appearance | **Theme gallery**: Cloud, Dream, Midnight, Mono; **Theme mode**: System / Light / Dark | Global app chrome |
| 2. Time & widget display | **Time format**: 24-hour / 12-hour; **Widget appearance**: Follow device / Day palette / Night palette; **Saved widget preferences** | Global time/display defaults and explicitly retained saved choices; see §12 |
| 3. Language | Current language; native-language list in a single-choice picker | Existing global language preference |
| 4. Accessibility | **Reduce motion**, helper explaining system animation settings are respected | Future global motion preference; system text scaling is always respected |
| 5. Resources | Import font; import icon; actual last-imported file/status | Existing import actions; don't imply unverified library integration |
| 6. About | Version from app metadata, licenses/attributions, Mates Orbit identity | Preserve Apache-2.0, GeoNames and third-party icon notices |

Theme gallery recipe:

- Each card contains a miniature workspace card, button, text lines, and surface layers rendered with that theme's actual resolved tokens. It is a non-interactive miniature inside a single selectable card; TalkBack reads the theme name/description/state once.
- Show the current effective brightness in previews; a compact labeled Light/Dark preview selector may inspect the alternate variant without changing the stored mode. It is preview-only, clearly named.
- Use two columns when each card can be at least 144dp wide; otherwise one column. Text/row height can grow. Do not bake controls into a screenshot with illegible captions.
- Theme name and one-line character description are outside the miniature. Selection uses the surrounding page's accent outline, a check and “Selected”; do not obscure the miniature with a tint.
- Choosing a theme applies app chrome immediately, with the independent mode unchanged. Previewing alternate brightness is not an implicit preference write.
- A quiet Mates Orbit accent can identify the default Cloud card. No paid/locked badges or nonexistent themes.

Settings should be reachable from the workspace and, in a later navigation change, from a labeled editor menu entry. Returning from Settings restores the same project/selection/scroll state; opening Settings must not implicitly save, discard, or recreate an editing session. The widget-configuration activity launched by Android also needs a Settings route with correct Back/result handling before editor controls can be removed from that path.

### 11.4 Branding: Mates Orbit

Brand concept: two rounded companion forms connected visually by one open orbital curve. Use a balanced asymmetry and generous negative space; avoid a literal clock face full of ticks or a detailed planetary illustration. The small mark must work in one color using the current accent or text color. Full-color expression can combine lavender with warm pearl/rose, but is decorative only.

Use the wordmark “Best Mates” in the rounded heading family, normal case. Keep clear space at least one-quarter of the mark's width. Target 24dp minimum for a standalone in-app mark; below that use a simplified approved single-color version. Use identity in workspace empty states, Settings/About and future launch assets sparingly; do not insert it into blank user projects. New brand vectors, adaptive icon safe-area review, and launcher asset replacement are later deliverables, not part of Phase 1.

## 12. Theme/time ownership and compatibility contract

**Decision:** global app theme mode and time format are Settings-owned. Remove `ThemePicker(config.theme)` and the editor time-format card only in a later approved integration, after Settings can express their existing behavior and both editor entry paths are covered. No persistence change is made in this phase.

There are three distinct concepts:

| Concept | Proposed owner | Existing source / required meaning |
| --- | --- | --- |
| App visual identity and brightness | Settings → Appearance | `app_settings.app_theme` currently identifies CLOUD/DREAM/MIDNIGHT/MONO. New independent app mode resolves System/Light/Dark for chrome only. |
| 24-hour / 12-hour formatting | Settings → Time & widget display | `WidgetConfig.use24Hour` currently controls saved project/widget time rendering. Target global format controls app time examples and new/following widgets. |
| Widget day/night appearance | Settings → Time & widget display → Widget appearance | Existing `WidgetConfig.theme` controls widget palette mode. Follow device maps to SYSTEM, Day palette to LIGHT, Night palette to DARK; it does not override timezone-derived solar state. |

Proposed defaults and upgrade behavior:

1. Fresh installation: Cloud identity, app mode System, time format 24-hour, widget appearance Follow device. Theme identity and app mode are independent. Time examples use the current locale and show both `18:40` / `6:40 PM` as explicitly illustrative format samples, never as live time.
2. Existing `app_theme`: keep its stored identity. When independent app mode is first introduced, seed Light for CLOUD/DREAM/MONO and Dark for MIDNIGHT to preserve the previous brightness until the user changes it. Do not infer the app theme or app mode from an arbitrary project's `theme`.
3. Existing saved projects **and standalone widget instances** retain their stored `theme` and `use24Hour`. A 12-hour project must not silently become 24-hour on upgrade, theme selection, reopening, saving, or duplication. Copies retain the source preference policy. Do not delete legacy fields or migrations.
4. New widgets/projects follow the global time format and widget-appearance defaults. App appearance changes affect chrome only; changing the app theme to Midnight must not force all widgets into night palettes.
5. Settings → **Saved widget preferences** lists saved projects and any standalone configured widgets. Each can retain its saved time/appearance values or adopt “Use Settings defaults” for each preference. Show the concrete saved value, offer 24-hour/12-hour and Follow device/Day/Night choices here, and keep this management out of the editor. Linked widget instances inherit the corresponding project's policy rather than diverging silently.
6. When the global time format or widget appearance changes, update new/following content and refresh affected previews/linked home-screen widgets. Retained saved choices remain intact; explain “Applies to widgets using Settings defaults” with a link to Saved widget preferences. A future bulk adoption action must name the affected scope and be explicitly applied by the user.
7. Formatting preference changes never alter a city's zone ID, date locale, instant, coordinates, or day/night solar calculation. Persisted per-layer colors/fonts remain user content.

This contract intentionally requires a later settings integration/migration task. The current schema has only concrete `theme` and `use24Hour` values and cannot represent “follow Settings” independently. Before implementing inheritance, specify versioned preference metadata and resolution/fallback behavior, preserve concrete legacy fields for compatibility, and test projects, standalone instances, linked instances, reopen/copy, and upgrades with differing values. Use a single display-preference resolver for preview/provider consumers; do not fork time logic or introduce ad hoc global reads in drawing code. Phase 2 token scaffolding does not authorize this migration.

## 13. Accessibility and adaptive rules

- Allocate real **48 × 48dp minimum interactive regions**, with 8dp preferred separation for distinct neighboring actions. Keep the 20dp icon artwork visually quiet. Android's [Compose accessibility defaults](https://developer.android.com/develop/ui/compose/accessibility/api-defaults) recommend the 48dp minimum and warn about overlapping expanded touch areas. A `.size(40.dp)` declaration alone is not proof of an accessible measured target; inspect actual semantics and hit regions later.
- Do not call the current toolbar/handles compliant based on their visual sizes. Review 40dp toolbar boxes, 42dp rotation targets, dense action rows, and custom pointer controls separately. Handle changes must respect the interaction boundary in §1/§7.
- Support TalkBack names, roles, selected/checked/disabled states, slider ranges/units, and meaningful action labels. Decorative marks are excluded. Do not announce an entire refreshed canvas on each pointer event.
- Long press and precise dragging must eventually have accessible alternatives: select an item from a labeled list/inspector and operate named controls. This is future interaction work, not an implied TASK-007 fix.
- Use visible focus for keyboard/Switch Access; logical order is navigation → canvas/selection → inspector → save actions. Modal focus is contained and restored. Focused fields/actions remain visible above the keyboard and navigation bars.
- Respect system font/display scaling; review at 100%, 130%, and 200% font scale, compact 320dp width, normal phone width, landscape/short height, and a larger tablet window. Adapt row height and column counts before reducing type or target sizes.
- Use start/end padding, localized strings, native language names, and appropriate RTL navigation. Do not reverse clock numerals or mathematical values. Review English, Chinese, and Arabic layouts plus long translated labels.
- Apply the contrast contract to final composited colors, including selected/pressed states and all eight theme/brightness combinations. Pair color with text, checkmarks, outlines, or icons. Do not encode 12/24-hour, locked, selected, error, or saved states by color alone.
- Honor system motion controls and the proposed reduction setting. Haptic feedback is supplemental and never the only signal; changing existing snap haptics is outside this phase.
- Status and errors remain readable long enough for assistive use; important failures need persistent text/retry rather than a transient toast. Touch feedback never depends on a glow or animation being enabled.

## 14. Proposed later files and components

All Kotlin paths below are relative to `app/src/main/java/com/bestmates/worldclock/`. Phase 2A–2C already provide `ui/theme/BestMatesPalette.kt`, `BestMatesColors.kt`, `BestMatesTypography.kt`, `BestMatesDimensions.kt`, `BestMatesElevation.kt`, `BestMatesStates.kt`, `BestMatesMotion.kt`, `BestMatesTheme.kt`, `ui/icons/OrbitIconMetrics.kt`, `ui/icons/OrbitIcons.kt`, and `ui/preview/BestMatesFoundationPreview.kt`. The debug source set also contains the isolated Design Lab Activity and manifest entry.

The following files/components remain proposals for a separately approved production-integration phase. The list is not authorization to create them now.

| Proposed file | Responsibility | Eligible stage after review |
| --- | --- | --- |
| `ui/components/OrbitSurface.kt` | Named flat/card/overlay surface recipes | Shared primitive work |
| `ui/components/OrbitButton.kt` | Primary/tonal/outlined/text/destructive and icon button recipes | Shared primitive work |
| `ui/components/OrbitSelection.kt` | State-only outline/check/halo decorators with no hit-test or gesture ownership | Shared primitive work |
| `ui/components/OrbitControls.kt` | Labeled slider, switch row, choice chip, swatch and field recipes | Shared primitive work |
| `ui/components/OrbitOverlay.kt` | Common dialog/sheet/menu chrome and accessibility structure; caller owns draft/apply behavior | Shared primitive work |
| `ui/components/OrbitThemePreview.kt` | Token-driven miniature UI card for gallery and design review | Isolated component review |
| `ui/editor/InspectorSection.kt` | Surface/heading/spacing wrapper for existing inspector content | Later editor paint integration |
| `ui/editor/CanvasChrome.kt` | Mat/grid/outline paint tokens only; preserve existing geometry and callbacks | Later guarded integration |
| `ui/workspace/ProjectCard.kt` | Workspace card and selection presentation | Later workspace integration |
| `ui/settings/SettingsScreen.kt`, `ThemeGallery.kt` | Settings IA and gallery; extraction only when that screen is the approved scope | Later Settings integration |
| `data/AppSettingsStore.kt` | Existing app preferences plus independently modeled mode/time/motion settings | Later settings/persistence task |
| `model/DisplayPreferences.kt`, `data/DisplayPreferenceResolver.kt` | Explicit inheritance/legacy policy and one resolved display contract | Later migration design and implementation |
| `ui/brand/MatesOrbitMark.kt` | Original scalable brand artwork after visual approval | Later brand asset work |

Avoid an all-purpose constants file and a mechanical extraction of the entire activity. Existing widget renderers must not depend on Compose composition locals. Do not add a design-system module, network dependency, font pack, icon dependency, or new navigation framework merely to host these tokens. The current single app module is sufficient.

Phase 2C.3 physical-device inspection is complete and the interaction foundation is visually frozen unless a serious regression is found. Phase 2D separately owns the full Orbit Charm redesign, Shape Atelier, Celestial Atelier, Type Wardrobe, and asset-picker visual language. Shared production components and screen-by-screen adoption begin only after a separately approved scope. Settings relocation, inheritance migration, canvas target resizing, and whole-screen redesigns need explicit later scopes. TASK-007 remains deferred through all of these proposals.

## 15. Verification and review gate

### Phase 2C.3 verification

- Source, history, task state, the Phase 2A token kernel, Phase 2B Lab, and the debug harness were re-audited before refinement. Phase 2C.3 changes stay inside the documented foundation and debug-preview boundary.
- Static sRGB contrast uses linearized channel luminance (`0.2126R + 0.7152G + 0.0722B`) and `(lighter + 0.05) / (darker + 0.05)`. The check covers 228 opaque pairs: three text roles and `borderControl` over all six resolved surfaces, primary and selected-container labels, and all light/dark supporting semantic pairs.
- Every tested active text pair is at least 4.5:1, every tested control edge is at least 3:1, and every filled semantic foreground/background pair is at least 4.5:1. The Cloud Light functional accent was deepened from the visual anchor specifically to clear the primary-button threshold.
- The Design Lab resolves all eight identity × brightness combinations locally, shows the complete Orbit Charm family, and keeps the future canvas study separate from production `LayoutCanvas`.
- The debug Activity alone controls its status/navigation bar colors and icon brightness. On the attached device the Compose scroll viewport resolved between the 139px status inset and 84px navigation inset, with content clipped inside that safe region while the atmosphere remained edge to edge.

| Theme / mode | Minimum UI text on six working surfaces | Minimum control edge | Primary label | Selected-container label |
| --- | --- | --- | --- | --- |
| Cloud / Light | 4.56:1 | 3.22:1 | 4.72:1 | 8.08:1 |
| Cloud / Dark | 4.77:1 | 3.13:1 | 8.07:1 | 8.96:1 |
| Dream / Light | 4.55:1 | 3.34:1 | 4.76:1 | 7.77:1 |
| Dream / Dark | 4.58:1 | 3.13:1 | 7.11:1 | 8.51:1 |
| Midnight / Light | 4.52:1 | 3.37:1 | 5.76:1 | 7.99:1 |
| Midnight / Dark | 4.74:1 | 3.14:1 | 8.08:1 | 8.63:1 |
| Mono / Light | 4.60:1 | 3.20:1 | 5.23:1 | 8.02:1 |
| Mono / Dark | 4.65:1 | 3.04:1 | 7.66:1 | 8.99:1 |

These calculations do not replace screenshot review. Atmosphere compositing, anti-aliased strokes, keyboard focus, disabled presentation, authored widget colors, real semantics/hit regions, and device rendering still require their appropriate physical or implementation review.

### Acceptance criteria for later implementation

| Review area | Required evidence before accepting integration |
| --- | --- |
| Theme completeness | All four identities in light/dark; mode independent from identity; explicit Material role mapping and system bars readable |
| Components | Resting, selected, focused, pressed, disabled, destructive, loading/error states; no active text below target contrast |
| Layout/accessibility | 48dp actual targets, readable large type, TalkBack/keyboard, RTL, narrow/short windows, modal keyboard handling |
| Editor preservation | Fixed canvas above independently scrolling inspector; no pointer/geometry/history/autosave changes hidden in a visual diff |
| Persistence/display | Legacy mixed 12/24-hour and theme values survive upgrade/reopen/copy; linked and standalone widget paths remain correct; migration explicitly reviewed |
| Rendering | Authored colors/fonts/alpha unchanged by app theme; no chrome effects leak into previews or home-screen widgets |
| Motion | Reduced/disabled system animations; no animated drag or moving hit regions; smooth performance on a physical device |
| Existing regression | Known post-rotation drag issue remains documented, not marked fixed; pin/add, empty projects and stored migrations still work |
| Code-task checks | Scoped diff, `assembleDebug`, task/session updates, focused commit per AI_RULES; appropriate targeted validation for the actual scope |

**Stop here for maintainer review.** All eight Lab variants have been inspected on a physical device. Review the frozen surface separation, typography density, Cute Atelier controls, Soft Halo, provisional Orbit Charm clarity, neutral canvas study, and system-bar contrast before authorizing any later work. Do not begin Phase 2D or production migration from this document alone.
