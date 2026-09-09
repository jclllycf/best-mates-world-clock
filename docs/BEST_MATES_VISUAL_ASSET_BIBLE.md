# Best Mates 2.0 — Visual Asset Bible

Status: **PHASE 2D-0 AUTHORITATIVE SPECIFICATION**

Date: 2026-09-09

Foundation baseline: `810ada4518be5fef0d82303d91c9e8166278c072`

Applies to: Phase 2D Orbit Charm, Shape Atelier, Celestial Atelier, Type Wardrobe, asset previews, and asset-picker presentation.

This document is the source of truth for Phase 2D visual assets. [BEST_MATES_UI_2_SPEC.md](BEST_MATES_UI_2_SPEC.md) remains authoritative for the frozen Phase 2C materials, tokens, interaction states, motion, accessibility, and app-level composition. If the two documents appear to conflict, preserve the Phase 2C foundation and use this Bible only for the visual objects and picker language that live inside it.

## 1. Product identity

Best Mates combines **Cute Atelier + Soft Crystal Atelier + Cozy Orbit**. Its objects should feel like **soft collectible objects for a personal creative atelier**: warm, personal, crafted, slightly dreamy, polished, cute without becoming childish, and expressive without becoming noisy.

The following systems are frozen and are not redesigned in Phase 2D:

- Pearl Glass for compact controls.
- Milk Crystal for cards, shelves, trays, and preview wells.
- Jelly Crystal for a primary Apply or Done action.
- Soft Halo as optional secondary selected emphasis.
- Quiet Motion and its existing press, release, and state timing.

Reject work that reads as generic Android, stock Material, a mixture of unrelated icon packs, clip art, emoji, presentation-software primitives, overdecorated kawaii, or hard industrial geometry.

## 2. Repository constraints

The Phase 2D implementation must begin from the current repository, not from mockups alone.

| Existing system | Current truth | Phase 2D constraint |
| --- | --- | --- |
| UI foundation | `ui/theme/*`, `ui/icons/*`, and the isolated Design Lab under `ui/preview/*` | Reuse the frozen Phase 2C materials and state hierarchy; do not invent asset-specific glass recipes. |
| Orbit commands | `OrbitIcons.kt` uses a 24×24 vector family with shared metrics | Current drawings are provisional. Replace only inside an approved Orbit phase and keep command semantics stable. |
| Shapes | 12 `WidgetShape` enum entries; names are persisted; `designAspect` supplies the natural initial proportion | Preserve persisted names or provide aliases/migration. This Bible does not change canvas transforms. |
| Shape preview | `ShapePreviewIcon` renders through `BackgroundRenderer` | Keep one canonical geometry source for picker and output wherever technically practical. |
| Celestial assets | 45 Sun and 47 Moon enum entries point to local SVGs; renderer crops alpha bounds, optically fits, recolors, and falls back locally | Keep built-ins offline and recolorable. Preserve enum identifiers or migrate safely. |
| Fonts | Persisted string IDs resolve mainly to Android system family aliases; no bundled font files are present | Curate later instead of multiplying aliases. Existing IDs and fallback behavior remain compatible. |
| Imports | Settings copies font/icon files to app-private storage and records a display name; current imported files are not a complete picker/persistence library | A later import integration must preserve existing local files and records and introduce stable local identities deliberately. |

Asset authoring and picker previews preserve the canonical intrinsic proportion and must not introduce automatic stretching. User-authored layer width/height and current editor transform behavior are outside this document and remain unchanged.

## 3. Common visual DNA

