# Changelog

## 0.3.0 — Best Mates 2.0 production migration baseline

This milestone moves the production app onto the shared Cute Atelier, Soft Crystal and Orbit foundation while retaining existing projects, widget data and editor behavior.

### Included

- Reconstructed production Workspace with compact project cards and Orbit selection actions.
- Extracted production Settings with Cloud, Dream, Midnight and Mono identity choices, app brightness, language, resource import and About/version information.
- Migrated production Editor chrome, inspector controls and Soft Atelier Work Mat colors without changing the canvas coordinate engine.
- Unified city, shape, font, color, palette and Sun/Moon picker/dialog presentation.
- Preserved the physically verified TASK-007 rotation-after-drag fix.
- Kept saved project IDs, WidgetConfig JSON, component IDs, city/timezone behavior, Add to Home and widget rendering contracts compatible.

### Deferred

- Phase 4 production experience and whole-app layout reconstruction.
- Celestial Atelier Final Pass and any wholesale Sun/Moon artwork replacement.
- Complete language packs, imported-resource registry/resolution, weather and automated UI tests.

### Release status

The source and debug APK are prepared for manual v0.3 smoke testing. No remote tag or GitHub Release is created by this change.

## 0.1.0 — Public preview

First public testing release of the standalone Android editor and widget.

### Included

- Local project workspace with create, copy, paste, rename and delete flows.
- Canvas editor for backgrounds, city blocks and custom text.
- Per-city IANA time zones, time/date display and local solar day/night state.
- Bundled global city database with continent, country and city selection.
- Multiple declared widget sizes from 1×1 to 4×6.
- Day/night palette controls, shapes, font choices and recolourable solar symbols.
- Cloud, Dream, Midnight and Mono app themes, plus an in-progress language system.

### Known limitations

- Some translated labels still fall back to English or Chinese outside the two most complete languages.
- Editor snapping, guides, rotation and resize interactions need further polish.
- Shape proportions and launcher sizing differ on some Android launchers.
- The icon/font libraries are evolving and are not yet final.
- Weather, export/import, cloud sync, iOS support and automated UI tests are not included.

## Release policy

Each public preview receives a GitHub Release with its source tag, changelog and directly installable testing APK. Earlier internal builds are intentionally not published because they were not verified for public testing.

## v0.1.0 APK verification

`BestMates-v0.1.0-debug.apk` SHA-256:

`E170865FA82CAE21E57E311B45519C4BD70C55E9033D8BB64F61808867075D68`
