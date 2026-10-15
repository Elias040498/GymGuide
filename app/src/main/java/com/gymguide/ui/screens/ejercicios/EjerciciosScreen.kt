
package com.gymguide.ui.screens.ejercicios

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gymguide.ui.components.ImagenCatalogo

@Composable
fun EjerciciosScreen(
    idGrupo: Int,
    onVolver: () -> Unit,
    onEjercicioSeleccionado: (Int) -> Unit,
    viewModel: EjerciciosViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    var busqueda by rememberSaveable(idGrupo) {
        mutableStateOf("")
    }

    LaunchedEffect(idGrupo, busqueda) {
        viewModel.cargarEjercicios(idGrupo, busqueda)
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
                    TextButton(onClick = onVolver) {
                        Text("Volver")
                    }

                    Text(
                        text = "Ejercicios",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = busqueda,
                onValueChange = {
                    busqueda = it
                },
                label = {
                    Text("Buscar ejercicio")
                },
                placeholder = {
                    Text("Escribe el nombre del ejercicio")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            when {

                estado.cargando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                estado.error != null -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = estado.error
                                ?: "No se pudieron cargar los ejercicios"
                        )

                        Button(
                            onClick = {
                                viewModel.cargarEjercicios(
                                    idGrupo,
                                    busqueda
                                )
                            }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }

                estado.ejercicios.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (busqueda.isBlank()) {
                                "No hay ejercicios registrados"
                            } else {
                                "No se encontraron ejercicios con ese nombre"
                            }
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(
                            bottom = 16.dp
                        )
                    ) {

                        item {
                            Text(
                                text = "Ejercicios disponibles",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        items(
                            items = estado.ejercicios,
                            key = { ejercicio ->
                                ejercicio.idEjercicio
                            }
                        ) { ejercicio ->

                            Card(
                                onClick = {
                                    onEjercicioSeleccionado(
                                        ejercicio.idEjercicio
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Column {

                                    // Imagen del ejercicio
                                    ImagenCatalogo(
                                        nombreImagen = ejercicio.imagen,
                                        descripcion = ejercicio.nombre,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                    )

                                    // Información del ejercicio
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement =
                                            Arrangement.spacedBy(8.dp)
                                    ) {

                                        Text(
                                            text = ejercicio.nombre,
                                            style =
                                                MaterialTheme.typography.titleLarge
                                        )

                                        Text(
                                            text = "Equipo: ${ejercicio.equipo}",
                                            style =
                                                MaterialTheme.typography.bodyMedium
                                        )

                                        Text(
                                            text = "Nivel: ${ejercicio.nivel}",
                                            style =
                                                MaterialTheme.typography.bodyMedium
                                        )

                                        Text(
                                            text = "Toca para ver instrucciones",
                                            color =
                                                MaterialTheme.colorScheme.primary,
                                            style =
                                                MaterialTheme.typography.labelLarge
                                        )
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
