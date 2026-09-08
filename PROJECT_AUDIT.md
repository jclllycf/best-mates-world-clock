# PROJECT AUDIT — Best Mates Android Repository

**Audit date:** 2026-09-08  
**Auditor:** Antigravity (Claude Opus 4.6)  
**Branch:** `main` at `49c0853`  
**Checkpoint:** `bacd712` (recovery branch `maintenance/pre-phase1-cleanup`)  
**Status:** READ-ONLY AUDIT — no source files modified

---

## Recovery Point (from Step 0)

| Field | Value |
|---|---|
| **Original branch** | `main` |
| **Checkpoint commit hash** | `bacd712` (`bacd712113afbd21959eb6110ef29fe93bc11bbf`) |
| **Checkpoint commit message** | `checkpoint: GPT-6 Phase 1 partial state before repository audit` |
| **Cleanup branch** | `maintenance/pre-phase1-cleanup` |
| **Restore command** | `git checkout maintenance/pre-phase1-cleanup` |

---

## 1. Executive Summary

The Best Mates repository is a compact, single-module Android project (12 Kotlin files, 173 tracked files, ~4,900 lines of Kotlin). It is **functionally healthy** — it builds, the dependency stack is modern (AGP 8.8.0, Kotlin 2.0.21, Compose BOM 2024.12.01), and there are no stale build artifacts accidentally tracked beyond one deliberate exception.

**Key concerns:**

| Area | Severity | Summary |
|---|---|---|
| **Monolithic MainActivity.kt** | 🟡 Architectural debt | 1,975 lines (~40% of all Kotlin), containing theme, i18n, settings, editor, canvas, gesture handling, dialogs, color picker, and widget pinning. |
| **Tracked debug APK** | 🟠 Repo bloat | `release-assets/BestMates-v0.1.0-debug.apk` (30 MB) is committed, bloating the .git directory. |
| **cities.db** | 🟡 Binary asset | 41.7 MB SQLite database tracked in git — necessary but dominates repo size. |
| **Dead code in MainActivity.kt** | 🟢 Easy cleanup | 3 dead private functions, 30 lines of commented-out code, 6 unused imports. |
| **Magic values** | 🟡 Technical debt | 57+ `Color(...)` literals, 131+ raw `.dp` values, duplicated constants across files. |
| **Stale documentation** | 🟢 Easy fix | `PROJECT_HANDOFF.md` has outdated rotation info superseded by code changes. |
| **Missing .gitignore entries** | 🟢 Easy fix | `/2/` directory and `.agents/` not ignored (leading to tracked reference images). |
| **No tests** | 🟡 Gap | No test directories, no test dependencies, no CI/CD. |

**Estimated totals:**
- Cleanup candidates: **30+**
- SAFE_DELETE items: **7**
- SAFE_CLEANUP items: **13**
- REVIEW_REQUIRED items: **8**
- PROTECTED_PHASE1 items: **10+**
- Architectural issues: **5** (monolith, no tests, no CI, inline i18n, constant divergence)

---

## 2. Repository Structure Assessment

### Tracked files: 173 files, ~72.6 MB

| Category | Files | Size |
|---|---|---|
| Kotlin source (`.kt`) | 12 | ~160 KB |
| Solar icon SVGs | 92 | ~78 KB |
| Widget XML configs (`res/xml/`) | 25 | ~13 KB |
| Drawables + layouts + values | 7 | ~10 KB |
| cities.db | 1 | 41.7 MB |
| Debug APK (release-assets) | 1 | 30.0 MB |
| Documentation (`.md`) | 14 | ~100 KB |
| Build scripts + Gradle wrapper | 7 | ~56 KB |
| Miscellaneous (LICENSE, `.gitignore`, etc.) | 14 | ~25 KB |

### Untracked local directories (gitignored)

