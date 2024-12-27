package com.pwhs.quickmem.presentation.app.classes.detail.members

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.users.ClassMemberModel
import com.pwhs.quickmem.presentation.app.classes.detail.component.ClassDetailEmpty
import com.pwhs.quickmem.presentation.app.classes.detail.members.component.ClassMemberItem
import com.pwhs.quickmem.presentation.app.library.component.SearchTextField
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun MembersTabScreen(
    modifier: Modifier = Modifier,
    isOwner: Boolean,
    members: List<ClassMemberModel> = emptyList(),
    onMembersItemClicked: (ClassMemberModel) -> Unit = {},
    onAddMembersClicked: () -> Unit = {},
    onDeletedClicked: (String) -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }

    val filterMembers = members.filter {
        searchQuery.trim().takeIf { query -> query.isNotEmpty() }?.let { query ->
            it.username.contains(query, ignoreCase = true)
        } != false
    }
    when {
        members.isEmpty() -> {
            ClassDetailEmpty(
                modifier = modifier,
                title = stringResource(R.string.txt_this_class_has_no_members),
                subtitle = stringResource(R.string.txt_add_members_to_share_them_with_your_class),
                buttonTitle = stringResource(R.string.txt_add_members),
                onAddClick = onAddMembersClicked,
                isOwner = isOwner
            )
        }

        else -> {
            LazyColumn(
                modifier = modifier,
            ) {
                item {
                    SearchTextField(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it },
                        placeholder = stringResource(R.string.txt_search_folders),
                    )
                }
                items(items = filterMembers, key = { it.id }) { member ->
                    ClassMemberItem(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        classMemberModel = member,
                        onClicked = onMembersItemClicked,
                        canDelete = isOwner,
                        onDeleteClicked = onDeletedClicked,
                    )
                }
                item {
                    if (filterMembers.isEmpty() && searchQuery.trim().isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.txt_no_members_found),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(60.dp))
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MembersTabScreenPreview() {
    QuickMemTheme {
        Scaffold { innerPadding ->
            MembersTabScreen(
                isOwner = false,
                modifier = Modifier.padding(innerPadding)
            )

        }
    }

}