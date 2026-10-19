package com.gymguide.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla detalle_rutina: ejercicios asignados a cada día con sus cantidades
 * (RF06, RN02, RN03).
 * CASCADE doble: se borra si desaparece el día o si desaparece el ejercicio.
 * orden define la secuencia de ejecución dentro del día.
 * Rangos (series 1..10, reps 1..50, descanso 30/60/90) se validan
 * en ViewModel/Repository antes de insertar (CU03: mensajes por campo).
 */
@Entity(
    tableName = "detalle_rutina",
    foreignKeys = [
        ForeignKey(
            entity = DiaRutina::class,
            parentColumns = ["id_dia"],
            childColumns = ["id_dia"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Ejercicio::class,
            parentColumns = ["id_ejercicio"],
            childColumns = ["id_ejercicio"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_dia", "orden"]),
        Index(value = ["id_ejercicio"])
    ]
)
data class DetalleRutina(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_detalle") val idDetalle: Int = 0,
    @ColumnInfo(name = "id_dia") val idDia: Int = 0,
    @ColumnInfo(name = "id_ejercicio") val idEjercicio: Int = 1,
    @ColumnInfo(name = "series") val series: Int,
    @ColumnInfo(name = "repeticiones") val repeticiones: Int,
    @ColumnInfo(name = "descanso_segundos") val descansoSegundos: Int,
    @ColumnInfo(name = "orden") val orden: Int = 0
)