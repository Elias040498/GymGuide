package com.gymguide.data.local.database

import android.content.ContentValues
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gymguide.data.local.dao.CatalogoDao
import com.gymguide.data.local.dao.RutinaDao
import com.gymguide.data.local.entity.Alternativa
import com.gymguide.data.local.entity.DetalleRutina
import com.gymguide.data.local.entity.DiaRutina
import com.gymguide.data.local.entity.Ejercicio
import com.gymguide.data.local.entity.GrupoMuscular
import com.gymguide.data.local.entity.PautaObjetivo
import com.gymguide.data.local.entity.Rutina
import android.database.sqlite.SQLiteDatabase

/**
 * Componente Database del trío de Room (Entity + DAO + Database).
 * - entities: las 7 tablas del Anexo C. Room las crea automáticamente.
 * - version: 1. Si cambian una Entity, suban el número o usen migración.
 * - exportSchema = false: evita el warning de esquema (no lo necesitamos).
 */
@Database(
    entities = [
        GrupoMuscular::class,
        Ejercicio::class,
        Alternativa::class,
        PautaObjetivo::class,
        Rutina::class,
        DiaRutina::class,
        DetalleRutina::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /** Room genera la implementación de estos DAOs automáticamente. */
    abstract fun catalogoDao(): CatalogoDao
    abstract fun rutinaDao(): RutinaDao

    companion object {
        private const val NOMBRE_BD = "gymguide.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Singleton: toda la app comparte UNA sola conexión a la BD.
         * applicationContext evita fugas de memoria (no guarda Activity).
         */
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    NOMBRE_BD
                )
                    .addCallback(callbackSemilla)
                    // Solo desarrollo: si cambia el esquema, recrea la BD.
                    // En producción se reemplaza por migraciones reales.
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }

        /**
         * La siembra corre UNA sola vez en la vida: cuando la BD se crea
         * por primera vez (onCreate del callback). Después ya no toca nada.
         */
        private val callbackSemilla = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                sembrarCatalogo(db)
            }
        }

        // ---------------- SIEMBRA DEL CATÁLOGO (O3) ----------------

        private fun sembrarCatalogo(db: SupportSQLiteDatabase) {
            // Transacción: o se siembra todo, o no se siembra nada.
            db.beginTransaction()
            try {
                // 1) RF01: cinco grupos musculares (ids 1..5 por orden).
                insertGrupo(db, "Espalda", "gm_espalda")
                insertGrupo(db, "Pecho", "gm_pecho")
                insertGrupo(db, "Piernas", "gm_piernas")
                insertGrupo(db, "Hombros", "gm_hombros")
                insertGrupo(db, "Brazos", "gm_brazos")

                // 2) O3: 24 ejercicios (ids 1..24 por orden de inserción).
                // ESPALDA (grupo 1)
                insertEjercicio(db, 1, "Jalón al pecho",
                    "1. Siéntate y fija los muslos en los rodillos.\n2. Agarra la barra ancha por encima de la cabeza.\n3. Lleva la barra al pecho alto juntando escápulas.\n4. Sube controlado sin extender del todo los codos.",
                    "Polea alta / máquina de jalón", "Principiante", "ej_jalon_pecho")
                insertEjercicio(db, 1, "Remo sentado con polea (agarre V)",
                    "1. Siéntate con las rodillas semiflexionadas.\n2. Agarra el mango V con la espalda recta.\n3. Tira hacia el abdomen sin balancear el torso.\n4. Regresa extendiendo los brazos controlado.",
                    "Polea baja con agarre V", "Principiante", "ej_remo_sentado")
                insertEjercicio(db, 1, "Remo en T",
                    "1. Colócate a horcajadas sobre la barra T.\n2. Flexiona el torso manteniendo la espalda neutra.\n3. Tira del agarre hacia el pecho bajo.\n4. Baja controlado hasta extender los brazos.",
                    "Barra T / remo T", "Intermedio", "ej_remo_t")
                insertEjercicio(db, 1, "Pull over en polea",
                    "1. Toma la cuerda de pie frente a la polea alta.\n2. Brazos semiflexionados y torso inclinado.\n3. Lleva las manos hacia los muslos en arco.\n4. Regresa lento hasta sentir el estiramiento.",
                    "Polea alta con cuerda", "Intermedio", "ej_pull_over")
                insertEjercicio(db, 1, "Peck deck invertido",
                    "1. Siéntate con el pecho apoyado en el respaldo.\n2. Agarra las manijas con los brazos abiertos.\n3. Cierra hacia atrás juntando escápulas.\n4. Vuelve al inicio sin perder tensión.",
                    "Peck deck invertido", "Principiante", "ej_peck_invertido")
                // PECHO (grupo 2)
                insertEjercicio(db, 2, "Press de banca con barra",
                    "1. Acuéstate con los ojos bajo la barra y pies firmes.\n2. Agarra la barra un poco más ancho que hombros.\n3. Baja la barra al pecho medio controlado.\n4. Empuja hasta extender codos sin bloqueo brusco.",
                    "Banca con estación de barra", "Principiante", "ej_press_barra")
                insertEjercicio(db, 2, "Press banca con mancuernas",
                    "1. Siéntate en banca suelta con mancuernas sobre rodillas.\n2. Acuéstate impulsándolas a la altura del pecho.\n3. Baja hasta formar ángulo de 90 grados en codos.\n4. Empuja hacia arriba sin chocarlas.",
                    "Banca suelta + mancuernas", "Principiante", "ej_press_mancuernas")
                insertEjercicio(db, 2, "Aperturas en peck deck",
                    "1. Ajusta el asiento para brazos paralelos al piso.\n2. Apoya antebrazos en las almohadillas.\n3. Cierra los brazos al frente contrayendo el pecho.\n4. Abre lento hasta sentir estiramiento.",
                    "Peck deck", "Principiante", "ej_peck_deck")
                insertEjercicio(db, 2, "Cruce de poleas",
                    "1. Toma un mango de cada polea con paso adelantado.\n2. Brazos abiertos y codos semiflexionados.\n3. Cruza las manos al frente a la altura del pecho.\n4. Regresa abierto controlando el peso.",
                    "Poleas dobles", "Intermedio", "ej_cruce_poleas")
                insertEjercicio(db, 2, "Press inclinado con mancuernas",
                    "1. Inclina la banca entre 30 y 45 grados.\n2. Sostén las mancuernas sobre el pecho superior.\n3. Baja controlado hasta codos en 90 grados.\n4. Empuja vertical sin juntar de golpe.",
                    "Banca inclinada + mancuernas", "Intermedio", "ej_press_inclinado")
                // PIERNAS (grupo 3)
                insertEjercicio(db, 3, "Sentadilla en máquina Smith",
                    "1. Coloca la barra a la altura de tus hombros.\n2. Pies adelantados al ancho de caderas.\n3. Baja hasta muslos paralelos al piso.\n4. Empuja el piso para volver arriba.",
                    "Máquina Smith", "Intermedio", "ej_sentadilla_smith")
                insertEjercicio(db, 3, "Prensa de piernas",
                    "1. Siéntate con espalda y cabeza apoyadas.\n2. Pies al ancho de hombros en la plataforma.\n3. Baja a 90 grados de rodilla sin despegar la espalda.\n4. Extiende sin bloquear rodillas.",
                    "Prensa 45", "Principiante", "ej_prensa")
                insertEjercicio(db, 3, "Extensión de cuádriceps",
                    "1. Ajusta el respaldo y el rodillo al empeine.\n2. Rodillas alineadas con el eje de la máquina.\n3. Extiende las piernas contrayendo arriba.\n4. Baja lento sin dejar caer el peso.",
                    "Máquina de extensión", "Principiante", "ej_extension_cuad")
                insertEjercicio(db, 3, "Curl de isquiotibiales",
                    "1. Apoya el rodillo sobre los talones.\n2. Flexiona las rodillas llevando el talón al glúteo.\n3. Sostén un segundo arriba.\n4. Extiende controlado sin hiperextender.",
                    "Máquina de curl femoral", "Principiante", "ej_curl_femoral")
                insertEjercicio(db, 3, "Elevación de talones",
                    "1. Apoya empeines u hombros según la máquina.\n2. Baja talones hasta sentir estiramiento.\n3. Sube al máximo contrayendo gemelos.\n4. Pausa y baja controlado.",
                    "Máquina de gemelos", "Principiante", "ej_gemelos")
                // HOMBROS (grupo 4)
                insertEjercicio(db, 4, "Press militar en máquina",
                    "1. Ajusta el asiento: manijas a la altura de hombros.\n2. Agarra con palmas al frente.\n3. Empuje hacia arriba sin bloqueo brusco.\n4. Baja hasta 90 grados controlado.",
                    "Máquina de press de hombros", "Principiante", "ej_press_militar")
                insertEjercicio(db, 4, "Elevaciones laterales con mancuernas",
                    "1. De pie con mancuernas a los costados.\n2. Codos semiflexionados.\n3. Sube los brazos hasta la altura de hombros.\n4. Baja lento sin balancear el torso.",
                    "Mancuernas", "Principiante", "ej_laterales")
                insertEjercicio(db, 4, "Elevaciones frontales con mancuernas",
                    "1. De pie con mancuernas al frente de los muslos.\n2. Sube hasta la altura de los hombros.\n3. Pausa breve arriba.\n4. Baja controlado.",
                    "Mancuernas", "Principiante", "ej_frontales")
                insertEjercicio(db, 4, "Pájaros con mancuernas",
                    "1. Inclina el torso con espalda neutra.\n2. Brazos colgando con palmas enfrentadas.\n3. Abre los brazos juntando escápulas.\n4. Baja lento.",
                    "Banca + mancuernas", "Intermedio", "ej_pajaros")
                // BRAZOS (grupo 5)
                insertEjercicio(db, 5, "Curl de bíceps con barra Z",
                    "1. De pie con barra Z al ancho de hombros.\n2. Codos pegados al torso.\n3. Sube la barra contrayendo bíceps.\n4. Baja controlado en tres segundos.",
                    "Barra Z", "Principiante", "ej_curl_barra")
                insertEjercicio(db, 5, "Curl martillo con mancuernas",
                    "1. Mancuernas con palmas enfrentadas.\n2. Codos fijos al costado.\n3. Sube sin girar la muñeca.\n4. Baja controlado.",
                    "Mancuernas", "Principiante", "ej_curl_martillo")
                insertEjercicio(db, 5, "Extensión de tríceps en polea",
                    "1. Toma la cuerda en polea alta con codos fijos.\n2. Extiende separando la cuerda abajo.\n3. Sostén un segundo.\n4. Regresa sin mover los codos.",
                    "Polea alta con cuerda", "Principiante", "ej_triceps_polea")
                insertEjercicio(db, 5, "Press francés con mancuerna",
                    "1. Acuéstate con la mancuerna tomada con ambas manos.\n2. Brazos extendidos sobre el pecho.\n3. Flexiona codos bajando tras la cabeza.\n4. Extiende sin abrir los codos.",
                    "Banca + mancuerna", "Intermedio", "ej_press_frances")
                insertEjercicio(db, 5, "Curl concentrado",
                    "1. Sentado con codo apoyado en la cara interna del muslo.\n2. Mancuerna colgando.\n3. Sube sin despegar el codo.\n4. Baja lento.",
                    "Banca + mancuerna", "Principiante", "ej_curl_concentrado")

                // 3) O3 / RN04: 16 relaciones de alternativa (mismo músculo).
                insertAlternativa(db, 1, 2, "Jalón ocupado: remo en polea trabaja el mismo dorsal.")
                insertAlternativa(db, 2, 1, "Polea baja ocupada: el jalón mantiene el trabajo de dorsal.")
                insertAlternativa(db, 6, 7, "Estación de barra ocupada: banca suelta y mancuernas.")
                insertAlternativa(db, 7, 6, "Mancuernas ocupadas: la estación de barra equivale.")
                insertAlternativa(db, 8, 9, "Peck deck ocupado: cruce de poleas trabaja el pecho.")
                insertAlternativa(db, 9, 8, "Poleas ocupadas: peck deck mantiene el trabajo de pecho.")
                insertAlternativa(db, 11, 12, "Smith ocupada: la prensa carga el mismo patrón de pierna.")
                insertAlternativa(db, 12, 11, "Prensa ocupada: Smith permite el mismo trabajo.")
                insertAlternativa(db, 12, 13, "Prensa ocupada: extensión enfoca cuádriceps igual.")
                insertAlternativa(db, 13, 12, "Extensión ocupada: la prensa también enfoca cuádriceps.")
                insertAlternativa(db, 16, 17, "Máquina de press ocupada: laterales trabajan deltoides.")
                insertAlternativa(db, 17, 16, "Mancuernas ocupadas: la máquina de press equivale.")
                insertAlternativa(db, 20, 21, "Barra Z ocupada: curl martillo con mancuernas.")
                insertAlternativa(db, 21, 20, "Mancuernas ocupadas: barra Z mantiene el bíceps.")
                insertAlternativa(db, 22, 23, "Polea ocupada: press francés trabaja el tríceps.")
                insertAlternativa(db, 23, 22, "Banca ocupada: la polea mantiene el trabajo de tríceps.")

                // 4) RN10: exactamente 3 pautas por ejercicio (72 filas).
                for (idEjercicio in 1..24) {
                    insertPauta(db, idEjercicio, "Fuerza", 4, "4-6", 120)
                    insertPauta(db, idEjercicio, "Hipertrofia", 3, "8-12", 60)
                    insertPauta(db, idEjercicio, "Resistencia", 3, "15-20", 45)
                }

                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }

        // ---- Helpers de inserción (ContentValues = "diccionario columna:valor") ----

        private fun insertGrupo(db: SupportSQLiteDatabase, nombre: String, imagen: String) {
            db.insert("grupo_muscular", SQLiteDatabase.CONFLICT_IGNORE, ContentValues().apply {
                put("nombre", nombre)
                put("imagen", imagen)
            })
        }

        private fun insertEjercicio(
            db: SupportSQLiteDatabase, idGrupo: Int, nombre: String,
            descripcion: String, equipo: String, nivel: String, imagen: String
        ) {
            db.insert("ejercicio", SQLiteDatabase.CONFLICT_IGNORE, ContentValues().apply {
                put("id_grupo", idGrupo)
                put("nombre", nombre)
                put("descripcion", descripcion)
                put("equipo", equipo)
                put("nivel", nivel)
                put("imagen", imagen)
            })
        }

        private fun insertAlternativa(
            db: SupportSQLiteDatabase, idEjercicio: Int, idAlternativo: Int, motivo: String
        ) {
            db.insert("alternativa", SQLiteDatabase.CONFLICT_IGNORE, ContentValues().apply {
                put("id_ejercicio", idEjercicio)
                put("id_alternativo", idAlternativo)
                put("motivo", motivo)
            })
        }

        private fun insertPauta(
            db: SupportSQLiteDatabase, idEjercicio: Int, objetivo: String,
            series: Int, repeticiones: String, descanso: Int
        ) {
            db.insert("pauta_objetivo", SQLiteDatabase.CONFLICT_IGNORE, ContentValues().apply {
                put("id_ejercicio", idEjercicio)
                put("objetivo", objetivo)
                put("series", series)
                put("repeticiones", repeticiones)
                put("descanso_segundos", descanso)
            })
        }
    }
}