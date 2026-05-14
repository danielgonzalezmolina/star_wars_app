package com.example.star_wars.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.star_wars.data.model.PlanetWithFilms
import com.example.star_wars.data.repository.PlanetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanetListViewModel @Inject constructor(
    private val repository: PlanetRepository
) : ViewModel() {

    var planetList by mutableStateOf(listOf<PlanetWithFilms>())
        private set

    init {
        loadPlanets()
    }

    private fun loadPlanets() {
        viewModelScope.launch {
            repository.getPlanetsWithFilmsFlow().collect { list ->
                planetList = list
            }
        }
    }

    fun deletePlanet(planetWithFilms: PlanetWithFilms) {
        viewModelScope.launch {
            repository.deletePlanet(planetWithFilms.planet)
        }
    }
}
