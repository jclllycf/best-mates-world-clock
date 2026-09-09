# BEST MATES TASKS

## Current development priority (2026-09-09)

1. Best Mates 2.0 — Phase 2D is closed: **Visual System Direction Established / Celestial Artwork Finalization Deferred**.
2. Phase 3 is the next implementation phase. Start it only under a separate explicit scope; do not infer bulk asset promotion or Celestial approval from Phase 2D closure.

Design foundation: [docs/BEST_MATES_UI_2_SPEC.md](docs/BEST_MATES_UI_2_SPEC.md).

Phase 2D asset language: [docs/BEST_MATES_VISUAL_ASSET_BIBLE.md](docs/BEST_MATES_VISUAL_ASSET_BIBLE.md).

The earlier UI-polish order (TASK-004, TASK-005, TASK-009, TASK-003) is historical backlog context, not authorization to start implementation during the Phase 2C review gate. TASK-004/005 remain implemented and awaiting physical-device visual review.

TASK-007 is deferred. Do not resume its remaining movement investigation unless the maintainer explicitly reprioritizes it.

### BM2-P2D-CLOSURE — Phase 2D Closure & Phase 3 Handoff
Status: COMPLETE / VISUAL_DIRECTION_ESTABLISHED / CELESTIAL_FINAL_ART_DEFERRED
Scope: preserve Golden Set 3 review work, freeze architecture decisions, and prepare the Phase 3 handoff

Completed:
- Preserved the Golden Set 3 Objects, Celestial, Orbit, Work Mat, Wallpapers, and Sources review pages without mass-promoting candidates into production.
- Established **Shape × Variant × Material × Appearance** as independent axes. Material is not part of persistent Shape identity, and existing transparent components remain supported.
- Kept the production Heart as canonical. Golden Set 3 object IDs remain review-only until an explicitly scoped migration accepts an individual asset.
- Accepted the Soft Atelier Work Mat as the visual basis for a later Editor migration while preserving the geometry engine, intrinsic proportions, gestures, guides, fixed canvas plus scrolling inspector, and widget behavior.
- Preserved accepted/provisional Orbit decisions: Golden Undo/Redo, global Settings gear, local Adjust sliders, production Pin, the user-identified Future canvas Rotate, and the general Orbit direction. Generic Apply buttons do not display decorative checkmarks.
- Added the Manual Device Review Policy to `PROJECT_HANDOFF.md`: the user is the primary physical-device visual reviewer unless autonomous screenshot inspection is explicitly requested or needed for a specific rendering defect.
- `assembleDebug --warning-mode all` passes; the debug APK was installed on connected PJF110 and the Design Lab was launched and left open for manual review without screenshot capture.
- Phase 3 is next but remains unstarted in this closure task. TASK-007 remains deferred.

### BM2-CELESTIAL-FINAL — Celestial Atelier Final Pass
Status: DEFERRED / FUTURE_FOCUSED_TASK
Scope: final Celestial artwork and explicit production-promotion decisions

Deferred work:
- Final Sun and Moon geometry, reference-quality crescent families, star/jewel companions, atmospheric variants, and the dedicated Crescent & Star option.
- Small-size optical correction, picker population, source/license review, and explicit per-asset production promotion.
- Preserve the current debug Celestial gallery, filters, Sources page, retained production collection, persisted production IDs, and current production behavior until this task is explicitly started.

Stop condition:
- Do not describe current new Sun/Moon candidates as visually approved and do not continue drawing them during Phase 3 work unless this focused task is explicitly authorized.

### BM2-P2D-DL — Visual Direction Lock / Golden Set 2
Status: HISTORICAL / SUPERSEDED_BY_GOLDEN_SET_3_AND_PHASE_2D_CLOSURE
Scope: audit and preview-only candidates; production IDs, renderers, and pickers remain unchanged

