package com.booktracker.lectio.ui.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.booktracker.lectio.R
import com.booktracker.lectio.ui.navigation.Screen
import com.booktracker.lectio.ui.theme.LectioTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {
    var showImage by remember { mutableStateOf(false) }
    var showTagline by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(true) }

    // Sequence of animations
    LaunchedEffect(Unit) {
        delay(1000)
        showImage = true
        delay(1000)
        showTagline = true
        delay(2500)
        showContent = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(300.dp, 300.dp)
        ) {
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000, easing = EaseInOut)),
                exit = fadeOut(animationSpec = tween(durationMillis = 1000, easing = EaseInOut)) +
                        scaleOut(
                            animationSpec = tween(durationMillis = 1000, easing = EaseInOut),
                            targetScale = 0.95f
                        )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(240.dp, 240.dp)
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = showImage,
                            enter = fadeIn(animationSpec = tween(durationMillis = 1000, easing = EaseInOut))
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo5),
                                contentDescription = "Lectio Logo",
                                modifier = Modifier
                                    .size(240.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    androidx.compose.animation.AnimatedVisibility(
                        visible = showTagline,
                        enter = slideInVertically(
                            initialOffsetY = { 100 },
                            animationSpec = tween(1000, easing = FastOutSlowInEasing, delayMillis = 100)
                        ) + fadeIn(
                            animationSpec = tween(1000, easing = FastOutSlowInEasing, delayMillis = 100)
                        ) + scaleIn(
                            initialScale = 0.9f,
                            animationSpec = tween(1000, easing = FastOutSlowInEasing, delayMillis = 100)
                        )
                    ) {
                        Text(
                            text = "Track Your Reading Journey",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
