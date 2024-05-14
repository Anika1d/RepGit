package mir.anika1d.repgit.ui.composable.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mir.anika1d.repgit.core.ui.theme.LocalPallet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownModeSearch(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: List<String>,
    selectedItem: String,
    onItemChange: (String) -> Unit
) {
    val localPallet = LocalPallet.current
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = onExpandedChange,
    ) {
        Text(
            text = "$selectedItem: ",
            color = localPallet.mint,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .defaultMinSize(minWidth = 40.dp)
        )
        DropdownMenu(
            modifier = Modifier
                .border(1.dp, localPallet.mint)
                .exposedDropdownSize()
                .background(localPallet.blue),

            expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
            for (i in items.indices) {
                if (i % 2 == 1)
                    HorizontalDivider(
                        color = localPallet.mint,
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp
                    )
                DropdownMenuItem(text = {
                    Text(
                        text = items[i],
                        color = localPallet.mint,
                        modifier = Modifier
                            .background(Color.Transparent),
                        maxLines = 1

                    )
                }, onClick = {
                    onItemChange(items[i])
                    onExpandedChange(false)
                }, contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                    colors = MenuItemColors(
                        textColor = localPallet.mint,
                        disabledTextColor = Color.Transparent,
                        leadingIconColor = localPallet.mint,
                        disabledLeadingIconColor = Color.Transparent,
                        trailingIconColor = localPallet.mint,
                        disabledTrailingIconColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .defaultMinSize(minWidth = 45.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun f() {
    val expanded = remember {
        mutableStateOf(false)
    }
    val listItem = remember {
        mutableStateListOf("all", "user")
    }
    val selItem = remember {
        mutableStateOf(listItem[0])
    }
    DropDownModeSearch(
        expanded = expanded.value, onExpandedChange = { expanded.value = it },
        onItemChange = { selItem.value = it },
        selectedItem = selItem.value,
        items = listItem
    )
}