| Directory | Size | Files | Purpose |
|---|---|---|---|
| `.tooling/` | ~1 GB | many | Offline JDK + Gradle + Android tools |
| `.city-data/` | ~52 MB | 3 | Raw GeoNames source data |
| `.gradle/`, `.gradle-local/`, `.kotlin/` | varies | many | Build caches (normal) |
| `.android-sdk/` | varies | many | Local Android SDK |
| `1/` | 1.8 MB | 5 | Reference screenshots (protected) |
| `KWGT/` | 4.1 MB | 7 | KWGT reference captures |
| `.tmp_androidsvg/` | 217 KB | 1 | Build artifact (classes.jar) |
| `.tmp_meteocons/` | 9.6 MB | 2,081 | Extracted npm package (stale) |

### Untracked root-level files (gitignored)

21 files totaling ~800 KB — all are **adb `uiautomator dump` XML captures** and screencap PNGs from past testing sessions. Already gitignored. Clutter on disk only.

---

## 3. Code Health Assessment

### 3.1 Kotlin Source Overview (12 files, ~4,888 lines)

| File | Lines | Role |
|---|---|---|
| `MainActivity.kt` | 1,975 | UI monolith (theme + i18n + settings + editor + canvas + dialogs) |
| `Models.kt` | 231 | Data models (WidgetConfig, City, enums) |
| `BackgroundRenderer.kt` | 223 | Widget background shape rendering |
| `WorldClockWidgetProvider.kt` | 189 | AppWidget provider + RemoteViews rendering |
| `ConfigStore.kt` | 144 | SharedPreferences persistence (JSON) |
| `SolarIconRenderer.kt` | 144 | SVG → Bitmap sun/moon icon rendering |
| `CityCatalog.kt` | 60 | SQLite city database queries |
| `CitySearchRepository.kt` | 28 | Network city search (GeoNames API) |
| `SizedWidgetProviders.kt` | 27 | 24 empty size-specific widget provider subclasses |
| `PinWidgetReceiver.kt` | 28 | BroadcastReceiver for pin-to-home |
| `ProjectStore.kt` | 23 | Project list persistence |
| `SolarCalculator.kt` | 23 | Day/night calculation |

### 3.2 Dead Code

| Location | Lines | Description |
|---|---|---|
| `MainActivity.kt:1621–1626` | 6 | Dead `RotationHandle` — superseded by `RotationHandleUnified` |
| `MainActivity.kt:1685–1690` | 6 | Dead `shapeRecommendedSizes` — never called |
| `MainActivity.kt:1736–1741` | 6 | Dead `CityEditor` — legacy list item, never called |
| `MainActivity.kt:1704–1733` | 30 | Commented-out `Canvas` shape preview (obsolete) |

### 3.3 Unused Imports (7 total)

| File | Import | Line |
|---|---|---|
| `MainActivity.kt` | `android.content.Context` | 7 |
| `MainActivity.kt` | `AnimatedVisibility` | 17 |
| `MainActivity.kt` | `SizeTransform` | 18 |
| `MainActivity.kt` | `drawscope.rotate` | 53 |
| `MainActivity.kt` | `IntOffset` | 59 |
| `MainActivity.kt` | `kotlin.math.max` | 88 |
| `WorldClockWidgetProvider.kt` | `model.WidgetLayer` | 13 |

### 3.4 Unused Variables

| File | Line | Variable |
|---|---|---|
| `WorldClockWidgetProvider.kt` | 44 | `val layers = config.layers` — assigned but never read |
| `Models.kt` | — | `newWidgetLayer(index: Int)` — parameter `index` never used |

### 3.5 TODO/FIXME, Debug Logging, Stale Println

**None found** across the entire codebase. The code is clean of development debugging artifacts.

### 3.6 Functions > 100 Lines

| Function | File | Lines | Span |
|---|---|---|---|
| `LayoutCanvas` | `MainActivity.kt` | 383 | L1048–L1430 |
| `WidgetEditor` | `MainActivity.kt` | 157 | L492–L648 |
| `EditorActionButton` | `MainActivity.kt` | 156 | L780–L935 |
| `SelectedElementEditor` | `MainActivity.kt` | 100 | L651–L750 |

