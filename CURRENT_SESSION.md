# CURRENT SESSION

## Current owner
ChatGPT Work (next)

## Current task
TASK-007 rotation-handle coordinate correction is implemented and awaits physical-device verification.

## Current branch
main

## Last known good commit
e5ae160 (chore(repo): remove unused assets and obsolete project files)

## Task status
TASK-007: IN_PROGRESS (reopened after physical-device verification)

## What has been completed
- Initial project takeover, baseline build validation (passed), and workflow files creation (AI_RULES.md, TASKS.md, CURRENT_SESSION.md).
- First implementation for TASK-007 (commit 31c3ebb):
  - Replaced linear `totalX * 0.45f` heuristic with atan2-from-center calculation in `MainActivity.kt`.
  - Added continuous angle unwrapping (`unwrapAngleDelta`) to support rotations beyond ±180° (e.g. 270°, 360°, 450°, 720°, negative).
  - Maintained raw Float persistence and backward compatibility.
  - Verified `assembleDebug` builds cleanly (PASS).

## Physical-device verification findings (Reopened TASK-007)
Device testing of commit 31c3ebb revealed the following issues:
1. Continuous 360° rotation became possible in principle.
2. Rotation-handle hit detection is poor:
   - Touching the top-right handle frequently fails to register.
   - Multiple attempts often needed to grab the rotation handle.
3. Component, city, and text rotation handles are visually inconsistent.
4. Rotation sensitivity feels slightly low.
5. Critical tracking defect:
   - Rotation tracks reasonably between 0° and ~45°.
   - Around 45°–60°, rotation starts to lag behind the finger.
   - Beyond ~60°, rotation becomes non-responsive / stops following the finger correctly.

## Confirmed TASK-007 investigation and implementation (2026-09-08)
- The old handler was a child of an object whose visual layer rotated. It used pointer coordinates local to that child together with an estimated object centre. Updating the object rotation then changed the reference frame of the next pointer event, producing a moving-reference-frame feedback loop.
- The correction renders the rotation control as an unrotated canvas sibling. The canvas origin and the control origin are measured with `onGloballyPositioned` / `positionInRoot`; both the object centre and the pointer are converted to root pixels before `atan2` runs.
- Components, cities, and text now use the same `RotationHandleUnified` implementation: a quiet 17dp visual control inside a 42dp touch target, at a rotated top-right anchor, clamped so the target is not clipped by the canvas.
- Angle unwrapping, raw unlimited rotation persistence, 15-degree snapping, guide feedback, and haptics remain in place. The sensitivity adjustment is deliberately small: 1.04x after geometric correction.
- `assembleDebug` passed after the implementation. TASK-007 remains IN_PROGRESS until physical-device testing confirms tracking across full turns.

## Repository cleanup completed (2026-09-07)

- Audited all tracked runtime assets, Android resources, documentation, Gradle inputs, manifest entries, widget XML, and dynamic asset/resource lookup paths.
- Removed 11 confirmed-unreferenced legacy resources (10 obsolete XML drawables and one unenumerated SVG), reducing the production resource footprint by 2,974 bytes.
- Retained the 92 enum-addressable solar SVGs, `cities.db`, widget metadata/layouts, launcher/preview assets, and third-party notices.
- Retained `release-assets/BestMates-v0.1.0-debug.apk` pending explicit maintainer approval because it is a release-distribution policy decision, not a build-safety decision.
- Full evidence and classification: `docs/REPOSITORY_CLEANUP_AUDIT.md`.
- Reference folders `1/` and `2/` were not touched.

## Next step: physical-device verification only

- Install the pending TASK-007 debug build and test the three shared rotation controls at 0°, 45°, 60°, 90°, 180°, 360° and across a second turn / negative turn.
- Confirm the 42dp target is easy to acquire without the visual handle looking heavy, the control stays aligned at canvas edges, and snapping/haptics still behave as expected.
- Do not start another feature task or mark TASK-007 DONE before that verification.

## Files/folders that must NOT be touched / deleted
- Reference folders: `1/` and `2/`
- Persistence contracts: `ConfigStore.kt`, `ProjectStore.kt`, `Models.kt` (do not break saved projects or migrations)
- Native widget provider declarations in `AndroidManifest.xml` and `res/xml/`

## Build status
PASS — `assembleDebug` verified after the pending TASK-007 coordinate correction (28s).
This focused TASK-007 change awaits physical-device verification before any new task begins.
