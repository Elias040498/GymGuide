
package com.gymguide.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.gymguide.data.local.entity.DetalleRutina
import com.gymguide.data.local.entity.DiaRutina
import com.gymguide.data.local.entity.Rutina
import kotlinx.coroutines.flow.Flow

/**
 * DAO para gestionar las rutinas de GymGuide.
 *
 * RF05: Crear, consultar, editar y eliminar rutinas.
 * RF06: Organizar ejercicios por días.
 * RN05: Mantener una rutina activa.
 * RN06: Eliminar días y detalles en cascada.
 */
@Dao
interface RutinaDao {

    // ==========================================
    // CONSULTAR RUTINAS
    // ==========================================

    /**
     * Obtiene las rutinas y permite buscar por nombre.
     */
    @Query(
        """
        SELECT * FROM rutina
        WHERE nombre LIKE '%' || :filtro || '%'
        ORDER BY fecha_creacion DESC
        """
    )
    fun observarResultinas(
        filtro: String
    ): Flow<List<Rutina>>

    /**
     * Obtiene la rutina activa.
     */
    @Query(
        """
        SELECT * FROM rutina
        WHERE activa = 1
        LIMIT 1
        """
    )
    suspend fun getRutinaActiva(): Rutina?

    /**
     * Obtiene una rutina con sus días y ejercicios.
     */
    @Transaction
    @Query(
        """
        SELECT * FROM rutina
        WHERE id_rutina = :idRutina
        """
    )
    suspend fun getRutinaCompleta(
        idRutina: Int
    ): RutinaCompleta?

    // ==========================================
    // OPERACIONES CRUD
    // ==========================================

    /**
     * Inserta una nueva rutina.
     */
    @Insert
    suspend fun insertRutina(
        rutina: Rutina
    ): Long

    /**
     * Actualiza una rutina existente.
     */
    @Update
    suspend fun updateRutina(
        rutina: Rutina
    )

    /**
     * Elimina una rutina.
     */
    @Delete
    suspend fun deleteRutina(
        rutina: Rutina
    )

    /**
     * Inserta un día.
     */
    @Insert
    suspend fun insertDia(
        dia: DiaRutina
    ): Long

    /**
     * Inserta un ejercicio en un día.
     */
    @Insert
    suspend fun insertDetalle(
        detalle: DetalleRutina
    )

    /**
     * Elimina los días de una rutina.
     * Los detalles se eliminan por CASCADE.
     */
    @Query(
        """
        DELETE FROM dia_rutina
        WHERE id_rutina = :idRutina
        """
    )
    suspend fun deleteDiasDeRutina(
        idRutina: Int
    )

    // ==========================================
    // CREAR RUTINA COMPLETA
    // ==========================================

    /**
     * Guarda la rutina con sus días y ejercicios.
     * Todo se ejecuta en una transacción.
     */
    @Transaction
    suspend fun guardarRutinaCompleta(
        rutina: Rutina,
        dias: List<DiaFormulario>
    ): Long {

        val idRutina = insertRutina(rutina).toInt()

        insertDiasConDetalles(
            idRutina = idRutina,
            dias = dias
        )

        return idRutina.toLong()
    }

    // ==========================================
    // EDITAR RUTINA COMPLETA
    // ==========================================

    /**
     * Actualiza la rutina.
     * Elimina sus días anteriores y guarda
     * los nuevos días y ejercicios.
     */
    @Transaction
    suspend fun editarRutinaCompleta(
        rutina: Rutina,
        dias: List<DiaFormulario>
    ) {

        updateRutina(rutina)

        deleteDiasDeRutina(
            idRutina = rutina.idRutina
        )

        insertDiasConDetalles(
            idRutina = rutina.idRutina,
            dias = dias
        )
    }

    // ==========================================
    // ELIMINAR RUTINA
    // ==========================================

    /**
     * Elimina la rutina.
     * Room y SQLite aplican el borrado en cascada.
     */
    @Transaction
    suspend fun eliminarRutina(
        rutina: Rutina
    ) {
        deleteRutina(rutina)
    }

    // ==========================================
    // ACTIVAR RUTINA
    // ==========================================

    /**
     * Desactiva las rutinas anteriores
     * y activa la seleccionada.
     */
    @Transaction
    suspend fun activarRutina(
        idRutina: Int
    ) {

        desactivarTodas()

        activar(idRutina)
    }

    // ==========================================
    // INSERTAR DÍAS Y EJERCICIOS
    // ==========================================

    /**
     * Crea los días y asigna sus ejercicios.
     *
     * Cada día recibe un ID generado por Room.
     * Cada detalle queda relacionado con ese día.
     */
    @Transaction
    suspend fun insertDiasConDetalles(
        idRutina: Int,
        dias: List<DiaFormulario>
    ) {

        for ((indice, dia) in dias.withIndex()) {

            val nuevoDia = DiaRutina(
                idRutina = idRutina,
                numeroDia = indice + 1,
                nombreDia = dia.nombreDia
            )

            val idDia = insertDia(
                nuevoDia
            ).toInt()

            for (detalle in dia.detalles) {

                val nuevoDetalle = detalle.copy(
                    idDetalle = 0,
                    idDia = idDia
                )

                insertDetalle(
                    nuevoDetalle
                )
            }
        }
    }

    // ==========================================
    // CONSULTAS INTERNAS
    // ==========================================

    /**
     * Desactiva todas las rutinas.
     */
    @Query(
        """
        UPDATE rutina
        SET activa = 0
        """
    )
    suspend fun desactivarTodas()

    /**
     * Activa la rutina seleccionada.
     */
    @Query(
        """
        UPDATE rutina
        SET activa = 1
        WHERE id_rutina = :idRutina
        """
    )
    suspend fun activar(
        idRutina: Int
    )
}

