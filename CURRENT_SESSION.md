# CURRENT SESSION

## Current owner
ChatGPT Work (next)

## Current task
Repository cleanup completed; await user approval before resuming TASK-007.

## Current branch
main

## Last known good commit
31c3ebb (fix(editor): replace heuristic rotation with atan2-from-center for unlimited rotation)

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

## Unverified investigation notes (from previous Sonnet session)
> [!WARNING]
> The following items are UNVERIFIED HYPOTHESES from previous preliminary inspection, NOT confirmed facts. The next agent must verify them directly:

- **Coordinate space mixing (UNVERIFIED):** The current atan2 calculation may be mixing local handle-space pointer deltas with canvas/layout coordinates or unrotated offsets.
- **Estimated object center inaccuracy (UNVERIFIED):** The hard-coded object center offset relative to the rotation handle (`-(width * dPx / 2f + dPx), 7f * dPx + height * dPx / 2f`) may be geometrically inaccurate after layout, alignment, and density conversion.
- **`graphicsLayer` rotation / hit-test interaction (UNVERIFIED):** Because the rotation handle is inside a composable with `graphicsLayer { rotationZ = ... }`, the visual handle position rotates on screen, but Compose's hit-testing and pointer coordinates may behave differently or diverge from visual position as rotation angle increases.
- **Handle visual vs touch target divergence (UNVERIFIED):** As the object rotates, the touch target area and visible handle might not stay aligned or may clip/drift.
- **Root/canvas measured coordinate solution (UNVERIFIED):** Measuring true coordinates in a shared coordinate space (e.g. root/canvas coordinates via `onGloballyPositioned` / `LayoutCoordinates`) to get actual object center `(centerX, centerY)` and pointer touch `(pointerX, pointerY)`, then computing `atan2(pointerY - centerY, pointerX - centerX)` in a single uniform space may be more reliable than estimating from handle offsets.
- **Touch target sizing (UNVERIFIED):** Increasing the invisible touch hit area (to ~36–44dp) while keeping the visible icon subtle/lightweight may resolve the poor grab acquisition.
- **Unified handle component (UNVERIFIED):** Component, city, and text rotation handles should eventually share one unified, robust implementation.

## Repository cleanup completed (2026-09-07)

- Audited all tracked runtime assets, Android resources, documentation, Gradle inputs, manifest entries, widget XML, and dynamic asset/resource lookup paths.
- Removed 11 confirmed-unreferenced legacy resources (10 obsolete XML drawables and one unenumerated SVG), reducing the production resource footprint by 2,974 bytes.
- Retained the 92 enum-addressable solar SVGs, `cities.db`, widget metadata/layouts, launcher/preview assets, and third-party notices.
- Retained `release-assets/BestMates-v0.1.0-debug.apk` pending explicit maintainer approval because it is a release-distribution policy decision, not a build-safety decision.
- Full evidence and classification: `docs/REPOSITORY_CLEANUP_AUDIT.md`.
- Reference folders `1/` and `2/` were not touched.

## Instructions for the next agent (ChatGPT Work)

### Step 1: Repository cleanup
Before resuming TASK-007 code changes, perform an audit and safely remove unused files, assets, and temporary images from the repository.

**Rules for cleanup:**
- Do NOT delete files blindly.
- Inventory candidate unused files.
- Search the entire codebase for all references (including dynamic name constructions and string identifiers).
- Classify each candidate:
  - definitely unused
  - probably unused
  - required / runtime-loaded
  - reference-only
- Inspect specifically: Android resources (`res/`), bundled assets (`assets/`), SVGs, drawable files, release artifacts, duplicate images, obsolete preview assets, and temporary files (e.g., `.tmp_*`).
- Account for dynamically loaded assets where simple text search may not be sufficient (e.g., dynamic icon lookup via reflection or name matching).
- Present a concrete deletion plan to the user before deleting ambiguous files.
- Run `assembleDebug` after cleanup to guarantee nothing required was removed.
- Review `git diff` carefully.
- **CRITICAL**: Keep reference folders `1` and `2` untouched unless explicitly authorized by the user.

### Step 2: Resume TASK-007
After cleanup is verified and committed:
- Investigate and resolve the rotation tracking defect (>45° lag/failure) and poor handle hit detection.
- Follow the requirements in `TASKS.md` (TASK-007).
- Do not mark TASK-007 as DONE until physical-device verification passes.

## Files/folders that must NOT be touched / deleted
- Reference folders: `1/` and `2/`
- Persistence contracts: `ConfigStore.kt`, `ProjectStore.kt`, `Models.kt` (do not break saved projects or migrations)
- Native widget provider declarations in `AndroidManifest.xml` and `res/xml/`

## Build status
PASS — assembleDebug verified at commit 31c3ebb (46s).
No uncommitted source code modifications.