| Property | Canonical rule | Reject when |
| --- | --- | --- |
| Curve character | Continuous, calm, slightly generous curves with intentional Bézier tangent continuity | A curve kinks, develops accidental flat spots, looks polygonal, or balloons like candy |
| Geometric softness | Soften transitions through proportion and curvature; keep a readable structural skeleton | Softening erases the symbol or makes it wet/plastic |
| Stroke rhythm | Keep apparent weight even through curves, joins, and diagonals; optically compensate tight bends | A bend clots, a diagonal looks heavier, or small details disappear |
| Terminals | Round and deliberate, with enough clearance to remain separate at the smallest approved size | Ends collide, taper accidentally, or resemble hooks |
| Corners | Prefer round joins and smooth shoulder transitions; paper notches may be concave but never sharp | Corners read as mechanical spikes |
| Negative space | Treat internal space as part of the asset. Preserve clear counters and separation between primary and secondary forms | Meaning depends on a hairline gap or crowded detail |
| Optical centering | Center perceived mass, allowing at most a small correction from the mathematical center | The object appears to sag, lean, or float off-center in its tile |
| Visual weight | Match siblings by perceived mass at target size, not by identical path area | Filled and outlined siblings look like different product families |
| Symmetry | Use symmetry for structural symbols; apply optical correction where perfect geometry looks stiff | Mechanical symmetry overrides visual balance |
| Organic asymmetry | Mild asymmetry is allowed in clouds, petals, blobs, and dreamy motifs | The silhouette looks accidental or lopsided |
| Decoration | Utility assets use none. Expressive assets may use one meaningful companion motif | A dot, heart, bead, or sparkle exists only to make the asset “cute” |
| Fill and outline | Each family declares a coherent treatment. Do not mix filled, duotone, and outline logic inside one set without a semantic reason | Style changes arbitrarily between siblings |

### Small-size and preview rules

- Judge the rasterized result at the real target size, not only the enlarged vector.
- Preserve at least one clearly readable silhouette and one dominant semantic cue.
- Remove or merge details that cannot survive the smallest target; do not make the base stroke thinner to save them.
- Use one consistent live area per family, then apply small optical offsets or overshoot when the perceived mass requires it.
- The preview must show the actual canonical artwork. Scaling, tinting, and the preview surface may change; geometry must not be substituted.
- Selected presentation follows: selected surface fill → controlled edge → one semantic check or dot → optional restrained Soft Halo. Never show dot and check for the same state.
- Artwork must remain clear over both Light and Dark Milk Crystal surfaces. Glass effects belong to the host surface, not inside every asset.

### Fill and outline ownership

- Orbit Charm is outline-first. A solid area is allowed only when it is essential to the command metaphor and remains subordinate to the common stroke rhythm.
- Shape Atelier masters are closed, fillable silhouettes with an optional renderer-controlled border; internal decoration is exceptional.
- Each Celestial pair declares outline, fill, or a controlled combination and uses that treatment consistently across its Sun and Moon.
- Type Wardrobe specimens render the real font; do not simulate personality with outlines, shadows, or decorative effects in the picker.

## 4. Curve language

Use as few control points as the silhouette needs. Adjacent Bézier segments should share a tangent unless a deliberate cusp communicates the object. A smooth curve may tighten gradually, but must not change curvature suddenly. Avoid long near-straight sections between visibly curved shoulders.

Organic shapes may shift lobe size, axis, or shoulder height slightly to feel crafted. Structural icons remain more regular. In both cases, review optical balance after rasterization; mathematical symmetry alone is not approval evidence.

## 5. Orbit Charm 2.0

### 5.1 Base construction

| Attribute | Rule |
| --- | --- |
| Viewport | 24 × 24 |
| Primary render size | Approximately 20dp |
| Additional checks | 16dp inline and 24dp prominent |
| Visual stroke | Approximately 1.8 viewport units; compensate optically rather than mixing arbitrary stroke widths |
| Caps and joins | Round |
| Live area | Normally within the existing 3–21 unit box; up to 0.5u optical overshoot when justified |
| Target | UI supplies an approximately 48dp interaction target independently from artwork size |
| Character | Lightweight, soft, precise, calm, recognizable, slightly crafted |

Cute character comes from proportion, curve, spacing, and rhythm. It does not come from attaching ornament to every command.

### 5.2 Decoration policy

| Class | Examples | Rule |
| --- | --- | --- |
| Utility | Back, Settings, Undo, Redo, Rename, Copy, Paste, Cut, Pin, Delete, Resize, Snap, Guides, Search, Close, Confirm | No decorative dots, hearts, sparkles, petals, or orbit beads |
| Expressive/creative | Create, Theme, Color, Sun, Moon | At most one restrained secondary motif, and only when it improves meaning |

A settings hub, scissors loop, location center, or resize corner is semantic geometry rather than decoration. A freestanding bead with no semantic role is decoration and must be removed.

### 5.3 Canonical command rules

**Check / Confirm**

