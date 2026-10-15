package com.gymguide.ui.screens.rutinas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gymguide.data.local.entity.Rutina

// Paleta de colores
private val Primary = Color(0xFF6B4EFF)
private val Background = Color(0xFFFAF7FF)
private val CardBg = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF2D3142)
private val TextLight = Color(0xFF9A9AB0)
private val ActiveGreen = Color(0xFF2E7D32)
private val InactiveOrange = Color(0xFFFF9800)

@Composable
fun RutinasScreen(
    onVolver: () -> Unit,
    onCrearRutina: () -> Unit,
    onVerDetalle: (Int) -> Unit,
    viewModel: RutinasViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    var busqueda by rememberSaveable {
        mutableStateOf("")
    }

    var rutinaEliminar by remember {
        mutableStateOf<Rutina?>(null)
    }

    LaunchedEffect(busqueda) {
        viewModel.observarRutinas(busqueda)
    }

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
                        Text("Volver", color = Primary, fontSize = 15.sp)
                    }

                    Text(
                        "Mis rutinas",
                        style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCrearRutina,
                containerColor = Primary,
                contentColor = Color.White
            ) {
                Text("+", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Buscador SIN ícono
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar rutina", color = TextLight) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = TextLight.copy(alpha = 0.35f),
                    cursorColor = Primary
                )
            )

            Spacer(Modifier.height(16.dp))

            when {
                estado.cargando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Primary)
                    }
                }

                estado.error != null -> {
                    Column {
                        Text(estado.error ?: "Error desconocido", color = Color.Red)
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.observarRutinas(busqueda) },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text("Reintentar")
                        }
                    }
                }

                estado.rutinas.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "No hay rutinas registradas.",
                            color = TextLight,
                            fontSize = 15.sp
                        )
                        Text(
                            "Presiona + para crear una.",
                            color = Primary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = estado.rutinas,
                            key = { it.idRutina }
                        ) { rutina ->

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
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        rutina.nombre,
                                        style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )

                                    Text(
                                        "Objetivo: ${rutina.objetivo}",
                                        color = TextLight,
                                        fontSize = 14.sp
                                    )

                                    Text(
                                        "${rutina.cantidadDias} días",
                                        color = if (rutina.activa == 1) ActiveGreen else InactiveOrange,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp
                                    )

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Button(
                                            onClick = { onVerDetalle(rutina.idRutina) },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Primary.copy(alpha = 0.1f),
                                                contentColor = Primary
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text("Ver / Editar", fontSize = 13.sp)
                                        }

                                        if (rutina.activa != 1) {
                                            Button(
                                                onClick = { viewModel.activarRutina(rutina.idRutina) },
                                                modifier = Modifier.weight(1f),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = InactiveOrange.copy(alpha = 0.1f),
                                                    contentColor = InactiveOrange
                                                ),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Text("Activar", fontSize = 13.sp)
                                            }
                                        }

                                        Button(
                                            onClick = { rutinaEliminar = rutina },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFFFFEBEE),
                                                contentColor = Color(0xFFE53935)
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text("Eliminar", fontSize = 13.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    rutinaEliminar?.let { rutina ->
        AlertDialog(
            onDismissRequest = { rutinaEliminar = null },
            containerColor = CardBg,
            title = {
                Text("Eliminar rutina", fontWeight = FontWeight.Bold, color = TextDark)
            },
            text = {
                Text(
                    "¿Estás seguro de eliminar '${rutina.nombre}'? " +
                            "También se eliminarán sus días y ejercicios.",
                    color = TextLight
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarRutina(rutina)
                        rutinaEliminar = null
                    }
                ) {
                    Text("Eliminar", color = Color(0xFFE53935), fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { rutinaEliminar = null }) {
                    Text("Cancelar", color = TextDark)
                }
            }
        )
    }
}