Completed:
- Locked the Phase 2D method as preserve → normalize → extend. Existing successful production assets remain compatible; future picker curation groups them by family instead of replacing the library wholesale.
- Temporarily locked the approved Golden Undo and Redo candidates. Settings, Font, Pin/Fix Position, and Rotate remain the existing reviewed core candidates.
- Audited production Sun/Moon sources into Canonical, Minimal, Crystal Halo, Dream Crescent, Horizon, Weather Celestial, optional Crescent & Star, and historical/source-pack roles. Existing BM simple/ring/horizon assets are preservation anchors; local pack variants remain resolvable but are not future defaults.
- Expanded the debug-only Celestial Golden Set to four original paired families (Minimal, Crystal Halo, Dream Crescent, Horizon) plus a separate, explicitly labeled Crescent & Star Moon study. It contains four Sun and five Moon vectors without changing any production enum or SVG.
- Locked Canonical Base Shape + Curated Variant as the Shape Atelier rule. Added nine debug-only candidates: canonical/soft Hearts, canonical/calm Clouds, canonical/soft Arches, canonical/atelier Tickets, and Postcard. The symmetric Heart remains available; Arch and Stationery candidates were redrawn after the earlier rejection.
- Added the visual-direction record and updated the Visual Asset Bible, UI spec, and ignored local-reference README with provenance, audit decisions, taxonomy, ownership boundaries, and Sol handoff requirements.
- Ran a clearly labeled static raster review at artwork sizes. `assembleDebug --warning-mode all` passes. No device is connected, so Cloud Light, Dream Light, Midnight Dark, and all-eight Design Lab device review remain pending.

Stop condition:
- Review Golden Set 2 on a physical device before any production ID migration, library expansion, Phase 3 UI adoption, or TASK-007 work.

### BM2-P2D-0.5 — Golden Visual Asset Set
Status: IMPLEMENTED / SUPERSEDED_BY_BM2-P2D-DL
Scope: isolated Golden masters and Design Lab presentation only; no production asset adoption

Completed:
- Added exactly six original 24×24 Golden Orbit candidates: soft-adjustment Settings; mirrored, separated-head Undo/Redo; intentional specimen-card Font; semantically correct Pin/Fix Position; and partial-orbit Rotate.
- Added exactly three original, tintable Sun/Moon pairs: Minimal Celestial, Soft Halo, and Dreamy Charm. Each pair shares its declared construction and appears together at 20dp, 28dp, and 40dp.
- Added exactly four original fillable Shape candidates: a mild-asymmetry Heart, low-lobe Cloud, tangent Soft Arch, and soft-notch Atelier Ticket. Their candidate preview retains intrinsic aspect ratio at thumbnail and larger size.
- Added three Type Wardrobe presentation cards using existing local/system families only: Clean Sans, Editorial Serif, and Friendly Display. No font file or dependency was added.
- Added a Design Lab Golden Asset Gallery using the frozen Milk Crystal, Pearl Glass, semantic selection, and Soft Halo presentation rules across the existing eight local theme variants.
- Added a lightweight candidate-catalog integrity report covering 19 entries: exact category counts, unique IDs, valid vector dimensions, successful shape-path construction, non-empty/in-bounds geometry, and direct gallery enumeration.
- Recorded the external proportion/style references and license status. Golden paths are original Kotlin geometry; no third-party path or binary was copied, adapted, or bundled.
- Kept every review handle under `golden.*`. Existing production shape names, Sun/Moon enum IDs/assets, font strings, `OrbitIcons`, persistence, renderers, widgets, color picker, app screens, transforms, TASK-007, and reference folders remain unchanged.
- `assembleDebug --warning-mode all` passes. No Android device was attached for this task, so physical-device review remains pending.

Stop condition:
- Review the Golden Asset Gallery before authorizing Phase 2D-1 or any production adoption/mass expansion.

### BM2-P2D-0 — Visual Asset Bible
Status: COMPLETE / APPROVED_AS_PHASE_2D_SOURCE_OF_TRUTH
Scope: documentation-only definition of the Phase 2D asset language and staged quality gates