- Use one canonical semantic check family with no decorative dot.
- Round endpoints and soften the central joint.
- Keep an open, calm angle; avoid a harsh narrow V.
- Balance the short ascending entry and longer exit optically.
- Verify independent legibility at 16dp, 20dp, and 24dp.

**Undo / Redo**

- Build a partial circular motion body with visible interior negative space.
- Keep the arrowhead visibly separate from the curved body at 16dp.
- Join motion and direction through flow, without a blob, kink, hook, or fused head.
- Author one canonical path logic and mirror it optically for the sibling; correct the mirror only when raster balance requires it.

**Font**

- Do not use literal “Aa” as a requirement.
- Choose one intentional typography metaphor: a stylized specimen, a dominant letterform with baseline, a miniature type card, or another original concept.
- It must read as font/type selection and feel native beside Settings, Pin, and Rotate, rather than like text pasted into the icon slot.

**Pin**

- Meaning is **Fix Position / Pin Position**.
- Communicate a point of anchoring and a clear stem or placement relationship.
- Avoid a closed shackle, containment box, or other full-lock metaphor.
- Phase 2D does not require a production Lock command.

**Rotate / Resize**

- Treat them as siblings in stroke, live area, direction, and terminal character.
- Rotate uses a smooth partial orbit with one clear directional terminal.
- Resize uses a clean diagonal expansion or corner relationship with separated arrowheads.
- Both remain visually quiet enough for future canvas controls.

### 5.4 Orbit approval checklist

- Meaning is recognizable without a label in its expected context.
- Silhouette and counters survive 16/20/24dp checks.
- Stroke mass matches adjacent Golden Set icons.
- No utility ornament or fused geometry is visible.
- Icon is optically centered within the common live area.
- Light/Dark tinting changes color only, not canonical geometry.

## 6. Shape Atelier

Component shapes are **collectible visual surfaces**, not a catalog of generic primitives.

| Family | Baseline members | Character |
| --- | --- | --- |
| Studio Cards | Soft Square, Mist Card, Soft Arch, Capsule, Postcard/Label | Quiet, useful, softly architectural, clean |
| Organic Forms | Cloud, Pebble, Petal, Drop, Soft Blob | Flowing, gentle, natural, balanced |
| Charm Forms | Heart, Four-petal, Quatrefoil/Clover, Bloom | Collectible, personal, lightly decorative |
| Paper/Stationery | Ticket, Tag, Flag, Note | Crafted paper objects with softened corners and notches |

The final library may omit a redundant or weak current silhouette, but a persisted identifier must remain resolvable through improved artwork, compatibility aliasing, or an explicit migration.

Current IDs remain compatibility anchors during later curation:

| Persisted ID | Intended Phase 2D direction |
| --- | --- |
| `ROUNDED` | Mist Card |
| `SQUIRCLE` | Soft Square |
| `PILL` | Soft Arch; the ID remains even if its display name changes |
| `CAPSULE` | Capsule |
| `CIRCLE` | Retain as a compatible basic form; final family placement is a 2D-2 decision |
| `CLOUD`, `PETAL`, `DROP` | Organic Forms |
| `DIAMOND` | Four-petal/Quatrefoil direction; avoid a literal diamond reading |
| `HEART` | Charm Forms |
| `TICKET` | Paper/Stationery Ticket |
| `WAVE` | Paper/Stationery Flag direction |

### 6.1 Shape quality standards

| Shape | Required character | Reject when |
| --- | --- | --- |
| Heart | Mature soft lobes, controlled center notch, elegant taper, stable lower point | Emoji-like, clip-art generic, pinched, or bulbous |
| Cloud | Controlled lobe rhythm, smooth lower contour, balanced mass | Repetitive cartoon bumps or an unrelated flat base |
| Petal/Drop | Clean shoulder-to-tip tension and elegant taper | Needle point, collapsed shoulder, or swollen balloon mass |
| Soft Arch | One smooth crown, clean tangent into sides, stationery/architectural softness | Dome kinks, abrupt vertical joins, or a tombstone feel |
| Ticket/Tag | Soft corners/notches and a collectible paper-object silhouette | Harsh perforations, mechanical cutouts, or label-template blandness |

Every accepted shape must work at picker-thumbnail and editor-canvas sizes. The dominant silhouette must remain clear with a fill, border, user color, and the current renderer’s supported depth effects.

### 6.2 Canonical shape preview