### 3.7 Hard-Coded / Magic Values

- **57+ color hex literals** in `MainActivity.kt` outside theme definitions (e.g., `Color(0xFF7655AF)` repeated 7+ times for selection)
- **131+ raw `.dp` values** (e.g., `16.dp`, `72.dp`, `252.dp`, `540.dp`)
- **12 raw `.sp` values** (e.g., `9.sp`, `22.sp`, `25.sp`)
- **9 distinct `RoundedCornerShape(…)` values** (5.dp through 18.dp)
- **Magic gesture thresholds:** `0.008f` drag sensitivity, `1.04f` rotation scale, `3.5f°` snap window
- **12 hardcoded day/night palette color pairs** in `DAY_NIGHT_PALETTES`

### 3.8 Constant Divergence

`ConfigStore.kt` has persistence fallback defaults that **diverge from `Models.kt` canonical defaults**:

| Constant | `Models.kt` | `ConfigStore.kt` |
|---|---|---|
| `dayColor` | `0x205D5774` | `0x385A526D` |
| `nightColor` | `0x42473D5D` | `0x55433D59` |
| `softness` | `16f` | `24.0` |
| `shadow` | `8f` | `12.0` |

### 3.9 Duplicated String Literals

- `"app_settings"` — 2 occurrences
- `"EEE, MMM d"` — 4 occurrences across 2 files
- `"HH:mm"` / `"h:mm a"` — 4 occurrences across 2 files
- SQL projection prefix — 3 occurrences in `CityCatalog.kt`
- `Bitmap.Config.ARGB_8888` — 6 occurrences across 2 files
- `"ids"` key — 5 occurrences in `ProjectStore.kt`

---

## 4. Resource / Assets Assessment

### Production resources (app/src/main/res/) — **CLEAN**

All 32 resource files are actively referenced. The prior 2026-09-07 audit already removed 11 unused drawable/SVG files. No unused resources remain.

### Solar icon SVGs (92 files, ~78 KB) — **CLEAN**

Every SVG has a 1:1 mapping with `SunIconStyle` / `MoonIconStyle` enum values. No orphans.

### cities.db (41.7 MB) — **REQUIRED but LARGE**

Opened at runtime by `CityCatalog.kt`. Generated by `tools/build_city_db.py` from GeoNames data. Cannot be removed but dominates repo size.

### Release APK (30 MB) — **REVIEW_REQUIRED**

`release-assets/BestMates-v0.1.0-debug.apk` is tracked in git via an explicit `.gitignore` exception. A second untracked `v0.8.0` APK (also 30 MB) exists on disk.

### Reference images — **PROTECTED**

- `1/` — 5 screenshots, gitignored, protected by `AI_RULES.md`
- `2/` — 4 images, **accidentally tracked** (committed in checkpoint `bacd712`) because `/2/` was not added to `.gitignore`

---

## 5. Build / Gradle Assessment

### Dependencies — **CURRENT**

| Dependency | Version | Status |
|---|---|---|
| AGP | 8.8.0 | ✅ Current |
| Kotlin | 2.0.21 | ✅ Current |
| Compose BOM | 2024.12.01 | ✅ Current |
| Gradle wrapper | 8.10.2 | ✅ Compatible |
| `core-ktx` | 1.15.0 | ✅ Current |
| `activity-compose` | 1.10.0 | ✅ Current |
| `material3` | (via BOM) | ✅ Current |
| `androidsvg-aar` | 1.4 | ✅ Used by `SolarIconRenderer.kt` |

### Unused dependencies (2)

| Dependency | Reason |
|---|---|
| `ui-tooling-preview` | No `@Preview` annotations exist in the codebase |
| `ui-tooling` (debugImpl) | Supporting lib for preview — same issue |

### Missing infrastructure

- No `app/src/test/` or `app/src/androidTest/` directories
- No test dependencies (JUnit, Espresso, Compose Testing)
- No `.github/workflows/` — no CI/CD pipeline

---

## 6. Architecture Hotspots

