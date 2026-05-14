package com.example.star_wars.data.repository

import com.example.star_wars.data.dao.PlanetDao
import com.example.star_wars.data.model.Planet
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlanetRepository @Inject constructor(
    private val planetDao: PlanetDao
) {
    fun getPlanetsFlow(): Flow<List<Planet>> = planetDao.getAll()
    fun getPlanetsWithFilmsFlow() = planetDao.getPlanetsWithFilms()
    suspend fun getPlanetById(id: Int) = planetDao.getById(id)
    suspend fun addPlanet(planet: Planet) = planetDao.insert(planet)
    suspend fun updatePlanet(planet: Planet) = planetDao.update(planet)
    suspend fun deletePlanet(planet: Planet) = planetDao.delete(planet)
    suspend fun checkIfPlanetExists(id: Int): Boolean = planetDao.exists(id)
    suspend fun checkIfPlanetNameExists(name: String): Boolean = planetDao.existsByName(name)
}