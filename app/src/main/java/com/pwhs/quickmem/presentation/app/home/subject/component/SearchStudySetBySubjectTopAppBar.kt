package com.pwhs.quickmem.presentation.app.home.subject.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.app.library.component.SearchTextField
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.gradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchStudySetBySubjectTopAppBar(
    modifier: Modifier = Modifier,
    name: String? = null,
    description: String? = null,
    color: Color,
    studySetCount: Int = 0,
    @DrawableRes icon: Int? = null,
    onNavigateBack: () -> Unit,
    onAddStudySet: () -> Unit = {},
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
) {
    var isSearchExpanded by remember { mutableStateOf(false) }
    LargeTopAppBar(
        modifier = modifier.background(color.gradientBackground()),
        colors = topAppBarColors(
            containerColor = Color.Transparent,
        ),
        title = {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(end = 16.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    icon?.let {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            tint = colorScheme.onSurface,
                            modifier = Modifier
                                .size(30.dp)
                                .padding(end = 8.dp)
                        )
                    }
                    name?.let {
                        Text(
                            text = name,
                            style = typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.onSurface,
                                fontSize = 20.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Text(
                    text = stringResource(R.string.txt_study_sets_count, studySetCount),
                    style = typography.bodyMedium.copy(
                        color = colorScheme.secondary
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                description?.let {
                    Text(
                        text = description,
                        style = typography.bodyMedium.copy(
                            color = colorScheme.secondary
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                AnimatedVisibility(
                    visible = isSearchExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    SearchTextField(
                        searchQuery = searchQuery,
                        onSearchQueryChange = onSearchQueryChange,
                        placeholder = stringResource(R.string.txt_search_study_sets),
                    )
                }
            }
        },
        expandedHeight = if (isSearchExpanded) 250.dp else 200.dp,
        collapsedHeight = 60.dp,
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack,
            ) {
                Icon(
                    imageVector = AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.txt_back),
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(
                onClick = onAddStudySet,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.txt_add_study_set),
                    tint = Color.White
                )
            }

            IconButton(
                onClick = { isSearchExpanded = !isSearchExpanded },
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.txt_search),
                    tint = Color.White
                )
            }
        }
    )
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
fun TopBarSearchPreview() {
    QuickMemTheme {
        Scaffold(
            topBar = {
                SearchStudySetBySubjectTopAppBar(
                    name = "Anthropology",
                    description = "Agriculture is the study of farming and cultivation of land.",
                    onNavigateBack = {},
                    color = Color(0xFF7f60f9)
                )
            }
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) { }
        }
    }
}