# BEST MATES TASKS

## P0 — Editor stability and correctness

### TASK-007 — Unlimited rotation and correct movement
Status: TODO
Priority: P0

Requirements:
- Remove the effective ±180° interaction limitation.
- Objects must support continuous rotation.
- 270°, 360°, 450°, 720° and negative rotations must remain usable.
- Movement direction must always follow finger movement in canvas coordinates.
- Rotating an object must not rotate the meaning of drag direction.
- Resize must remain correct after rotation.
- Existing saved projects must remain compatible.
- Do not solve this by clamping rotation.

Root cause (identified 2026-09-07):
- Drag delta accumulation (total += drag) is in local/screen space.
- The rotateLocalDelta() helper exists but the background layer drag (lines 1104–1132) explicitly
  comments it out ("Rotating them again reverses movement at 180 degrees"), leaving drag
  broken for any rotated layer.
- The city and text drag code may have the same issue (needs inspection).
- The rotation handle uses totalX * 0.45 heuristic – works for slow drags but not a true
  angle computation.
- Persistence: rotation is stored as raw Float in JSON; no normalization on save. Unlimited
  rotation values will round-trip correctly once the gesture is fixed.

Files involved:
- app/src/main/java/com/bestmates/worldclock/MainActivity.kt
  - rotateLocalDelta() (line 1365)
  - Layer drag handler (lines 1101–1133)
  - City drag handler (~lines 1195–1260)
  - Text drag handler (~lines 1275–1340)
  - snapRotation() (line 1390)

Acceptance tests:
- Drag right at 0°  → object moves right
- Drag right at 90° → object moves right
- Drag right at 180° → object moves right
- Drag right at 270° → object moves right
- Drag right at 450° → object moves right
- Negative rotation also works
- Project can be saved/reopened correctly
- assembleDebug passes


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
Status: TODO

Rework:
- rename
- undo
- redo
- pin/fix
- paste

Use reference images from folder "2" when available.
Keep icon style consistent with the rest of the app.


### TASK-005 — Editor top-right icons
Status: TODO

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
