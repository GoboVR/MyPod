/**
 * MyPod addition: D-Pad / game controller / keyboard focus helper.
 *
 * Context: Jetpack Compose's built-in `clickable()` modifier already makes an
 * element focusable and already responds to DPAD_CENTER/Enter to trigger its
 * onClick when focused -- and Android's AndroidComposeView already routes a
 * connected controller's D-Pad AND physical/keyboard arrow keys through the
 * same KEYCODE_DPAD_UP/DOWN/LEFT/RIGHT key codes, moving focus between
 * `clickable`/`focusable` elements automatically. That means most of Meld's
 * screens (which use plain `clickable {}` rows) should already have
 * reasonable D-Pad/keyboard support without any extra code.
 *
 * The exceptions are components that bypass `clickable()` and roll their own
 * gesture handling with `pointerInput { detectTapGestures { ... } }` --
 * things like the seek bar, mini player swipe, lyrics view, and bottom
 * sheet drag handle. Those need an explicit, designed D-Pad equivalent
 * (e.g. "focus the seek bar, then Left/Right seeks") rather than a blind
 * wrapper, so they're deliberately not patched here -- see MYPOD_TODO.md.
 *
 * Use [focusableSelectable] on any custom composable that needs an explicit,
 * consistent focus-highlight + D-Pad/keyboard/touch select behavior.
 */
package com.metrolist.music.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp

/**
 * Wraps [Modifier.clickable] with a visible focus ring so a D-Pad/keyboard
 * user can always see which row/tile is selected -- clickable() alone gives
 * you the *behavior* for free, but not a visual focus indicator, which Meld
 * (a touch-first app) doesn't currently draw anywhere.
 */
@Composable
fun Modifier.focusableSelectable(
    onSelect: () -> Unit
): Modifier {
    var isFocused by remember { mutableStateOf(false) }
    val focusColor = LocalContentColor.current
    return this
        .onFocusChanged { isFocused = it.isFocused }
        .then(
            if (isFocused) {
                Modifier
                    .border(2.dp, focusColor.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                    .background(focusColor.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
            } else {
                Modifier
            }
        )
        .clickable { onSelect() }
        .onKeyEvent { event ->
            // clickable() already triggers onClick for DirectionCenter/Enter;
            // this is a defensive fallback in case that changes upstream.
            if (event.key == Key.DirectionCenter || event.key == Key.Enter) {
                onSelect()
                true
            } else {
                false
            }
        }
}
