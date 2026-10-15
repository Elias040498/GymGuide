
package com.gymguide.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ImagenCatalogo(
    nombreImagen: String?,
    descripcion: String,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(170.dp)
) {
    val context = LocalContext.current

    val idImagen = if (!nombreImagen.isNullOrBlank()) {
        context.resources.getIdentifier(
            nombreImagen,
            "drawable",
            context.packageName
        )
    } else {
        0
    }

    if (idImagen != 0) {
        Image(
            painter = painterResource(idImagen),
            contentDescription = descripcion,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    } else {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Imagen no disponible",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
