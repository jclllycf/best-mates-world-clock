# Best Mates

**Languages:** [English](README.md) · [中文](docs/README_zh-CN.md) · [Русский](docs/README_ru.md) · [العربية](docs/README_ar.md) · [Türkçe](docs/README_tr.md) · [Deutsch](docs/README_de.md)

Best Mates is an open-source Android world-clock widget editor. It is being built for people who want a personal, visual home-screen widget without having to learn a formula-driven editor.

> **Status: v0.2.0.** Best Mates is usable as an early public Android release. The core widget editor works, the main interface has moved onto the Best Mates 2.0 visual system, and further layout, visual and asset refinements are still planned.

## What works today

- Create several independent widget projects inside the app.
- Add up to ten cities and up to five custom text layers per project.
- Browse a bundled continent → country → city catalogue or search English city names.
- Show local time, date, country and a per-city day/night symbol.
- Calculate local daylight from the selected city’s coordinates and timezone; daylight-saving changes are handled by Android time zones.
- Use a canvas editor with backgrounds, cities and text layers, plus move, resize, rotate, copy, paste, pin, undo and redo controls.
- Add the result to the Android home screen through the app, with declared widget sizes from 1×1 through 4×6.
- Choose day/night colours, shape treatments, fonts and solar icon styles.
- Store all projects locally. The app has no account, analytics SDK, ad SDK or device-location permission.
- Use the Best Mates 2.0 visual foundation: the reconstructed Workspace, migrated Settings, refreshed Editor controls and Inspector, Soft Atelier work canvas, Crystal pickers/dialogs and Orbit Charm controls.
- Use Cloud, Dream, Midnight and Mono identities with light, dark and system appearance modes.

## Download Best Mates

The public release asset is **`BestMates-v0.2.0.apk`**, published from this repository’s [Releases](../../releases) page after release signing is configured. Debug builds are for development and are not public release assets.

1. Download the APK on an Android device.
2. Allow installation from the browser or file manager when Android asks.
3. Open **Best Mates**, create a project and tap **Add to Home**.
4. If the launcher does not offer a pin prompt, long-press the home screen and add a Best Mates widget from its widget picker.

## Feedback wanted

Please use [Issues](../../issues) for bug reports and feature ideas. The most useful reports include:

- phone model and Android version;
- Best Mates version;
- exact steps to reproduce the problem;
- a screenshot or short screen recording, with private information hidden.

Current priorities include Phase 4 layout and production-experience reconstruction, final Sun/Moon refinement, expressive display typography, localisation polish, widget/object material refinement, minor UI and interaction fixes, and formal automated testing. Weather remains exploratory future work. See [CHANGELOG.md](CHANGELOG.md) and [PROJECT_HANDOFF.md](PROJECT_HANDOFF.md) for the detailed current state.

## Build from source

Install Android SDK Platform 35 and JDK 17, set `ANDROID_HOME`, then run:

```powershell
.\gradlew.bat assembleDebug
```

The debug APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

## Privacy

All widget projects live on the device. City search can call Open-Meteo Geocoding only after the user enters a query; saved cities continue to work offline. No account or precise location is required.

## Attribution and licence

City search uses [Open-Meteo Geocoding](https://open-meteo.com/en/docs/geocoding-api). Solar-icon notices are in [app/src/main/assets/THIRD_PARTY_NOTICES.md](app/src/main/assets/THIRD_PARTY_NOTICES.md).

Copyright 2026 Best Mates contributors. Released under the [Apache License 2.0](LICENSE).