### 6.1 MainActivity.kt — The Monolith (1,975 lines)

Contains at least **10 distinct responsibilities** that would typically be separate files/modules:

| Responsibility | Lines | Notes |
|---|---|---|
| Theme definitions (`BestMatesTheme`) | L93–L320 | 4 app themes, typography, color schemes |
| Inline i18n dictionary | L116–L278 | ~160 lines of 7-language translation maps |
| Navigation (`BestMatesApp`) | L331–L381 | Animated screen transitions |
| Workspace screen | L384–L444 | Project browser with copy/cut/paste |
| Settings screen | L447–L475 | Theme + language + resource import |
| Editor orchestrator | L492–L648 | Undo/redo stack, autosave, top bar |
| Element inspector | L651–L935 | Component/City/Text panels + 13 icon buttons |
| Interactive canvas | L1048–L1430 | 383-line composable with gesture detection |
| Layout math + snapping | L1432–L1507 | Pure functions for coordinate math |
| Dialogs + pickers | L1630–L1945 | Shape picker, city picker, color picker, font picker |
| Widget launcher integration | L1947–L1974 | Pin-to-home + refresh |

> ⚠️ **DO NOT decompose this monolith now.** It would conflict with GPT-6's unfinished Phase 1 work which plans to introduce semantic tokens, new theme architecture, and settings redesign. The spec explicitly states Phase 1 is specification-only with no code extraction.

### 6.2 Inline i18n System

~160 lines of `mapOf(...)` dictionaries for 7 languages live inside `MainActivity.kt`. This is functional but fragile — adding a key requires editing multiple inline maps. Phase 1 spec does not address this.

### 6.3 Constant Divergence (Models.kt vs ConfigStore.kt)

Default styling values differ between the canonical model constructors and the persistence layer's fallback defaults. This could cause subtle visual differences for widgets loaded from storage vs newly created widgets.

### 6.4 Lack of Tests

No unit tests, no instrumented tests, no CI pipeline. The only verification is manual adb testing (evidenced by the 21 root-level uiautomator dumps).

### 6.5 Font Parsing Duplication

`fontFamilyName()` and `fontStyle()` logic is duplicated between `MainActivity.kt` (L1628–1629) and `WorldClockWidgetProvider.kt` (L169, L186), with slightly different implementations.

---

## 7. SAFE_DELETE — Proven unnecessary, safe to remove

| # | Item | Location | Evidence |
|---|---|---|---|
| SD-1 | Dead function `RotationHandle` | `MainActivity.kt:1621–1626` | Superseded by `RotationHandleUnified` (L1527–1619); never called |
| SD-2 | Dead function `shapeRecommendedSizes` | `MainActivity.kt:1685–1690` | Never called anywhere in the project |
| SD-3 | Dead function `CityEditor` | `MainActivity.kt:1736–1741` | Legacy city list item; never called |
| SD-4 | Commented-out Canvas code | `MainActivity.kt:1704–1733` | 30 lines of obsolete shape preview; replaced by `BackgroundRenderer.render()` |
| SD-5 | Unused variable `val layers` | `WorldClockWidgetProvider.kt:44` | Assigned from `config.layers` but never read |
| SD-6 | Unused import `WidgetLayer` | `WorldClockWidgetProvider.kt:13` | Not referenced after `layers` variable removal |
| SD-7 | 6 unused imports | `MainActivity.kt:7,17,18,53,59,88` | `Context`, `AnimatedVisibility`, `SizeTransform`, `rotate`, `IntOffset`, `max` |

**Estimated savings:** ~55 lines of code removed, zero behavioral change.

---

## 8. SAFE_CLEANUP — Low-risk mechanical cleanup

