# Repository cleanup audit — 2026-09-07

Scope: tracked repository content was audited before removal. Local folders `1/` and `2/` were excluded and untouched.

## A. Definitely safe to delete — removed

| Item | Evidence |
| --- | --- |
| `app/src/main/assets/solar_icons/iconoir_star.svg` | `SolarIconRenderer` builds its asset path from `SunIconStyle` / `MoonIconStyle`. This filename is the only SVG absent from both enums, and has no literal reference anywhere in the repository. |
| `app/src/main/res/drawable/heart_day.xml`, `heart_night.xml`, `panel_day.xml`, `panel_night.xml` | No Android XML, manifest, Gradle, Kotlin, renderer, or documentation reference. Runtime background rendering is Compose code in `BackgroundRenderer.kt`, not these drawables. |
| `app/src/main/res/drawable/shape_circle.xml`, `shape_cloud.xml`, `shape_diamond.xml`, `shape_heart.xml`, `shape_pill.xml`, `shape_rounded.xml` | No resource reference or dynamic drawable lookup. Current widget-shape rendering uses `WidgetShape` plus Compose drawing code. |

The removed production resources total **2,974 bytes**. They were legacy XML/vector alternatives rather than the active renderer assets.

## B. Probably unused — retained for review

| Item | Why it was retained |
| --- | --- |
| `release-assets/BestMates-v0.1.0-debug.apk` | It is a 31.5 MB committed release binary, while the public GitHub Release is the intended download location. It is not needed to compile the app, but removing a historic tracked release artifact changes repository distribution policy. Keep or remove it only with maintainer approval. |

## C. Definitely required — retained

| Item group | Evidence |
| --- | --- |
| `app/src/main/assets/cities.db` | `CityCatalog.kt` opens it by the literal runtime filename `cities.db`. |
| 92 enumerated files in `app/src/main/assets/solar_icons/` | `Models.kt` maps each selectable sun/moon style to its filename; `SolarIconRenderer.kt` dynamically opens `solar_icons/<enum asset>`. |
| `THIRD_PARTY_NOTICES.md` | Linked from the public README; preserves icon-license notices. |
| `ic_best_mates_launcher.xml` | Referenced by `AndroidManifest.xml` as the app and round icon. |
| `widget_preview_background.xml`, `widget_preview.xml`, `widget_world.xml` | Used by the widget preview/layout path. |
| `res/xml/world_clock_widget_*.xml` and `world_clock_widget_info.xml` | Explicitly declared by widget receivers in `AndroidManifest.xml`. |
| README and localized documentation | Public repository documentation; the root README links each localization. |

## D. Reference/development-only material — keep out of production source

The following local/ignored areas are not production app assets and were deliberately not changed: `1/`, `2/`, `KWGT/`, `.android-sdk/`, `.city-data/`, `.gradle/`, `.gradle-local/`, `.kotlin/`, `.tmp_androidsvg/`, `.tmp_meteocons/`, and `.tooling/`. In particular, `1/` and `2/` are user reference folders and must not be touched without explicit authorization.

## Audit method

References were checked across Kotlin, Gradle, manifest declarations, `res/layout`, `res/xml`, resource identifiers, widget providers, renderers, documentation, and dynamic asset paths. The only `getIdentifier` calls found resolve runtime view IDs (`city_slot_*`, `custom_text_*`, and city sub-view IDs), not drawables or assets.

No tracked PNG, JPG, WebP, font, screenshot, or duplicate SVG files were found. No exact duplicate SVG payloads were found among the selectable icon assets.