- Use the same canonical geometry renderer as the Editor/widget path wherever technically practical.
- Preserve the authored intrinsic proportion in the picker and initial fit. Do not stretch the thumbnail.
- Fit to the largest appropriate size inside a consistent safe area, with neither tiny floating artwork nor contact with tile edges.
- Center perceived mass; allow documented compensation for unusually tall, wide, or asymmetric forms.
- Make silhouette primary and name secondary.
- Present the shape in a Milk Crystal preview well. Selection uses selected fill, refined edge, one semantic indicator, and optional Soft Halo.
- Compare preview and actual added result before approval. A preview-only tracing is not an acceptable final geometry source.

## 7. Celestial Atelier

Sun and Moon artwork is user widget content, so it may be more expressive than toolbar commands while sharing the same calm curves, controlled detail, negative space, and optical balance. Design and review Sun/Moon as paired families.

Pairing is a visual catalog and quality rule. It does not automatically couple the two existing independent picker selections or change saved behavior. A later picker task may group matched counterparts while still preserving every saved Sun and Moon ID.

| Family | Sun | Moon | Ornament |
| --- | --- | --- | --- |
| Minimal Celestial | Quiet disk with balanced restrained rays | Graceful open crescent | None or essential geometry only |
| Crystal Halo | Controlled disk/ring/halo relationship | Matching crescent with halo or orbit relationship | Halo/orbit is the single secondary structure |
| Dream Crescent | Softer, more personal ray or disk rhythm | Long crescent with matching mass | At most one small sparkle outside the essential counter |
| Horizon | Half sun or crescent held by semantic horizon bands | Matched crescent held by the same horizon language | Horizon bands only; no charm motif |
| Crescent & Star | Not applicable; this is a dedicated symbolic Moon study | Long crescent with one balanced star | Explicit cultural/symbolic label; never the generic default Moon |

### 7.1 Sun geometry

- Prefer 8 rays for the baseline masters. Another count requires a family-level reason and must not create noise at 20dp.
- Keep ray intervals regular; use one deliberate rhythm rather than alternating arbitrary lengths.
- Ray lengths should differ only as part of a declared pattern. Opposing rays remain optically balanced.
- Start Golden Set exploration near a central disk diameter of 36–44% of the live area and a total ray envelope of 78–86%; adjust by raster review.
- Preserve clear separation between disk and rays for outline masters. Filled masters must keep a calm overall mass.
- Optical center is based on the complete ray envelope, not the disk alone.

### 7.2 Moon geometry

- Keep crescent thickness substantial enough to survive 20dp, generally about 24–34% of the outer diameter at its broadest point.
- Outer and inner curves should flow as a deliberate pair; avoid a pasted circular cutout that produces uneven tension.
- Tips may narrow but remain softened and separated. No needle tips.
- Balance the open negative space against the crescent mass so the symbol does not appear to fall sideways.
- A companion motif must remain outside the crescent’s essential counter and must not become a selection-like dot.

### 7.3 Celestial technical rules

- Verify every master at approximately 20dp, 28dp, 40dp, and representative widget sizes.
- Keep canonical artwork recolorable through the existing renderer; do not depend on fixed brand colors.
- Use the same canonical geometry in Light and Dark. Preview foreground, surface, and selection colors adapt to the theme; user widget colors remain user-controlled.
- Built-ins are local and deterministic. Rendering failure must retain a local fallback.

## 8. External references and licensing

External study may cover proportion, curvature, stroke rhythm, category conventions, celestial symbols, and open-source design systems. It must not become proprietary tracing, reproduction of paid packs, copying from closed commercial apps, or unlicensed 1:1 imitation.

Any asset directly included or adapted must add a provenance record before merge:

| Field | Required record |
| --- | --- |
| Source | Stable project/repository and asset reference |
| Author/project | Copyright holder or upstream project |
| License | Exact compatible license and required notice text |
| Modification | Geometry, naming, cleanup, or format changes |
| Usage | Where and how Best Mates ships the asset |

If an external work only informs research, record it as inspiration when useful and create original Best Mates geometry. Do not claim upstream artwork as original. License verification is a gate, not a cleanup step after mass production.

## 9. Type Wardrobe

