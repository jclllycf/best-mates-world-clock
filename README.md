# Best Mates

Best Mates is an open-source Android world-clock widget editor. It is being built for people who want a personal, visual home-screen widget without having to learn a formula-driven editor.

> **Status: v0.8.0 public preview.** This is an early, usable testing build. It is intended for feedback and has known rough edges.

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

## Try the preview APK

Download **`BestMates-v0.8.0-debug.apk`** from this repository’s [Releases](../../releases) page. It is a debug-signed preview for testing, not a Play Store build.

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

The current priorities are editor interaction, full UI localisation, clearer solar-icon choices, robust shape rendering and better widget sizing across launchers. See [CHANGELOG.md](CHANGELOG.md) and [PROJECT_HANDOFF.md](PROJECT_HANDOFF.md) for the detailed current state.

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
