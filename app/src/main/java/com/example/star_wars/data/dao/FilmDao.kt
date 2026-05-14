package com.example.star_wars.data.dao

import androidx.room.*
import com.example.star_wars.data.model.Film
import com.example.star_wars.data.model.FilmPlanetCrossRef
import com.example.star_wars.data.model.FilmWithPlanets
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(film: Film)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilmPlanetCrossRef(crossRef: FilmPlanetCrossRef)

    @Transaction
    @Query("SELECT * FROM films")
    fun getFilmsWithPlanets(): Flow<List<FilmWithPlanets>>

    @Transaction
    @Query("SELECT * FROM films WHERE episode_id = :episodeId")
    suspend fun getFilmWithPlanetsById(episodeId: Int): FilmWithPlanets?

    @Query("DELETE FROM FilmPlanetCrossRef WHERE episode_id = :episodeId")
    suspend fun deleteCrossRefsForFilm(episodeId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM films WHERE episode_id = :id)")
    suspend fun exists(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM films WHERE title = :title)")
    suspend fun existsByTitle(title: String): Boolean
}
