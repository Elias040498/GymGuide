package com.gymguide.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

// ==========================================
// RUTINA CON DÍAS
// ==========================================
data class RutinaConDias(
    @Embedded val rutina: Rutina,
    @Relation(
        parentColumn = "id_rutina",
        entityColumn = "id_rutina",
        entity = DiaRutina::class
    )
    val dias: List<DiaConEjercicios>
)

// ==========================================
// DÍA CON EJERCICIOS
// ==========================================
data class DiaConEjercicios(
    @Embedded val dia: DiaRutina,
    @Relation(
        parentColumn = "id_dia",
        entityColumn = "id_dia",
        entity = DetalleRutina::class
    )
    val ejercicios: List<DetalleConEjercicio>
)

// ==========================================
// DETALLE CON EJERCICIO — ✅ CORREGIDO EL NOMBRE
// ==========================================
data class DetalleConEjercicio(
    @Embedded val detalle: DetalleRutina,
    @Relation(
        parentColumn = "id_ejercicio",
        entityColumn = "id_ejercicio",  // ← Tu columna se llama ASÍ
        entity = Ejercicio::class
    )
    val ejercicio: Ejercicio
)

// ==========================================
// RUTINA COMPLETA (usada en tu DAO)
// ==========================================
data class RutinaCompleta(
    @Embedded val rutina: Rutina,
    @Relation(
        parentColumn = "id_rutina",
        entityColumn = "id_rutina",
        entity = DiaRutina::class
    )
    val dias: List<DiaConEjercicios>
)

// ==========================================
// DÍA CON DETALLES
// ==========================================
data class DiaConDetalles(
    @Embedded val dia: DiaRutina,
    @Relation(
        parentColumn = "id_dia",
        entityColumn = "id_dia",
        entity = DetalleRutina::class
    )
    val detalles: List<DetalleRutina>
)

// ==========================================
// FORMULARIO DE PANTALLA
// ==========================================
data class DiaFormulario(
    val numeroDia: Int = 0,
    val nombreDia: String,
    val detalles: MutableList<DetalleRutina> = mutableListOf()
)