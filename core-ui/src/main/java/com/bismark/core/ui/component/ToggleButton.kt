package com.bismark.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Jetpack Compose Playground toggle button with icon and checked icon content slots. Wraps Material 3
 * [IconButton].
 *
 * @param checked Whether the toggle button is currently checked.
 * @param onCheckedChange Called when the user clicks the toggle button and toggles checked.
 * @param modifier Modifier to be applied to the toggle button.
 * @param enabled Controls the enabled state of the toggle button. When `false`, this toggle button
 * will not be clickable and will appear disabled to accessibility services.
 * @param icon The icon content to show when unchecked.
 * @param checkedBackgroundRadius The background radius that will be used to draw a background color
 * behind the checkedIcon when this toggle button is checked.
 * @param checkedIcon The icon content to show when checked.
 */
@Composable
fun JPGToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable () -> Unit,
    checkedBackgroundRadius: Dp = JPGToggleButtonDefaults.ToggleButtonSize / 2,
    checkedIcon: @Composable () -> Unit = icon
) {
    val checkedColor = MaterialTheme.colorScheme.primaryContainer
    val checkedRadius = with(LocalDensity.current) {
        checkedBackgroundRadius.toPx()
    }
    IconButton(
        onClick = { onCheckedChange(!checked) },
        modifier = Modifier
            .toggleable(value = checked, enabled = enabled, role = Role.Button, onValueChange = {})
            .drawBehind {
                if (checked) drawCircle(
                    color = checkedColor,
                    radius = checkedRadius
                )
            }
            .then(modifier),
        enabled = enabled,
        content = {
            Box(
                modifier = Modifier.sizeIn(
                    maxWidth = JPGToggleButtonDefaults.ToggleButtonIconSize,
                    maxHeight = JPGToggleButtonDefaults.ToggleButtonIconSize
                )
            ) {
                if (checked) checkedIcon() else icon()
            }
        }
    )
}

/**
 * Jetpack Compose Playground toggle button default values.
 */
object JPGToggleButtonDefaults {

    val ToggleButtonSize = 40.dp
    val ToggleButtonIconSize = 18.dp
}
