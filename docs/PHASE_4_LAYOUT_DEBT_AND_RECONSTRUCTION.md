# Phase 4 — Layout Debt and Production Experience Reconstruction

This is a debt register and planning input. It does not authorize Phase 4 implementation.

Phase 3 produced a coherent v0.3 migration baseline. Phase 4 should work from the real production screens and preserve the contracts recorded in `PHASE_3_PRODUCTION_MIGRATION_BASELINE.md`.

## Required planning pass for every screen

Before implementation, record:

1. Behaviors that must survive.
2. Data and controls that must remain accessible.
3. Legacy layout decisions that can be discarded.
4. The new Best Mates 2.0 information hierarchy.
5. Primary and secondary actions.
6. Fixed and scrolling regions.
7. Safe-area, font-scale and compact-width behavior.

## Workspace

- Do not repeat broad visual polishing before evaluating the complete app as one flow.
- Reassess identity, collection hierarchy, empty-state authorship and project-card composition only if device use shows real friction.
- Preserve all Phase 3B/3B.1 project operations and the compact non-scrolling selection toolbar.

## Settings

- The Phase 3 Settings screen is intentionally conventional and vertically grouped.
- Reconstruct the final information architecture, Visual Theme Gallery, About placement and imported-resource management together.
- Keep app identity/brightness separate from concrete widget-authored theme and time-format values unless a versioned inheritance contract is designed and tested.

## Editor

- The fixed canvas at the top plus vertically scrolling Inspector below remains the protected starting structure.
- Reconsider canvas/tool/Inspector relationships, top-bar density, contextual editing, lower action placement and compact-height behavior as one spatial system.
- Plan state extraction before moving LayoutCanvas. Do not mix coordinate refactoring with layout redesign.
- Validate all drag, resize, rotate, snap, guide, Undo/Redo, Pin, save/reopen and Add to Home behavior after every structural step.

## Pickers and libraries

- Turn shape, font, color and asset libraries into clearer atelier/collection experiences without changing persisted IDs.
- Evaluate search/filter hierarchy, selected-item context, thumbnail scale and compact-width behavior from production use.
- Type Wardrobe can expand only through semantic roles and verified font availability/licensing.
- Celestial picker artwork remains a separate deferred final visual project; Phase 4 layout work must not silently replace its collection.

## Typography

- Expressive typography may serve safe display roles such as Best Mates, Your Worlds, Settings, Create and library identities.
- Actions, values, Inspector controls, data labels, fields, menus and accessibility-critical text remain highly readable.
- Central semantic font roles must precede any screen-specific font adoption.

## Cross-screen debt

- Test large font scale, smallest supported width, status/navigation insets and keyboard-visible layouts.
- Consolidate localization outside `MainActivity.kt` and audit missing translations without changing stored language codes.
- Decide whether Editor state/pickers become route-level packages after a measured coupling review.
- Verify launcher sizing and normalized-square preview behavior separately from UI reconstruction.
- Keep Golden review assets, reference screenshots and unknown-license material outside runtime resources.

