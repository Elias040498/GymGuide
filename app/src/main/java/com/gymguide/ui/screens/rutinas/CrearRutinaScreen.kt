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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.gymguide.data.local.entity.DetalleRutina
import com.gymguide.data.local.entity.DiaFormulario

// Paleta de colores
private val Primary = Color(0xFF6B4EFF)
private val Background = Color(0xFFFAF7FF)
private val CardBg = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF2D3142)
private val TextLight = Color(0xFF9A9AB0)
private val ErrorRed = Color(0xFFE53935)
private val EjercicioItemBg = Color(0xFFF3EFFE)

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
    var mostrarDialogo by remember { mutableStateOf<DiaFormulario?>(null) }
    var mostrarExito by remember { mutableStateOf(false) }

    val opcionesDias = listOf(4, 5, 6)
    val nombresDias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")

    var diasConEjercicios by remember {
        mutableStateOf(generarDiasVacios(4, nombresDias))
    }

    LaunchedEffect(cantidadDias) {
        diasConEjercicios = generarDiasVacios(cantidadDias, nombresDias)
    }

    Scaffold(
        topBar = {
            Surface(tonalElevation = 2.dp, color = CardBg) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onVolver) {
                        Text("Volver", color = Primary, fontSize = 15.sp)
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
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
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = TextLight.copy(alpha = 0.35f),
                        cursorColor = Primary
                    )
                )

                OutlinedTextField(
                    value = objetivo,
                    onValueChange = { objetivo = it },
                    label = { Text("Objetivo *", color = TextLight) },
                    placeholder = { Text("Ej: Fuerza, Resistencia...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
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
                    "Ejercicios por día",
                    color = TextDark,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )

                diasConEjercicios.forEach { dia ->
                    DiaSeccion(
                        dia = dia,
                        onAgregar = { mostrarDialogo = dia }
                    )
                }
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
                        diasConEjercicios.all { it.detalles.isEmpty() } ->
                            errorMensaje = "Agrega al menos un ejercicio"
                        else -> {
                            errorMensaje = null
                            viewModel.crearRutina(
                                nombre = nombre,
                                objetivo = objetivo,
                                cantidadDias = cantidadDias,
                                dias = diasConEjercicios,
                                onExito = { mostrarExito = true }
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
                    containerColor = Primary
                )
            ) {
                Text(
                    "GUARDAR RUTINA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        if (mostrarExito) {
            AlertDialog(
                onDismissRequest = {},
                shape = RoundedCornerShape(20.dp),
                containerColor = CardBg,
                title = { Text("✅ ¡Rutina Guardada!", fontWeight = FontWeight.Bold, color = TextDark) },
                text = { Text("Tu rutina se guardó correctamente.", color = TextLight) },
                confirmButton = {
                    Button(
                        onClick = onGuardarExitoso,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Aceptar")
                    }
                }
            )
        }

        mostrarDialogo?.let { dia ->
            DialogoAgregarEjercicio(
                onConfirmar = { series, reps, descanso ->
                    dia.detalles.add(
                        DetalleRutina(
                            idDetalle = 0,
                            idDia = 0,
                            idEjercicio = 1,
                            orden = dia.detalles.size + 1,
                            series = series,
                            repeticiones = reps,
                            descansoSegundos = descanso
                        )
                    )
                    diasConEjercicios = diasConEjercicios.map { it.copy() }
                    mostrarDialogo = null
                },
                onCancelar = { mostrarDialogo = null }
            )
        }
    }
}

@Composable
fun DiaSeccion(
    dia: DiaFormulario,
    onAgregar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Primary.copy(alpha = 0.04f), RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Text(
            text = dia.nombreDia,
            fontWeight = FontWeight.Bold,
            color = Primary,
            fontSize = 15.sp
        )

        Spacer(Modifier.height(8.dp))

        if (dia.detalles.isEmpty()) {
            Text(
                "📋 Sin ejercicios asignados",
                color = TextLight,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
        } else {
            dia.detalles.forEachIndexed { idx, detalle ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(EjercicioItemBg, RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${idx + 1}",
                        fontWeight = FontWeight.Bold,
                        color = Primary,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .background(Primary.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            "✅ Ejercicio agregado",
                            fontWeight = FontWeight.SemiBold,
                            color = TextDark,
                            fontSize = 14.sp
                        )
                        Text(
                            "${detalle.series} series  •  ${detalle.repeticiones} repeticiones  •  ${detalle.descansoSegundos}s descanso",
                            color = TextLight,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }

        Button(
            onClick = onAgregar,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary.copy(alpha = 0.08f),
                contentColor = Primary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("+ Agregar ejercicio", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun DialogoAgregarEjercicio(
    onConfirmar: (series: Int, reps: Int, descanso: Int) -> Unit,
    onCancelar: () -> Unit
) {
    var seriesTxt by remember { mutableStateOf("") }
    var repsTxt by remember { mutableStateOf("") }
    var descansoTxt by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onCancelar,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color(0xFFFFFBF8),
        title = {
            Text(
                "Agregar Ejercicio",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextDark
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = seriesTxt,
                    onValueChange = { seriesTxt = it },
                    label = { Text("Series (1-10)", color = TextLight) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Color(0xFFE0D8D0),
                        cursorColor = Primary
                    )
                )
                OutlinedTextField(
                    value = repsTxt,
                    onValueChange = { repsTxt = it },
                    label = { Text("Repeticiones (1-50)", color = TextLight) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Color(0xFFE0D8D0),
                        cursorColor = Primary
                    )
                )
                OutlinedTextField(
                    value = descansoTxt,
                    onValueChange = { descansoTxt = it },
                    label = { Text("Descanso en segundos", color = TextLight) },
                    supportingText = { Text("30 / 60 / 90 segundos", color = TextLight) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Color(0xFFE0D8D0),
                        cursorColor = Primary
                    )
                )
                error?.let {
                    Text(
                        it,
                        color = ErrorRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val s = seriesTxt.toIntOrNull()
                    val r = repsTxt.toIntOrNull()
                    val d = descansoTxt.toIntOrNull()

                    error = when {
                        s == null || s !in 1..10 -> "Series: valor entre 1 y 10"
                        r == null || r !in 1..50 -> "Repeticiones: valor entre 1 y 50"
                        d == null || d !in 30..90 -> "Descanso: 30, 60 o 90 segundos"
                        else -> null
                    }

                    if (error == null) {
                        onConfirmar(s!!, r!!, d!!)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE8935A)
                )
            ) {
                Text("Agregar", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar", color = TextLight, fontWeight = FontWeight.SemiBold)
            }
        }
    )
}

private fun generarDiasVacios(
    cantidad: Int,
    nombres: List<String>
): List<DiaFormulario> {
    return List(cantidad) { indice ->
        DiaFormulario(
            numeroDia = indice + 1,
            nombreDia = nombres[indice],
            detalles = mutableListOf()
        )
    }
}