Completed:
- Defined the shared “soft collectible objects for a personal creative atelier” DNA across Orbit Charm 2.0, Shape Atelier, Celestial Atelier, Type Wardrobe, previews, and picker presentation.
- Defined measurable Orbit construction, decoration limits, canonical Confirm/Undo/Redo/Font/Pin/Rotate/Resize rules, and 16/20/24dp review criteria.
- Defined Studio Card, Organic, Charm, and Paper/Stationery shape families; shape-specific quality bars; canonical-renderer preview rules; and intrinsic-proportion presentation without changing canvas transforms.
- Defined paired Minimal Celestial, Soft Halo, and Dreamy Charm Sun/Moon families, geometry guidance, 20/28/40dp checks, recoloring, and offline requirements.
- Defined Type Wardrobe categories, a 10–16 family eventual target subject to size/licensing, clock-specific specimens, multilingual/fallback review, and Milk Crystal presentation.
- Defined one unified asset-picker hierarchy using the frozen Pearl Glass, Milk Crystal, Jelly Crystal, Soft Halo, and semantic selection contracts.
- Defined external-reference and provenance requirements, strict saved-ID compatibility, and the Phase 2D-0.5 Golden Orbit/Celestial/Shape/Type gate.
- Confirmed the production color picker and all production UI, interaction, persistence, widget, TASK-007, and reference-folder work remain outside Phase 2D-0.

Stop condition:
- Phase 2D-0.5 is not started. Await review of the Bible before producing Golden Set assets.

### BM2-P2C.3 — Interaction Surface Closure & Visual Hygiene
Status: IMPLEMENTED / PHYSICALLY_VERIFIED / VISUALLY_FROZEN
Scope: final Soft Crystal interaction materials, semantic selection hygiene, specified provisional icon corrections, Design Lab control consistency, and debug-only safe-inset behavior

Completed:
- Removed the built-in decorative dot from `OrbitIcons.Confirm`, so every Design Lab check-bearing state now uses a plain semantic check. Chips and compact selectors may retain one 6dp semantic dot only when they do not show a check.
- Added shared 6dp dot, 18dp ordinary-check, and 16dp compact-check state tokens. Selected cards, picker rows, theme cards, swatches, and project rows use selected fill, a controlled 1.5dp edge, one semantic indicator, and only then an optional Soft Halo.
- Rounded and rebalanced the provisional Confirm path for 16/20/24dp rendering; separated the Font `A` and `a`; rebuilt Undo/Redo as true mirrored curves with readable heads; and optically balanced Pin while keeping it distinct from Lock. Full icon-language work remains deferred to Phase 2D.
- Froze Pearl Glass, Milk Crystal, and Jelly Crystal recipes with controlled translucency, a subtle vertical tonal body, low-alpha identity contamination, a diffuse internal pearl veil, a translucent edge, ambient shadow, upper highlight, lower refraction, and restrained warm reflection. Jelly Crystal remains opaque through its primary label band.
- Applied the crystal depth hierarchy to selected cards and canvas-study selection in addition to the existing actions, icon controls, chips, toggles, slider, fields, picker rows, swatches, and compact controls. Selection-aware controls now expose selected semantics and keep approximately 48dp targets.
- Preserved Quiet Motion at 80ms press, 120ms release, and 160ms state transition. Pressed controls use 0.75dp downward travel, `.987` scale, 0.5dp elevation, denser tone, 22% shadow, 48%/46% compressed highlight, and 112% lower refraction without spring, overshoot, or bounce.
- Made the debug Activity explicitly edge to edge and applied `safeDrawingPadding()` outside the Design Lab scroll modifier. Background atmosphere can extend behind system chrome while readable/interactive content stays inside status/navigation insets.
- Verified `assembleDebug --warning-mode all` passes with no new warnings. Installed and launched the APK on attached device `PJF110`; inspected Cloud Light, Dream Light, and Midnight Dark across editor tools, inspector, picker, icon family, Halo, canvas, color controls, actions, switches, sliders, fields, and states; and quickly reviewed all eight identity × brightness combinations. No clipping, horizontal overflow, system-bar overlap, checkmark dots, Font collision, or fused Undo/Redo geometry was observed.
- Production Workspace, Settings, Editor, theme adoption, persistence, widget rendering/providers, gestures, canvas geometry, TASK-007, and reference folders `1/` and `2/` remain unchanged.

