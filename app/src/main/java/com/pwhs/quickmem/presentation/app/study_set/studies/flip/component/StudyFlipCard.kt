package com.pwhs.quickmem.presentation.app.study_set.studies.flip.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.flashcard.FlashCardResponseModel

@Composable
fun StudyFlipFlashCard(
    modifier: Modifier = Modifier,
    flashCard: FlashCardResponseModel,
    isSwipingLeft: Boolean = false,
    isSwipingRight: Boolean = false,
    stillLearningColor: Color,
    knownColor: Color,
    isShowingEffect: Boolean = false,
    flashCardColor: Color = Color.White,
    isFlipped: Boolean = false,
    onChangeFlipped: (Boolean) -> Unit = {},
) {
    var isAnimationFinished by remember { mutableStateOf(true) }

    // Animate rotation
    val rotation = animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
        finishedListener = {
            isAnimationFinished = true
        }
    )

    // Animate alpha (fade) and scale for effects
    val effectAlpha by animateFloatAsState(
        targetValue = if (isShowingEffect) 1f else 0f,
        animationSpec = tween(durationMillis = 300)
    )
    val effectScale by animateFloatAsState(
        targetValue = if (isShowingEffect) 1.2f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    Card(
        onClick = {
            if (isAnimationFinished) {
                onChangeFlipped(!isFlipped)
                isAnimationFinished = false
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 12f * density
            },
        border = BorderStroke(
            width = 2.dp,
            color = when {
                isShowingEffect -> when {
                    isSwipingLeft -> stillLearningColor
                    isSwipingRight -> knownColor
                    else -> flashCardColor
                }

                else -> flashCardColor
            }
        )
    ) {
        BoxWithConstraints {
            if (maxHeight > 720.dp) {
                if (rotation.value <= 90f) {
                    Box(
                        Modifier.fillMaxSize()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .animateContentSize()
                                .align(Alignment.Center)
                                .graphicsLayer(
                                    scaleX = effectScale,
                                    scaleY = effectScale,
                                    alpha = effectAlpha
                                )
                        ) {
                            when {
                                isShowingEffect -> when {
                                    isSwipingLeft -> {
                                        Text(
                                            text = stringResource(R.string.txt_still_learning),
                                            style = typography.bodyLarge.copy(
                                                fontSize = 20.sp,
                                                color = stillLearningColor,
                                                fontWeight = FontWeight.Black
                                            )
                                        )
                                    }

                                    isSwipingRight -> {
                                        Text(
                                            text = stringResource(R.string.txt_known),
                                            style = typography.bodyLarge.copy(
                                                fontSize = 20.sp,
                                                color = knownColor,
                                                fontWeight = FontWeight.Black
                                            )
                                        )
                                    }

                                    else -> {
                                        LazyColumn(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(30.dp)
                                        ) {
                                            item {
                                                flashCard.termImageURL?.let { url ->
                                                    if (url.isEmpty()) {
                                                        return@let
                                                    }
                                                    AsyncImage(
                                                        model = ImageRequest.Builder(LocalContext.current)
                                                            .data(flashCard.termImageURL)
                                                            .error(R.drawable.ic_image_error)
                                                            .build(),
                                                        contentDescription = null,
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier
                                                            .size(100.dp)
                                                            .padding(16.dp),
                                                    )
                                                }
                                            }
                                            item {
                                                Text(
                                                    text = flashCard.term,
                                                    style = typography.bodyLarge.copy(
                                                        fontSize = when {
                                                            // tiny
                                                            flashCard.term.length <= 5 -> 28.sp
                                                            // small
                                                            flashCard.term.length <= 10 -> 22.sp
                                                            // medium
                                                            flashCard.term.length <= 15 -> 18.sp
                                                            // large
                                                            else -> 13.sp
                                                        }
                                                    ),
                                                    textAlign = when {
                                                        flashCard.term.length <= 25 -> TextAlign.Center
                                                        else -> TextAlign.Start
                                                    },
                                                    modifier = Modifier
                                                        .padding(30.dp)
                                                        .wrapContentSize(Alignment.Center),
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Box(
                        Modifier
                            .fillMaxSize(),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .animateContentSize()
                                .align(Alignment.Center)
                                .graphicsLayer(
                                    rotationY = 180f,
                                    scaleX = effectScale,
                                    scaleY = effectScale,
                                    alpha = effectAlpha
                                )
                        ) {
                            when {
                                isShowingEffect -> when {
                                    isSwipingLeft -> {
                                        Text(
                                            text = stringResource(R.string.txt_still_learning),
                                            style = typography.bodyLarge.copy(
                                                fontSize = 20.sp,
                                                color = stillLearningColor,
                                                fontWeight = FontWeight.Black
                                            )
                                        )
                                    }

                                    isSwipingRight -> {
                                        Text(
                                            text = stringResource(R.string.txt_known),
                                            style = typography.bodyLarge.copy(
                                                fontSize = 20.sp,
                                                color = knownColor,
                                                fontWeight = FontWeight.Black
                                            ),
                                            modifier = Modifier
                                                .padding(40.dp)
                                                .wrapContentSize(Alignment.Center),
                                            softWrap = true
                                        )
                                    }

                                    else -> {
                                        LazyColumn(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(40.dp)
                                        ) {
                                            item {
                                                flashCard.definitionImageURL?.let { url ->
                                                    if (url.isEmpty()) {
                                                        return@let
                                                    }
                                                    AsyncImage(
                                                        model = flashCard.definitionImageURL,
                                                        contentDescription = null,
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier
                                                            .size(200.dp)
                                                            .padding(16.dp)
                                                    )
                                                }
                                            }
                                            item {
                                                Text(
                                                    text = flashCard.definition,
                                                    style = typography.bodyLarge.copy(
                                                        fontSize = when {
                                                            // tiny
                                                            flashCard.definition.length <= 5 -> 30.sp
                                                            // small
                                                            flashCard.definition.length <= 10 -> 25.sp
                                                            // medium
                                                            flashCard.definition.length <= 15 -> 20.sp
                                                            // large
                                                            else -> 15.sp
                                                        }
                                                    ),
                                                    textAlign = when {
                                                        flashCard.term.length <= 25 -> TextAlign.Center
                                                        else -> TextAlign.Start
                                                    },
                                                    modifier = Modifier
                                                        .wrapContentSize(Alignment.Center),
                                                    softWrap = true
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (rotation.value <= 90f) {
                    Box(
                        Modifier.fillMaxSize()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .animateContentSize()
                                .align(Alignment.Center)
                                .graphicsLayer(
                                    scaleX = effectScale,
                                    scaleY = effectScale,
                                    alpha = effectAlpha
                                )
                        ) {
                            when {
                                isShowingEffect -> when {
                                    isSwipingLeft -> {
                                        Text(
                                            text = stringResource(R.string.txt_still_learning),
                                            style = typography.bodyLarge.copy(
                                                fontSize = 18.sp,
                                                color = stillLearningColor,
                                                fontWeight = FontWeight.Black
                                            )
                                        )
                                    }

                                    isSwipingRight -> {
                                        Text(
                                            text = stringResource(R.string.txt_known),
                                            style = typography.bodyLarge.copy(
                                                fontSize = 18.sp,
                                                color = knownColor,
                                                fontWeight = FontWeight.Black
                                            )
                                        )
                                    }

                                    else -> {
                                        LazyColumn(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(30.dp)
                                        ) {
                                            item {
                                                flashCard.termImageURL?.let { url ->
                                                    if (url.isEmpty()) {
                                                        return@let
                                                    }
                                                    AsyncImage(
                                                        model = ImageRequest.Builder(LocalContext.current)
                                                            .data(flashCard.termImageURL)
                                                            .error(R.drawable.ic_image_error)
                                                            .build(),
                                                        contentDescription = null,
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier
                                                            .size(100.dp)
                                                            .padding(16.dp),
                                                    )
                                                }
                                            }
                                            item {
                                                Text(
                                                    text = flashCard.term,
                                                    style = typography.bodyLarge.copy(
                                                        fontSize = when {
                                                            // tiny
                                                            flashCard.term.length <= 5 -> 28.sp
                                                            // small
                                                            flashCard.term.length <= 10 -> 22.sp
                                                            // medium
                                                            flashCard.term.length <= 15 -> 18.sp
                                                            // large
                                                            else -> 13.sp
                                                        }
                                                    ),
                                                    textAlign = when {
                                                        flashCard.term.length <= 25 -> TextAlign.Center
                                                        else -> TextAlign.Start
                                                    },
                                                    modifier = Modifier
                                                        .padding(30.dp)
                                                        .wrapContentSize(Alignment.Center),
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Box(
                        Modifier
                            .fillMaxSize(),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .animateContentSize()
                                .align(Alignment.Center)
                                .graphicsLayer(
                                    rotationY = 180f,
                                    scaleX = effectScale,
                                    scaleY = effectScale,
                                    alpha = effectAlpha
                                )
                        ) {
                            when {
                                isShowingEffect -> when {
                                    isSwipingLeft -> {
                                        Text(
                                            text = stringResource(R.string.txt_still_learning),
                                            style = typography.bodyLarge.copy(
                                                fontSize = 18.sp,
                                                color = stillLearningColor,
                                                fontWeight = FontWeight.Black
                                            )
                                        )
                                    }

                                    isSwipingRight -> {
                                        Text(
                                            text = stringResource(R.string.txt_known),
                                            style = typography.bodyLarge.copy(
                                                fontSize = 18.sp,
                                                color = knownColor,
                                                fontWeight = FontWeight.Black
                                            ),
                                            modifier = Modifier
                                                .padding(30.dp)
                                                .wrapContentSize(Alignment.Center),
                                            softWrap = true
                                        )
                                    }

                                    else -> {
                                        LazyColumn(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(30.dp)
                                        ) {
                                            item {
                                                flashCard.definitionImageURL?.let { url ->
                                                    if (url.isEmpty()) {
                                                        return@let
                                                    }
                                                    AsyncImage(
                                                        model = ImageRequest.Builder(LocalContext.current)
                                                            .data(flashCard.definitionImageURL)
                                                            .error(R.drawable.ic_image_error)
                                                            .build(),
                                                        contentDescription = null,
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier
                                                            .size(100.dp)
                                                            .padding(16.dp),
                                                    )
                                                }
                                            }
                                            item {
                                                Text(
                                                    text = flashCard.definition,
                                                    style = typography.bodyLarge.copy(
                                                        fontSize = when {
                                                            // tiny
                                                            flashCard.definition.length <= 5 -> 28.sp
                                                            // small
                                                            flashCard.definition.length <= 10 -> 22.sp
                                                            // medium
                                                            flashCard.definition.length <= 15 -> 18.sp
                                                            // large
                                                            else -> 13.sp
                                                        }
                                                    ),
                                                    textAlign = when {
                                                        flashCard.term.length <= 25 -> TextAlign.Center
                                                        else -> TextAlign.Start
                                                    },
                                                    modifier = Modifier
                                                        .wrapContentSize(Alignment.Center),
                                                    softWrap = true
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
