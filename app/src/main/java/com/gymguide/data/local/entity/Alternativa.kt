
package com.gymguide.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "alternativa",

    primaryKeys = [
        "id_ejercicio",
        "id_alternativo"
    ],

    foreignKeys = [
        ForeignKey(
            entity = Ejercicio::class,
            parentColumns = ["id_ejercicio"],
            childColumns = ["id_ejercicio"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Ejercicio::class,
            parentColumns = ["id_ejercicio"],
            childColumns = ["id_alternativo"],
            onDelete = ForeignKey.CASCADE
        )
    ],

    indices = [
        Index(value = ["id_alternativo"])
    ]
)
data class Alternativa(

    @ColumnInfo(name = "id_ejercicio")
    val idEjercicio: Int,

    @ColumnInfo(name = "id_alternativo")
    val idAlternativo: Int,

    @ColumnInfo(name = "motivo")
    val motivo: String? = null
)