| # | Item | Description | Risk |
|---|---|---|---|
| SC-1 | Add `/2/` to `.gitignore` | Matches existing `/1/` and `/KWGT/` patterns | None |
| SC-2 | Add `/.agents/` to `.gitignore` | Empty directory, no purpose | None |
| SC-3 | Delete untracked `release-assets/BestMates-v0.8.0-debug.apk` | 30 MB local disk file, not tracked in git, duplicate of v0.1.0 content | None (disk only) |
| SC-4 | Update stale rotation info in `PROJECT_HANDOFF.md` | Sections 5 & 10 claim `[-180,180]` clamp; commits `31c3ebb`/`60a9bb5` implemented unlimited rotation | Documentation accuracy |
| SC-5 | Delete 21 root-level uiautomator XML dumps + solar_scroll.png | ~800 KB of adb test captures; gitignored, never referenced | None (disk only) |
| SC-6 | Delete `.tmp_androidsvg/` directory | 217 KB temp build artifact; gitignored | None (disk only) |
| SC-7 | Delete `.tmp_meteocons/` directory | 9.6 MB extracted npm package; gitignored, 2,081 files | None (disk only) |
| SC-8 | Remove unused `index` parameter from `Models.kt:newWidgetLayer()` | Parameter declared but never read | Trivial API change |
| SC-9 | Unused `bindCity` parameter `shape` | `WorldClockWidgetProvider.kt` — parameter never used in function body | Trivial cleanup |
| SC-10 | Align `SolarCalculator.kt` math imports | Uses `kotlin.math.*` wildcard + `java.lang.Math` — pick one consistently | Style consistency |
| SC-11 | Extract duplicated SQL prefix in `CityCatalog.kt` | Same SELECT projection appears 3 times | Reduces duplication |
| SC-12 | Extract duplicated date/time format strings | `"EEE, MMM d"`, `"HH:mm"`, `"h:mm a"` appear across 2 files | Reduces duplication |
| SC-13 | Extract duplicated `Bitmap.Config.ARGB_8888` usage | 6 occurrences across 2 files — could be a constant | Minor cleanup |

---

## 9. REVIEW_REQUIRED — Possibly unused or needs human review

| # | Item | Why review is needed |
|---|---|---|
| RR-1 | **Tracked debug APK** (`release-assets/BestMates-v0.1.0-debug.apk`, 30 MB) | Deliberately committed via `.gitignore` exception. Removing it changes repository distribution policy and requires maintainer decision. The prior audit (2026-09-07) deferred this. |
| RR-2 | **`cities.db` size** (41.7 MB) | Necessary at runtime but dominates repo size. Could potentially be reduced by using `cities15000.txt` instead of `cities500.txt`, or by Git LFS. Requires product decision on city coverage. |
| RR-3 | **Constant divergence** (Models.kt vs ConfigStore.kt defaults) | May be intentional (different defaults for legacy widgets vs new widgets) or may be a bug. Needs maintainer clarification. |
| RR-4 | **`ui-tooling-preview` dependency** | Unused because no `@Preview` composables exist. Could be useful for future development. Remove only if certain no previews will be added. |
| RR-5 | **Font parsing duplication** across `MainActivity.kt` and `WorldClockWidgetProvider.kt` | Both implement `fontFamilyName()`/`fontStyle()` with slightly different logic. Unifying risks affecting widget rendering. |
| RR-6 | **`2/` directory tracked in git** | Committed in checkpoint `bacd712`. Should be untracked (`git rm -r --cached 2/`) after adding `/2/` to `.gitignore`. But these are protected reference images — needs explicit approval. |
| RR-7 | **No test infrastructure** | No tests exist. Adding test dependencies and directories is recommended but is a project decision, not a cleanup item. |
| RR-8 | **`CURRENT_SESSION.md` last known commit** | References `60a9bb5` but subsequent commits exist. Minor staleness — may be intentional session log behavior. |

---

## 10. PROTECTED_PHASE1 — GPT-6 unfinished Phase 1 work

> ⚠️ **DO NOT modify any of these items.** They are part of GPT-6's unfinished Phase 1 UI redesign work.

