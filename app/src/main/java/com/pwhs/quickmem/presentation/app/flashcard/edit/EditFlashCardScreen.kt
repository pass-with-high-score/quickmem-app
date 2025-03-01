package com.pwhs.quickmem.presentation.app.flashcard.edit

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.pwhs.quickmem.domain.model.pixabay.SearchImageResponseModel
import com.pwhs.quickmem.presentation.ads.BannerAds
import com.pwhs.quickmem.presentation.app.flashcard.component.ChipSelectImage
import com.pwhs.quickmem.presentation.app.flashcard.component.ExplanationCard
import com.pwhs.quickmem.presentation.app.flashcard.component.FlashCardTextFieldContainer
import com.pwhs.quickmem.presentation.app.flashcard.component.FlashCardTopAppBar
import com.pwhs.quickmem.presentation.app.flashcard.component.FlashcardSelectImageBottomSheet
import com.pwhs.quickmem.presentation.app.flashcard.component.HintCard
import com.pwhs.quickmem.presentation.components.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.ImageCompressor
import com.pwhs.quickmem.utils.bitmapToUri
import com.pwhs.quickmem.utils.toColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.launch
import java.io.File

@Destination<RootGraph>(
    navArgs = EditFlashCardArgs::class
)
@Composable
fun EditFlashCardScreen(
    modifier: Modifier = Modifier,
    viewModel: EditFlashCardViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val imageCompressor = remember { ImageCompressor(context) }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is EditFlashCardUiEvent.FlashCardSaved -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_flashcard_saved), Toast.LENGTH_SHORT
                    ).show()
                    resultNavigator.navigateBack(true)
                }

                is EditFlashCardUiEvent.FlashCardSaveError -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_flashcard_save_error), Toast.LENGTH_SHORT
                    ).show()
                }

                is EditFlashCardUiEvent.FlashCardDeleted -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_flashcard_deleted), Toast.LENGTH_SHORT
                    ).show()
                    resultNavigator.navigateBack(true)
                }
            }
        }
    }
    CreateFlashCard(
        modifier = modifier,
        term = uiState.term,
        definition = uiState.definition,
        definitionImageUri = uiState.definitionImageUri,
        definitionImageURL = uiState.definitionImageURL ?: "",
        hint = uiState.hint ?: "",
        showHint = uiState.showHint,
        explanation = uiState.explanation ?: "",
        showExplanation = uiState.showExplanation,
        isLoading = uiState.isLoading,
        onTermChanged = { viewModel.onEvent(EditFlashCardUiAction.FlashCardTermChanged(it)) },
        onDefinitionChanged = {
            viewModel.onEvent(
                EditFlashCardUiAction.FlashCardDefinitionChanged(
                    it
                )
            )
        },
        onDefinitionImageChanged = { uri ->
            if (uri == null) {
                viewModel.onEvent(EditFlashCardUiAction.FlashCardDefinitionImageChanged(null))
                return@CreateFlashCard
            }
            scope.launch {
                val compressedImageBytes = imageCompressor.compressImage(uri, 200 * 1024L) // 200KB
                val compressedImageUri = compressedImageBytes?.let {
                    Uri.fromFile(
                        File(
                            context.cacheDir,
                            "compressed_image_${System.currentTimeMillis()}.jpg"
                        ).apply {
                            writeBytes(it)
                        })
                }
                viewModel.onEvent(
                    EditFlashCardUiAction.FlashCardDefinitionImageChanged(
                        compressedImageUri
                    )
                )
            }
        },
        onHintChanged = { viewModel.onEvent(EditFlashCardUiAction.FlashCardHintChanged(it)) },
        onShowHintClicked = { viewModel.onEvent(EditFlashCardUiAction.ShowHintClicked(it)) },
        onExplanationChanged = {
            viewModel.onEvent(
                EditFlashCardUiAction.FlashCardExplanationChanged(
                    it
                )
            )
        },
        onShowExplanationClicked = {
            viewModel.onEvent(
                EditFlashCardUiAction.ShowExplanationClicked(
                    it
                )
            )
        },
        onUploadImage = { viewModel.onEvent(EditFlashCardUiAction.UploadImage(it)) },
        onDeleteImage = {
            viewModel.onEvent(
                EditFlashCardUiAction.RemoveImage(
                    uiState.definitionImageURL ?: ""
                )
            )
        },
        onNavigationBack = {
            resultNavigator.setResult(true)
            navigator.navigateUp()
        },
        onSaveFlashCardClicked = {
            viewModel.onEvent(EditFlashCardUiAction.SaveFlashCard)
        },
        queryImage = uiState.queryImage,
        searchImageResponseModel = uiState.searchImageResponseModel,
        onQueryImageChanged = { viewModel.onEvent(EditFlashCardUiAction.OnQueryImageChanged(it)) },
        onDefinitionImageUrlChanged = {
            viewModel.onEvent(EditFlashCardUiAction.OnDefinitionImageChanged(it))
        },
        isSearchImageLoading = uiState.isSearchImageLoading,
        studySetColor = uiState.studyColorModel?.hexValue?.toColor() ?: colorScheme.primary,
        onDeleteFlashCard = {
            viewModel.onEvent(EditFlashCardUiAction.OnDeleteFlashCard)
        }
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun CreateFlashCard(
    modifier: Modifier = Modifier,
    term: String = "",
    definition: String = "",
    definitionImageUri: Uri? = null,
    definitionImageURL: String = "",
    isLoading: Boolean = false,
    hint: String = "",
    showHint: Boolean = false,
    explanation: String = "",
    showExplanation: Boolean = false,
    onTermChanged: (String) -> Unit = {},
    onDefinitionChanged: (String) -> Unit = {},
    onDefinitionImageChanged: (Uri?) -> Unit = {},
    onHintChanged: (String) -> Unit = {},
    onShowHintClicked: (Boolean) -> Unit = {},
    onExplanationChanged: (String) -> Unit = {},
    onShowExplanationClicked: (Boolean) -> Unit = {},
    onUploadImage: (Uri) -> Unit = {},
    onDeleteImage: () -> Unit = {},
    onNavigationBack: () -> Unit = {},
    onSaveFlashCardClicked: () -> Unit = {},
    queryImage: String = "",
    searchImageResponseModel: SearchImageResponseModel? = null,
    onQueryImageChanged: (String) -> Unit = {},
    onDefinitionImageUrlChanged: (String) -> Unit = {},
    isSearchImageLoading: Boolean = false,
    studySetColor: Color = colorScheme.primary,
    onDeleteFlashCard: () -> Unit = {},
) {

    val imageCropper = rememberImageCropper()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val imagePicker = rememberImagePicker(onImage = { uri ->
        scope.launch {
            when (val result = imageCropper.crop(uri, context)) {
                CropResult.Cancelled -> { /* Handle cancellation */
                }

                is CropError -> { /* Handle error */
                }

                is CropResult.Success -> {
                    onDefinitionImageChanged(context.bitmapToUri(result.bitmap))
                }
            }
        }
    })

    val cropState = imageCropper.cropState

    var showSearchImageBottomSheet by remember {
        mutableStateOf(false)
    }

    val searchImageBottomSheet = rememberModalBottomSheetState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                FlashCardTopAppBar(
                    onNavigationBack = onNavigationBack,
                    onSaveFlashCardClicked = onSaveFlashCardClicked,
                    enableSaveButton = term.isNotEmpty() && definition.isNotEmpty(),
                    title = stringResource(R.string.txt_edit_flashcard),
                    color = studySetColor,
                )
            },
            modifier = modifier
                .fillMaxSize()
        ) { innerPadding ->
            Box(contentAlignment = Alignment.TopCenter) {
                LazyColumn(
                    modifier = modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .imePadding()
                ) {

                    item {
                        ChipSelectImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            onUploadImage = onUploadImage,
                            imageUri = definitionImageUri,
                            imageUrl = definitionImageURL,
                            onDeleteImage = onDeleteImage,
                            onChooseImage = {
                                showSearchImageBottomSheet = true
                            },
                            label = stringResource(R.string.txt_definition_image),
                        )
                    }
                    item {
                        FlashCardTextFieldContainer(
                            term = term,
                            onTermChanged = onTermChanged,
                            definition = definition,
                            onDefinitionChanged = onDefinitionChanged,
                            color = studySetColor
                        )
                    }

                    item {
                        if (showHint || hint.isNotEmpty()) {
                            HintCard(
                                hint = hint,
                                onHintChanged = onHintChanged,
                                onShowHintClicked = onShowHintClicked,
                                color = studySetColor
                            )
                        }
                    }

                    item {
                        if (showExplanation || explanation.isNotEmpty()) {
                            ExplanationCard(
                                explanation = explanation,
                                onExplanationChanged = onExplanationChanged,
                                onShowExplanationClicked = onShowExplanationClicked,
                                color = studySetColor
                            )
                        }
                    }

                    item {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            color = studySetColor.copy(alpha = 0.5f)
                        )
                    }

                    item {
                        Text(
                            text = stringResource(R.string.txt_make_your_term_and_definition_as_clear_as_possible_you_can_add_hint_and_explanation_to_help_you_remember_better),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .padding(bottom = 32.dp),
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }

                }
                BannerAds(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
                LoadingOverlay(isLoading = isLoading)
            }
            if (showSearchImageBottomSheet) {
                FlashcardSelectImageBottomSheet(
                    modifier = Modifier,
                    searchImageBottomSheet = searchImageBottomSheet,
                    onDismissRequest = {
                        showSearchImageBottomSheet = false
                    },
                    queryImage = queryImage,
                    searchImageResponseModel = searchImageResponseModel,
                    onQueryImageChanged = onQueryImageChanged,
                    isSearchImageLoading = isSearchImageLoading,
                    onDefinitionImageUrlChanged = {
                        onDefinitionImageUrlChanged(it)
                        onDefinitionImageChanged(null)
                    },
                    imagePicker = imagePicker
                )
            }
        }
        if (cropState != null) {
            ImageCropperDialog(
                state = cropState
            )
        }
    }

}


@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
fun CreateFlashCardPreview() {
    QuickMemTheme {
        CreateFlashCard()
    }
}