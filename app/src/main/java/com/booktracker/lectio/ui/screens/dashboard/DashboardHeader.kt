package com.booktracker.lectio.ui.screens.dashboard

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Pages
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.booktracker.lectio.ui.theme.LectioTheme

@Composable
fun DashboardHeader(
    totalPages: Int,
    currentlyReading: Int,
    finished: Int
) {
    var show by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        show = true
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // LEFT CARD - From Left
        AnimatedVisibility(
            visible = show,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            )+ fadeIn(
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            ) + scaleIn(
                initialScale = 0.9f,
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            ) + fadeOut(
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            ) + scaleOut(
                targetScale = 0.9f,
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            ),
            modifier = Modifier.weight(1f)
        ) {
            StatCard(
                title = "Currently Reading",
                value = "$currentlyReading",
                icon = Icons.Default.Book,
            )
        }

        // CENTER CARD - From Top
        AnimatedVisibility(
            visible = show,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(700, easing = FastOutSlowInEasing, delayMillis = 100)
            ) + fadeIn(
                animationSpec = tween(700, easing = FastOutSlowInEasing, delayMillis = 100)
            ) + scaleIn(
                initialScale = 0.9f,
                animationSpec = tween(700, easing = FastOutSlowInEasing, delayMillis = 100)
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(700, easing = FastOutSlowInEasing)
            ) + fadeOut(
                animationSpec = tween(700, easing = FastOutSlowInEasing)
            ) + scaleOut(
                targetScale = 0.9f,
                animationSpec = tween(700, easing = FastOutSlowInEasing)
            ),
            modifier = Modifier.weight(1f)
        ) {
            StatCard(
                title = "Finished Books",
                value = "$finished",
                icon = Icons.Default.Done,
            )
        }

        // RIGHT CARD - From Right
        AnimatedVisibility(
            visible = show,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(800, easing = FastOutSlowInEasing, delayMillis = 200)
            ) + fadeIn(
                animationSpec = tween(800, easing = FastOutSlowInEasing, delayMillis = 200)
            ) + scaleIn(
                initialScale = 0.9f,
                animationSpec = tween(800, easing = FastOutSlowInEasing, delayMillis = 200)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            ) + fadeOut(
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            ) + scaleOut(
                targetScale = 0.9f,
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            ),
            modifier = Modifier.weight(1f)
        ) {
            StatCard(
                title = "Pages Read",
                value = "$totalPages",
                icon = Icons.Default.Pages,
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DashboardHeaderPreview() {
    LectioTheme {
        Surface {
            DashboardHeader(
                totalPages = 1240,
                currentlyReading = 4,
                finished = 20
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DashboardHeaderDarkPreview() {
    LectioTheme(darkTheme = true) {
        Surface {
            DashboardHeader(
                totalPages = 1240,
                currentlyReading = 4,
                finished = 20
            )
        }
    }
}