| # | Item | Location | Phase 1 Relevance |
|---|---|---|---|
| P1-1 | `docs/BEST_MATES_UI_2_SPEC.md` | 588 lines | **Primary Phase 1 deliverable** — complete design specification for Best Mates 2.0 |
| P1-2 | `AppThemeStyle` enum (Cloud/Dream/Midnight/Mono) | `MainActivity.kt:93–95` | Theme definitions referenced by Phase 1 spec |
| P1-3 | `BestMatesTheme` composable | `MainActivity.kt:280–320` | Light/dark palette system — will be refactored by Phase 2 |
| P1-4 | Orbit Softline icon implementation | `MainActivity.kt:780–935` | `EditorActionButton` draws 13 custom outline icons per spec |
| P1-5 | Selection state styling | `MainActivity.kt:1151,1274,1365` | Current Soft Halo prototype (`Color(0xFF7655AF)`) |
| P1-6 | Quiet Motion timing | `MainActivity.kt:357–362` | `tween(170)` to `tween(260)` transitions per spec |
| P1-7 | Settings screen design | `MainActivity.kt:447–475` | Theme swatch gallery — Phase 1 designs enhanced version |
| P1-8 | Inline color constants | Throughout `MainActivity.kt` | 57+ color literals are Phase 2 token extraction targets |
| P1-9 | Typography definitions | `MainActivity.kt:108–114` | `BestMatesTypography` — Phase 1 spec defines replacement |
| P1-10 | `2/` reference images | Root directory | Committed in checkpoint; visual design reference material |
| P1-11 | Checkpoint commit `bacd712` | git history | Recovery snapshot of all Phase 1 partial state |

---

## 11. Recommended Cleanup Sequence

Execute in this order to minimize risk:

### Phase A — Zero-risk cleanup (no code changes, no git history impact)

1. Delete local-only untracked files: root XML dumps, `solar_scroll.png`, `.tmp_*` directories (SC-5, SC-6, SC-7)
2. Delete untracked `BestMates-v0.8.0-debug.apk` from disk (SC-3)

### Phase B — Safe git changes (code cleanup, no behavioral impact)

3. Add `/2/` and `/.agents/` to `.gitignore` (SC-1, SC-2)
4. Remove 7 unused imports from `MainActivity.kt` and `WorldClockWidgetProvider.kt` (SD-7, SD-6)
5. Remove dead `RotationHandle` function (SD-1)
6. Remove dead `shapeRecommendedSizes` function (SD-2)
7. Remove dead `CityEditor` function (SD-3)
8. Remove commented-out Canvas code block (SD-4)
9. Remove unused `val layers` variable (SD-5)
10. Remove unused `bindCity` parameter `shape` (SC-9)

### Phase C — Low-risk documentation + minor refactors

11. Update `PROJECT_HANDOFF.md` stale rotation info (SC-4)
12. Extract duplicated SQL prefix in `CityCatalog.kt` (SC-11)
13. Extract duplicated date/time format constants (SC-12)
14. Clean up `SolarCalculator.kt` math imports (SC-10)

### Phase D — Requires maintainer decision

15. Decide on tracked APK (`release-assets/BestMates-v0.1.0-debug.apk`) (RR-1)
16. Decide on `cities.db` size reduction strategy (RR-2)
17. Review and resolve constant divergence in ConfigStore.kt (RR-3)
18. Decide on `ui-tooling-preview` dependency (RR-4)

---

## 12. Validation Plan

### After Phase A (disk cleanup)

```powershell
# Verify nothing tracked was lost
git status
# Confirm working tree is clean
```

### After Phase B (code cleanup)

```powershell
# Build the project
.\gradlew.bat assembleDebug

# Verify no compilation errors
# Verify APK produces expected size
# Manual verification on device that:
#   - Editor canvas renders correctly
#   - Rotation handles work
#   - Shape previews render
#   - City selection works
#   - Widget pins to home screen
```

### After Phase C (documentation + refactors)

```powershell
# Rebuild
.\gradlew.bat assembleDebug

# Verify city search still works
# Verify date/time formatting in editor and widget
```

---

## 13. Risks

