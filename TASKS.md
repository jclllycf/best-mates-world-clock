# BEST MATES TASKS

## Current development priority (2026-09-08)

1. Best Mates 2.0 — Phase 2C.2: Crystal Atelier Final Foundation Polish — implemented and awaiting maintainer review.
2. Review the final Soft Crystal Design Lab, then stop before Phase 3. Do not create shared production components or migrate production screens until the next approved scope.

Design foundation: [docs/BEST_MATES_UI_2_SPEC.md](docs/BEST_MATES_UI_2_SPEC.md).

The earlier UI-polish order (TASK-004, TASK-005, TASK-009, TASK-003) is historical backlog context, not authorization to start implementation during the Phase 2C review gate. TASK-004/005 remain implemented and awaiting physical-device visual review.

TASK-007 is deferred. Do not resume its remaining movement investigation unless the maintainer explicitly reprioritizes it.

### BM2-P2C.2 — Crystal Atelier Final Foundation Polish
Status: IMPLEMENTED / AWAITING_MAINTAINER_REVIEW
Scope: Soft Crystal tokens, isolated Design Lab treatments, Orbit Charm cleanup, and production-behavior audit only

Completed:
- Replaced the preview-only pressable-depth recipe with typed Pearl Glass, Milk Crystal, and Jelly Crystal families covering fill alpha, light/dark elevation and shadow, tonal shifts, translucent edge, upper highlight, lower refraction, optional warm reflection, and highlight length.
- Applied the hierarchy to Design Lab actions, icon controls, chips, toggles with a lavender-resin track and pearl-white thumb, slider thumb/value tags, project/theme cards, inspector controls, picker rows, text fields, color-swatch frames, and icon/shape studies. Direct sample colors remain untinted and all interactive studies retain 48dp targets.
- Implemented Quiet Motion press compression at 80ms in / 120ms out: 0.75dp movement, `.987` scale, 0.5dp elevation, 22% resting shadow, 48% highlight, 72% refraction, and a 1.25dp toggle-thumb expansion. The 160ms token remains available for small state settling; no spring or bounce was added.
- Cleaned non-semantic dots from normal utility icons. Restrained ornament is reserved for Theme, Sun, Moon, and Confirm. Rebuilt Font as separated vector `A` + lowercase `a`, and Pin as a soft rounded tack with a short needle.
- Audited production Pin/Lock behavior without changing it: only Pin exists; its `locked` flag blocks position/resize/rotation manipulation while property editing remains available. There is no separate full-edit Lock command; foundation Lock artwork remains reserved for a future explicit behavior contract.
- Audited production color capability without changing it: the existing picker supports continuous HSV hue/saturation/value plus independent opacity and writes arbitrary ARGB colors. The Lab now shows a continuous hue/saturation study, separate brightness/opacity controls, and a separate recommended palette.
- Used gradients, alpha, borders, shadows, and `drawWithCache`; no backdrop blur, per-frame brush churn, production component extraction, or production UI adoption was added.
- Verified `assembleDebug` passes. All eight variants rendered on the attached phone; Cloud Light, Dream Light, and Midnight Dark were inspected for icon cleanliness, Font/Pin balance, actions, chips, switches, picker rows, and icon actions.
- Production `MainActivity.kt`, navigation, Workspace, Settings, Editor, persistence, models, widget rendering, canvas behavior, gestures, TASK-007, resources/manifests, and reference folders remain unchanged.

Review gate:
- Stop for maintainer review. Do not begin Phase 3 or extract/migrate these foundation recipes into production components until explicitly authorized.

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
- Do not begin Phase 3, production component extraction, screen migration, Settings persistence work, or TASK-007 investigation until explicitly authorized.
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
- Verify the Design Lab on a physical device across all eight variants, then stop. Do not begin Phase 3.
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

