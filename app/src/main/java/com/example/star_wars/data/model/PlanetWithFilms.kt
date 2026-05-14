package com.example.star_wars.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlanetWithFilms(
    @Embedded val planet: Planet,
    @Relation(
        parentColumn = "id",
        entityColumn = "episode_id",
        associateBy = Junction(FilmPlanetCrossRef::class)
    )
    val films: List<Film>
)
