package mir.anika1d.repgit.ui.composable.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mir.anika1d.repgit.core.data.model.search.SearchQuery
import mir.anika1d.repgit.core.ui.theme.LocalPallet

@Composable
fun SearchQueryLayout(
    modifier: Modifier,
    item: SearchQuery,
    onDelete: () -> Unit,
    onClick: () -> Unit,
) {
    val localPallet = LocalPallet.current
    Row(
        modifier = modifier
            .padding(4.dp)
            .clickable { onClick.invoke() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = item.name,
            style = TextStyle(
                color = localPallet.dirtyWhite, fontSize = 19.sp
            ),
            maxLines = 1, overflow = TextOverflow.Ellipsis
        )
        IconButton(onClick = { onDelete.invoke() }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "delete_search_query",
                tint = localPallet.mint
            )
        }

    }
}