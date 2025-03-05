package com.pwhs.quickmem.presentation.app.search_result.user.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.users.SearchUserResponseModel
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun SearchUserResultItem(
    modifier: Modifier = Modifier,
    onClicked: (SearchUserResponseModel?) -> Unit = {},
    searchMemberModel: SearchUserResponseModel?,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        onClick = { onClicked(searchMemberModel) },
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.onSurface.copy(alpha = 0.12f)
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AsyncImage(
                        model = searchMemberModel?.avatarUrl,
                        contentDescription = stringResource(R.string.txt_avatar),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        searchMemberModel?.username ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ClassMemberItemPreview() {
    QuickMemTheme {
        Scaffold {
            LazyColumn(
                modifier = Modifier.padding(it)
            ) {
                items(10) {
                    SearchUserResultItem(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        searchMemberModel = SearchUserResponseModel(
                            id = "id",
                            username = "username",
                            avatarUrl = "",
                        )
                    )
                }
            }
        }
    }
}