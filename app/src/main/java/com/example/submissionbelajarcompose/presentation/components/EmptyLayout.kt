package com.example.submissionbelajarcompose.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.submissionbelajarcompose.R
import com.example.submissionbelajarcompose.ui.theme.AppTheme

@Composable
fun EmptyLayout() {
    val preloaderLottieComposition = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.empty_animation)
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition.value,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
    )
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LottieAnimation(

            composition = preloaderLottieComposition.value,
            progress = preloaderProgress,
            modifier = Modifier
                .height(400.dp)

        )

        Text(
            text = "Hmm, masih kosong. Yuk, isi dengan resep lezat dan jadikan momen makan lebih bermakna!",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}