| Risk | Mitigation |
|---|---|
| Removing dead code breaks something not caught by static analysis | Recovery branch `maintenance/pre-phase1-cleanup` at `bacd712`. Build verification after each phase. |
| Constant divergence fix changes widget appearance for existing users | Mark as REVIEW_REQUIRED; only fix with explicit maintainer approval and understanding of migration. |
| `.gitignore` changes affect CI or deployment scripts | No CI exists. `.gitignore` changes are additive (new ignore rules only). |
| Font parsing unification causes widget rendering differences | Mark as REVIEW_REQUIRED; do not unify without side-by-side testing. |
| Phase 1 work is accidentally modified | All Phase 1 items explicitly listed in PROTECTED_PHASE1. No SAFE_DELETE or SAFE_CLEANUP item touches Phase 1 code. |

---

## 14. Estimated Repository-Space Improvement

### Git-tracked size (current: ~72.6 MB)

| Action | Savings | Requires |
|---|---|---|
| Remove tracked APK (`v0.1.0`) | -30 MB (future clones; .git retains history) | Maintainer approval (RR-1) |
| Untrack `2/` reference images | -373 KB | Maintainer approval (RR-6) |
| Dead code removal | ~55 lines (~4 KB) | SAFE_DELETE (no approval needed) |

### Local disk savings (not tracked)

| Action | Savings |
|---|---|
| Delete `.tmp_meteocons/` | -9.6 MB |
| Delete untracked `v0.8.0` APK | -30 MB |
| Delete root XML/PNG test dumps | -800 KB |
| Delete `.tmp_androidsvg/` | -217 KB |
| **Total local disk savings** | **~40.6 MB** |

### Potential future savings (requires product decisions)

| Action | Savings |
|---|---|
| Move `cities.db` to Git LFS | -41.7 MB from git objects |
| Move APK to GitHub Releases only | -30 MB from tracked files |
| Reduce city database (cities15000 vs cities500) | Potentially -30+ MB |

---

## Appendix A: Complete Tracked File Inventory

173 files tracked in git:
- 12 Kotlin source files
- 92 SVG icon assets
- 25 widget XML configurations
- 7 resource files (drawables, layouts, values)
- 14 documentation files
- 1 SQLite database (41.7 MB)
- 1 debug APK (30 MB)
- 1 Python build script
- 7 Gradle/build files
- 2 GitHub issue templates
- 11 miscellaneous (LICENSE, .gitignore, manifests, etc.)

## Appendix B: MainActivity.kt Structural Map

```
Lines 001–092  Imports (91 lines, 6 unused)
Lines 093–114  Enums & Typography (AppThemeStyle, AppLanguage, BestMatesTypography)     [PROTECTED_PHASE1]
Lines 116–278  Inline i18n dictionary (7 languages, ~160 lines)
Lines 280–320  BestMatesTheme composable (light/dark color schemes)                     [PROTECTED_PHASE1]
Lines 322–329  MainActivity class declaration
Lines 331–381  BestMatesApp root composable (navigation transitions)                    [PROTECTED_PHASE1 — Quiet Motion]
Lines 384–444  WorkspaceScreen + WorkspaceThumbnail
Lines 447–475  SettingsScreen                                                          [PROTECTED_PHASE1]
Lines 492–648  WidgetEditor (undo/redo, autosave)
Lines 651–750  SelectedElementEditor (inspector)
Lines 752–935  EditorActionRow + EditorActionButton (13 outline icons)                  [PROTECTED_PHASE1 — Orbit Softline]
Lines 937–1045 Element inspectors (font, city part, sun/moon icon pickers)
Lines 1048–1430 LayoutCanvas (383-line interactive canvas)
Lines 1432–1507 Layout math & snapping utilities
Lines 1509–1626 ResizeHandle, RotationHandleUnified, RotationHandle (DEAD)
Lines 1628–1741 Font/Shape helpers + ShapePicker + dead CityEditor
Lines 1744–1795 City selection dialogs
Lines 1796–1945 Color pickers + Day/Night palettes + ThemePicker
Lines 1947–1974 Widget launcher integration (pin + refresh)
```
