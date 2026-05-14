package com.example.star_wars.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.composables.Earth
import com.example.star_wars.data.model.Planet
import com.example.star_wars.data.model.PlanetWithFilms
import com.example.star_wars.ui.composables.SearchBar
import kotlinx.coroutines.launch

@Composable
fun PlanetListScreen(
    modifier: Modifier = Modifier,
    planetList: List<PlanetWithFilms>,
    onPlanetClick: (Planet) -> Unit,
    onDeletePlanet: (PlanetWithFilms) -> Unit
) {
    var planetToDelete by remember { mutableStateOf<PlanetWithFilms?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var selectedClimate by remember { mutableStateOf<String?>(null) }

    val availableClimates = remember(planetList) {
        planetList.flatMap { it.planet.climate.split(",") }
            .map { it.trim().lowercase() }
            .filter { it.isNotEmpty() && it != "unknown" }
            .distinct()
            .take(5)
    }

    val filteredList = remember(searchQuery, selectedClimate, planetList) {
        planetList.filter { item ->
            val matchesSearch = item.planet.name.contains(searchQuery, ignoreCase = true)
            val matchesFilter = selectedClimate == null ||
                    item.planet.climate.contains(selectedClimate!!, ignoreCase = true)
            matchesSearch && matchesFilter
        }
    }

    planetToDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { planetToDelete = null },
            title = { Text("¿Eliminar planeta?") },
            text = { Text("¿Estás seguro de que quieres borrar ${item.planet.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeletePlanet(item)
                        planetToDelete = null
                        scope.launch { snackbarHostState.showSnackbar("Planeta eliminado") }
                    }
                ) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { planetToDelete = null }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        PlanetListContent(
            modifier = Modifier.padding(paddingValues),
            planetList = filteredList,
            searchQuery = searchQuery,
            selectedClimate = selectedClimate,
            availableClimates = availableClimates,
            onQueryChange = { searchQuery = it },
            onFilterChange = { selectedClimate = it },
            onPlanetClick = onPlanetClick,
            onDeleteClick = { planet -> planetToDelete = planet }
        )
    }
}

@Composable
fun PlanetListContent(
    planetList: List<PlanetWithFilms>,
    searchQuery: String,
    selectedClimate: String?,
    availableClimates: List<String>,
    onQueryChange: (String) -> Unit,
    onFilterChange: (String?) -> Unit,
    onPlanetClick: (Planet) -> Unit,
    onDeleteClick: (PlanetWithFilms) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(
            query = searchQuery,
            onQueryChange = onQueryChange
        )

        FilterOptionBar(
            selectedClimate = selectedClimate,
            availableClimates = availableClimates,
            onFilterChange = onFilterChange
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (planetList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No hay resultados", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(planetList, key = { it.planet.id }) { planetWithFilms ->
                    PlanetItem(
                        planetWithFilms = planetWithFilms,
                        onPlanetClick = onPlanetClick,
                        onDeleteClick = { onDeleteClick(planetWithFilms) }
                    )
                }
            }
        }
    }
}

@Composable
fun PlanetItem(
    planetWithFilms: PlanetWithFilms,
    onPlanetClick: (Planet) -> Unit,
    onDeleteClick: () -> Unit
) {
    val planet = planetWithFilms.planet
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onPlanetClick(planet) },
        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.Delete, "Borrar", tint = colorScheme.error)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = colorScheme.primaryContainer, modifier = Modifier.size(48.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Earth, null, modifier = Modifier.size(24.dp))
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(planet.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    PlanetAttribute(Icons.Default.Thermostat, "Clima", planet.climate)
                    PlanetAttribute(Icons.Default.Landscape, "Terreno", planet.terrain)
                    PlanetAttribute(Icons.Default.Groups, "Órbita", planet.orbital_period.toString())
                    
                    if (planetWithFilms.films.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Movie, null, modifier = Modifier.size(14.dp), tint = colorScheme.secondary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Películas: " + planetWithFilms.films.joinToString { it.title },
                                style = MaterialTheme.typography.labelMedium,
                                color = colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlanetAttribute(
    icon: ImageVector,
    label: String,
    value: String
) {
    if (value.isNotEmpty() && value != "unknown") {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$label: ",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun FilterOptionBar(
    selectedClimate: String?,
    availableClimates: List<String>,
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
            selected = selectedClimate == null,
            onClick = { onFilterChange(null) },
            label = { Text("Todos") }
        )

        availableClimates.forEach { climate ->
            FilterChip(
                selected = selectedClimate == climate,
                onClick = { onFilterChange(if (selectedClimate == climate) null else climate) },
                label = { Text(climate.replaceFirstChar { it.uppercase() }) },
                leadingIcon = if (selectedClimate == climate) {
                    { Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp)) }
                } else null
            )
        }
    }
}
