# Changelog

## v0.4.0 (in development)

Full reconstruction of the design language, editor, widget renderer and persistence.
See `PROJECT_STATUS.md` for details, verification and before/after screenshots.

### Added / Changed

- "Daybook" design language: warm paper and ink, Fraunces and Figtree, new components,
  icon family, launcher icon and splash.
- Workspace gallery of live widget previews with stable ordering and real widget sizes.
- Canvas-first editor: pinch/rotate, edge handles, magnetic guides, contextual inspector,
  font/colour/shape/sun-moon libraries, layers, autosave, one undo step per gesture.
- Shared scene engine so the editor, previews and home-screen widget draw identically.
- 21 bundled open-licensed widget typefaces plus 14 live system styles for time and date.
- City search in any language.

### Fixed

- Project name leaked onto the home-screen widget as a header.
- Pinned widgets never received later edits.
- Rotation of cities and text was ignored on the home screen.
- The pin dialog always offered a generic 4×4 placeholder instead of the design.
- Workspace order reshuffled between launches.

### Data

- Schema 9 with an explicit migration ladder and a one-time raw backup of each older
  project; covered by JVM tests on real v1–v8 payloads.
## v0.2.0

Best Mates 2.0 production migration release.

### Added / Changed

- Introduced the Best Mates 2.0 visual foundation.
- Reconstructed the main Workspace.
- Migrated Settings to the Crystal interface.
- Migrated Editor controls and Inspector.
- Unified city, font, shape, colour and other pickers/dialogs.
- Added the Soft Atelier working canvas.
- Integrated updated Orbit Charm controls.
- Improved drag behavior for rotated objects.
- Improved theme and light/dark presentation.
- Preserved existing projects and widget configuration compatibility.

### Known limitations

- Final Sun/Moon visual redesign is deferred.
- Some UI layouts will receive a larger reconstruction in Phase 4.
- A small number of visual and interaction bugs remain.
- Automated test coverage is still limited.

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
