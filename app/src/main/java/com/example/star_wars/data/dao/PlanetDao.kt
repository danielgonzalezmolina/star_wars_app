package com.example.star_wars.data.dao

import androidx.room.*
import com.example.star_wars.data.model.Planet
import com.example.star_wars.data.model.PlanetWithFilms
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanetDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(planet: Planet)

    @Delete
    suspend fun delete(planet: Planet)

    @Update
    suspend fun update(planet: Planet)


    @Query("SELECT * FROM planet")
    fun getAll(): Flow<List<Planet>>

    @Transaction
    @Query("SELECT * FROM planet")
    fun getPlanetsWithFilms(): Flow<List<PlanetWithFilms>>

    @Query("SELECT * FROM planet WHERE id = :id")
    suspend fun getById(id: Int): Planet?

    @Query("SELECT EXISTS (SELECT * FROM planet WHERE id = :id)")
    suspend fun exists(id: Int): Boolean

    @Query("SELECT EXISTS (SELECT * FROM planet WHERE name = :name)")
    suspend fun existsByName(name: String): Boolean

    @Query("DELETE FROM planet")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(planets: List<Planet>)
}