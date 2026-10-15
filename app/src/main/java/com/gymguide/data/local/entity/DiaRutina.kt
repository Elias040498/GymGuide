package com.gymguide.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla dia_rutina: cada día numerado de una rutina (RF06, RN01).
 * UNIQUE(id_rutina, numero_dia): no puede haber dos "Día 1" en la misma rutina.
 * CASCADE (RN06): si se borra la rutina, sus días se borran solos.
 */
@Entity(
    tableName = "dia_rutina",
    foreignKeys = [
        ForeignKey(
            entity = Rutina::class,
            parentColumns = ["id_rutina"],
            childColumns = ["id_rutina"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_rutina", "numero_dia"], unique = true)]
)
data class DiaRutina(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_dia") val idDia: Int = 0,
    @ColumnInfo(name = "id_rutina") val idRutina: Int,
    @ColumnInfo(name = "numero_dia") val numeroDia: Int,
    @ColumnInfo(name = "nombre_dia") val nombreDia: String // "Lunes", "Día 1"...
)