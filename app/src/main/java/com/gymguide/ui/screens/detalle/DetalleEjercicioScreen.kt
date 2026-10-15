
package com.gymguide.ui.screens.detalle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gymguide.ui.components.ImagenCatalogo

@Composable
fun DetalleEjercicioScreen(
    idEjercicio: Int,
    onVolver: () -> Unit,
    onVerAlternativa: (Int) -> Unit,
    viewModel: DetalleEjercicioViewModel = viewModel()
) {

    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(idEjercicio) {
        viewModel.cargarDetalle(idEjercicio)
    }

    Scaffold(
        topBar = {
            Surface(
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextButton(
                        onClick = onVolver
                    ) {
                        Text("Volver")
                    }

                    Text(
                        text = "Detalle del ejercicio",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when {

                estado.cargando -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(
                            Alignment.Center
                        )
                    )
                }

                estado.error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment =
                            Alignment.CenterHorizontally,
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = estado.error
                                ?: "No se pudo cargar el ejercicio"
                        )

                        Button(
                            onClick = {
                                viewModel.cargarDetalle(idEjercicio)
                            }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }

                estado.ejercicio != null -> {

                    val informacion = estado.ejercicio!!
                    val ejercicio = informacion.ejercicio

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(
                                rememberScrollState()
                            )
                            .padding(16.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(16.dp)
                    ) {

                        // Nombre del ejercicio
                        Text(
                            text = ejercicio.nombre,
                            style =
                                MaterialTheme.typography.headlineMedium
                        )

                        // Imagen principal
                        ImagenCatalogo(
                            nombreImagen = ejercicio.imagen,
                            descripcion = ejercicio.nombre,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                        )

                        // Grupo muscular
                        Text(
                            text = "Grupo muscular: ${informacion.nombreGrupo}",
                            style =
                                MaterialTheme.typography.titleMedium
                        )

                        // Información general
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement =
                                    Arrangement.spacedBy(8.dp)
                            ) {

                                Text(
                                    text = "Información del ejercicio",
                                    style =
                                        MaterialTheme.typography.titleLarge
                                )

                                Text(
                                    text = "Equipo: ${ejercicio.equipo}"
                                )

                                Text(
                                    text = "Nivel: ${ejercicio.nivel}"
                                )
                            }
                        }

                        // Instrucciones
                        Text(
                            text = "Cómo realizar el ejercicio",
                            style =
                                MaterialTheme.typography.titleLarge
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = ejercicio.descripcion,
                                modifier = Modifier.padding(16.dp),
                                style =
                                    MaterialTheme.typography.bodyLarge
                            )
                        }

                        HorizontalDivider()

                        // Pautas según objetivo
                        Text(
                            text = "Pautas según tu objetivo",
                            style =
                                MaterialTheme.typography.titleLarge
                        )

                        if (estado.pautas.isEmpty()) {
                            Text(
                                text = "No hay pautas registradas"
                            )
                        } else {

                            estado.pautas.forEach { pauta ->

                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement =
                                            Arrangement.spacedBy(8.dp)
                                    ) {

                                        Text(
                                            text = pauta.objetivo,
                                            style =
                                                MaterialTheme.typography.titleMedium
                                        )

                                        Text(
                                            text = "Series: ${pauta.series}"
                                        )

                                        Text(
                                            text = "Repeticiones: ${pauta.repeticiones}"
                                        )

                                        Text(
                                            text = "Descanso: ${pauta.descansoSegundos} segundos"
                                        )
                                    }
                                }
                            }
                        }

                        HorizontalDivider()

                        // Ejercicios alternativos
                        Text(
                            text = "Ejercicios alternativos",
                            style =
                                MaterialTheme.typography.titleLarge
                        )

                        if (estado.alternativas.isEmpty()) {

                            Text(
                                text = "No hay alternativas registradas para este ejercicio."
                            )

                        } else {

                            estado.alternativas.forEach { alternativa ->

                                Card(
                                    onClick = {
                                        onVerAlternativa(
                                            alternativa.idEjercicio
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    Column {

                                        // Imagen de la alternativa
                                        ImagenCatalogo(
                                            nombreImagen = alternativa.imagen,
                                            descripcion = alternativa.nombre,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(160.dp)
                                        )

                                        Column(
                                            modifier = Modifier.padding(16.dp),
                                            verticalArrangement =
                                                Arrangement.spacedBy(8.dp)
                                        ) {

                                            Text(
                                                text = alternativa.nombre,
                                                style =
                                                    MaterialTheme.typography.titleMedium
                                            )

                                            Text(
                                                text = "Equipo: ${alternativa.equipo}"
                                            )

                                            Text(
                                                text = "Nivel: ${alternativa.nivel}"
                                            )

                                            alternativa.motivo?.let { motivo ->
                                                Text(
                                                    text = "Motivo: $motivo"
                                                )
                                            }

                                            TextButton(
                                                onClick = {
                                                    onVerAlternativa(
                                                        alternativa.idEjercicio
                                                    )
                                                }
                                            ) {
                                                Text("Ver ejercicio")
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )
                    }
                }

                else -> {
                    Text(
                        text = "Ejercicio no encontrado",
                        modifier = Modifier.align(
                            Alignment.Center
                        )
                    )
                }
            }
        }
    }
}