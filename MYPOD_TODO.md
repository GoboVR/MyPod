# MyPod fork of Meld — status

## What this fork is
A fork of [Meld](https://github.com/FrancescoGrazioso/Meld) (itself a fork of
Metrolist), renamed to MyPod, with D-Pad/controller/keyboard navigation and
iPod/Zune theme options layered on top. GPL-licensed, same as upstream.

## Renaming
Done via the build script's existing `METROLIST_APPLICATION_ID` /
`METROLIST_APP_NAME` env vars (no source files needed editing) — see
`.github/workflows/mypod-build.yml`.

## D-Pad / controller / keyboard navigation
**Good news:** Jetpack Compose's built-in `clickable()` modifier — which
most of Meld's rows/tiles already use — is already focusable and already
responds to Enter/DPAD_CENTER to trigger a click when focused. Android also
already routes a connected controller's D-Pad and physical/keyboard arrow
keys through the same key codes Compose uses for focus movement. So most of
the app (home screen, library, search results, playlists) should already
have *usable* D-Pad/keyboard navigation without any code changes — this
needs confirming on a real device/controller, since it can't be verified
without building and running the APK.

**Added:** `ui/component/DPadFocus.kt` — a `focusableSelectable()` modifier
that adds a visible focus ring (clickable() alone gives no visual
indicator of what's focused, since Meld is touch-first). Not yet applied
everywhere — apply it to a row/tile as you touch that screen.

**Known gaps (not yet addressed, needs real design + device testing):**
Components that bypass `clickable()` with raw gesture detection
(`pointerInput { detectTapGestures {...} }`) don't get D-Pad support for
free and need a deliberate D-Pad equivalent designed for each:
- `ui/component/BigSeekBar.kt`, `WavySlider.kt`, `SquigglySlider.kt` — the
  playback seek bar (needs e.g. "focus it, then Left/Right seeks")
- `ui/player/Player.kt`, `MiniPlayer.kt`, `Thumbnail.kt` — swipe gestures
  for next/previous track
- `ui/component/BottomSheet.kt`, `DraggableScrollBarOverlay.kt` — drag
  handles
- `ui/component/ExperimentalLyrics.kt` — swipe-based lyrics view
- `ui/component/SearchBar.kt` — already has some key handling, worth
  double-checking it's complete
- `ui/component/SpotifyHomeSectionRow.kt`, `Dialog.kt`

## Theme
Added "Zune" (burnt orange, `#B5490A`) and "iPod" (steel blue-grey,
`#5A6B7A`) as real, selectable entries in Settings → Theme → the existing
color palette list (`ThemeScreen.kt`). These work today — Meld already
generates its whole Material You color scheme from one seed color, so
adding a preset is just adding a color + name.

**Not yet done:** actual iPod/Zune *visual identity* beyond an accent
color — the iPod click-wheel circular nav, the Zune tile-based home
layout, Zune's oversized "twist" typography. That's a bigger layout-level
project, not a color swap, and is a good next step once the base app is
confirmed working.

## Build
`.github/workflows/mypod-build.yml` — debug-only, no signing secrets
required (Android auto-generates a debug keystore for debug builds).
Produces `MyPod-debug-apk` as a workflow artifact.
