package com.pwhs.quickmem.presentation.app.study_set.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.subject.SubjectModel

@Composable
fun StudySetSubjectInput(
    modifier: Modifier = Modifier,
    subjectModel: SubjectModel?,
    onShowBottomSheet: () -> Unit
) {
    Column(
        modifier = modifier.padding(top = 10.dp)
    ) {
        Text(
            text = stringResource(R.string.txt_subject),
            style = typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        OutlinedTextField(
            shape = RoundedCornerShape(10.dp),
            value = stringResource(subjectModel?.subjectName ?: R.string.txt_general),
            onValueChange = { },
            placeholder = { Text(stringResource(R.string.txt_general)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .clickable {
                    onShowBottomSheet()
                },
            readOnly = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = subjectModel?.iconRes ?: R.drawable.ic_all),
                    contentDescription = stringResource(
                        subjectModel?.subjectName ?: R.string.txt_general
                    ),
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            },
            enabled = false,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = colorScheme.primary,
                unfocusedIndicatorColor = colorScheme.onSurface.copy(alpha = 0.12f),
                disabledTextColor = Color.Black,
                disabledContainerColor = Color.Transparent,
                disabledIndicatorColor = colorScheme.onSurface.copy(alpha = 0.12f),
                disabledPlaceholderColor = Color.Black,
            ),
        )
    }
}