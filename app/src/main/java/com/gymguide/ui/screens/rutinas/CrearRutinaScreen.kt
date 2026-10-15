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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gymguide.data.local.dao.DiaFormulario

// Paleta de colores profesional
private val Primary = Color(0xFF6B4EFF)
private val Background = Color(0xFFFAF7FF)
private val CardBg = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF2D3142)
private val TextLight = Color(0xFF9A9AB0)
private val ErrorRed = Color(0xFFE53935)

@Composable
fun CrearRutinaScreen(
    onGuardarExitoso: () -> Unit,
    onVolver: () -> Unit,
    viewModel: RutinasViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var objetivo by remember { mutableStateOf("") }
    var cantidadDias by remember { mutableStateOf(4) }
    var errorMensaje by remember { mutableStateOf<String?>(null) }
    val opcionesDias = listOf(4, 5, 6)
    val nombresDias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")

    Scaffold(
        topBar = {
            Surface(
                tonalElevation = 2.dp,
                color = CardBg
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onVolver) {
                        Text(
                            "Volver",
                            color = Primary,
                            fontSize = 15.sp
                        )
                    }
                    Text(
                        "Nueva Rutina",
                        style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Tarjeta principal con sombra
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 6.dp,
                        spotColor = Primary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .background(CardBg, RoundedCornerShape(20.dp))
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre de la rutina *", color = TextLight) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = TextLight.copy(alpha = 0.35f),
                        cursorColor = Primary
                    )
                )

                OutlinedTextField(
                    value = objetivo,
                    onValueChange = { objetivo = it },
                    label = { Text("Objetivo * (ej: Fuerza, Hipertrofia)", color = TextLight) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = TextLight.copy(alpha = 0.35f),
                        cursorColor = Primary
                    )
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "Días por semana:",
                    color = TextDark,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    opcionesDias.forEach { d ->
                        Button(
                            onClick = { cantidadDias = d },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (cantidadDias == d) Primary else Background,
                                contentColor = if (cantidadDias == d) Color.White else TextDark
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = if (cantidadDias == d) 4.dp else 0.dp
                            )
                        ) {
                            Text(
                                "$d días",
                                fontWeight = if (cantidadDias == d) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    "Ejercicios",
                    color = TextDark,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                Text(
                    "Los ejercicios se asignarán desde tu catálogo",
                    color = TextLight,
                    fontSize = 13.sp
                )
            }

            errorMensaje?.let { mensaje ->
                Text(
                    mensaje,
                    color = ErrorRed,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    when {
                        nombre.isBlank() -> errorMensaje = "Escribe un nombre"
                        objetivo.isBlank() -> errorMensaje = "Escribe un objetivo"
                        else -> {
                            errorMensaje = null
                            val diasFormulario = List(cantidadDias) { indice ->
                                DiaFormulario(
                                    nombreDia = nombresDias[indice],
                                    detalles = emptyList()
                                )
                            }
                            viewModel.crearRutina(
                                nombre = nombre,
                                objetivo = objetivo,
                                cantidadDias = cantidadDias,
                                dias = diasFormulario,
                                onExito = onGuardarExitoso
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = nombre.isNotBlank() && objetivo.isNotBlank(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    disabledContainerColor = TextLight.copy(alpha = 0.25f)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Text(
                    "GUARDAR RUTINA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}