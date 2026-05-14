package com.example.star_wars.data.repository

import com.example.star_wars.data.dao.FilmDao
import com.example.star_wars.data.model.Film
import com.example.star_wars.data.model.FilmPlanetCrossRef
import com.example.star_wars.data.model.FilmWithPlanets
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilmRepository @Inject constructor(
    private val filmDao: FilmDao
) {
    fun getFilmsWithPlanets(): Flow<List<FilmWithPlanets>> = filmDao.getFilmsWithPlanets()

    suspend fun getFilmWithPlanetsById(id: Int) = filmDao.getFilmWithPlanetsById(id)

    suspend fun insertFilm(film: Film) = filmDao.insert(film)

    suspend fun checkIfFilmExists(id: Int) = filmDao.exists(id)
    suspend fun checkIfFilmTitleExists(title: String) = filmDao.existsByTitle(title)

    suspend fun saveFilmWithPlanets(film: Film, planetIds: List<Int>) {
        filmDao.insert(film)
        filmDao.deleteCrossRefsForFilm(film.episode_id)
        planetIds.forEach { planetId ->
            filmDao.insertFilmPlanetCrossRef(FilmPlanetCrossRef(film.episode_id, planetId))
        }
    }

    suspend fun addPlanetToFilm(filmId: Int, planetId: Int) {
        filmDao.insertFilmPlanetCrossRef(FilmPlanetCrossRef(filmId, planetId))
    }
}
