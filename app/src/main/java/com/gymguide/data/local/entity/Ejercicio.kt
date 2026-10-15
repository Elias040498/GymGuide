package com.gymguide.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla ejercicio: catálogo de ejercicios por músculo (RF02, RF03).
 * descripcion guarda los pasos separados por saltos de línea (RN11).
 * Índice compuesto (id_grupo, nombre) para acelerar la búsqueda filtrada.
 */
@Entity(
    tableName = "ejercicio",
    foreignKeys = [
        ForeignKey(
            entity = GrupoMuscular::class,
            parentColumns = ["id_grupo"],
            childColumns = ["id_grupo"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_grupo", "nombre"])]
)
data class Ejercicio(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_ejercicio") val idEjercicio: Int = 0,
    @ColumnInfo(name = "id_grupo") val idGrupo: Int,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "descripcion") val descripcion: String,
    @ColumnInfo(name = "equipo") val equipo: String,
    @ColumnInfo(name = "nivel") val nivel: String,
    @ColumnInfo(name = "imagen") val imagen: String? = null
)