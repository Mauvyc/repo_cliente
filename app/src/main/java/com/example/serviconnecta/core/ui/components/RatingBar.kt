package com.example.serviconnecta.core.ui.components

import androidx.compose.foundation.clickable // <--- Importante: Agregado para que funcione .clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
    rating: Double,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    starSize: Int = 16,
    tint: Color = Color(0xFFFFC107)
) {
    Row(modifier = modifier) {
        repeat(maxStars) { index ->
            Icon(
                imageVector = if (index < rating.toInt()) {
                    Icons.Filled.Star
                } else {
                    Icons.Outlined.Star
                },
                contentDescription = "Star $index",
                tint = tint,
                modifier = Modifier.size(starSize.dp)
            )
        }
    }
}

@Composable
fun InteractiveRatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    starSize: Int = 24,
    tint: Color = Color(0xFFFFC107)
) {
    Row(modifier = modifier) {
        repeat(maxStars) { index ->
            // Se eliminó la línea errónea que estaba aquí suelta

            Icon(
                imageVector = if (index < rating) {
                    Icons.Filled.Star
                } else {
                    Icons.Outlined.Star
                },
                contentDescription = "Star ${index + 1}",
                tint = tint,
                modifier = Modifier
                    .size(starSize.dp)
                    .clickable { // <--- Ahora sí funciona correctamente
                        onRatingChanged(index + 1)
                    }
            )
        }
    }
}