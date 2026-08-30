package com.gobovr.mypod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "MyPod",
            style = ZuneTileTitle,
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
        )
        DemoFocusableList(
            items = listOf("Now Playing", "Library", "Search", "Settings")
        ) { selected ->
            // wired up to real screens next
        }
    }
}
