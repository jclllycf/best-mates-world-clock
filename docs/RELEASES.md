# Release workflow

Best Mates uses small public-preview releases while the editor evolves.

## What a release contains

- A debug APK that anyone can install for testing.
- The exact source snapshot used to build it.
- A changelog entry and feedback templates.

## Creating the next preview

1. Update the version in `app/build.gradle.kts` and `CHANGELOG.md`.
2. Build the debug APK with Java 17.
3. Install it on a phone and perform a short smoke test.
4. Create a Git tag such as `v0.9.0` and a GitHub Release.
5. Attach the named APK and invite feedback through GitHub Issues.

## Feedback first

Public previews are intentionally incomplete. Please include the device model, Android version, widget size, and a screen recording or screenshot when reporting a problem.
