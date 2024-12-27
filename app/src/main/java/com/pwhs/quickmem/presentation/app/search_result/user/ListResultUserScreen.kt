package com.pwhs.quickmem.presentation.app.search_result.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.users.SearchUserResponseModel
import com.pwhs.quickmem.presentation.app.search_result.user.component.SearchUserResultItem
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun ListResultUserScreen(
    modifier: Modifier = Modifier,
    users: LazyPagingItems<SearchUserResponseModel>? = null,
    onUserItemClicked: (SearchUserResponseModel?) -> Unit = {},
    onUserRefresh: () -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = if (users?.itemCount == 0) 40.dp else 0.dp)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(users?.itemCount ?: 0) {
            val user = users?.get(it)
            SearchUserResultItem(
                searchMemberModel = user,
                onClicked = onUserItemClicked
            )
        }
        item {
            if (users?.itemCount == 0) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_group),
                    contentDescription = stringResource(R.string.txt_no_users_found),
                    tint = colorScheme.onSurface,
                )
                Text(
                    text = stringResource(R.string.txt_no_users_found),
                    style = typography.titleMedium.copy(
                        textAlign = TextAlign.Center,
                        color = colorScheme.onSurface
                    ),
                )
            }
        }
        item {
            users?.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(36.dp),
                            color = colorScheme.primary
                        )
                    }

                    loadState.refresh is LoadState.Error -> {
                        Image(
                            imageVector = Icons.Default.Error,
                            contentDescription = stringResource(R.string.txt_error),
                        )
                        Text(
                            text = stringResource(R.string.txt_error_occurred),
                            style = typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = onUserRefresh,
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text(text = stringResource(R.string.txt_retry))
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(36.dp),
                            color = colorScheme.primary
                        )

                    }

                    loadState.append is LoadState.Error -> {
                        Image(
                            imageVector = Icons.Default.Error,
                            contentDescription = stringResource(R.string.txt_error),
                        )
                        Text(
                            text = stringResource(R.string.txt_error_occurred),
                            style = typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = { retry() },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text(text = stringResource(R.string.txt_retry))
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.padding(60.dp))
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun ListResultUserScreenPreview() {
    QuickMemTheme {
        ListResultUserScreen()
    }
}