
package com.gymguide.ui.screens.grupos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gymguide.ui.components.ImagenCatalogo

@Composable
fun GruposScreen(
    onVolver: () -> Unit,
    onGrupoSeleccionado: (Int) -> Unit,
    viewModel: GruposViewModel = viewModel()
) {

    val estado by viewModel.uiState.collectAsStateWithLifecycle()

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
                        text = "Grupos musculares",
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
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                estado.error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = estado.error
                                ?: "Error desconocido"
                        )

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = viewModel::cargarGrupos
                        ) {
                            Text("Reintentar")
                        }
                    }
                }

                estado.grupos.isEmpty() -> {
                    Text(
                        text = "No hay grupos musculares registrados",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        item {
                            Text(
                                text = "Selecciona un grupo muscular",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        items(
                            items = estado.grupos,
                            key = { it.idGrupo }
                        ) { grupo ->


                            Card(
                                onClick = {
                                    onGrupoSeleccionado(grupo.idGrupo)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    ImagenCatalogo(
                                        nombreImagen = grupo.imagen,
                                        descripcion = "Imagen de ${grupo.nombre}"
                                    )

                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = grupo.nombre,
                                            style = MaterialTheme.typography.titleLarge
                                        )

                                        Text(
                                            text = "Explorar ejercicios",
                                            style = MaterialTheme.typography.bodyMedium
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
