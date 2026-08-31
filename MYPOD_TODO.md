# MyPod (from-scratch build) — status

This replaces the earlier "fork all of Meld" approach. MyPod is now its
own small app that reuses two specific pieces of Meld/Metrolist (both
GPL-3.0, credited below), not Meld's UI or app structure.

## What's reused from Meld, and why
- `innertube/` — the YouTube Music API client module, unmodified except
  package namespace stays `com.metrolist.innertube` (kept as-is since
  it's a self-contained library, not app code)
- `app/.../utils/YTPlayerUtils.kt` + its dependencies (`utils/potoken/`,
  `utils/sabr/`, `utils/cipher/`, `utils/Fix403.kt`) — the stream-URL
  resolution pipeline (proof-of-origin tokens, cipher deobfuscation,
  per-client fallback chains). Repackaged to `com.gobovr.mypod`.
  This is genuinely the hardest part of YouTube Music integration to
  get right blind, so it's reused rather than rewritten — see the
  conversation history for why.

Everything else (login screen, library screen, playlist detail screen,
navigation, player wiring, themes) is new, written for MyPod specifically.

## What's actually working (real, wired to real APIs)
- **Login**: `YouTubeLoginScreen.kt` — real Google sign-in via WebView,
  captures the session cookie the same way Meld does, validates it via
  `YouTube.accountInfo()`, persists it (`YouTubeAuthStore.kt`, plain
  SharedPreferences) so you stay logged in across app restarts
- **Playlist sync**: `LibraryScreen.kt` — fetches your real YouTube Music
  playlists via `YouTube.library("FEmusic_liked_playlists")`
- **Playlist contents**: `PlaylistDetailScreen.kt` — fetches real songs
  in a playlist via `YouTube.playlist(id).completed()` (paginates through
  all songs, not just the first page)
- **Streaming**: `MyPodPlayer.kt` — resolves a real playable stream URL
  via the vendored `YTPlayerUtils.playerResponseForPlayback(...)` and
  plays it through ExoPlayer

## Honestly not yet done
- **No actual UI verification** — none of this has been built/run on a
  device yet. First build attempt will likely surface real compile
  errors (version mismatches, missed imports) — expect an iteration
  or two, same as the earlier Meld-fork attempts.
- **Download for offline playlists** — not started. Next major feature.
- **No MediaSessionService** — playback won't survive backgrounding,
  no lock-screen controls, no notification. `MyPodPlayer` is a bare
  ExoPlayer instance for now.
- **No Now Playing / player UI** — songs start playing on tap but
  there's no screen showing what's currently playing, no seek bar,
  no pause/skip controls yet.
- **No search screen** — browsing is playlist-only right now.
- **Zune/iPod visual identity** — the theme work from the earlier Meld
  fork (typography, colors) hasn't been ported to this from-scratch
  app yet. Right now this uses Material3 defaults.
- **D-Pad/keyboard nav** — `ui/nav/DirectionalFocus.kt` exists and is
  wired into the Home/Library/Playlist rows, but untested on real
  hardware.

## License
`innertube/` and the vendored `utils/` files are GPL-3.0 (Meld/Metrolist),
credited above. MyPod's own new code is also GPL-3.0, consistent with
reusing GPL-licensed code.
