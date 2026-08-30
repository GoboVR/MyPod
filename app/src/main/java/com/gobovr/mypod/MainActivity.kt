package com.gobovr.mypod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gobovr.mypod.ui.nav.DemoFocusableList
import com.gobovr.mypod.ui.theme.MyPodZuneTheme
import com.gobovr.mypod.ui.theme.ZuneTileTitle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPodZuneTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "my pod", style = ZuneTileTitle, modifier = Modifier)
        DemoFocusableList(
            items = listOf("Now Playing", "Library", "Search", "Settings")
        ) { selected ->
            // wired up to real screens next
        }
    }
}
