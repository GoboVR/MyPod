package com.gobovr.mypod.ui.nav

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp

/**
 * MyPod's core input abstraction.
 *
 * Android already maps a connected game controller's D-Pad AND its analog
 * stick (when treated as a digital D-Pad) to KEYCODE_DPAD_UP/DOWN/LEFT/RIGHT
 * key events, and physical/keyboard arrow keys produce the exact same key
 * codes. That means one focus-navigation system, built on Compose's built-in
 * `focusable()` + `onKeyEvent`, handles D-Pad, joystick, AND keyboard for free
 * -- we don't need three separate input handlers.
 *
 * Each focusable row/tile in the UI wraps its Modifier with
 * `.focusableListItem()` below. Up/Down/Left/Right move focus using
 * Compose's built-in FocusManager (moveFocus), and DPAD_CENTER / Enter
 * triggers onSelect.
 */
@Composable
fun Modifier.focusableListItem(
    onSelect: () -> Unit
): Modifier {
    var isFocused by remember { mutableStateOf(false) }
    return this
        .focusable()
        .onFocusChanged { isFocused = it.isFocused }
        .onKeyEvent { event ->
            if (event.key == Key.DirectionCenter || event.key == Key.Enter) {
                onSelect()
                true
            } else {
                false
            }
        }
}

/** Simple demo list proving focus moves correctly with D-Pad/joystick/arrow keys. */
@Composable
fun DemoFocusableList(items: List<String>, onSelect: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        items.forEach { label ->
            val requester = remember { FocusRequester() }
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .focusRequester(requester)
                    .focusableListItem { onSelect(label) }
                    .padding(vertical = 12.dp)
            )
        }
    }
}
