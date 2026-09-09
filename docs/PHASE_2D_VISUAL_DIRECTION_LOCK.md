# Best Mates 2.0 — Phase 2D visual direction lock

Status: **DIRECTION LOCK — Golden Set 2 review required before production adoption**
Date: 2026-09-09
Baseline: `e89d10369a00791cfdb882d96ee00d6dd6c953ff`

Phase 2D continues the frozen **Cute Atelier + Soft Crystal + Orbit Charm** foundation. This is a preserve → normalize → extend program, not a replacement program. Pearl Glass, Milk Crystal, Jelly Crystal, Soft Halo, Quiet Motion, editor geometry, gesture behavior, color-picker behavior, persistence, widget semantics, and reference folders `1/` / `2/` remain outside this work.

## Locked operating rule

Every visual asset is classified before work starts:

| Classification | Meaning |
| --- | --- |
| KEEP | Existing canonical asset remains visibly available without geometry work. |
| KEEP + NORMALIZE | Keep the ID and asset, but later group, label, crop, preview, or align it with the family system. |
| REDRAW | Preserve semantic/stable-ID compatibility while replacing weak canonical geometry with an original Best Mates master. |
| REPLACE | Retain an explicit compatibility path, but give the old concept a better family role or successor. |
| REFERENCE ONLY | Keep current saved-data resolution intact, but do not present it as a current curated default. |
| DEFER | Keep unchanged until a focused, licensed, migration-safe stage. |

## Orbit Charm

Undo and Redo are **approved for now and temporarily locked**. Their separate arrowheads, open curved bodies, mirror relationship, and 16/20/24dp raster behavior are the current family reference. Do not iterate on them without a demonstrated system inconsistency.

Settings, Font, Pin/Fix Position, and Rotate remain Golden candidates. Their present direction is sound: soft adjustment rows; a constructed specimen card rather than raw `Aa`; a pushpin rather than a Lock; and a partial orbital Rotate. They need only family-level comparison during later implementation, not speculative redesign.

## Celestial Atelier architecture

The production library remains available. Future curation groups it into these roles without changing independently persisted Sun/Moon choices:

| Family | Purpose | Current audit decision |
| --- | --- | --- |
| Canonical Celestial | Most legible default Sun and Moon | KEEP + NORMALIZE: simple filled/ring Suns and plain long crescent Moons. |
| Minimal Celestial | Quiet outline pair for smallest sizes | KEEP + NORMALIZE; retain only the clearest low-detail masters. |
| Crystal Halo | Controlled ring/orbit relationship for Soft Crystal presentation | KEEP + NORMALIZE existing ring/crescent-ring ideas; refine only as original Golden geometry. |
| Dream Crescent | One expressive crescent treatment with at most one companion sparkle | REDRAW as original Golden candidates while preserving older starry IDs. |
| Horizon | Sunrise, sunset, and horizon conditions | KEEP + NORMALIZE; horizon lines are semantic, not decoration. |
| Weather Celestial | Cloud, rain, snow, mist, haze, and storm conditions | KEEP + NORMALIZE and present as a separate functional group. |
| Crescent & Star | Optional dedicated cultural/symbolic moon family | DEFER production adoption; do not make it the generic default Moon. |
| Historical / source-pack variants | Existing Lucide, Tabler, Phosphor, Fluent, Iconoir, MDI, and Meteocons choices | REFERENCE ONLY in future curation, while remaining resolvable for saved data. |

The preferred crescent has a long unbroken outside arc, a deliberate inner cutout, rounded terminals, a broad stable counter, and a silhouette that reads before any ornament. A sparkle must sit outside the essential crescent counter. Crescent & Star uses a single balanced star with explicit cultural labeling and dedicated small-size review; it never silently replaces Canonical Celestial.

### Production celestial audit

