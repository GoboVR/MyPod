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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gobovr.mypod.playback.MyPodPlayer
import com.gobovr.mypod.ui.nav.focusableListItem
import com.metrolist.innertube.YouTube
import com.metrolist.innertube.models.SongItem
import com.metrolist.innertube.utils.completed

@Composable
fun PlaylistDetailScreen(playlistId: String, playlistTitle: String) {
    val context = LocalContext.current
    var songs by remember { mutableStateOf<List<SongItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(playlistId) {
        YouTube.playlist(playlistId).completed()
            .onSuccess { page ->
                songs = page.songs
                isLoading = false
            }
            .onFailure { error ->
                errorMessage = error.message ?: "Couldn't load this playlist"
                isLoading = false
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = playlistTitle,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        when {
            isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            errorMessage != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(errorMessage.orEmpty())
            }
            songs.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No songs in this playlist")
            }
            else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(songs) { song ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusableListItem { MyPodPlayer.playSong(context, song.id, playlistId) }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(song.title, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            song.artists.joinToString { it.name },
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
