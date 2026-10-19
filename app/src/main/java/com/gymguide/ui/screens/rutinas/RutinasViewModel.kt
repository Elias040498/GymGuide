package com.gymguide.ui.screens.rutinas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.gymguide.data.local.database.AppDatabase
import com.gymguide.data.local.entity.Rutina
import com.gymguide.data.local.entity.RutinaCompleta
import com.gymguide.data.local.entity.DiaFormulario
import com.gymguide.data.repository.RutinaRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class RutinasUiState(
    val cargando: Boolean = true,
    val rutinas: List<Rutina> = emptyList(),
    val filtro: String = "",
    val error: String? = null
)

class RutinasViewModel(
    application: Application
) : AndroidViewModel(application) {

    // ✅ DEBE IR ARRIBA: antes de usarla
    private val repository = RutinaRepository(
        AppDatabase.getInstance(application).rutinaDao()
    )

    private val _uiState = MutableStateFlow(RutinasUiState())
    val uiState = _uiState.asStateFlow()

    // ✅ Estado para detalle de rutina — corregido el tipo
    private val _rutinaCompleta = mutableStateOf<RutinaCompleta?>(null)
    val rutinaCompleta: androidx.compose.runtime.State<RutinaCompleta?> = _rutinaCompleta

    private var observacionJob: Job? = null

    init {
        observarRutinas("")
    }

    fun observarRutinas(filtro: String) {
        observacionJob?.cancel()

        observacionJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                cargando = true,
                filtro = filtro,
                error = null
            )

            try {
                repository.observarRutinas(filtro).collectLatest { lista ->
                    _uiState.value = RutinasUiState(
                        cargando = false,
                        rutinas = lista,
                        filtro = filtro
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    cargando = false,
                    error = e.message ?: "Error al cargar rutinas"
                )
            }
        }
    }

    fun cargarRutinaCompleta(idRutina: Int) {
        viewModelScope.launch {
            try {
                _rutinaCompleta.value = repository.obtenerRutinaCompleta(idRutina)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al cargar detalle"
                )
            }
        }
    }

    fun activarRutina(idRutina: Int) {
        viewModelScope.launch {
            try {
                repository.activarRutina(idRutina)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al activar rutina"
                )
            }
        }
    }

    fun eliminarRutina(rutina: Rutina) {
        viewModelScope.launch {
            try {
                repository.eliminarRutina(rutina)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al eliminar rutina"
                )
            }
        }
    }

    fun crearRutina(
        nombre: String,
        objetivo: String,
        cantidadDias: Int,
        dias: List<DiaFormulario>,
        onExito: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val rutina = Rutina(
                    nombre = nombre,
                    objetivo = objetivo,
                    cantidadDias = cantidadDias
                )
                repository.guardarRutinaCompleta(rutina, dias)
                onExito()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al guardar la rutina"
                )
            }
        }
    }
}