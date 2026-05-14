package com.example.star_wars.data.model

//Clase de cuando era una práctica en grupo. La mantengo para posible uso futuro
data class Films(
    var title: String = "",
    var episode_id: Int = 0,
    var opening_crawl: String = "",
    var director: String = "",
    var producer: String = "",
    var release_date: String = "",
    var era: String = "",
    var rating: String = "",
    var is_original_trilogy: Boolean = false,
    var species: List<String> = emptyList(),
    var starships: List<String> = emptyList(),
    var vehicles: List<String> = emptyList(),
    var characters: List<String> = emptyList(),
    var planets: List<String> = emptyList(),
    var url: String = "",
    var created: String = "",
    var edited: String = ""
)