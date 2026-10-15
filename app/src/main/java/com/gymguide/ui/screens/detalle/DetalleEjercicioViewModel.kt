
package com.gymguide.ui.screens.detalle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gymguide.data.local.dao.EjercicioConGrupo
import com.gymguide.data.local.dao.AlternativaView
import com.gymguide.data.local.database.AppDatabase
import com.gymguide.data.local.entity.PautaObjetivo
import com.gymguide.data.repository.CatalogoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleEjercicioUiState(
    val cargando: Boolean = true,
    val ejercicio: EjercicioConGrupo? = null,
    val pautas: List<PautaObjetivo> = emptyList(),
    val alternativas: List<AlternativaView> = emptyList(),
    val error: String? = null
)

class DetalleEjercicioViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = CatalogoRepository(
        AppDatabase.getInstance(application).catalogoDao()
    )

    private val _uiState =
        MutableStateFlow(DetalleEjercicioUiState())

    val uiState = _uiState.asStateFlow()

    fun cargarDetalle(idEjercicio: Int) {
        viewModelScope.launch {
            _uiState.value = DetalleEjercicioUiState()

            try {
                val ejercicio = repository.obtenerDetalle(idEjercicio)
                val pautas = repository.obtenerPautas(idEjercicio)
                val alternativas =
                    repository.obtenerAlternativas(idEjercicio)

                _uiState.value = DetalleEjercicioUiState(
                    cargando = false,
                    ejercicio = ejercicio,
                    pautas = pautas,
                    alternativas = alternativas,
                    error = if (ejercicio == null)
                        "No se encontró el ejercicio"
                    else null
                )

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = DetalleEjercicioUiState(
                    cargando = false,
                    error = e.message
                        ?: "Error al cargar el ejercicio"
                )
            }
        }
    }
}