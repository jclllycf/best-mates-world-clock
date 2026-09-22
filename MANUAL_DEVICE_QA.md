# Manual Device QA Checklist

This document must be executed by a human operator on a physical Android device or emulator. Headless automated testing has validated the build, tests, and persistence, but interaction mechanics require device QA.

## INSTALL / LAUNCH
- [ ] fresh install
- [ ] upgrade install if an older APK/config is available
- [ ] app opens
- [ ] no crash

## WORKSPACE
- [ ] create project
- [ ] open project
- [ ] rename/delete if supported
- [ ] reopen project

## EDITOR
- [ ] layer drag
- [ ] 45° rotated drag
- [ ] resize
- [ ] rotation through ±180°
- [ ] snapping
- [ ] city edit
- [ ] text edit
- [ ] multiline text
- [ ] legacy font state if test data exists
- [ ] undo/redo
- [ ] copy/paste
- [ ] lock
- [ ] delete

## WIDGET
- [ ] add widget from launcher
- [ ] configure widget
- [ ] confirm home-screen render
- [ ] compare editor vs widget
- [ ] resize launcher widget
- [ ] verify multiple widget instances
- [ ] verify widget refresh
- [ ] reboot persistence if practical

## TIME
- [ ] 12/24h
- [ ] multiple timezones
- [ ] date rollover

## SOLAR
- [ ] sun during daytime
- [ ] moon during nighttime
- [ ] celestial style switching

## THEME
- [ ] light
- [ ] dark
- [ ] system

## LOCALIZATION
- [ ] Chinese
- [ ] English
- [ ] at least one non-Latin language, preferably Arabic
- [ ] layout stability

## ASSETS
- [ ] curated fonts
- [ ] sun library
- [ ] moon library
- [ ] Islamic crescent/star
- [ ] shape selector

## EDGE CASES
- [ ] delete currently selected item
- [ ] rotate then drag
- [ ] rotate then resize
- [ ] smallest practical widget size
- [ ] largest practical widget size
- [ ] process restart
- [ ] device reboot if practical
