package com.gymguide.ui.screens.rutinas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gymguide.data.local.entity.DiaConEjercicios  // ✅ Tipo correcto
import com.gymguide.data.local.entity.RutinaCompleta

// Paleta de colores — igual al resto de pantallas
private val Primary = Color(0xFF6B4EFF)
private val Background = Color(0xFFFAF7FF)
private val CardBg = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF2D3142)
private val TextLight = Color(0xFF9A9AB0)
private val ActiveGreen = Color(0xFF2E7D32)

@Composable
fun DetalleRutinaScreen(
    idRutina: Int,
    onVolver: () -> Unit,
    onEditar: () -> Unit,
    viewModel: RutinasViewModel = viewModel()
) {
    val rutinaCompleta by viewModel.rutinaCompleta

    LaunchedEffect(idRutina) {
        viewModel.cargarRutinaCompleta(idRutina)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBg)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onVolver) {
                    Text("Volver", color = Primary, fontSize = 15.sp)
                }
                Text(
                    "Detalle de Rutina",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                TextButton(onClick = onEditar) {
                    Text("Editar", color = Primary, fontSize = 15.sp)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
                .padding(16.dp)
        ) {
            if (rutinaCompleta == null) {
                Text(
                    "Cargando...",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = TextLight,
                    fontSize = 15.sp
                )
            } else {
                val datos: RutinaCompleta = rutinaCompleta!!
                val dias: List<DiaConEjercicios> = datos.dias

                // Tarjeta principal con datos de la rutina
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 4.dp,
                            spotColor = Primary.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(18.dp)
                        ),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            datos.rutina.nombre,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            "Objetivo: ${datos.rutina.objetivo}",
                            color = TextLight,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        if (datos.rutina.activa == 1) {
                            Text(
                                "✓ RUTINA ACTIVA",
                                color = ActiveGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(dias) { dia ->
                        TarjetaDia(dia = dia)
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaDia(dia: DiaConEjercicios) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                spotColor = Primary.copy(alpha = 0.05f),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Primary.copy(alpha = 0.06f)
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                dia.dia.nombreDia,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = Primary
            )

            if (dia.ejercicios.isEmpty()) {
                Text(
                    "Sin ejercicios asignados",
                    color = TextLight,
                    modifier = Modifier.padding(top = 8.dp),
                    fontSize = 14.sp
                )
            } else {
                dia.ejercicios.forEach { ejercicio ->
                    Text(
                        "• ${ejercicio.detalle.series}s × ${ejercicio.detalle.repeticiones}r | ${ejercicio.detalle.descansoSegundos}s",
                        color = TextDark,
                        modifier = Modifier.padding(top = 4.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}