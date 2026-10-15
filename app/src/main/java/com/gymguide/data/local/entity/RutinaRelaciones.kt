package com.gymguide.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

// Rutina con todos sus días y ejercicios
data class RutinaConDias(
    @Embedded val rutina: Rutina,
    @Relation(
        parentColumn = "id_rutina",
        entityColumn = "id_rutina"
    )
    val dias: List<DiaConEjercicios>
)

data class DiaConEjercicios(
    @Embedded val dia: DiaRutina,
    @Relation(
        parentColumn = "id_dia",
        entityColumn = "id_dia"
    )
    val ejercicios: List<DetalleConEjercicio>
)

data class DetalleConEjercicio(
    @Embedded val detalle: DetalleRutina,
    @Relation(
        parentColumn = "id_ejercicio",
        entityColumn = "id"
    )
    val ejercicio: Ejercicio
)

// Coincide con tu DAO: getRutinaCompleta
data class RutinaCompleta(
    @Embedded val rutina: Rutina,
    @Relation(
        parentColumn = "id_rutina",
        entityColumn = "id_rutina"
    )
    val dias: List<DiaConDetalles>
)

data class DiaConDetalles(
    @Embedded val dia: DiaRutina,
    @Relation(
        parentColumn = "id_dia",
        entityColumn = "id_dia"
    )
    val detalles: List<DetalleRutina>
)

// Clase usada en tu DAO: guardarRutinaCompleta / editarRutinaCompleta
data class DiaFormulario(
    val nombreDia: String,
    val detalles: List<DetalleRutina>
)