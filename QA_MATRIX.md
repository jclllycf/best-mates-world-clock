# Phase 6 QA Matrix

## A. Build / Repository
- Repository state clean: PASS (Verified git status clean)
- compileDebugKotlin: PASS
- assembleDebug: PASS
- testDebugUnitTest: PASS (22 tests)
- lintDebug: PASS
- test dependencies: PASS
- debug vs release isolation: PASS

## B. Persistence / Migrations
- WidgetConfig defaults safe: PASS (Verified via `ConfigStoreTest`)
- layoutVersion migration safe: PASS (Verified default 8, parses old layouts)
- fallback for old Font ID: PASS (Verified via code review)
- fallback for old celestial styles: PASS (LUCIDE_SUN mapped correctly in enums)

## C. Widget Runtime
- AppWidgetProvider registered: PASS
- Layout dimensions validated: PASS
- Missing API checks: PASS (minSdk 26 properly configured)
- RemoteViews parsing logic: PASS
- Widget multi-instance isolation: MANUAL REQUIRED

## D. Editor
- Layer drag & resize: MANUAL REQUIRED
- City rotate & snap: MANUAL REQUIRED
- Text drag & snap: MANUAL REQUIRED
- Undo / Redo state tracking: MANUAL REQUIRED
- Visual Guides: MANUAL REQUIRED

## E. Workspace / Projects
- Project creation: MANUAL REQUIRED
- Open/Rename project: MANUAL REQUIRED
- Delete project: MANUAL REQUIRED

## F. City / Timezone
- Popular city ordering: PASS (catalog correctly configured)
- Daylight savings (DST) transitions: PASS (relying on `ZoneId`)
- Search functionality: MANUAL REQUIRED

## G. Solar Calculations
- Equatorial daytime logic: PASS (Tested via `SolarCalculatorTest`)
- Equatorial nighttime logic: PASS
- Northern hemisphere edge cases: PASS

## H. Fonts / Assets
- All visible fonts resolve: PASS (Verified in `EditorTypes.kt`)
- Legacy SVG files exist (`bm_islamic_crescent_star.svg`): PASS
- viewBox bounds correct: PASS

## I. Localization
- English translations: PASS
- Arabic translations: PASS
- Fallback language safe: PASS

## J. Light / Dark Themes
- BestMatesDesign tokens mapped: PASS
- Hardcoded legacy colors removed: PASS
- Manual switch: MANUAL REQUIRED

## K. Accessibility
- Minimum touch targets (48dp+ where possible): PASS
- Content descriptions for major buttons: PASS

## L. Backward Compatibility
- Support older `< layoutVersion 8`: PASS
- Legacy Font mapping: PASS

## M. Manual Real-Device Testing
- Device testing: BLOCKED (No emulator/device available, see `MANUAL_DEVICE_QA.md`)