Type Wardrobe is a curated set of distinct typographic moods for world-clock widgets. It is not a count target. The eventual built-in range is approximately 10–16 excellent families, subject to APK-size, glyph coverage, redistribution rights, and licensing review. User font import remains the route for broader customization.

| Category | Selection intent |
| --- | --- |
| Rounded Sans | Warm, compact, highly readable default |
| Friendly Geometric Sans | Clean construction with humane rhythm |
| Clean Neutral Sans | Quiet information hierarchy and dependable numerals |
| Humanist Sans | Open forms and readable city names |
| Elegant Serif | Refined time/date display without fragile hairlines |
| Soft Editorial Serif | Warm publication character at moderate sizes |
| Friendly Display | Personal accent for short city names |
| Refined Display | Distinctive but controlled headline mood |
| Mono/Technical | Clear tabular rhythm and clock precision |
| Readable Handwritten | Human warmth without sacrificing digits or punctuation |

Not every category needs multiple families. Two fonts that render materially the same mood or resolve to the same system face do not count as diversity.

### 9.1 Font evaluation matrix

Evaluate each candidate with these exact specimen types:

```text
Tokyo
18:40
Tue, Sep 8
AM  PM
```

Check numeral distinction, colon placement, punctuation, uppercase/lowercase rhythm, city-name legibility, small-size behavior, weights actually shipped, multilingual glyph coverage, and fallback transitions. A font does not qualify solely because one decorative word looks attractive. Redistribution license and bundled byte cost must be known before adoption.

### 9.2 Type Wardrobe presentation

- Make the actual specimen the primary content: city, time, and date at useful sizes.
- Show the family name secondarily in the standard Best Mates UI typeface, never in the candidate face alone.
- Use a Milk Crystal shelf/card with clear spacing and a semantic selected state.
- Avoid one tiny date line repeated across every tile.
- A missing glyph or fallback face must be visible during review, not hidden by a Latin-only showcase.

## 10. Unified asset-picker language

All asset pickers are shelves in the same creative atelier.

| Layer | Material and role |
| --- | --- |
| Tray/shelf | Milk Crystal container that groups one decision context |
| Preview card/well | Milk Crystal; artwork remains visually dominant |
| Filter/category/compact action | Pearl Glass |
| Selected item | Selected fill + refined edge + one semantic check or dot + optional restrained Soft Halo |
| Primary Apply/Done | Jelly Crystal only when the workflow requires explicit commit |

Use this information order:

1. Title.
2. Category/filter when it materially reduces search.
3. Visual assets.
4. Compact metadata.
5. Apply/Done only when selection is not already committed by the item action.

Preview priority is fixed: Shape → silhouette; Sun/Moon → celestial artwork; Font → specimen; Icon → actual icon. Labels and metadata remain secondary. Avoid generic AlertDialog styling, huge empty modals, plain text-only asset lists, and borders around every nested element.

The host controls selection semantics and touch targets. An artwork dot or sparkle must never be mistaken for the selected indicator. Keep artwork readable above the material treatment; do not stack glow, refraction, and Halo until the asset loses contrast.

## 11. Light, Dark, and offline behavior

- Keep one canonical geometry across Light and Dark unless an asset has a documented technical need for a separate raster.
- Adapt picker foreground, preview surface, edge, and selected treatment with foundation tokens.
- Dark presentation uses the frozen plum/aubergine surfaces and restrained internal reflection; do not add white glass glare.
- Widget artwork uses the user’s saved colors. App theme must not rewrite those colors.
- All production built-ins ship locally. No remote icon URL, web font, cloud API, or runtime download may be required to render a saved project.
- External web research is permitted during design work; the resulting production system remains deterministic offline.

## 12. Saved-data compatibility

Current enum names and font strings are persisted data. Treat them as stable IDs, not display labels.

1. Prefer `existing stable ID → improved artwork` when meaning remains the same.
2. Keep display name, category, asset filename, and stable ID separate in future catalogs.
3. If meaning or ID must change, add a compatibility alias or explicit safe migration before removal.
4. Preserve unknown/legacy values long enough to resolve them safely; do not silently replace a user’s choice merely because the new catalog is smaller.
5. Verify reopen, copy, widget-instance loading, and editor/widget parity for every migrated built-in.
6. Preserve shape choices, Sun/Moon choices, font strings, and app-private imported resources.
7. An import-library redesign must inventory the existing `filesDir/imports` content and preference records before assigning new stable local IDs.

