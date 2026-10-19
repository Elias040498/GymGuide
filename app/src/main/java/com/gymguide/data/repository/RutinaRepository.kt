package com.gymguide.data.repository

import com.gymguide.data.local.dao.RutinaDao
import com.gymguide.data.local.entity.DiaFormulario
import com.gymguide.data.local.entity.Rutina
import com.gymguide.data.local.entity.RutinaCompleta
import kotlinx.coroutines.flow.Flow

class RutinaRepository(
    private val rutinaDao: RutinaDao
) {
    // Observar todas las rutinas
    fun observarRutinas(filtro: String = ""): Flow<List<Rutina>> =
        rutinaDao.observarResultinas(filtro)

    // AQUÍ ESTÁ EL PROBLEMA: Cambia el nombre según tu DAO
    suspend fun obtenerRutinaCompleta(idRutina: Int): RutinaCompleta? =
        rutinaDao.getRutinaCompleta(idRutina)

    // Guardar rutina completa
    suspend fun guardarRutinaCompleta(
        rutina: Rutina,
        dias: List<DiaFormulario>
    ): Long = rutinaDao.guardarRutinaCompleta(rutina, dias)

    // Editar rutina completa — advertencia NO es error
    suspend fun editarRutinaCompleta(
        rutina: Rutina,
        dias: List<DiaFormulario>
    ) = rutinaDao.editarRutinaCompleta(rutina, dias)

    suspend fun activarRutina(idRutina: Int) =
        rutinaDao.activarRutina(idRutina)

    suspend fun eliminarRutina(rutina: Rutina) =
        rutinaDao.eliminarRutina(rutina)
}