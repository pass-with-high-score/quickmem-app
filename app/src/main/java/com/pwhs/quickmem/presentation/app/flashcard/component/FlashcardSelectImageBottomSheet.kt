package com.pwhs.quickmem.presentation.app.flashcard.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mr0xf00.easycrop.ImagePicker
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.pixabay.SearchImageResponseModel
import com.pwhs.quickmem.presentation.app.library.component.SearchTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardSelectImageBottomSheet(
    modifier: Modifier = Modifier,
    searchImageBottomSheet: SheetState,
    onQueryImageChanged: (String) -> Unit,
    onDefinitionImageUrlChanged: (String) -> Unit,
    queryImage: String,
    isSearchImageLoading: Boolean,
    searchImageResponseModel: SearchImageResponseModel?,
    imagePicker: ImagePicker,
    onDismissRequest: () -> Unit,
) {
    val context = LocalContext.current
    ModalBottomSheet(
        modifier = modifier,
        sheetState = searchImageBottomSheet,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.txt_search_image),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SearchTextField(
                    searchQuery = queryImage,
                    onSearch = {
                        onQueryImageChanged(queryImage)
                    },
                    onSearchQueryChange = {
                        onQueryImageChanged(it)
                    },
                    placeholder = stringResource(R.string.txt_search_image),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                )
                IconButton(
                    onClick = {
                        onDismissRequest()
                        imagePicker.pick(mimetype = "image/*")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = stringResource(R.string.txt_import_from_gallery),
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (queryImage.isEmpty()) {
                    Text(text = stringResource(R.string.txt_please_enter_a_keyword_to_search_images))
                }
            }
            if (isSearchImageLoading) {
                LinearProgressIndicator(
                    color = colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(searchImageResponseModel?.images?.size ?: 0) { index ->
                    val image = searchImageResponseModel?.images?.get(index) ?: return@items
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(image.imageUrl)
                            .error(R.drawable.ic_image_error)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                            .clickable {
                                onDefinitionImageUrlChanged(image.imageUrl)
                                onDismissRequest()
                            }
                    )
                }
            }
        }
    }
}