A curated picker may stop offering a weak legacy built-in to new projects only after the old ID still renders, remains inspectable when loaded, and has a documented compatibility path. Hiding an entry is never permission to delete its renderer or silently substitute another asset.

No Phase 2D asset task may change the JSON schema, `ConfigStore`, `ProjectStore`, or widget provider behavior unless a separately approved migration scope explicitly requires it.

## 13. Phase 2D-0.5 Golden Set gate

The Golden Set establishes the quality bar before library expansion. It is accepted as a coherent group; a weak master must be revised before its style is multiplied.

| Set | Required masters |
| --- | --- |
| Golden Orbit | Settings, Undo, Redo, Font, Pin, Rotate |
| Golden Celestial | Four paired families: 4 Sun + 4 Moon, covering Minimal Celestial, Crystal Halo, Dream Crescent, and Horizon; plus one dedicated Crescent & Star Moon study |
| Golden Shape | Nine candidates covering canonical Heart/Cloud/Arch/Ticket plus refined variants and Postcard |
| Type Wardrobe | Picker/presentation master using existing fonts only; no new font bundle |

The 2D-0.5 review must show:

- Orbit raster checks at 16dp, 20dp, and 24dp, with sibling weight and live-area overlays.
- Celestial checks at 20dp, 28dp, 40dp, and representative widget scale, with all three Sun/Moon pairs together.
- Shape silhouettes at canonical picker size and representative canvas size, preserving intrinsic preview proportion.
- Type presentation with `Tokyo`, `18:40`, `Tue, Sep 8`, and `AM  PM` using current fonts.
- Cloud Light, Dream Light, and Midnight Dark representative presentation, plus a quick pass across all eight identity × brightness combinations.
- Provenance/license status for every externally derived asset.
- No production-screen adoption, persistence change, or mass asset expansion hidden in the Golden Set diff.

Approval questions are concrete: Does each object belong to the same studio? Does meaning survive at target size? Is the family coherent without decoration carrying it? Do preview and output geometry agree? If any answer is no, stop and revise the master.

## 14. Phase 2D sequence and boundaries

| Phase | Deliverable |
| --- | --- |
| 2D-0 | This Visual Asset Bible |
| 2D-0.5 | Golden Visual Asset Set |
| 2D-1 | Full Orbit Charm 2.0 family |
| 2D-2 | Shape Atelier |
| 2D-3 | Celestial Atelier |
| 2D-4 | Type Wardrobe |
| 2D-5 | Unified Asset Picker Language |

The production color picker is completely outside Phase 2D behavior. Do not redesign its color wheel, hue/SV behavior, ARGB conversion, opacity, gestures, persistence, or selection logic. A later production-UI migration may apply the already frozen surrounding chrome only.

Phase 2D does not authorize changes to production Workspace, Settings, Editor, `LayoutCanvas`, gestures, drag, rotation, resize, TASK-007, persistence contracts, `ConfigStore`, `ProjectStore`, widget provider behavior, or reference folders `1/` and `2/`.

## 15. Per-asset acceptance checklist

- [ ] Fits the “soft collectible object” identity and its declared family.
- [ ] Uses calm continuous curves and intentional negative space.
- [ ] Meets its target-size raster checks without collision or ambiguous detail.
- [ ] Matches sibling optical weight and centering.
- [ ] Obeys utility/expressive decoration limits.
- [ ] Uses canonical geometry in preview and output.
- [ ] Remains readable on representative Light and Dark surfaces.
- [ ] Uses only frozen Pearl/Milk/Jelly/Halo presentation roles.
- [ ] Ships locally and deterministically if built in.
- [ ] Has verified provenance and license when directly included or adapted.
- [ ] Preserves stable IDs or includes an approved alias/migration.
- [ ] Does not change production interaction, rendering contracts, or color-picker behavior outside its approved phase.

## 16. Phase 2D Golden Set implementation record

Phase 2D-0.5 established the original candidate catalog. The Visual Direction Lock expands it into Golden Set 2 under `ui/preview`. Neither stage replaces the provisional production-facing `OrbitIcons`, existing `WidgetShape` values, Sun/Moon enums or SVG files, font strings, renderer paths, or picker behavior. Every candidate ID begins with `golden.` and exists only in the Design Lab; these labels are review handles, not saved-data identifiers.

