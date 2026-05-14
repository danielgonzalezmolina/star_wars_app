package com.example.star_wars.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.star_wars.data.dao.PlanetDao
import com.example.star_wars.data.model.Planet
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

@Database(
    version = 8,
    entities = [Planet::class],
    exportSchema = false
)
abstract class StarWarsDatabase : RoomDatabase() {

    abstract fun getPlanetDao(): PlanetDao

    companion object {
        @Volatile
        private var INSTANCE: StarWarsDatabase? = null

        fun getDatabase(context: Context): StarWarsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StarWarsDatabase::class.java,
                    "star_wars_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Executors.newSingleThreadExecutor().execute {
                                INSTANCE?.let { database ->
                                    prepopulateDatabase(database)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun prepopulateDatabase(database: StarWarsDatabase) {
            val planetDao = database.getPlanetDao()

            runBlocking {
                val initialPlanets = listOf(
                    Planet(
                        name = "Tatooine",
                        climate = "Arid",
                        terrain = "Desert",
                        orbital_period = 304
                    ),
                    Planet(
                        name = "Hoth",
                        climate = "Frozen",
                        terrain = "Tundra",
                        orbital_period = 549
                    ),
                    Planet(
                        name = "Endor",
                        climate = "Temperate",
                        terrain = "Forests",
                        orbital_period = 402
                    )
                )
                planetDao.insertAll(initialPlanets)
            }
        }
    }
}