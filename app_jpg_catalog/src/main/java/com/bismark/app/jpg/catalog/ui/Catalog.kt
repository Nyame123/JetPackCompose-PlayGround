package com.bismark.app.jpg.catalog.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bismark.core.ui.component.JPGDropdownMenuButton
import com.bismark.core.ui.component.JPGFilledButton
import com.bismark.core.ui.component.JPGFilterChip
import com.bismark.core.ui.component.JPGNavigationBar
import com.bismark.core.ui.component.JPGNavigationBarItem
import com.bismark.core.ui.component.JPGOutlinedButton
import com.bismark.core.ui.component.JPGTab
import com.bismark.core.ui.component.JPGTabRow
import com.bismark.core.ui.component.JPGTextButton
import com.bismark.core.ui.component.JPGToggleButton
import com.bismark.core.ui.component.JPGTopicTag
import com.bismark.core.ui.component.JPGViewToggleButton
import com.bismark.core.ui.icons.JPGIcons
import com.bismark.core.ui.theme.JPGTheme

/**
 * Now in Android component catalog.
 */
@Composable
fun JpgCatalog() {
    JPGTheme {
        Surface {
            val contentPadding = WindowInsets
                .systemBars
                .add(WindowInsets(left = 16.dp, top = 16.dp, right = 16.dp, bottom = 16.dp))
                .asPaddingValues()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "JPG Catalog",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
                item { Text("Buttons", Modifier.padding(top = 16.dp)) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        JPGFilledButton(onClick = {}) {
                            Text(text = "Enabled")
                        }
                        JPGOutlinedButton(onClick = {}) {
                            Text(text = "Enabled")
                        }
                        JPGTextButton(onClick = {}) {
                            Text(text = "Enabled")
                        }
                    }
                }
                item { Text("Disabled buttons", Modifier.padding(top = 16.dp)) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        JPGFilledButton(
                            onClick = {},
                            enabled = false
                        ) {
                            Text(text = "Disabled")
                        }
                        JPGOutlinedButton(
                            onClick = {},
                            enabled = false
                        ) {
                            Text(text = "Disabled")
                        }
                        JPGTextButton(
                            onClick = {},
                            enabled = false
                        ) {
                            Text(text = "Disabled")
                        }
                    }
                }
                item { Text("Buttons with leading icons", Modifier.padding(top = 16.dp)) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        JPGFilledButton(
                            onClick = {},
                            text = { Text(text = "Enabled") },
                            leadingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                        JPGOutlinedButton(
                            onClick = {},
                            text = { Text(text = "Enabled") },
                            leadingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                        JPGTextButton(
                            onClick = {},
                            text = { Text(text = "Enabled") },
                            leadingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                    }
                }
                item { Text("Disabled buttons with leading icons", Modifier.padding(top = 16.dp)) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        JPGFilledButton(
                            onClick = {},
                            enabled = false,
                            text = { Text(text = "Disabled") },
                            leadingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                        JPGOutlinedButton(
                            onClick = {},
                            enabled = false,
                            text = { Text(text = "Disabled") },
                            leadingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                        JPGTextButton(
                            onClick = {},
                            enabled = false,
                            text = { Text(text = "Disabled") },
                            leadingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                    }
                }
                item { Text("Buttons with trailing icons", Modifier.padding(top = 16.dp)) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        JPGFilledButton(
                            onClick = {},
                            text = { Text(text = "Enabled") },
                            trailingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                        JPGOutlinedButton(
                            onClick = {},
                            text = { Text(text = "Enabled") },
                            trailingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                        JPGTextButton(
                            onClick = {},
                            text = { Text(text = "Enabled") },
                            trailingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                    }
                }
                item { Text("Disabled buttons with trailing icons", Modifier.padding(top = 16.dp)) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        JPGFilledButton(
                            onClick = {},
                            enabled = false,
                            text = { Text(text = "Disabled") },
                            trailingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                        JPGOutlinedButton(
                            onClick = {},
                            enabled = false,
                            text = { Text(text = "Disabled") },
                            trailingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                        JPGTextButton(
                            onClick = {},
                            enabled = false,
                            text = { Text(text = "Disabled") },
                            trailingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                    }
                }
                item { Text("Small buttons", Modifier.padding(top = 16.dp)) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        JPGFilledButton(
                            onClick = {},
                            small = true
                        ) {
                            Text(text = "Enabled")
                        }
                        JPGOutlinedButton(
                            onClick = {},
                            small = true
                        ) {
                            Text(text = "Enabled")
                        }
                        JPGTextButton(
                            onClick = {},
                            small = true
                        ) {
                            Text(text = "Enabled")
                        }
                    }
                }
                item { Text("Disabled small buttons", Modifier.padding(top = 16.dp)) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        JPGFilledButton(
                            onClick = {},
                            enabled = false,
                            small = true
                        ) {
                            Text(text = "Disabled")
                        }
                        JPGOutlinedButton(
                            onClick = {},
                            enabled = false,
                            small = true
                        ) {
                            Text(text = "Disabled")
                        }
                        JPGTextButton(
                            onClick = {},
                            enabled = false,
                            small = true
                        ) {
                            Text(text = "Disabled")
                        }
                    }
                }
                item { Text("Small buttons with leading icons", Modifier.padding(top = 16.dp)) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        JPGFilledButton(
                            onClick = {},
                            small = true,
                            text = { Text(text = "Enabled") },
                            leadingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                        JPGOutlinedButton(
                            onClick = {},
                            small = true,
                            text = { Text(text = "Enabled") },
                            leadingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                        JPGTextButton(
                            onClick = {},
                            small = true,
                            text = { Text(text = "Enabled") },
                            leadingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                    }
                }
                item {
                    Text(
                        "Disabled small buttons with leading icons",
                        Modifier.padding(top = 16.dp)
                    )
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        JPGFilledButton(
                            onClick = {},
                            enabled = false,
                            small = true,
                            text = { Text(text = "Disabled") },
                            leadingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                        JPGOutlinedButton(
                            onClick = {},
                            enabled = false,
                            small = true,
                            text = { Text(text = "Disabled") },
                            leadingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                        JPGTextButton(
                            onClick = {},
                            enabled = false,
                            small = true,
                            text = { Text(text = "Disabled") },
                            leadingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                    }
                }
                item { Text("Small buttons with trailing icons", Modifier.padding(top = 16.dp)) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        JPGFilledButton(
                            onClick = {},
                            small = true,
                            text = { Text(text = "Enabled") },
                            trailingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                        JPGOutlinedButton(
                            onClick = {},
                            small = true,
                            text = { Text(text = "Enabled") },
                            trailingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                        JPGTextButton(
                            onClick = {},
                            small = true,
                            text = { Text(text = "Enabled") },
                            trailingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                    }
                }
                item {
                    Text(
                        "Disabled small buttons with trailing icons",
                        Modifier.padding(top = 16.dp)
                    )
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        JPGFilledButton(
                            onClick = {},
                            enabled = false,
                            small = true,
                            text = { Text(text = "Disabled") },
                            trailingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                        JPGOutlinedButton(
                            onClick = {},
                            enabled = false,
                            small = true,
                            text = { Text(text = "Disabled") },
                            trailingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                        JPGTextButton(
                            onClick = {},
                            enabled = false,
                            small = true,
                            text = { Text(text = "Disabled") },
                            trailingIcon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            }
                        )
                    }
                }
                item { Text("Dropdown menu", Modifier.padding(top = 16.dp)) }
                item {
                    JPGDropdownMenuButton(
                        text = { Text("Newest first") },
                        items = listOf("Item 1", "Item 2", "Item 3"),
                        onItemClick = {},
                        itemText = { item -> Text(item) }
                    )
                }
                item { Text("Chips", Modifier.padding(top = 16.dp)) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        var firstChecked by remember { mutableStateOf(false) }
                        JPGFilterChip(
                            checked = firstChecked,
                            onCheckedChange = { checked -> firstChecked = checked },
                            text = { Text(text = "Enabled".uppercase()) }
                        )
                        var secondChecked by remember { mutableStateOf(true) }
                        JPGFilterChip(
                            checked = secondChecked,
                            onCheckedChange = { checked -> secondChecked = checked },
                            text = { Text(text = "Enabled".uppercase()) }
                        )
                        var thirdChecked by remember { mutableStateOf(true) }
                        JPGFilterChip(
                            checked = thirdChecked,
                            onCheckedChange = { checked -> thirdChecked = checked },
                            enabled = false,
                            text = { Text(text = "Disabled".uppercase()) }
                        )
                    }
                }
                item { Text("Toggle buttons", Modifier.padding(top = 16.dp)) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        var firstChecked by remember { mutableStateOf(false) }
                        JPGToggleButton(
                            checked = firstChecked,
                            onCheckedChange = { checked -> firstChecked = checked },
                            icon = {
                                Icon(
                                    painter = painterResource(id = JPGIcons.BookmarkBorder),
                                    contentDescription = null
                                )
                            },
                            checkedIcon = {
                                Icon(
                                    painter = painterResource(id = JPGIcons.Bookmark),
                                    contentDescription = null
                                )
                            }
                        )
                        var secondChecked by remember { mutableStateOf(true) }
                        JPGToggleButton(
                            checked = secondChecked,
                            onCheckedChange = { checked -> secondChecked = checked },
                            icon = {
                                Icon(
                                    painter = painterResource(id = JPGIcons.BookmarkBorder),
                                    contentDescription = null
                                )
                            },
                            checkedIcon = {
                                Icon(
                                    painter = painterResource(id = JPGIcons.Bookmark),
                                    contentDescription = null
                                )
                            }
                        )
                        var thirdChecked by remember { mutableStateOf(false) }
                        JPGToggleButton(
                            checked = thirdChecked,
                            onCheckedChange = { checked -> thirdChecked = checked },
                            icon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            },
                            checkedIcon = {
                                Icon(imageVector = JPGIcons.Check, contentDescription = null)
                            }
                        )
                        var fourthChecked by remember { mutableStateOf(true) }
                        JPGToggleButton(
                            checked = fourthChecked,
                            onCheckedChange = { checked -> fourthChecked = checked },
                            icon = {
                                Icon(imageVector = JPGIcons.Add, contentDescription = null)
                            },
                            checkedIcon = {
                                Icon(imageVector = JPGIcons.Check, contentDescription = null)
                            }
                        )
                    }
                }
                item { Text("View toggle", Modifier.padding(top = 16.dp)) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        var firstExpanded by remember { mutableStateOf(false) }
                        JPGViewToggleButton(
                            expanded = firstExpanded,
                            onExpandedChange = { expanded -> firstExpanded = expanded },
                            compactText = { Text(text = "Compact view") },
                            expandedText = { Text(text = "Expanded view") }
                        )
                        var secondExpanded by remember { mutableStateOf(true) }
                        JPGViewToggleButton(
                            expanded = secondExpanded,
                            onExpandedChange = { expanded -> secondExpanded = expanded },
                            compactText = { Text(text = "Compact view") },
                            expandedText = { Text(text = "Expanded view") }
                        )
                    }
                }
                item { Text("Tags", Modifier.padding(top = 16.dp)) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        var firstFollowed by remember { mutableStateOf(false) }
                        JPGTopicTag(
                            followed = firstFollowed,
                            onFollowClick = { firstFollowed = true },
                            onUnfollowClick = { firstFollowed = false },
                            onBrowseClick = {},
                            text = { Text(text = "Topic".uppercase()) },
                            followText = { Text(text = "Follow") },
                            unFollowText = { Text(text = "Unfollow") },
                            browseText = { Text(text = "Browse topic") }
                        )
                        var secondFollowed by remember { mutableStateOf(true) }
                        JPGTopicTag(
                            followed = secondFollowed,
                            onFollowClick = { secondFollowed = true },
                            onUnfollowClick = { secondFollowed = false },
                            onBrowseClick = {},
                            text = { Text(text = "Topic".uppercase()) },
                            followText = { Text(text = "Follow") },
                            unFollowText = { Text(text = "Unfollow") },
                            browseText = { Text(text = "Browse topic") }
                        )
                    }
                }
                item { Text("Tabs", Modifier.padding(top = 16.dp)) }
                item {
                    var selectedTabIndex by remember { mutableStateOf(0) }
                    val titles = listOf("Topics", "People")
                    JPGTabRow(selectedTabIndex = selectedTabIndex) {
                        titles.forEachIndexed { index, title ->
                            JPGTab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { Text(text = title) }
                            )
                        }
                    }
                }
                item { Text("Navigation", Modifier.padding(top = 16.dp)) }
                item {
                    var selectedItem by remember { mutableStateOf(0) }
                    val items = listOf("For you", "Episodes", "Saved", "Interests")
                    val icons = listOf(
                        JPGIcons.UpcomingBorder,
                        JPGIcons.MenuBookBorder,
                        JPGIcons.BookmarksBorder
                    )
                    val selectedIcons = listOf(
                        JPGIcons.Upcoming,
                        JPGIcons.MenuBook,
                        JPGIcons.Bookmarks
                    )
                    val tagIcon = JPGIcons.Tag
                    JPGNavigationBar {
                        items.forEachIndexed { index, item ->
                            JPGNavigationBarItem(
                                icon = {
                                    if (index == 3) {
                                        Icon(imageVector = tagIcon, contentDescription = null)
                                    } else {
                                        Icon(
                                            painter = painterResource(id = icons[index]),
                                            contentDescription = item
                                        )
                                    }
                                },
                                selectedIcon = {
                                    if (index == 3) {
                                        Icon(imageVector = tagIcon, contentDescription = null)
                                    } else {
                                        Icon(
                                            painter = painterResource(id = selectedIcons[index]),
                                            contentDescription = item
                                        )
                                    }
                                },
                                label = { Text(item) },
                                selected = selectedItem == index,
                                onClick = { selectedItem = index }
                            )
                        }
                    }
                }
            }
        }
    }
}
