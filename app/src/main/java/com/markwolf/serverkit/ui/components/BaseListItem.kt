package com.markwolf.serverkit.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun BaseListItem(
    @DrawableRes iconId:  Int,
    heading: String,
    subheading: String,
    @DrawableRes buttonIconId: Int,
    buttonHandler: () -> Unit,
) {
    Row (
        modifier = Modifier.padding(0.dp, 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val primaryColor = MaterialTheme.colorScheme.primary

        Icon(
            painter = painterResource(iconId),
            contentDescription = null,
            modifier = Modifier.padding(8.dp).drawBehind {
                drawRoundRect(
                    color = primaryColor,
                    cornerRadius = CornerRadius(8.dp.toPx()),
                    size = Size(size.width, size.height)
                )
            }.size(48.dp).padding(8.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Column {
            Row (
                modifier = Modifier.fillMaxWidth().padding(0.dp, 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column (verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = heading, style = MaterialTheme.typography.titleSmall)
                    Text(text = subheading, color = Color.Gray, style = MaterialTheme.typography.labelMedium)
                }
                IconButton(
                    onClick = buttonHandler,
                ) {
                    Icon(
                        painter = painterResource(buttonIconId),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            HorizontalDivider()
        }
    }
}