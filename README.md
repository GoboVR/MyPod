# MyPod

An Android music player forked from [Meld](https://github.com/FrancescoGrazioso/Meld)
(GNU GPL) with D-Pad/joystick/keyboard navigation and iPod/Zune-inspired themes.

## License
Meld is GPL-licensed. This project is a derivative work and is distributed
under the GNU GPL as well.

## Status: early scaffold
This is a starting skeleton, not the full app yet. What's here:
- Basic Compose Android project structure
- `ui/theme/ZuneTheme.kt` — first pass at the Zune-style dark/orange theme
- `ui/nav/DirectionalFocus.kt` — the D-Pad/joystick/keyboard focus navigation
  system (one system handles all three input types, since Android maps
  D-Pad, controller-D-Pad, and arrow keys to the same key codes)
- `.github/workflows/build.yml` — CI that builds a debug APK on every push

## Not yet built
- Real Meld library/player code merged in
- YouTube login + search + download
- iPod theme
- Actual now-playing / library / search screens (currently placeholders)

## Building locally (once you're ready)
This needs Android Studio (or the `gradle` CLI) with JDK 17. We'll walk
through that together when you get there.
