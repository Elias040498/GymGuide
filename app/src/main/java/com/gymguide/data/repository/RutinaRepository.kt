package com.gymguide.data.repository

import com.gymguide.data.local.dao.RutinaDao
import com.gymguide.data.local.dao.DiaFormulario  // MISMO PAQUETE DEL DAO
import com.gymguide.data.local.entity.Rutina
import kotlinx.coroutines.flow.Flow

class RutinaRepository(
    private val rutinaDao: RutinaDao
) {
    fun observarRutinas(filtro: String): Flow<List<Rutina>> =
        rutinaDao.observarResultinas(filtro)

    suspend fun obtenerRutinaActiva(): Rutina? =
        rutinaDao.getRutinaActiva()

    suspend fun obtenerRutinaCompleta(idRutina: Int) =
        rutinaDao.getRutinaCompleta(idRutina)

    suspend fun guardarRutinaCompleta(
        rutina: Rutina,
        dias: List<DiaFormulario>
    ): Long = rutinaDao.guardarRutinaCompleta(rutina, dias)

    suspend fun editarRutinaCompleta(
        rutina: Rutina,
        dias: List<DiaFormulario>
    ) = rutinaDao.editarRutinaCompleta(rutina, dias)

    suspend fun activarRutina(idRutina: Int) =
        rutinaDao.activarRutina(idRutina)

    suspend fun eliminarRutina(rutina: Rutina) =
        rutinaDao.eliminarRutina(rutina)
}