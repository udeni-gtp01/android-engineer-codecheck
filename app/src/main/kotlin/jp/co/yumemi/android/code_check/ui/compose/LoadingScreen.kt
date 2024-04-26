package jp.co.yumemi.android.code_check.ui.compose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import jp.co.yumemi.android.code_check.R

/**
 * A composable function for displaying a loading screen with a circular loading animation.
 */
@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadingAnimation()
    }
}

/**
 * A composable function for displaying a circular loading animation.
 *
 * @param indicatorSize The size of the circular loading animation.
 * @param circleColors The list of colors to use for the circular loading animation.
 * @param animationDuration The duration of the loading animation in milliseconds.
 */
@Composable
fun LoadingAnimation(
    indicatorSize: Dp = dimensionResource(id = R.dimen.dp_50),
    circleColors: List<Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.onPrimary,
    ),
    animationDuration: Int = 360
) {

    val infiniteTransition =
        rememberInfiniteTransition(label = stringResource(id = R.string.loading))

    val rotateAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                easing = LinearEasing
            )
        ), label = stringResource(id = R.string.loading)
    )

    CircularProgressIndicator(
        progress = { 1f },
        modifier = Modifier
            .size(size = indicatorSize)
            .rotate(degrees = rotateAnimation)
            .border(
                width = dimensionResource(id = R.dimen.dp_5),
                brush = Brush.sweepGradient(circleColors),
                shape = CircleShape
            ),
        color = MaterialTheme.colorScheme.background,
        strokeWidth = dimensionResource(id = R.dimen.dp_1),
    )
}