| Current group | Classification | Direction |
| --- | --- | --- |
| `BM_SUN_SIMPLE_FILL`, `BM_SUN_SIMPLE_ROUND` | KEEP + NORMALIZE | Canonical Sun candidates; retain filled and outline contrast as intentional choices. |
| `BM_SUN_HORIZON_*`, `BM_SUNRISE_ARROW`, `BM_SUNSET_ARROW`, `BM_SUN_MIST` | KEEP + NORMALIZE | Horizon family; standardize naming and preview grouping later. |
| `BM_SUN_CLOUD_*`, `BM_SUN_WAVE_*`, `BM_SUN_CLOUD_RAIN` | KEEP + NORMALIZE | Weather Celestial, never mixed into the default clear-sun shelf. |
| `BM_MOON_PLAIN_*`, `BM_CRESCENT_RING` | KEEP + NORMALIZE | Canonical / Crystal Halo Moon anchors. |
| `BM_MOON_STARS_*`, `BM_MOON_HORIZON_*`, `BM_MOON_SLEEP_*`, `BM_MOON_CLOUD_*` | KEEP + NORMALIZE | Dream, Horizon, and Weather subfamilies; cap new decorative motifs but retain existing IDs. |
| `BM_ISLAMIC_CRESCENT_STAR` | DEFER | Preserve the existing ID and audit provenance; only surface later as a clearly labeled dedicated symbolic variant. |
| Lucide, Tabler, Phosphor, Fluent, Iconoir, MDI, Meteocons entries | REFERENCE ONLY | Existing local assets stay compatible. They are not the Best Mates default visual language and are not deleted in Phase 2D. |

The existing third-party notices record ISC, MIT, and Apache-2.0 sources where applicable. Local Kustom reference screenshots have unknown source/license and are inspiration only. No reference SVG or screenshot is copied into a new Golden asset.

## Shape Atelier architecture

Canonical recognizable forms always remain available. Style variants extend a base; they never replace it.

| Base | Curated variants after approval | Current production decision |
| --- | --- | --- |
| Heart | Canonical Heart, Soft Heart | `HEART`: KEEP + NORMALIZE. Keep the symmetric base; a softened/asymmetric study is separate. |
| Cloud | Canonical Cloud, Calm Cloud | `CLOUD`: REDRAW. Replace current geometry only through a stable-ID-compatible source later. |
| Arch | Canonical Arch, Soft Arch | `PILL`: REDRAW Golden candidate; retain its persisted ID and intrinsic proportion. |
| Stationery | Canonical Ticket, Atelier Ticket, Postcard | `TICKET`: REDRAW; `WAVE`: DEFER as a separate Flag/Paper direction. |
| Studio basics | Mist Card, Soft Square, Capsule, Circle | `ROUNDED`, `SQUIRCLE`, `CAPSULE`, `CIRCLE`: KEEP + NORMALIZE. |
| Other organic/charm forms | Petal, Drop, Four-petal | `PETAL`, `DROP`: KEEP + NORMALIZE. `DIAMOND`: REPLACE in presentation as Four-petal while retaining compatibility. |

Thumbnail artwork may use optical scale and safe padding, but it must render the same canonical geometry and intrinsic aspect ratio used on the editor canvas and widget. It may not stretch an object just to fill a card.

## Golden Set 2 implementation

Golden Set 2 is implemented as a preview-only review set, not a library expansion:

- Orbit: retain the six current candidates; Undo/Redo are locked.
- Celestial: Minimal Sun/Moon, Crystal Halo Sun/Moon, Dream Crescent Sun/Moon, Horizon Sun/Moon, and one dedicated Crescent & Star Moon study.
- Shapes: Canonical Heart, Soft Heart, Canonical Cloud, Calm Cloud, Canonical Arch, Soft Arch, Canonical Ticket, Atelier Ticket, and Postcard.
- Type Wardrobe: preserve the existing three-card presentation; lock taxonomy, multilingual fallback review, and license gate before font acquisition.

## Handoff to Sol after review

Sol may implement approved original vectors, metadata, picker grouping, renderer-backed preview, and production migration only after the Golden Set 2 review passes. It must preserve existing IDs, route legacy entries through a compatibility catalog, retain offline/recolorable rendering, keep user imports, and avoid changes to color-picker behavior, gestures, widget semantics, or TASK-007.

Phase 2D is visually ready to lock only when the Golden Set 2 candidates receive Cloud Light, Dream Light, and Midnight Dark review, the family hierarchy is accepted, and the compatibility catalog is documented. Phase 3 remains blocked until then.