Frozen boundary:
- Phase 2C is visually frozen unless a serious regression is found. Stop before Phase 2D.
- Phase 2D owns the full Orbit Charm redesign, Shape Atelier, Celestial Atelier, Type Wardrobe, and asset-picker visual language.

### BM2-P2C.2 — Crystal Atelier Final Foundation Polish
Status: IMPLEMENTED / SUPERSEDED BY BM2-P2C.3
Scope: Soft Crystal tokens, isolated Design Lab treatments, Orbit Charm cleanup, and production-behavior audit only

Completed:
- Replaced the preview-only pressable-depth recipe with typed Pearl Glass, Milk Crystal, and Jelly Crystal families covering fill alpha, light/dark elevation and shadow, tonal shifts, translucent edge, upper highlight, lower refraction, optional warm reflection, and highlight length.
- Applied the hierarchy to Design Lab actions, icon controls, chips, toggles with a lavender-resin track and pearl-white thumb, slider thumb/value tags, project/theme cards, inspector controls, picker rows, text fields, color-swatch frames, and icon/shape studies. Direct sample colors remain untinted and all interactive studies retain 48dp targets.
- Historical Phase 2C.2 press feedback used 80ms in / 120ms out, 0.75dp movement, `.987` scale, 0.5dp elevation, 22% resting shadow, 48% highlight, 72% refraction, and a 1.25dp toggle-thumb expansion. Phase 2C.3 supersedes its refraction value with the frozen 112% compressed lower edge.
- Phase 2C.2 removed non-semantic dots from normal utility icons and temporarily allowed Confirm ornament. Phase 2C.3 supersedes that exception: Confirm is now a plain semantic check; ornament remains limited to Theme, Sun, and Moon.
- Audited production Pin/Lock behavior without changing it: only Pin exists; its `locked` flag blocks position/resize/rotation manipulation while property editing remains available. There is no separate full-edit Lock command; foundation Lock artwork remains reserved for a future explicit behavior contract.
- Audited production color capability without changing it: the existing picker supports continuous HSV hue/saturation/value plus independent opacity and writes arbitrary ARGB colors. The Lab now shows a continuous hue/saturation study, separate brightness/opacity controls, and a separate recommended palette.
- Used gradients, alpha, borders, shadows, and `drawWithCache`; no backdrop blur, per-frame brush churn, production component extraction, or production UI adoption was added.
- Verified `assembleDebug` passes. All eight variants rendered on the attached phone; Cloud Light, Dream Light, and Midnight Dark were inspected for icon cleanliness, Font/Pin balance, actions, chips, switches, picker rows, and icon actions.
- Production `MainActivity.kt`, navigation, Workspace, Settings, Editor, persistence, models, widget rendering, canvas behavior, gestures, TASK-007, resources/manifests, and reference folders remain unchanged.

Review gate:
- Superseded by BM2-P2C.3. Use its frozen review gate and do not extract/migrate these recipes into production components until explicitly authorized.

### BM2-P2C.1 — Soft Button Depth Polish
Status: IMPLEMENTED / SUPERSEDED BY BM2-P2C.2
Scope: Pressable-depth tokens and isolated Design Lab button/icon-action recipes only

