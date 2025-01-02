package com.pwhs.quickmem.presentation.app.profile.change_avatar

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mr0xf00.easycrop.CropError
import com.mr0xf00.easycrop.CropResult
import com.mr0xf00.easycrop.crop
import com.mr0xf00.easycrop.rememberImageCropper
import com.mr0xf00.easycrop.rememberImagePicker
import com.mr0xf00.easycrop.ui.ImageCropperDialog
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.users.AvatarResponseModel
import com.pwhs.quickmem.presentation.app.profile.change_avatar.component.AvatarItem
import com.pwhs.quickmem.presentation.app.profile.change_avatar.component.ChangeAvatarTopAppBar
import com.pwhs.quickmem.presentation.component.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.bitmapToUri
import com.pwhs.quickmem.utils.rememberImageCameraCapture
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.launch

@Destination<RootGraph>
@Composable
fun ChangeAvatarScreen(
    modifier: Modifier = Modifier,
    viewModel: ChangeAvatarViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ChangeAvatarUiEvent.AvatarUpdated -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_update_image_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    resultNavigator.navigateBack(true)
                }

                is ChangeAvatarUiEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    ChangeAvatar(
        modifier = modifier,
        isLoading = uiState.isLoading,
        avatarUrls = uiState.avatarUrls,
        selectedAvatarUrl = uiState.selectedAvatarUrl,
        onSelectedPicture = { avatarUrl ->
            viewModel.onEvent(ChangeAvatarUiAction.ImageSelected(avatarUrl))
        },
        onDoneClick = {
            viewModel.onEvent(ChangeAvatarUiAction.SaveClicked)
        },
        onNavigateBack = {
            resultNavigator.navigateBack(false)
        },
        onImageUriChange = { uri ->
            viewModel.onEvent(ChangeAvatarUiAction.OnImageUriChanged(uri))
        }
    )
}

@Composable
fun ChangeAvatar(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    avatarUrls: List<AvatarResponseModel> = emptyList(),
    selectedAvatarUrl: String? = null,
    onNavigateBack: () -> Unit = {},
    onSelectedPicture: (String) -> Unit = {},
    onDoneClick: () -> Unit = {},
    onImageUriChange: (Uri) -> Unit = {}
) {
    val imageCropper = rememberImageCropper()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val captureImage = rememberImageCameraCapture { uri ->
        scope.launch {
            when (val result = imageCropper.crop(uri, context)) {
                CropResult.Cancelled -> { /* Handle cancellation */
                }

                is CropError -> { /* Handle error */
                }

                is CropResult.Success -> {
                    onImageUriChange(context.bitmapToUri(result.bitmap))
                }
            }
        }
    }
    val imagePicker = rememberImagePicker(onImage = { uri ->
        scope.launch {
            when (val result = imageCropper.crop(uri, context)) {
                CropResult.Cancelled -> { /* Handle cancellation */
                }

                is CropError -> { /* Handle error */
                }

                is CropResult.Success -> {
                    onImageUriChange(context.bitmapToUri(result.bitmap))
                }
            }
        }
    })
    val cropState = imageCropper.cropState
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                ChangeAvatarTopAppBar(
                    title = stringResource(R.string.txt_choose_a_picture),
                    onDoneClick = onDoneClick,
                    onNavigateBack = onNavigateBack
                )
            }
        ) { paddingValues ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = modifier.fillMaxSize(),
                    // Dont you content padding, it not work
                ) {
                    item {
                        AvatarItem(
                            imageId = R.drawable.ic_camera,
                            isSelected = selectedAvatarUrl == null,
                            onSelected = {
                                captureImage()
                            }
                        )
                    }

                    item {
                        AvatarItem(
                            imageId = R.drawable.ic_gallecy,
                            isSelected = selectedAvatarUrl == null,
                            onSelected = {
                                imagePicker.pick()
                            }
                        )
                    }

                    items(avatarUrls) { avatarUrl ->
                        AvatarItem(
                            avatarUrl = avatarUrl.url,
                            isSelected = avatarUrl.url == selectedAvatarUrl,
                            onSelected = {
                                onSelectedPicture(avatarUrl.url)
                            }
                        )
                    }
                }
                LoadingOverlay(
                    isLoading = isLoading
                )
            }
        }
        if (cropState != null) {
            ImageCropperDialog(
                state = cropState,
            )
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
private fun ChangeAvatarScreenPreview() {
    QuickMemTheme {
        ChangeAvatar()
    }
}

