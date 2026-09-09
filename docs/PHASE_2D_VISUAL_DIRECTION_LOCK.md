# Best Mates 2.0 — Phase 2D visual direction lock

Status: **CLOSED — VISUAL SYSTEM DIRECTION ESTABLISHED / CELESTIAL ARTWORK FINALIZATION DEFERRED**
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

The accepted or provisionally accepted direction is Golden Undo/Redo, a gear for global Settings, sliders for local Adjust surfaces, the current production Pin, the Rotate artwork identified by the user in the Future canvas language, and the general Orbit Charm curve/stroke character. Font remains a candidate study. Minor optical polish may happen later, but the complete Orbit family must not be reopened without a focused scope. Generic Apply buttons do not carry decorative checkmarks.

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

## Phase 2D closure and handoff

Golden Set 3 supersedes Golden Set 2 as the active debug review surface while preserving the earlier sets as history. Its Objects, Celestial, Orbit, Work Mat, Wallpapers, and Sources pages remain available for future study. Candidate IDs remain separate from saved production IDs, and no candidate becomes production artwork merely by appearing in the gallery.

Phase 2D now closes with the visual system direction established. It does **not** claim visual approval of the new Sun/Moon candidates. The deferred **Celestial Atelier Final Pass** owns final Sun/Moon geometry, reference-quality crescents, star/jewel companion work, atmospheric variants, Crescent & Star, target-size optical correction, picker population, license/source review, and explicit promotion decisions.

Future object architecture is **Shape × Variant × Material × Appearance**. Material is a rendering choice and must not be embedded in a persistent Shape ID. Existing transparent components remain supported. The current production Heart remains the canonical Heart; other Golden Set 3 objects remain studies until individually accepted.

The Soft Atelier Work Mat is accepted as the basis for later Editor visual migration. That migration must preserve the current geometry engine, intrinsic proportions, drag, resize, rotate, snap, guides, fixed canvas plus scrolling inspector, and widget geometry behavior. TASK-007 remains deferred.

Phase 3 is the next implementation phase. Its work starts only under a separate authorized scope and must follow the Manual Device Review Policy in `PROJECT_HANDOFF.md`.
