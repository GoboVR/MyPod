package com.gobovr.mypod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gobovr.mypod.auth.YouTubeAuthStore
import com.gobovr.mypod.ui.nav.focusableListItem
import com.gobovr.mypod.ui.screens.LibraryScreen
import com.gobovr.mypod.ui.screens.PlaylistDetailScreen
import com.gobovr.mypod.ui.screens.YouTubeLoginScreen
import com.gobovr.mypod.ui.theme.MyPodZuneTheme
import com.gobovr.mypod.ui.theme.ZuneTileTitle
import com.metrolist.innertube.YouTube
import com.metrolist.innertube.models.PlaylistItem

/**
 * Simple, explicit screen-state navigation (a sealed class + one mutable
 * "current screen" var) rather than pulling in Navigation-Compose -- the
 * app only has a few screens right now, and this keeps every screen
 * transition visible in one place while the app is this small. Worth
 * switching to real Navigation-Compose once there are more than a
 * handful of screens or back-stack behavior gets complex.
 */
private sealed class Screen {
    object Login : Screen()
    object Home : Screen()
    object Library : Screen()
    data class PlaylistDetail(val playlistId: String, val playlistTitle: String) : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPodZuneTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyPodApp()
                }
            }
        }
    }
}

@Composable
fun MyPodApp() {
    val context = LocalContext.current

    // On first entry: if we already have a stored YouTube Music login,
    // restore it into the YouTube singleton and skip straight past login.
    var screen by remember {
        mutableStateOf<Screen>(
            if (YouTubeAuthStore.isLoggedIn(context)) {
                YouTube.cookie = YouTubeAuthStore.cookie(context)
                YouTube.visitorData = YouTubeAuthStore.visitorData(context)
                YouTube.dataSyncId = YouTubeAuthStore.dataSyncId(context)
                Screen.Home
            } else {
                Screen.Login
            }
        )
    }

    when (val current = screen) {
        is Screen.Login -> YouTubeLoginScreen(onLoginComplete = { screen = Screen.Home })
        is Screen.Home -> HomeScreen(onNavigateToLibrary = { screen = Screen.Library })
        is Screen.Library -> Column(modifier = Modifier.fillMaxSize()) {
            ScreenTopBar(title = "Library", onBack = { screen = Screen.Home })
            LibraryScreen(onPlaylistClick = { playlist: PlaylistItem ->
                screen = Screen.PlaylistDetail(playlist.id, playlist.title)
            })
        }
        is Screen.PlaylistDetail -> Column(modifier = Modifier.fillMaxSize()) {
            ScreenTopBar(title = current.playlistTitle, onBack = { screen = Screen.Library })
            PlaylistDetailScreen(playlistId = current.playlistId, playlistTitle = current.playlistTitle)
        }
    }
}

@Composable
private fun ScreenTopBar(title: String, onBack: () -> Unit) {
    Row(modifier = Modifier.padding(8.dp)) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 8.dp, top = 12.dp)
        )
    }
}

@Composable
fun HomeScreen(onNavigateToLibrary: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "MyPod",
            style = ZuneTileTitle,
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
        )
        Column {
            listOf("Now Playing" to {}, "Library" to onNavigateToLibrary, "Search" to {}, "Settings" to {})
                .forEach { (label, action) ->
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusableListItem { action() }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
        }
    }
}
