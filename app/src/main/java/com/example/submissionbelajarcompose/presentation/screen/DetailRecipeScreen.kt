package com.example.submissionbelajarcompose.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.submissionbelajarcompose.R
import com.example.submissionbelajarcompose.ui.theme.AppTheme

@Composable
fun DetailRecipeScreen() {
    Column {
        Image(
            painter = painterResource(id = R.drawable.nasi_goreng),
            contentDescription = "Recipe Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentScale = ContentScale.Crop
        )

        Box(
            Modifier.padding(10.dp)
        ) {
            Column {
                Text(text = "Nasi Goreng", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Nasi goreng adalah makanan yang terbuat dari nasi yang digoreng dan diaduk dalam minyak goreng atau margarin, seringkali ditambahkan kecap manis, bawang merah, bawang putih, daging ayam, udang, ikan, atau telur, dan sayuran seperti wortel, kacang polong, dan cabe. Nasi goreng adalah makanan yang populer di Indonesia dan Malaysia.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text("Bahan:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))
                Text("• Nasi", style = MaterialTheme.typography.bodyMedium)
                Text("• Minyak goreng", style = MaterialTheme.typography.bodyMedium)
                Text("• Bawang merah", style = MaterialTheme.typography.bodyMedium)

            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewDetailRecipeScreen() {
    AppTheme {
        DetailRecipeScreen()
    }
}