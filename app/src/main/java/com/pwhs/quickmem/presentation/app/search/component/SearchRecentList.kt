package com.pwhs.quickmem.presentation.app.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.search.SearchQueryModel
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun SearchRecentList(
    modifier: Modifier = Modifier,
    listResult: List<SearchQueryModel> = emptyList(),
    onSearchRecent: (String) -> Unit = {},
    onClearAll: () -> Unit = {},
    onDelete: (SearchQueryModel) -> Unit = {},
) {
    Column(
        modifier = modifier.padding(start = 20.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.txt_recent),
                style = typography.bodyLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = colorScheme.onSurface
                )
            )

            TextButton(
                onClick = onClearAll,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.txt_clear_all),
                    style = typography.bodyLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = colorScheme.primary
                    )
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(listResult.asReversed()) { result ->
                SearchRecentItem(
                    modifier = Modifier.padding(top = 8.dp),
                    query = result.query,
                    onSearchRecent = onSearchRecent,
                    onDelete = { onDelete(result) }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewListSearchRecent() {
    val sampleData = listOf(
        SearchQueryModel(id = 1, timestamp = 1682939400000L, query = "Android development"),
        SearchQueryModel(id = 2, timestamp = 1683025800000L, query = "Compose UI"),
        SearchQueryModel(id = 3, timestamp = 1683112200000L, query = "Kotlin programming"),
        SearchQueryModel(id = 4, timestamp = 1683198600000L, query = "Clean architecture"),
        SearchQueryModel(id = 5, timestamp = 1683285000000L, query = "Room database")
    )

    QuickMemTheme {
        Scaffold { innerPadding ->
            SearchRecentList(
                listResult = sampleData,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}