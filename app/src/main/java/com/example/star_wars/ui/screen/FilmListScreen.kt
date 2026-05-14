package com.example.star_wars.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.star_wars.data.model.FilmWithPlanets
import com.example.star_wars.ui.composables.SearchBar

@Composable
fun FilmListScreen(
    modifier: Modifier = Modifier,
    filmList: List<FilmWithPlanets>,
    onFilmClick: (FilmWithPlanets) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedDirector by remember { mutableStateOf<String?>(null) }

    val availableDirectors = remember(filmList) {
        filmList.map { it.film.director }
            .filter { it.isNotEmpty() }
            .distinct()
            .sorted()
    }

    val filteredList = remember(searchQuery, selectedDirector, filmList) {
        filmList.filter { item ->
            val matchesSearch = item.film.title.contains(searchQuery, ignoreCase = true)
            val matchesFilter = selectedDirector == null || item.film.director == selectedDirector
            matchesSearch && matchesFilter
        }
    }

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            )

            DirectorFilterBar(
                selectedDirector = selectedDirector,
                availableDirectors = availableDirectors,
                onFilterChange = { selectedDirector = it }
            )

            if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No hay películas", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredList, key = { it.film.episode_id }) { filmWithPlanets ->
                        FilmItem(
                            filmWithPlanets = filmWithPlanets,
                            onFilmClick = onFilmClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DirectorFilterBar(
    selectedDirector: String?,
    availableDirectors: List<String>,
    onFilterChange: (String?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedDirector == null,
            onClick = { onFilterChange(null) },
            label = { Text("Todos") }
        )

        availableDirectors.forEach { director ->
            FilterChip(
                selected = selectedDirector == director,
                onClick = { onFilterChange(if (selectedDirector == director) null else director) },
                label = { Text(director) },
                leadingIcon = if (selectedDirector == director) {
                    { Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp)) }
                } else null
            )
        }
    }
}

@Composable
fun FilmItem(
    filmWithPlanets: FilmWithPlanets,
    onFilmClick: (FilmWithPlanets) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onFilmClick(filmWithPlanets) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Movie, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Episodio ${filmWithPlanets.film.episode_id}: ${filmWithPlanets.film.title}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(Modifier.height(8.dp))
            Text(text = "Director: ${filmWithPlanets.film.director}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Fecha: ${filmWithPlanets.film.release_date}", style = MaterialTheme.typography.bodySmall)
            
            if (filmWithPlanets.planets.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Public, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.secondary)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Planetas: " + filmWithPlanets.planets.joinToString { it.name },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}
