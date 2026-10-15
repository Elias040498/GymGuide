package com.gymguide.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla rutina: cabecera del plan semanal (RF05, RN01, RN05).
 * cantidad_dias 4..6 y activa 0/1 se validan en ViewModel/Repository.
 * fecha_creacion en epoch (Long) para poder ordenar rutinas por fecha.
 */
@Entity(tableName = "rutina")
data class Rutina(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_rutina") val idRutina: Int = 0,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "cantidad_dias") val cantidadDias: Int,
    @ColumnInfo(name = "objetivo") val objetivo: String,
    @ColumnInfo(name = "activa") val activa: Int = 0,
    @ColumnInfo(name = "fecha_creacion") val fechaCreacion: Long = System.currentTimeMillis()
)