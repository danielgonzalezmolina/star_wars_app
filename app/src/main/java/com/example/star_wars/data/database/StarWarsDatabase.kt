package com.example.star_wars.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.star_wars.data.dao.FilmDao
import com.example.star_wars.data.dao.PlanetDao
import com.example.star_wars.data.model.Film
import com.example.star_wars.data.model.FilmPlanetCrossRef
import com.example.star_wars.data.model.Planet
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

@Database(
    version = 10,
    entities = [
        Planet::class,
        Film::class,
        FilmPlanetCrossRef::class
    ],
    exportSchema = false
)
abstract class StarWarsDatabase : RoomDatabase() {

    abstract fun getPlanetDao(): PlanetDao
    abstract fun getFilmDao(): FilmDao

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
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Executors.newSingleThreadExecutor().execute {
                                INSTANCE?.let { database ->
                                    prepopulateDatabase(database)
                                }
                            }
                        }

                        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                            super.onDestructiveMigration(db)
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
            val filmDao = database.getFilmDao()

            runBlocking {
                val initialPlanets = listOf(
                    Planet(id = 1, name = "Tatooine", climate = "Arid", terrain = "Desert", orbital_period = 304),
                    Planet(id = 2, name = "Hoth", climate = "Frozen", terrain = "Tundra", orbital_period = 549),
                    Planet(id = 3, name = "Endor", climate = "Temperate", terrain = "Forests", orbital_period = 402)
                )
                planetDao.insertAll(initialPlanets)

                val initialFilms = listOf(
                    Film(
                        episode_id = 4,
                        title = "A New Hope",
                        director = "George Lucas",
                        release_date = "1977-05-25"
                    ),
                    Film(
                        episode_id = 5,
                        title = "The Empire Strikes Back",
                        director = "Irvin Kershner",
                        release_date = "1980-05-21"
                    )
                )
                initialFilms.forEach { filmDao.insert(it) }

                filmDao.insertFilmPlanetCrossRef(FilmPlanetCrossRef(4, 1)) // A New Hope -> Tatooine
                filmDao.insertFilmPlanetCrossRef(FilmPlanetCrossRef(5, 2)) // Empire -> Hoth
            }
        }
    }
}