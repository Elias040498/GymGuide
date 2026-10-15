
package com.gymguide.data.local.dao

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.gymguide.data.local.entity.DetalleRutina
import com.gymguide.data.local.entity.DiaRutina
import com.gymguide.data.local.entity.Ejercicio
import com.gymguide.data.local.entity.Rutina

// Ejercicio con su grupo muscular
data class EjercicioConGrupo(
    @Embedded
    val ejercicio: Ejercicio,

    val nombreGrupo: String
)

// Alternativas de un ejercicio
data class AlternativaView(
    @ColumnInfo(name = "id_ejercicio")
    val idEjercicio: Int,

    val nombre: String,
    val equipo: String,
    val nivel: String,
    val imagen: String?,
    val motivo: String?
)

// Detalle de rutina con información del ejercicio
data class DetalleConEjercicio(
    @Embedded
    val detalle: DetalleRutina,

    @Relation(
        parentColumn = "id_ejercicio",
        entityColumn = "id_ejercicio"
    )
    val ejercicio: Ejercicio
)

// Día con sus ejercicios
data class DiaConDetalles(
    @Embedded
    val dia: DiaRutina,

    @Relation(
        entity = DetalleRutina::class,
        parentColumn = "id_dia",
        entityColumn = "id_dia"
    )
    val detalles: List<DetalleConEjercicio>
)

// Rutina completa con días y ejercicios
data class RutinaCompleta(
    @Embedded
    val rutina: Rutina,

    @Relation(
        entity = DiaRutina::class,
        parentColumn = "id_rutina",
        entityColumn = "id_rutina"
    )
    val dias: List<DiaConDetalles>
)

// Datos recibidos desde el formulario
data class DiaFormulario(
    val nombreDia: String,
    val detalles: List<DetalleRutina>
)