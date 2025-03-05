package com.pwhs.quickmem.presentation.app.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.notification.GetNotificationResponseModel
import com.pwhs.quickmem.utils.calculateTimeAgo

@Composable
fun NotificationItem(
    notification: GetNotificationResponseModel,
    onMarkAsRead: (String) -> Unit,
) {

    val backgroundColor = if (notification.isRead) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.background
    }

    val textColor = if (notification.isRead) {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val timeAgo = notification.createdAt.calculateTimeAgo()

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
            if (!notification.isRead) {
                onMarkAsRead(notification.id)
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = notification.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (timeAgo != "N/A") "${notification.message}  $timeAgo" else stringResource(
                        R.string.txt_invalid_date_format
                    ),
                    fontSize = 14.sp,
                    color = textColor,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (!notification.isRead) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_circle),
                    contentDescription = stringResource(R.string.txt_unread),
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
