# BEST MATES TASKS

## Current development priority (2026-09-08)

1. Best Mates 2.0 — Phase 2B: Foundation Design Lab — implemented and awaiting visual review.
2. Stop before Phase 2C. Do not create shared production components or migrate production screens until the next approved scope.

Design foundation: [docs/BEST_MATES_UI_2_SPEC.md](docs/BEST_MATES_UI_2_SPEC.md).

The earlier UI-polish order (TASK-004, TASK-005, TASK-009, TASK-003) is historical backlog context, not authorization to start implementation during the Phase 2A review gate. TASK-004/005 remain implemented and awaiting physical-device visual review.

TASK-007 is deferred. Do not resume its remaining movement investigation unless the maintainer explicitly reprioritizes it.

### BM2-P2B — Foundation Design Lab
Status: IMPLEMENTED / AWAITING_VISUAL_REVIEW
Scope: One isolated Compose `@Preview` only

Completed:
- Added `ui/preview/BestMatesFoundationPreview.kt`, a private, vertically scrollable Best Mates Design Lab accessible only through Android Studio `@Preview`.
- Added local, non-persistent selectors for all eight resolved Cloud/Dream/Midnight/Mono × Light/Dark variants.
- Rendered the required atmosphere/surface, palette, typography, spacing, radii, controls, swatches, states, Soft Halo, overlay/scrim, Orbit metric, and Quiet Motion references from the Phase 2A foundation tokens.
- Used compact preview-only helper composables inside that one file. No shared production UI component or icon-library migration was added.
- Verified `assembleDebug` passes.

Review gate:
- Review the lab on a normal phone-size Compose preview across all eight variants before authorizing Phase 2C.
- Production navigation, app-root theming, Workspace, Settings, Editor, persistence, models, widgets, gestures, canvas behavior, and TASK-007 remain unchanged.
- The Compose `Modifier.shadow` used for the optional halo maps the approved 6dp blur to the supported elevation-shadow approximation; no custom shadow renderer or token adjustment was introduced.

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

