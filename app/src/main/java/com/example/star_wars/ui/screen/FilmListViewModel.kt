package com.example.star_wars.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.star_wars.data.model.FilmWithPlanets
import com.example.star_wars.data.repository.FilmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmListViewModel @Inject constructor(
    private val repository: FilmRepository
) : ViewModel() {

    var filmList by mutableStateOf(listOf<FilmWithPlanets>())
        private set

    init {
        loadFilms()
    }

    private fun loadFilms() {
        viewModelScope.launch {
            repository.getFilmsWithPlanets().collect { list ->
                filmList = list
            }
        }
    }
}
