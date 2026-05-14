package com.example.star_wars.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.star_wars.data.model.Planet
import com.example.star_wars.data.repository.PlanetRepository
import com.example.star_wars.util.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanetEditViewModel @Inject constructor(
    private val repository: PlanetRepository,
    private val notificationHelper: NotificationHelper
) : ViewModel() {

    var selectedPlanet by mutableStateOf<Planet?>(null)
        private set

    var showDuplicateError by mutableStateOf(false)
        private set

    var duplicateName by mutableStateOf("")
        private set

    fun loadPlanet(planetId: Int) {
        if (planetId == 0) {
            selectedPlanet = Planet()
            return
        }
        viewModelScope.launch {
            selectedPlanet = repository.getPlanetById(planetId)
        }
    }

    fun dismissError() {
        showDuplicateError = false
    }

    fun savePlanet(planet: Planet, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isEditing = planet.id != 0
            if (!isEditing) {
                if (repository.checkIfPlanetExists(planet.id) || repository.checkIfPlanetNameExists(planet.name)) {
                    duplicateName = planet.name
                    showDuplicateError = true
                    onResult(false)
                } else {
                    repository.addPlanet(planet)
                    notificationHelper.showCreationNotification(planet.name, isPlanet = true)
                    onResult(true)
                }
            } else {
                repository.updatePlanet(planet)
                onResult(true)
            }
        }
    }
}
