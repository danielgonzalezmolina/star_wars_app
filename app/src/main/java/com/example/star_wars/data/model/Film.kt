package com.example.star_wars.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "films", indices = [androidx.room.Index(value = ["title"], unique = true)])
data class Film(
    @PrimaryKey
    val episode_id: Int = 0,
    val title: String = "",
    val opening_crawl: String = "",
    val director: String = "",
    val producer: String = "",
    val release_date: String = "",
    val era: String = "",
    val rating: String = "",
    val is_original_trilogy: Boolean = false,
    val url: String = "",
    val created: String = "",
    val edited: String = ""
)