Completed:
- Added a typed light/dark pressable-depth recipe with restrained elevation, tonal vertical shift, outer shadow, 1dp upper inner highlight, lower edge, and compressed pressed-state values.
- Applied the recipe only to preview-local primary, secondary, outlined, destructive, and Orbit Charm icon-action surfaces. Primary has no hard border; text actions remain flat.
- Added side-by-side Resting/Pressed references for primary, secondary, outlined, and icon actions while keeping the Design Lab compact and interactive.
- Reused Quiet Motion at 80ms press / 120ms release with 1dp downward translation, `.988` visual scale, reduced shadow, and shortened highlight; touch targets remain unchanged.
- Checked all primary-gradient endpoints across the eight variants; the minimum text contrast is 4.51:1 at the Cloud Light resting top.
- Installed and launched the refreshed debug APK on the attached 1080 × 2340 phone. All eight theme combinations rendered; Cloud Light, Dream Light, and Midnight Dark button sections were inspected directly.
- Verified `assembleDebug` passes. Production navigation, app-root theme, Workspace, Settings, Editor, persistence, widgets, gestures, real canvas, TASK-007, manifests/resources, and reference folders remain unchanged.

Review gate:
- This recipe was superseded by BM2-P2C.2. Use the Phase 2C.2 review gate; do not extract the historical recipe into production components.

### BM2-P2C — Cute Atelier Visual Refinement
Status: APPROVED / COMPLETE
Scope: Foundation tokens, reusable Orbit Charm vectors, isolated Design Lab, debug-only system bars, and the design specification

Completed:
- Reinterpreted Cloud, Dream, Midnight, and Mono in both Light and Dark with identity-specific neutral environments, stronger surface separation, Powder Blush/Milk Pearl atmosphere, deep plum hierarchy, and a neutral lavender canvas mat.
- Deepened functional Cloud accent, muted text, and light control-edge values where required by static contrast checks while retaining the supplied colors as visual north-star swatches.
- Refined typography, radii, border widths, depth, pressed state, and Soft Halo 2.0 tokens without changing the Phase 2A resolver or Material bridge architecture.
- Added `ui/icons/OrbitIcons.kt` with 27 reusable production-ready Orbit Charm vectors, including the orbit-based Rotate and sibling Resize marks. Production icon call sites remain untouched.
- Rebuilt the Design Lab as Experience Showcase followed by compact Token Diagnostics. It uses custom preview-only button, toggle, slider, chip, field, picker, canvas, color-control, and selection recipes rather than stock Material samples.
- Preserved local non-persistent switching across all eight identity × brightness variants. The debug Activity now synchronizes status/navigation bar colors and light/dark system icon appearance with the selected Lab variant.
- Updated `docs/BEST_MATES_UI_2_SPEC.md` as the authoritative Cute Atelier / Cozy Orbit target and recorded the current contrast evidence and future component boundary.
- `assembleDebug` and `:app:processReleaseMainManifest` pass. Generated-manifest inspection finds the Lab Activity only in debug outputs. The refreshed APK was installed and all eight selector combinations plus representative long-scroll checkpoints were inspected on the attached 1080 × 2340 device.

Approval record:
- Commit `16f19ba` is the approved visual baseline for Cute Atelier / Cozy Orbit. Phase 2C.1 is limited to button-depth polish and does not reopen the broader direction.
- Historical gate retained: no production component extraction, screen migration, Settings persistence work, or TASK-007 investigation was authorized.
- Production navigation, app-root theming, Workspace, Settings, Editor, persistence, models, widgets, rendering, gestures, canvas behavior, and reference folders remain unchanged.

### BM2-P2B.1 — Design Lab device harness
Status: IMPLEMENTED / AWAITING_PHYSICAL_DEVICE_REVIEW
Scope: Debug source set only, plus the existing Design Lab host visibility

Completed:
- Added `app/src/debug/AndroidManifest.xml` with a debug-only, non-launcher `BestMatesDesignLabActivity` entry.
- Added `app/src/debug/java/com/bestmates/worldclock/debug/BestMatesDesignLabActivity.kt`, which renders the existing `BestMatesFoundationDesignLab()` without duplicating its UI.
- Changed only that existing host composable from file-private to module-internal so the debug source set can reuse it. The `@Preview` remains private and production has no caller.
- The local Design Lab identity/brightness selector remains available on device for all eight Cloud/Dream/Midnight/Mono × Light/Dark variants; it writes no preference.
- `assembleDebug` and `:app:processReleaseMainManifest` pass. The merged debug manifest contains the harness; the merged release manifest does not.

