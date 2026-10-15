package com.gymguide.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.gymguide.ui.screens.rutinas.RutinasScreen
import com.gymguide.ui.screens.rutinas.CrearRutinaScreen  // Agregamos
import com.gymguide.ui.screens.home.HomeScreen
import com.gymguide.ui.screens.grupos.GruposScreen
import com.gymguide.ui.screens.ejercicios.EjerciciosScreen
import com.gymguide.ui.screens.detalle.DetalleEjercicioScreen
import com.gymguide.ui.screens.rutinas.DetalleRutinaScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // INICIO
        composable("home") {
            HomeScreen(
                onNavigate = { ruta ->
                    navController.navigate(ruta)
                }
            )
        }

        // GRUPOS MUSCULARES
        composable("grupos") {
            GruposScreen(
                onVolver = {
                    navController.popBackStack()
                },
                onGrupoSeleccionado = { idGrupo ->
                    navController.navigate("ejercicios/$idGrupo")
                }
            )
        }

        // LISTADO DE EJERCICIOS
        composable("ejercicios/{idGrupo}") { entrada ->
            val idGrupo = entrada.arguments
                ?.getString("idGrupo")
                ?.toIntOrNull()

            if (idGrupo != null) {
                EjerciciosScreen(
                    idGrupo = idGrupo,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onEjercicioSeleccionado = { idEjercicio ->
                        navController.navigate("detalle/$idEjercicio")
                    }
                )
            }
        }

        // DETALLE DEL EJERCICIO
        composable("detalle/{idEjercicio}") { entrada ->
            val idEjercicio = entrada.arguments
                ?.getString("idEjercicio")
                ?.toIntOrNull()

            if (idEjercicio != null) {
                DetalleEjercicioScreen(
                    idEjercicio = idEjercicio,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onVerAlternativa = { idAlternativo ->
                        navController.navigate("detalle/$idAlternativo")
                    }
                )
            }
        }

        // ========== RUTINAS ==========
        composable("rutinas") {
            RutinasScreen(
                onVolver = { navController.popBackStack() },
                onCrearRutina = { navController.navigate("crear_rutina") },
                onVerDetalle = { idRutina ->
                    navController.navigate("detalle_rutina/$idRutina")
                }
            )
        }
        //Usar CrearRutinaScreen
        composable("crear_rutina") {
            CrearRutinaScreen(
                onGuardarExitoso = {
                    // Vuelve a la lista y muestra la nueva rutina
                    navController.popBackStack("rutinas", inclusive = false)
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }



        composable("detalle_rutina/{idRutina}") { entrada ->
            val idRutina = entrada.arguments
                ?.getString("idRutina")
                ?.toIntOrNull()

            if (idRutina != null) {
                DetalleRutinaScreen(
                    idRutina = idRutina,
                    onVolver = { navController.popBackStack() },
                    onEditar = { navController.navigate("editar_rutina/$idRutina") }
                )
            }
        }




        // Preparar para edición
        composable("editar_rutina/{idRutina}") { entrada ->
            val idRutina = entrada.arguments
                ?.getString("idRutina")
                ?.toIntOrNull()

            if (idRutina != null) {
                // Próximo paso: CrearEditarRutinaScreen
                // Por ahora dejamos el espacio listo
                ModuloScreen(
                    titulo = "Editar rutina $idRutina",
                    onVolver = {
                        navController.popBackStack()
                    }
                )
            }
        }

        // TEMPORIZADOR
        composable("temporizador") {
            ModuloScreen(
                titulo = "Temporizador",
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // CALCULADORA IMC
        composable("imc") {
            ModuloScreen(
                titulo = "Calculadora IMC",
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}