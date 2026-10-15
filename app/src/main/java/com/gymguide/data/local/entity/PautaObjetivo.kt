package com.gymguide.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla pauta_objetivo: cantidades sugeridas por objetivo (RF03, RN08, RN10).
 * UNIQUE(id_ejercicio, objetivo): máximo una pauta de Fuerza, una de
 * Hipertrofia y una de Resistencia por ejercicio.
 * repeticiones es TEXT porque guarda rangos como "8-12".
 * Solo lectura: se siembra una vez y el MVP no la edita.
 */
@Entity(
    tableName = "pauta_objetivo",
    foreignKeys = [
        ForeignKey(
            entity = Ejercicio::class,
            parentColumns = ["id_ejercicio"],
            childColumns = ["id_ejercicio"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_ejercicio", "objetivo"], unique = true)]
)
data class PautaObjetivo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_pauta") val idPauta: Int = 0,
    @ColumnInfo(name = "id_ejercicio") val idEjercicio: Int,
    @ColumnInfo(name = "objetivo") val objetivo: String, // "Fuerza" | "Hipertrofia" | "Resistencia"
    @ColumnInfo(name = "series") val series: Int,
    @ColumnInfo(name = "repeticiones") val repeticiones: String, // "4-6", "8-12", "15-20"
    @ColumnInfo(name = "descanso_segundos") val descansoSegundos: Int
)