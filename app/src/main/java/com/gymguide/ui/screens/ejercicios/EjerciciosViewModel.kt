
package com.gymguide.ui.screens.ejercicios

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gymguide.data.local.database.AppDatabase
import com.gymguide.data.local.entity.Ejercicio
import com.gymguide.data.repository.CatalogoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EjerciciosUiState(
    val cargando: Boolean = true,
    val ejercicios: List<Ejercicio> = emptyList(),
    val filtro: String = "",
    val error: String? = null
)

class EjerciciosViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = CatalogoRepository(
        AppDatabase.getInstance(application).catalogoDao()
    )

    private val _uiState = MutableStateFlow(EjerciciosUiState())
    val uiState = _uiState.asStateFlow()

    private var cargaJob: Job? = null

    fun cargarEjercicios(idGrupo: Int, filtro: String = "") {
        cargaJob?.cancel()

        cargaJob = viewModelScope.launch {
            _uiState.value = EjerciciosUiState(
                cargando = true,
                filtro = filtro
            )

            try {
                val lista = repository.obtenerEjercicios(
                    idGrupo,
                    filtro
                )

                _uiState.value = EjerciciosUiState(
                    cargando = false,
                    ejercicios = lista,
                    filtro = filtro
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = EjerciciosUiState(
                    cargando = false,
                    filtro = filtro,
                    error = e.message ?: "Error al cargar ejercicios"
                )
            }
        }
    }
}
