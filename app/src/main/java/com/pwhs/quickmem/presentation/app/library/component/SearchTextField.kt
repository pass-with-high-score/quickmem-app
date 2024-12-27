package com.pwhs.quickmem.presentation.app.library.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    searchQuery: String = "",
    placeholder: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    errorMessage: String? = null,
    onSearch: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            maxLines = 1,
            placeholder = {
                Text(
                    text = placeholder,
                    style = typography.bodyLarge.copy(
                        color = colorScheme.onSurface.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            textStyle = typography.bodyLarge.copy(
                color = colorScheme.onBackground,
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch()
                }
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = stringResource(R.string.txt_search),
                    modifier = Modifier.size(24.dp),
                    tint = colorScheme.onSurface
                )
            },
            supportingText = {
                if (errorMessage?.isEmpty() == true) {
                    Text(
                        text = errorMessage,
                        style = typography.bodySmall.copy(
                            color = colorScheme.error
                        )
                    )
                }
            },
            colors = colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                cursorColor = colorScheme.onSurface,
                focusedTextColor = colorScheme.onSurface,
                unfocusedTextColor = colorScheme.onSurface,
                errorContainerColor = Color.Transparent,
            ),
            isError = errorMessage?.isNotEmpty() == true,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SearchTextFieldPreview() {
    QuickMemTheme {
        Scaffold(
            topBar = {
                SearchTextField(
                    placeholder = "Search..."
                )
            }
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) { }
        }
    }
}