
package com.gymguide.ui.screens.grupos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gymguide.data.local.database.AppDatabase
import com.gymguide.data.local.entity.GrupoMuscular
import com.gymguide.data.repository.CatalogoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GruposUiState(
    val cargando: Boolean = true,
    val grupos: List<GrupoMuscular> = emptyList(),
    val error: String? = null
)

class GruposViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = CatalogoRepository(
        AppDatabase.getInstance(application).catalogoDao()
    )

    private val _uiState = MutableStateFlow(GruposUiState())

    val uiState: StateFlow<GruposUiState> =
        _uiState.asStateFlow()

    init {
        cargarGrupos()
    }

    fun cargarGrupos() {
        viewModelScope.launch {
            _uiState.value = GruposUiState(cargando = true)

            try {
                val grupos = repository.obtenerGrupos()

                _uiState.value = GruposUiState(
                    cargando = false,
                    grupos = grupos
                )

            } catch (e: Exception) {
                _uiState.value = GruposUiState(
                    cargando = false,
                    error = e.message
                        ?: "No se pudieron cargar los grupos"
                )
            }
        }
    }
}