Device access:
- Install the debug APK, then run: `adb shell am start -n com.bestmates.worldclock/com.bestmates.worldclock.debug.BestMatesDesignLabActivity`
- The Activity has no launcher intent and is excluded from recents. It exists only in the debug APK.

Review gate:
- Historical gate retained: verify the Design Lab on a physical device across all eight variants, then stop.
- Production navigation, app-root theming, Workspace, Settings, Editor, persistence, models, widgets, gestures, canvas behavior, TASK-007, and reference folders remain unchanged.

### BM2-P2B — Foundation Design Lab
Status: ENGINEERING_PASS / VISUAL_SUPERSEDED_BY_PHASE_2C
Scope: One isolated Compose `@Preview` only

Completed:
- Added `ui/preview/BestMatesFoundationPreview.kt`, a private, vertically scrollable Best Mates Design Lab accessible only through Android Studio `@Preview`.
- Added local, non-persistent selectors for all eight resolved Cloud/Dream/Midnight/Mono × Light/Dark variants.
- Rendered the required atmosphere/surface, palette, typography, spacing, radii, controls, swatches, states, Soft Halo, overlay/scrim, Orbit metric, and Quiet Motion references from the Phase 2A foundation tokens.
- Used compact preview-only helper composables inside that one file. No shared production UI component or icon-library migration was added.
- Verified `assembleDebug` passes.

Historical review result:
- Engineering isolation passed; the Phase 2B visual treatment was not approved. Phase 2C supersedes its presentation while retaining the kernel and harness.
- Production navigation, app-root theming, Workspace, Settings, Editor, persistence, models, widgets, gestures, canvas behavior, and TASK-007 remain unchanged.

### BM2-P2A — Foundation Kernel
Status: COMPLETE / FOUNDATION_AUTHORIZED
Scope: Typed Compose design-system kernel only

Completed:
- Added raw palette references and typed semantic colors for Cloud, Dream, Midnight, and Mono in independent Light/Dark resolution.
- Added an explicit Material 3 `ColorScheme` bridge for all roles supported by the pinned Material 3 1.3.1 dependency.
- Added the complete UI typography, spacing, radius, shared dimension, elevation, Soft Halo, state, motion, reduced-motion, and Orbit Softline metric tokens.
- Added pure identity/brightness/design resolvers and the opt-in `BestMatesDesignTheme`/`BestMatesDesign` access contract.
- Verified all 104 approved target colors are present exactly and `assembleDebug` passes.

Review gate:
- The production `BestMatesTheme` and every production screen remain unchanged and do not import the new kernel.
- No preview, shared component, icon artwork migration, persistence inheritance, screen migration, or app-root adoption was started.
- No persistence/model/widget-renderer/interaction file changed. TASK-007 remains deferred.
- No tests were added because the project has no existing JVM test source set/dependency setup; no framework was added for this phase.

### BM2-P1 — Design Foundation
Status: APPROVED / COMPLETE
Scope: Documentation only

Completed:
- Inspected the current Compose theme, shared controls, Settings/editor ownership, scattered visual constants, and recent git history.
- Defined Soft Orbit / Warm Pearl + Lavender, Orbit Softline, Quiet Motion, Soft Halo, Floating Creation Space, Scrolling Soft Inspector, Visual Theme Gallery, and Mates Orbit branding.
- Specified semantic token architecture, concrete palettes for all four theme identities in light/dark, typography, dimensions, states, accessibility, surfaces, controls, overlays, and motion.
- Defined Settings ownership of global theme mode and time format, with explicit preservation of existing saved widget display values and a separately scoped future migration.
- Listed proposed token files, shared components, and later screen/settings integration boundaries.

Review record:
- Phase 1 was approved as the authoritative contract for Phase 2A.
- Preserve fixed top canvas + vertically scrolling inspector below.
- Do not change core editor interaction logic or reference folders `1/` and `2/`.
- No application code or persistence was changed. No new build/device validation is claimed for this documentation task.

