package com.pwhs.quickmem.presentation.app.flashcard.create

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
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
import com.pwhs.quickmem.domain.model.flashcard.LanguageModel
import com.pwhs.quickmem.domain.model.flashcard.VoiceModel
import com.pwhs.quickmem.domain.model.pixabay.SearchImageResponseModel
import com.pwhs.quickmem.presentation.ads.BannerAds
import com.pwhs.quickmem.presentation.app.flashcard.component.ChipSelectImage
import com.pwhs.quickmem.presentation.app.flashcard.component.ExplanationCard
import com.pwhs.quickmem.presentation.app.flashcard.component.FlashCardTextField
import com.pwhs.quickmem.presentation.app.flashcard.component.FlashCardTopAppBar
import com.pwhs.quickmem.presentation.app.flashcard.component.FlashcardSelectImageBottomSheet
import com.pwhs.quickmem.presentation.app.flashcard.component.HintCard
import com.pwhs.quickmem.presentation.app.flashcard.component.LanguageBottomSheet
import com.pwhs.quickmem.presentation.app.flashcard.component.VoiceBottomSheet
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
import timber.log.Timber
import java.io.File

@Destination<RootGraph>(
    navArgs = CreateFlashCardArgs::class
)
@Composable
fun CreateFlashCardScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateFlashCardViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val imageCompressor = remember { ImageCompressor(context) }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                CreateFlashCardUiEvent.FlashCardSaved -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_flashcard_saved), Toast.LENGTH_SHORT
                    ).show()
                }

                CreateFlashCardUiEvent.FlashCardSaveError -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_flashcard_save_error), Toast.LENGTH_SHORT
                    ).show()
                }

                CreateFlashCardUiEvent.LoadImage -> {
                    Timber.d("Load image")
                }
            }
        }
    }
    CreateFlashCard(
        modifier = modifier,
        term = uiState.term,
        termImageUri = uiState.termImageUri,
        termImageURL = uiState.termImageURL ?: "",
        onTermImageChanged = {
            viewModel.onEvent(
                CreateFlashCardUiAction.FlashCardTermImageChanged(
                    it
                )
            )
        },
        onTermImageUrlChanged = { viewModel.onEvent(CreateFlashCardUiAction.OnTermImageChanged(it)) },
        definition = uiState.definition,
        definitionImageUri = uiState.definitionImageUri,
        definitionImageURL = uiState.definitionImageURL ?: "",
        hint = uiState.hint ?: "",
        showHint = uiState.showHint,
        explanation = uiState.explanation ?: "",
        showExplanation = uiState.showExplanation,
        isLoading = uiState.isLoading,
        onTermChanged = { viewModel.onEvent(CreateFlashCardUiAction.FlashCardTermChanged(it)) },
        onDefinitionChanged = {
            viewModel.onEvent(
                CreateFlashCardUiAction.FlashCardDefinitionChanged(
                    it
                )
            )
        },
        onDefinitionImageChanged = { uri ->
            if (uri == null) {
                viewModel.onEvent(CreateFlashCardUiAction.FlashCardDefinitionImageChanged(null))
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
                    CreateFlashCardUiAction.FlashCardDefinitionImageChanged(
                        compressedImageUri
                    )
                )
            }
        },
        onHintChanged = { viewModel.onEvent(CreateFlashCardUiAction.FlashCardHintChanged(it)) },
        onShowHintClicked = { viewModel.onEvent(CreateFlashCardUiAction.ShowHintClicked(it)) },
        onExplanationChanged = {
            viewModel.onEvent(
                CreateFlashCardUiAction.FlashCardExplanationChanged(
                    it
                )
            )
        },
        onShowExplanationClicked = {
            viewModel.onEvent(
                CreateFlashCardUiAction.ShowExplanationClicked(
                    it
                )
            )
        },
        onUploadImage = { imageUri, isTerm ->
            viewModel.onEvent(CreateFlashCardUiAction.UploadImage(imageUri, isTerm))
        },
        onDeleteImage = { isTerm ->
            viewModel.onEvent(
                CreateFlashCardUiAction.RemoveImage(
                    imageURL = if (isTerm) uiState.termImageURL
                        ?: "" else uiState.definitionImageURL ?: "",
                    isTerm = isTerm
                )
            )
        },
        onNavigationBack = {
            resultNavigator.setResult(uiState.isCreated)
            navigator.navigateUp()
        },
        onSaveFlashCardClicked = {
            viewModel.onEvent(CreateFlashCardUiAction.SaveFlashCard)
        },
        termQueryImage = uiState.termQueryImage,
        termSearchImageResponseModel = uiState.termSearchImageResponseModel,
        onQueryTermImageChanged = {
            viewModel.onEvent(
                CreateFlashCardUiAction.OnQueryTermImageChanged(
                    it
                )
            )
        },
        onDefinitionImageUrlChanged = {
            viewModel.onEvent(CreateFlashCardUiAction.OnDefinitionImageChanged(it))
        },
        isSearchDefinitionImageLoading = uiState.isSearchDefinitionImageLoading,
        definitionQueryImage = uiState.definitionQueryImage,
        definitionSearchImageResponseModel = uiState.definitionSearchImageResponseModel,
        onQueryDefinitionImageChanged = {
            viewModel.onEvent(
                CreateFlashCardUiAction.OnQueryDefinitionImageChanged(
                    it
                )
            )
        },
        isSearchTermImageLoading = uiState.isSearchTermImageLoading,
        studySetColor = uiState.studyColorModel?.hexValue?.toColor() ?: colorScheme.primary,
        termLanguageModel = uiState.termLanguageModel,
        termVoiceModel = uiState.termVoiceCode,
        termVoicesModel = uiState.termVoicesModel,
        definitionLanguageModel = uiState.definitionLanguageModel,
        definitionVoiceModel = uiState.definitionVoiceCode,
        definitionVoicesModel = uiState.definitionVoicesModel,
        languageModels = uiState.languageModels,
        onSelectTermLanguageClicked = { languageCode ->
            viewModel.onEvent(CreateFlashCardUiAction.OnSelectTermLanguageClicked(languageCode))
        },
        onTermVoiceSelected = { voiceCode ->
            viewModel.onEvent(CreateFlashCardUiAction.OnSelectTermVoiceClicked(voiceCode))
        },
        onSelectDefinitionLanguageClicked = { languageCode ->
            viewModel.onEvent(CreateFlashCardUiAction.OnSelectDefinitionLanguageClicked(languageCode))
        },
        onDefinitionVoiceSelected = { voiceCode ->
            viewModel.onEvent(CreateFlashCardUiAction.OnSelectDefinitionVoiceClicked(voiceCode))
        },
    )
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
)
@Composable
fun CreateFlashCard(
    modifier: Modifier = Modifier,
    term: String = "",
    termImageUri: Uri? = null,
    termImageURL: String = "",
    definition: String = "",
    definitionImageUri: Uri? = null,
    definitionImageURL: String = "",
    onTermImageChanged: (Uri?) -> Unit = {},
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
    onUploadImage: (Uri, Boolean) -> Unit = { _, _ -> },
    onDeleteImage: (Boolean) -> Unit = {},
    onNavigationBack: () -> Unit = {},
    onSaveFlashCardClicked: () -> Unit = {},
    termQueryImage: String = "",
    termSearchImageResponseModel: SearchImageResponseModel? = null,
    onQueryTermImageChanged: (String) -> Unit = {},
    definitionQueryImage: String = "",
    definitionSearchImageResponseModel: SearchImageResponseModel? = null,
    onQueryDefinitionImageChanged: (String) -> Unit = {},
    onDefinitionImageUrlChanged: (String) -> Unit = {},
    onTermImageUrlChanged: (String) -> Unit = {},
    isSearchTermImageLoading: Boolean = false,
    isSearchDefinitionImageLoading: Boolean = false,
    studySetColor: Color = colorScheme.primary,
    termLanguageModel: LanguageModel? = null,
    termVoiceModel: VoiceModel? = null,
    termVoicesModel: List<VoiceModel> = emptyList(),
    definitionLanguageModel: LanguageModel? = null,
    definitionVoiceModel: VoiceModel? = null,
    languageModels: List<LanguageModel> = emptyList(),
    definitionVoicesModel: List<VoiceModel> = emptyList(),
    onSelectTermLanguageClicked: (LanguageModel) -> Unit = { _ -> },
    onTermVoiceSelected: (VoiceModel) -> Unit = { _ -> },
    onSelectDefinitionLanguageClicked: (LanguageModel) -> Unit = { _ -> },
    onDefinitionVoiceSelected: (VoiceModel) -> Unit = { _ -> },
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val definitionImageCropper = rememberImageCropper()
    val definitionImagePicker = rememberImagePicker(onImage = { uri ->
        scope.launch {
            when (val result = definitionImageCropper.crop(uri, context)) {
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

    val definitionCropState = definitionImageCropper.cropState

    var showDefinitionSearchImageBottomSheet by remember {
        mutableStateOf(false)
    }

    val searchDefinitionImageBottomSheet = rememberModalBottomSheetState()

    val termImageCropper = rememberImageCropper()
    val termImagePicker = rememberImagePicker(onImage = { uri ->
        scope.launch {
            when (val result = termImageCropper.crop(uri, context)) {
                CropResult.Cancelled -> { /* Handle cancellation */
                }

                is CropError -> { /* Handle error */
                }

                is CropResult.Success -> {
                    onTermImageChanged(context.bitmapToUri(result.bitmap))
                }
            }
        }
    })

    val termCropState = termImageCropper.cropState

    var showTermSearchImageBottomSheet by remember {
        mutableStateOf(false)
    }

    val searchTermImageBottomSheet = rememberModalBottomSheetState()

    var showTermSelectLanguageBottomSheet by remember {
        mutableStateOf(false)
    }

    var showTermSelectVoiceBottomSheet by remember {
        mutableStateOf(false)
    }

    var showDefinitionSelectLanguageBottomSheet by remember {
        mutableStateOf(false)
    }

    var showDefinitionSelectVoiceBottomSheet by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                FlashCardTopAppBar(
                    onNavigationBack = onNavigationBack,
                    onSaveFlashCardClicked = onSaveFlashCardClicked,
                    enableSaveButton = term.isNotEmpty() && definition.isNotEmpty(),
                    title = stringResource(R.string.txt_create_flashcard),
                    color = studySetColor
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
                        Card(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = 5.dp,
                                focusedElevation = 8.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = colorScheme.surface
                            ),
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                FlashCardTextField(
                                    value = term,
                                    onValueChange = onTermChanged,
                                    hint = stringResource(R.string.txt_term),
                                    color = studySetColor
                                )
                                FlowRow(
                                    modifier = Modifier.padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    InputChip(
                                        selected = showTermSelectLanguageBottomSheet,
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = when {
                                                showTermSelectLanguageBottomSheet -> studySetColor
                                                else -> Color.Gray
                                            }
                                        ),
                                        onClick = {
                                            showTermSelectLanguageBottomSheet = true
                                        },
                                        label = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Language,
                                                    contentDescription = null,
                                                    tint = when {
                                                        showTermSelectLanguageBottomSheet -> studySetColor
                                                        else -> Color.Gray
                                                    }
                                                )
                                                Text(
                                                    text = termLanguageModel?.name
                                                        ?: stringResource(R.string.txt_english_us),
                                                    color = when {
                                                        showTermSelectLanguageBottomSheet -> studySetColor
                                                        else -> Color.Gray
                                                    }
                                                )
                                            }
                                        }
                                    )

                                    InputChip(
                                        selected = false,
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = when {
                                                false -> studySetColor
                                                else -> Color.Gray
                                            }
                                        ),
                                        onClick = {
                                            showTermSelectVoiceBottomSheet = true
                                        },
                                        label = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.KeyboardVoice,
                                                    contentDescription = null,
                                                    tint = when {
                                                        showTermSelectVoiceBottomSheet -> studySetColor
                                                        else -> Color.Gray
                                                    }
                                                )
                                                Text(
                                                    text = termVoiceModel?.name
                                                        ?: stringResource(R.string.txt_voice),
                                                    color = when {
                                                        showTermSelectVoiceBottomSheet -> studySetColor
                                                        else -> Color.Gray
                                                    }
                                                )
                                            }
                                        }
                                    )

                                    InputChip(
                                        selected = showHint,
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = when {
                                                showHint -> studySetColor
                                                else -> Color.Gray
                                            }
                                        ),
                                        onClick = {
                                            onShowHintClicked(!showHint)
                                        },
                                        label = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Lightbulb,
                                                    contentDescription = null,
                                                    tint = when {
                                                        showHint -> studySetColor
                                                        else -> Color.Gray
                                                    }
                                                )
                                                Text(
                                                    text = stringResource(R.string.txt_hint),
                                                    color = when {
                                                        showHint -> studySetColor
                                                        else -> Color.Gray
                                                    }
                                                )
                                            }
                                        }
                                    )

                                    ChipSelectImage(
                                        onUploadImage = {
                                            onUploadImage(it, true)
                                        },
                                        imageUri = termImageUri,
                                        imageUrl = termImageURL,
                                        onDeleteImage = {
                                            onDeleteImage(true)
                                        },
                                        color = when {
                                            termImageUri != null || termImageURL.isNotEmpty() -> studySetColor
                                            else -> Color.Gray
                                        },
                                        onChooseImage = {
                                            showTermSearchImageBottomSheet = true
                                        },
                                        label = stringResource(R.string.txt_term_image)
                                    )
                                }
                            }
                        }
                    }
                    item {
                        if (showHint) {
                            HintCard(
                                hint = hint,
                                onHintChanged = onHintChanged,
                                onShowHintClicked = onShowHintClicked,
                                color = studySetColor
                            )
                        }
                    }
                    // Definition
                    item {
                        Card(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = 5.dp,
                                focusedElevation = 8.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = colorScheme.surface
                            ),
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                FlashCardTextField(
                                    value = definition,
                                    onValueChange = onDefinitionChanged,
                                    hint = stringResource(R.string.txt_definition),
                                    color = studySetColor
                                )
                                FlowRow(
                                    modifier = Modifier.padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    InputChip(
                                        selected = showDefinitionSelectLanguageBottomSheet,
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = when {
                                                showDefinitionSelectLanguageBottomSheet -> studySetColor
                                                else -> Color.Gray
                                            }
                                        ),
                                        onClick = {
                                            showDefinitionSelectLanguageBottomSheet = true
                                        },
                                        label = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Language,
                                                    contentDescription = null,
                                                    tint = when {
                                                        showDefinitionSelectLanguageBottomSheet -> studySetColor
                                                        else -> Color.Gray
                                                    }
                                                )
                                                Text(
                                                    text = definitionLanguageModel?.name
                                                        ?: stringResource(R.string.txt_english_us),
                                                    color = when {
                                                        showDefinitionSelectLanguageBottomSheet -> studySetColor
                                                        else -> Color.Gray
                                                    }
                                                )
                                            }
                                        }
                                    )

                                    InputChip(
                                        selected = false,
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = when {
                                                false -> studySetColor
                                                else -> Color.Gray
                                            }
                                        ),
                                        onClick = {
                                            showDefinitionSelectVoiceBottomSheet = true
                                        },
                                        label = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.KeyboardVoice,
                                                    contentDescription = null,
                                                    tint = when {
                                                        showDefinitionSelectVoiceBottomSheet -> studySetColor
                                                        else -> Color.Gray
                                                    }
                                                )
                                                Text(
                                                    text = definitionVoiceModel?.name
                                                        ?: stringResource(R.string.txt_voice),
                                                    color = when {
                                                        showDefinitionSelectVoiceBottomSheet -> studySetColor
                                                        else -> Color.Gray
                                                    }
                                                )
                                            }
                                        }
                                    )

                                    InputChip(
                                        selected = showExplanation,
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = when {
                                                showExplanation -> studySetColor
                                                else -> Color.Gray
                                            }
                                        ),
                                        onClick = {
                                            onShowExplanationClicked(!showExplanation)
                                        },
                                        label = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Lightbulb,
                                                    contentDescription = null,
                                                    tint = when {
                                                        showExplanation -> studySetColor
                                                        else -> Color.Gray
                                                    }
                                                )
                                                Text(
                                                    text = stringResource(R.string.txt_explanation),
                                                    color = when {
                                                        showExplanation -> studySetColor
                                                        else -> Color.Gray
                                                    }
                                                )
                                            }
                                        }
                                    )

                                    ChipSelectImage(
                                        onUploadImage = {
                                            onUploadImage(it, false)
                                        },
                                        imageUri = definitionImageUri,
                                        imageUrl = definitionImageURL,
                                        onDeleteImage = {
                                            onDeleteImage(false)
                                        },
                                        color = when {
                                            definitionImageUri != null || definitionImageURL.isNotEmpty() -> studySetColor
                                            else -> Color.Gray
                                        },
                                        onChooseImage = {
                                            showDefinitionSearchImageBottomSheet = true
                                        },
                                        label = stringResource(R.string.txt_definition_image)
                                    )
                                }
                            }
                        }
                    }
                    item {
                        if (showExplanation) {
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
        }
    }
    if (showDefinitionSearchImageBottomSheet) {
        FlashcardSelectImageBottomSheet(
            modifier = Modifier,
            searchImageBottomSheet = searchDefinitionImageBottomSheet,
            onDismissRequest = {
                showDefinitionSearchImageBottomSheet = false
            },
            queryImage = definitionQueryImage,
            searchImageResponseModel = definitionSearchImageResponseModel,
            onQueryImageChanged = onQueryDefinitionImageChanged,
            isSearchImageLoading = isSearchDefinitionImageLoading,
            onImageUrlChanged = {
                onDefinitionImageUrlChanged(it)
                onDefinitionImageChanged(null)
            },
            imagePicker = definitionImagePicker
        )
    }

    if (definitionCropState != null) {
        ImageCropperDialog(
            state = definitionCropState,
        )
    }

    if (showTermSearchImageBottomSheet) {
        FlashcardSelectImageBottomSheet(
            modifier = Modifier,
            searchImageBottomSheet = searchTermImageBottomSheet,
            onDismissRequest = {
                showTermSearchImageBottomSheet = false
            },
            queryImage = termQueryImage,
            searchImageResponseModel = termSearchImageResponseModel,
            onQueryImageChanged = onQueryTermImageChanged,
            isSearchImageLoading = isSearchTermImageLoading,
            onImageUrlChanged = {
                onTermImageUrlChanged(it)
                onTermImageChanged(null)
            },
            imagePicker = termImagePicker
        )
    }

    if (termCropState != null) {
        ImageCropperDialog(
            state = termCropState,
        )
    }

    if (showTermSelectLanguageBottomSheet) {
        LanguageBottomSheet(
            onDismissRequest = {
                showTermSelectLanguageBottomSheet = false
            },
            languageList = languageModels,
            onLanguageSelected = { language ->
                onSelectTermLanguageClicked(language)
                showTermSelectVoiceBottomSheet = true
            },
            languageModel = termLanguageModel,
        )
    }

    if (showTermSelectVoiceBottomSheet) {
        VoiceBottomSheet(
            onDismissRequest = {
                showTermSelectVoiceBottomSheet = false
            },
            voiceList = termVoicesModel,
            onVoiceSelected = onTermVoiceSelected,
            voiceModel = termVoiceModel,
        )
    }
    if (showDefinitionSelectLanguageBottomSheet) {
        LanguageBottomSheet(
            onDismissRequest = {
                showDefinitionSelectLanguageBottomSheet = false
            },
            languageList = languageModels,
            onLanguageSelected = { language ->
                onSelectDefinitionLanguageClicked(language)
                showDefinitionSelectVoiceBottomSheet = true
            },
            languageModel = definitionLanguageModel,
        )
    }

    if (showDefinitionSelectVoiceBottomSheet) {
        VoiceBottomSheet(
            onDismissRequest = {
                showDefinitionSelectVoiceBottomSheet = false
            },
            voiceList = definitionVoicesModel,
            onVoiceSelected = onDefinitionVoiceSelected,
            voiceModel = definitionVoiceModel,
        )
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