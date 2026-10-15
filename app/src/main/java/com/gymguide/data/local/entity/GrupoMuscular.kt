package com.gymguide.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla grupo_muscular: catálogo de músculos (RF01).
 * nombre es UNIQUE: no pueden existir dos grupos "Espalda".
 */
@Entity(
    tableName = "grupo_muscular",
    indices = [Index(value = ["nombre"], unique = true)]
)
data class GrupoMuscular(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_grupo") val idGrupo: Int = 0,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "imagen") val imagen: String? = null
)