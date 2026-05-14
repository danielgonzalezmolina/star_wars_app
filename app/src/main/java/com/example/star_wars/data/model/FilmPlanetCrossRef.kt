package com.example.star_wars.data.model

import androidx.room.Entity

@Entity(primaryKeys = ["episode_id", "id"])
data class FilmPlanetCrossRef(
    val episode_id: Int,
    val id: Int
)