### Golden definitions

| Area | Candidate masters | Defining decision |
| --- | --- | --- |
| Orbit Charm | Settings, Undo, Redo, Font, Pin, Rotate | Original 24×24 vectors with 1.8u rounded strokes; recognition comes from soft proportion, clear negative space, and calm curve tension rather than appended ornaments. |
| Celestial Atelier | Minimal Celestial, Crystal Halo, Dream Crescent, and Horizon Sun/Moon pairs; Crescent & Star Moon | Four paired, tintable families plus one explicitly symbolic Moon study: quiet universal outline, controlled shared halo language, one restrained shared sparkle, and semantic horizon bands. |
| Shape Atelier | Canonical + Soft Heart, Canonical + Calm Cloud, Canonical + Soft Arch, Canonical + Atelier Ticket, Postcard | Nine original fillable paths with explicit intrinsic ratios. Canonical recognizable forms remain separate from curated variants. |
| Type Wardrobe | Clean Sans, Editorial Serif, Friendly Display | Three Milk Crystal presentation cards using existing device families only. The specimen is `Tokyo`, `18:40`, `Tue, Sep 8`, and `AM  PM`; the family label stays in normal UI typography. |

Settings uses three soft adjustment rows instead of an industrial gear. Undo and Redo are manually mirrored relatives whose arrowheads are separate from their open motion bodies. Font is a miniature type-specimen card with one constructed `A` and a curved specimen baseline, avoiding the repeatedly awkward raw `Aa` pairing. Pin depicts the real Fix Position semantics as a balanced pushpin and does not introduce Lock. Rotate uses a partial orbital arc and a separate directional terminal instead of a full-circle stock arrow.

Orbit appears at actual 16dp, 20dp, and 24dp artwork sizes in the Gallery while future controls retain their independent 48dp target. Four Celestial pairs and the separate Crescent & Star study appear at 20dp, 28dp, and 40dp; the 40dp rendering is the representative widget-use check. Nine Shape cards show a 42dp-high picker thumbnail and an 88dp-high larger canonical preview through the same aspect-preserving path fitter. The Gallery reuses the frozen Milk Crystal shelves, Pearl Glass compact labels, semantic selection check, and optional secondary Soft Halo in all eight local identity × brightness combinations.

The lightweight debug integrity check enumerates 6 Orbit vectors, 4 Sun vectors, 5 Moon vectors, 9 Shape paths, and 3 Type cards: 27 candidates total. It rejects incorrect category counts, duplicate candidate IDs, invalid vector viewports/default sizes, path-construction failures, empty bounds, and paths leaving the supplied canvas. Gallery composition renders the enumerated candidates directly, so there is no parallel resource list to drift and no remote dependency to resolve.

### References, licensing, and provenance

The proportion and category study used the official [Tabler icon design guide](https://tabler.io/icons/icon-design-guide) and MIT-licensed [Tabler Icons](https://github.com/tabler/tabler-icons), the ISC-licensed [Lucide](https://lucide.dev/) system, the MIT-licensed multi-weight [Phosphor core](https://github.com/phosphor-icons/core), and the MIT-licensed paired-weather [Meteocons](https://github.com/basmilius/meteocons). These sources informed general questions such as 24×24 proportion, rounded stroke rhythm, negative space, family consistency, and paired celestial weight. No source path, glyph outline, SVG, font, or binary is copied, adapted, bundled, or redistributed. All Golden vector and shape geometry is original Kotlin code, so Phase 2D-0.5 adds no third-party asset notice or runtime license dependency.

Rejected concepts are recorded so they are not multiplied later: a literal cramped `Aa`; an industrial gear; full-circle or hook-like Rotate; fused Undo/Redo heads; a target-like four-mark Halo Sun; an emoji-symmetric Heart; a high-bump cartoon Cloud; harsh Ticket notches; unrelated Sun/Moon drawings; and decorative dots or sparkles on ordinary utility commands.

### Freeze and next gate

The Golden Set is implemented for visual review and remains a candidate set. Production stable IDs and output stay unchanged; saved projects, imported resources, color selection, widget rendering, canvas transforms, and TASK-007 are unaffected. Phase 2D-1, family expansion, external font bundling, and production picker migration require a separate approval after this gate.