## P0 — Editor stability and correctness

### TASK-007 — Unlimited rotation and correct movement
Status: PARTIALLY_COMPLETE / DEFERRED
Priority: DEFERRED

Requirements:
- Remove the effective ±180° interaction limitation.
- Objects must support continuous rotation.
- 270°, 360°, 450°, 720° and negative rotations must remain usable.
- Movement direction must always follow finger movement in canvas coordinates.
- Rotating an object must not rotate the meaning of drag direction.
- Resize must remain correct after rotation.
- Existing saved projects must remain compatible.
- Do not solve this by clamping rotation.

Physical-device verification feedback (Reopened 2026-09-07):
- Continuous 360° rotation became possible in principle.
- Rotation-handle hit detection is poor: user often needs several attempts to grab the handle.
- Component/city/text rotation handles are visually inconsistent and need unification.
- Rotation sensitivity feels slightly low (needs modest increase without crude multiplier overshoot).
- Tracking begins to feel wrong around 45°–60°.
- Beyond roughly 60°, rotation no longer follows the finger correctly.
- Note: Do NOT mark TASK-007 as DONE until physical-device verification passes.

Implementation awaiting verification (2026-09-08):
- Rotation is now calculated in root coordinates: the canvas origin and rotation-control origin are measured, and the object's canvas centre is converted to the same root-pixel space before `atan2`.
- The rotation control is an unrotated sibling of the rotated object, removing the previous moving-reference-frame feedback loop.
- All component, city, and text calls share `RotationHandleUnified`: 17dp visible control, 42dp touch target, same icon/opacity/anchor, 15-degree snapping, guide feedback, and existing haptics.
- A modest 1.04x response adjustment is applied only after the coordinate-space correction. `assembleDebug` passes; physical-device verification is still required.

Deferred physical-device finding (2026-09-08):
- Rotation sensitivity is acceptable in the current build.
- Post-rotation drag movement does not consistently follow the user's finger direction on physical device.
- Do not investigate or patch this remaining movement defect while TASK-007 is deferred.
- TASK-007 is not DONE.

Files involved:
- app/src/main/java/com/bestmates/worldclock/MainActivity.kt
  - Layer rotation handle
  - City rotation handle
  - Text rotation handle
  - angleBetween(), unwrapAngleDelta(), snapRotation()
  - RotationHandleUnified()

Acceptance tests:
- Touch target easy to acquire reliably (e.g. 36–44dp touch area)
- Drag right at 0°  → object moves right
- Drag right at 90° → object moves right
- Drag right at 180° → object moves right
- Drag right at 270° → object moves right
- Drag right at 450° → object moves right
- Continuous tracking works accurately through 0°..360°+ without drifting or lagging beyond 45°-60°
- Negative rotation also works
- Project can be saved/reopened correctly
- assembleDebug passes
- Physical device verification passes


### TASK-010 — Multiline text spacing
Status: TODO
Priority: P0

Requirements:
- Reduce excessive spacing between wrapped lines.
- Prevent glyph overlap for every supported font.
- Use real font metrics where possible.
- Editor preview and home-screen widget must match closely.
- Decorative fonts must not collide vertically.

Root cause:
- renderCustomText() uses fontMetrics.descent - fontMetrics.ascent * 1.36 as lineHeight.
  This value is device-dependent and may exceed actual visual line height for display fonts.
- Both WorldClockWidgetProvider.renderCustomText() and the editor preview share this logic,
  but they may diverge if one is updated without the other.

Files involved:
- app/src/main/java/com/bestmates/worldclock/widget/WorldClockWidgetProvider.kt
  (renderCustomText, line 98)
- app/src/main/java/com/bestmates/worldclock/MainActivity.kt
  (editor text rendering)

Acceptance tests:
- Two-line text
- Three-line text
- Large font size
- Small font size
- Different fonts
- Narrow text box
- Rotated text
- Widget rendering


