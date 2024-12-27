package com.pwhs.quickmem.presentation.app.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.subject.SubjectModel

@Composable
fun SubjectItem(
    modifier: Modifier = Modifier,
    onSearchStudySetBySubject: (id: Int) -> Unit,
    subject: SubjectModel,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = { onSearchStudySetBySubject(subject.id) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = subject.iconRes ?: R.drawable.ic_all),
                    contentDescription = stringResource(subject.subjectName),
                    tint = subject.color!!,
                    modifier = Modifier
                        .size(35.dp)
                )

                Text(
                    text = stringResource(subject.subjectName),
                    style = typography.titleMedium.copy(
                        color = colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f)
                )

                Text(
                    text = when (subject.studySetCount) {
                        0 -> stringResource(R.string.txt_no_study_sets)
                        1 -> stringResource(R.string.txt_one_study_set)
                        else -> stringResource(
                            R.string.txt_study_sets_library,
                            subject.studySetCount
                        )
                    },
                    style = typography.bodyMedium.copy(
                        color = colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    ),
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SubjectItemPreview() {
    SubjectItem(
        onSearchStudySetBySubject = {},
        subject = SubjectModel(
            id = 1,
            subjectName = R.string.txt_general,
            iconRes = R.drawable.ic_all,
            color = Color(0xFF7f60f9),
            subjectDescription = R.string.txt_general_subjects_that_do_not_fit_into_specific_categories,
        ),
    )
}
