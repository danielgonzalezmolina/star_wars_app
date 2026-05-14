package com.example.star_wars.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.star_wars.data.model.Planet // USAR EL MODELO DE DATA
import com.example.star_wars.data.repository.PlanetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanetViewModel @Inject constructor(
    private val repository: PlanetRepository
) : ViewModel() {

    var planetList by mutableStateOf(listOf<Planet>())
        private set

    var selectedPlanet by mutableStateOf<Planet?>(null)
        private set

    var showDuplicateError by mutableStateOf(false)
        private set

    var duplicateName by mutableStateOf("")
        private set

    init {
        loadPlanets()
    }

    private fun loadPlanets() {
        viewModelScope.launch {
            repository.getPlanetsFlow().collect { list ->
                planetList = list
            }
        }
    }

    fun onPlanetSelected(planet: Planet?) {
        selectedPlanet = planet
    }

    fun dismissError() {
        showDuplicateError = false
    }

    fun savePlanet(planet: Planet) {
        viewModelScope.launch {
            if (selectedPlanet == null) {
                if (repository.checkIfPlanetExists(planet.id)) {
                    duplicateName = planet.name
                    showDuplicateError = true
                } else {
                    repository.addPlanet(planet)
                    selectedPlanet = null
                }
            } else {
                repository.updatePlanet(planet)
                selectedPlanet = null
            }
        }
    }

    fun deletePlanet(planet: Planet) {
        viewModelScope.launch {
            repository.deletePlanet(planet)
        }
    }
}