## P1 — Editor control system

### TASK-003 — Unified rotation handle
Status: TODO

Requirements:
- Component, city and text must use the same rotation handle.
- Same icon.
- Same visual size.
- Same opacity.
- Same hit area.
- Same behavior.
- Visually subtle like the resize handle.
- Still immediately recognizable as rotation.

Note: RotationHandleUnified() already exists at line 1410. Verify all three element types
use it consistently. Old RotationHandle() (line 1432) appears to be a legacy variant.

Prefer one reusable RotationHandle implementation.


### TASK-006 — Unified snap / guide engine
Status: TODO

Requirements:
Create a reusable snapping system supporting:
- center alignment
- left/right alignment
- top/bottom alignment
- equal width
- equal height
- city same-size indication
- text same-size indication
- rotation-angle snapping
- visual guide display
- optional subtle haptic feedback

Do not implement separate disconnected snapping systems.


### TASK-004 — Editor action icons
Status: IMPLEMENTED — awaiting visual review (2026-09-08)

Rework:
- rename
- undo
- redo
- pin/fix
- paste

Use reference images from folder "2" when available.
Keep icon style consistent with the rest of the app.

Implementation:
- Rebuilt rename, undo, redo, pin and paste as one custom rounded-outline Canvas family.
- Uses a 20dp visual canvas with 1.6dp rounded strokes inside a 40dp accessible touch target.
- Redrawn from the visual language of the references in `2/`; no external icon library is mixed into the editor toolbar.


### TASK-005 — Editor top-right icons
Status: IMPLEMENTED — awaiting visual review (2026-09-08)

Rework:
- autosave
- add component
- add city
- add text

Icons must be:
- intuitive
- visually lightweight
- consistent with Best Mates
- theme compatible

Implementation:
- Rebuilt autosave, add component, add city and add text with the same stroke, spacing, roundness and theme-aware tint system as TASK-004.
- Existing actions and editor layout remain unchanged; only their visual marks and touch target consistency changed.


## P1 — Shape library

### TASK-008 — Rebuild shape geometry
Status: TODO

Must fix:
- capsule
- ticket
- flag
- drop

Investigate:
- cloud
- petal
- wave
- other rough placeholder paths

Rules:
- visible shape must fill its own bounding box naturally
- support square, landscape and portrait proportions
- do not force proportional resizing
- no artificial empty margins

Add a curated number of new shapes.
Prefer quality over quantity.


## P2 — Asset libraries

### TASK-001 — Curated font library
Status: TODO

Requirements:
- Remove fonts that look nearly identical.
- Add visually distinct fonts.
- Curate by style category rather than raw quantity.
- Suggested categories:
  geometric, rounded, humanist, serif, editorial serif, display,
  handwriting, mono, condensed, playful, elegant, retro

If importing external fonts:
- only use fonts with clear redistribution/app embedding permissions
- keep license notices
- update THIRD_PARTY_NOTICES.md where needed
- do not add fonts with unclear licensing


### TASK-002 — Sun/moon icon library
Status: TODO

Requirements:
- Remove/rebuild ugly, misaligned or incorrect icons.
- Inspect reference assets in folder "1" if available.
- Add new sun/moon designs inspired by the references.
- Preserve consistent sizing and optical alignment.
- Add tasteful crescent/star designs including Islamic-inspired celestial motifs.

Important:
If a reference icon is third-party artwork, do not blindly pixel-copy it.
Recreate the visual language as an original SVG unless rights clearly allow direct reproduction.

Maintain license/attribution information.


## P2 — UI polish

### TASK-009 — Interaction animation and UI refinement
Status: TODO

Requirements:
Improve:
- navigation transitions
- back transitions
- button press feedback
- editor selection transitions
- dialogs
- visual warmth
- theme adaptation
- overall main-screen polish

Style:
- soft, calm, warm, airy, subtle, comfortable
- no excessive bounce
- no over-animation

Do this after editor correctness tasks are stable.

