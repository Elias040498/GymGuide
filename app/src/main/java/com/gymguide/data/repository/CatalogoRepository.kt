
package com.gymguide.data.repository

import com.gymguide.data.local.dao.CatalogoDao
import com.gymguide.data.local.entity.GrupoMuscular
import com.gymguide.data.local.entity.Ejercicio
import com.gymguide.data.local.entity.PautaObjetivo
import com.gymguide.data.local.dao.EjercicioConGrupo
import com.gymguide.data.local.dao.AlternativaView

class CatalogoRepository(
    private val catalogoDao: CatalogoDao
) {

    suspend fun obtenerGrupos(): List<GrupoMuscular> {
        return catalogoDao.getGrupos()
    }

    suspend fun obtenerEjercicios(
        idGrupo: Int,
        filtro: String = ""
    ): List<Ejercicio> {
        return catalogoDao.getEjerciciosPorGrupo(
            idGrupo,
            filtro
        )
    }

    suspend fun obtenerDetalle(
        idEjercicio: Int
    ): EjercicioConGrupo? {
        return catalogoDao.getEjercicioConGrupo(idEjercicio)
    }

    suspend fun obtenerAlternativas(
        idEjercicio: Int
    ): List<AlternativaView> {
        return catalogoDao.getAlternativas(idEjercicio)
    }

    suspend fun obtenerPautas(
        idEjercicio: Int
    ): List<PautaObjetivo> {
        return catalogoDao.getPautas(idEjercicio)
    }
}
