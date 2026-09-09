# Phase 2D design references

This is a local visual-study workspace for Phase 2D. It is not an Android runtime resource directory: do not copy its contents into `app/src/main/res`, `app/src/main/assets`, or any production source set without a separately approved implementation and licensing review.

`phase2d/celestial/keep_existing` holds captures of the current shipped celestial assets for comparison only. `sun_reference`, `moon_reference`, and `crescent_star_reference` hold visual studies of the relevant celestial forms.

`phase2d/shapes/heart`, `cloud`, `arch`, and `stationery` hold silhouette and proportion studies for those shape categories. `phase2d/orbit/approved` holds approved Orbit-command reference captures. `phase2d/screenshots` holds local Design Lab and device screenshots used during review.

Reference images may be proprietary or have unknown licensing. They are visual-study material only unless their licensing is explicitly verified. Do not trace, bundle, redistribute, or treat any reference image as a production asset without that verification.

Except for this README and empty `.gitkeep` folder markers, all files in `design_refs/` are ignored by Git and must remain local.

## Current direction notes — 2026-09-09

**What works:** restrained eight-ray suns; one filled and one ring/outline core sun; long continuous crescents with broad, quiet negative space; one optional paired sparkle; and simple horizon bands that make sunrise/sunset readable without weather clutter.

**Preserve for audit:** the existing simple/ring/horizon Sun and plain/long-crescent Moon examples. Their production identifiers stay compatible while the future picker groups them into a clearer family system.

**Redrawn as original Golden candidates:** Soft Arch and Atelier Ticket. The standard symmetric Heart remains a canonical base; a softer Heart is a separate variant, never a replacement.

**Inspiration only:** the Kustom Widget screenshots in `sun_reference` and `moon_reference`. Their source and license are unknown, so they must not be traced, bundled, or shipped. They inform only proportion, negative space, ray rhythm, and crescent quality.

**Priority:** 1) Celestial family architecture and crescent quality; 2) canonical-plus-variant Shape taxonomy; 3) Golden Set 2 static review; 4) later production metadata and picker migration. No reference image is approved for direct adoption unless its source and license are recorded and verified.
