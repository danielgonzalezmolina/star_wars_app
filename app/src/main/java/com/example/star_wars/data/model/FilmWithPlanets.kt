package com.example.star_wars.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FilmWithPlanets(
    @Embedded val film: Film,
    @Relation(
        parentColumn = "episode_id",
        entityColumn = "id",
        associateBy = Junction(FilmPlanetCrossRef::class)
    )
    val planets: List<Planet>
)
