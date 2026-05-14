package com.example.star_wars.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.star_wars.data.model.Film
import com.example.star_wars.data.model.Planet
import com.example.star_wars.data.repository.FilmRepository
import com.example.star_wars.data.repository.PlanetRepository
import com.example.star_wars.util.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmEditViewModel @Inject constructor(
    private val filmRepository: FilmRepository,
    private val planetRepository: PlanetRepository,
    private val notificationHelper: NotificationHelper
) : ViewModel() {

    var selectedFilm by mutableStateOf<Film?>(null)
        private set

    var allPlanets by mutableStateOf(listOf<Planet>())
        private set

    var selectedPlanetIds by mutableStateOf(setOf<Int>())
        private set

    var showDuplicateError by mutableStateOf(false)
        private set

    var duplicateTitle by mutableStateOf("")
        private set

    init {
        loadAllPlanets()
    }

    private fun loadAllPlanets() {
        viewModelScope.launch {
            allPlanets = planetRepository.getPlanetsFlow().first()
        }
    }

    fun loadFilm(episodeId: Int) {
        if (episodeId == 0) {
            selectedFilm = Film()
            selectedPlanetIds = emptySet()
            return
        }
        viewModelScope.launch {
            val filmWithPlanets = filmRepository.getFilmWithPlanetsById(episodeId)
            selectedFilm = filmWithPlanets?.film
            selectedPlanetIds = filmWithPlanets?.planets?.map { it.id }?.toSet() ?: emptySet()
        }
    }

    fun togglePlanetSelection(planetId: Int) {
        selectedPlanetIds = if (selectedPlanetIds.contains(planetId)) {
            selectedPlanetIds - planetId
        } else {
            selectedPlanetIds + planetId
        }
    }

    fun dismissError() {
        showDuplicateError = false
    }

    fun saveFilm(film: Film, isNew: Boolean, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (isNew && (filmRepository.checkIfFilmExists(film.episode_id) || filmRepository.checkIfFilmTitleExists(film.title))) {
                duplicateTitle = film.title
                showDuplicateError = true
                onResult(false)
            } else {
                filmRepository.saveFilmWithPlanets(film, selectedPlanetIds.toList())
                if (isNew) {
                    notificationHelper.showCreationNotification(film.title, isPlanet = false)
                }
                onResult(true)
            }
        }
    }
}
