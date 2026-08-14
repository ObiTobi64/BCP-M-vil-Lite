package com.example.misejercicios.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.misejercicios.R
import com.example.misejercicios.ui.theme.BcpBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onNavigateNext: () -> Unit) {
    val scale = remember { Animatable(0.5f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        val job1 = launch {
            scale.animateTo(1f, animationSpec = tween(800, easing = FastOutSlowInEasing))
        }
        val job2 = launch {
            alpha.animateTo(1f, animationSpec = tween(600))
        }
        delay(2200)
        onNavigateNext()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BcpBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
        ) {
            BcpLogoPlaceholder()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.splash_tagline),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
fun BcpLogoPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(dimensionResource(R.dimen.logo_width))
            .height(dimensionResource(R.dimen.logo_height))
            .background(Color.White, shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_sm)))
            .padding(horizontal = dimensionResource(R.dimen.spacing_lg), vertical = dimensionResource(R.dimen.spacing_md)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.mipmap.bcp_logo_foreground),
            contentDescription = stringResource(R.string.app_name),
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}
