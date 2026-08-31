package com.gobovr.mypod.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gobovr.mypod.ui.nav.focusableListItem
import com.metrolist.innertube.YouTube
import com.metrolist.innertube.models.PlaylistItem
import com.metrolist.innertube.utils.completed

/**
 * "Playlist syncing" in practice: this fetches the signed-in account's
 * YouTube Music playlists fresh from YouTube.library() every time the
 * screen opens. That IS the sync -- there's no separate local database
 * copy to go stale, since a plain API fetch is closer to "what it is
 * right now" than a cache would be, and this app has no offline mode yet
 * for library browsing (only per-song download, once that's built).
 */
@Composable
fun LibraryScreen(onPlaylistClick: (PlaylistItem) -> Unit) {
    var playlists by remember { mutableStateOf<List<PlaylistItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        YouTube.library("FEmusic_liked_playlists").completed()
            .onSuccess { page ->
                playlists = page.items.filterIsInstance<PlaylistItem>()
                isLoading = false
            }
            .onFailure { error ->
                errorMessage = error.message ?: "Couldn't load your playlists"
                isLoading = false
            }
    }

    when {
        isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        errorMessage != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(errorMessage.orEmpty())
        }
        playlists.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No playlists found")
        }
        else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(playlists) { playlist ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusableListItem { onPlaylistClick(playlist) }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Text(playlist.title, style = MaterialTheme.typography.titleMedium)
                    playlist.songCountText?.let {
                        Text(it, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
