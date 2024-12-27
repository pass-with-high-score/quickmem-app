package com.pwhs.quickmem.presentation.app.settings.user_info.change_role.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.core.data.enums.UserRole
import com.pwhs.quickmem.util.upperCaseFirstLetter

@Composable
fun RoleItem(
    role: UserRole,
    isSelected: Boolean = false,
    onRoleChanged: (UserRole) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRoleChanged(role) }
    ) {
        RadioButton(
            selected = isSelected,
            onClick = { onRoleChanged(role) }
        )
        Text(
            text = role.name.lowercase().upperCaseFirstLetter(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}