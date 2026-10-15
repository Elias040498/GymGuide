package com.gymguide.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.gymguide.data.local.entity.Ejercicio
import com.gymguide.data.local.entity.GrupoMuscular
import com.gymguide.data.local.entity.PautaObjetivo

/**
 * DAO de SOLO LECTURA del catálogo precargado (RN10).
 * Cubre RF01, RF02, RF03, RF04 y las evidencias de conteo del O3.
 */
@Dao
interface CatalogoDao {

    /** RF01: los cinco grupos musculares. */
    @Query("SELECT * FROM grupo_muscular ORDER BY nombre")
    suspend fun getGrupos(): List<GrupoMuscular>

    /** RF02: ejercicios de un grupo con búsqueda por nombre (LIKE). */
    @Query(
        "SELECT * FROM ejercicio " +
                "WHERE id_grupo = :idGrupo AND nombre LIKE '%' || :filtro || '%' " +
                "ORDER BY nombre"
    )
    suspend fun getEjerciciosPorGrupo(idGrupo: Int, filtro: String): List<Ejercicio>

    /** RF03: ejercicio completo + nombre de su músculo para el Detalle. */
    @Query(
        "SELECT e.*, g.nombre AS nombreGrupo " +
                "FROM ejercicio e " +
                "INNER JOIN grupo_muscular g ON g.id_grupo = e.id_grupo " +
                "WHERE e.id_ejercicio = :idEjercicio"
    )
    suspend fun getEjercicioConGrupo(idEjercicio: Int): EjercicioConGrupo?

    /** RF04 / RN04: alternativas del mismo músculo con su equipo y motivo. */
    @Query(
        "SELECT e.id_ejercicio, e.nombre, e.equipo, e.nivel, e.imagen, a.motivo " +
                "FROM alternativa a " +
                "INNER JOIN ejercicio e ON e.id_ejercicio = a.id_alternativo " +
                "WHERE a.id_ejercicio = :idEjercicio " +
                "ORDER BY e.nombre"
    )
    suspend fun getAlternativas(idEjercicio: Int): List<AlternativaView>

    /** RF03 / RN08: las tres pautas (Fuerza, Hipertrofia, Resistencia). */
    @Query(
        "SELECT * FROM pauta_objetivo " +
                "WHERE id_ejercicio = :idEjercicio " +
                "ORDER BY objetivo"
    )
    suspend fun getPautas(idEjercicio: Int): List<PautaObjetivo>

    // ---- Evidencias de conteo para el O3 y el plan de pruebas ----

    @Query("SELECT COUNT(*) FROM ejercicio")
    suspend fun getTotalEjercicios(): Int

    @Query("SELECT COUNT(*) FROM alternativa")
    suspend fun getTotalAlternativas(): Int

    /** Verifica RN04: alternativas que realmente comparten músculo. */
    @Query(
        "SELECT COUNT(*) FROM alternativa a " +
                "INNER JOIN ejercicio e1 ON e1.id_ejercicio = a.id_ejercicio " +
                "INNER JOIN ejercicio e2 ON e2.id_ejercicio = a.id_alternativo " +
                "WHERE e1.id_grupo = e2.id_grupo"
    )
    suspend fun getTotalAlternativasMismoMusculo(): Int
}