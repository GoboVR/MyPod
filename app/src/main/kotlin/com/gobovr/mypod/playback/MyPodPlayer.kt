package com.gobovr.mypod.playback

import android.content.Context
import android.net.ConnectivityManager
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.gobovr.mypod.constants.AudioQuality
import com.gobovr.mypod.utils.YTPlayerUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * MyPod's playback engine: fetches a real, currently-valid audio stream URL
 * for a YouTube Music video ID (via the vendored YTPlayerUtils pipeline --
 * poToken generation, per-client fallback, cipher deobfuscation) and feeds
 * it directly into ExoPlayer as a MediaItem URI. No special HTTP headers
 * are needed at the ExoPlayer DataSource level -- the URL itself carries
 * whatever signed params are required, same as Meld does.
 *
 * Kept as a simple singleton object for now (single ExoPlayer instance,
 * app-lifetime); a real MediaSessionService (needed for lock-screen
 * controls / Android Auto / background playback surviving app-close) is a
 * good next step, not yet built.
 */
object MyPodPlayer {
    private var exoPlayer: ExoPlayer? = null

    fun player(context: Context): ExoPlayer =
        exoPlayer ?: ExoPlayer.Builder(context.applicationContext).build().also { exoPlayer = it }

    /**
     * Resolves [videoId] to a playable stream and starts playback.
     * Errors (network, YouTube-side failures) are logged, not thrown --
     * caller can observe [player] state for now; a proper error/loading
     * UI state is a good next step.
     */
    fun playSong(context: Context, videoId: String, playlistId: String? = null) {
        val connectivityManager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        CoroutineScope(Dispatchers.IO).launch {
            YTPlayerUtils.playerResponseForPlayback(
                videoId = videoId,
                playlistId = playlistId,
                audioQuality = AudioQuality.AUTO,
                connectivityManager = connectivityManager,
            ).onSuccess { playbackData ->
                val mediaItem = MediaItem.fromUri(playbackData.streamUrl)
                val exo = player(context)
                CoroutineScope(Dispatchers.Main).launch {
                    exo.setMediaItem(mediaItem)
                    exo.prepare()
                    exo.play()
                }
            }.onFailure { error ->
                Timber.e(error, "MyPodPlayer: failed to resolve stream for $videoId")
            }
        }
    }
}
