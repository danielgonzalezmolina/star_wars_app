package com.example.star_wars.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.composables.Earth
import com.example.star_wars.data.model.Planet
import kotlinx.coroutines.launch

@Composable
fun PlanetListScreen(
    modifier: Modifier = Modifier,
    planetList: List<Planet>,
    onPlanetClick: (Planet) -> Unit,
    onDeletePlanet: (Planet) -> Unit
) {
    var planetToDelete by remember { mutableStateOf<Planet?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var selectedClimate by remember { mutableStateOf<String?>(null) }

    val availableClimates = remember(planetList) {
        planetList.flatMap { it.climate.split(",") }
            .map { it.trim().lowercase() }
            .filter { it.isNotEmpty() && it != "unknown" }
            .distinct()
            .take(5)
    }

    val filteredList = remember(searchQuery, selectedClimate, planetList) {
        planetList.filter { planet ->
            val matchesSearch = planet.name.contains(searchQuery, ignoreCase = true)
            val matchesFilter = selectedClimate == null ||
                    planet.climate.contains(selectedClimate!!, ignoreCase = true)
            matchesSearch && matchesFilter
        }
    }

    planetToDelete?.let { planet ->
        AlertDialog(
            onDismissRequest = { planetToDelete = null },
            title = { Text("¿Eliminar planeta?") },
            text = { Text("¿Estás seguro de que quieres borrar ${planet.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeletePlanet(planet)
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
    planetList: List<Planet>,
    searchQuery: String,
    selectedClimate: String?,
    availableClimates: List<String>,
    onQueryChange: (String) -> Unit,
    onFilterChange: (String?) -> Unit,
    onPlanetClick: (Planet) -> Unit,
    onDeleteClick: (Planet) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        /*Para mejorar los estilos y agregar nueva funcionalidad a la página, he añadido un
        *buscador y una barra de filtros para filtrar por tipo de clima
        */

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
                items(planetList, key = { it.name }) { planet ->
                    PlanetItem(
                        planet = planet,
                        onPlanetClick = onPlanetClick,
                        onDeleteClick = { onDeleteClick(planet) }
                    )
                }
            }
        }
    }
}



@Composable
fun PlanetItem(
    planet: Planet,
    onPlanetClick: (Planet) -> Unit,
    onDeleteClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onPlanetClick(planet) },
        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            // Icono de borrar
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
                    // Adaptado a los campos reales de tu clase Planet:
                    PlanetAttribute(Icons.Default.Thermostat, "Clima", planet.climate)
                    PlanetAttribute(Icons.Default.Landscape, "Terreno", planet.terrain)
                    PlanetAttribute(Icons.Default.Groups, "Órbita", planet.orbital_period.toString())
                }
            }
        }
    }
}

@Composable
fun PlanetAttribute(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Buscar planeta...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Borrar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
    )
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

@Preview(showBackground = true)
@Composable
fun PlanetListScreenPreview() {
    MaterialTheme {
        PlanetListScreen(
            planetList = listOf(
                Planet(
                    id = 1,
                    name = "Tatooine",
                    climate = "Arid",
                    terrain = "Desert",
                    rotation_period = 23,
                    orbital_period = 304,
                    discovery_date = "1977-05-25",
                    is_colonized = true
                ),
                Planet(
                    id = 2,
                    name = "Alderaan",
                    climate = "Temperate",
                    terrain = "Grasslands, Mountains",
                    rotation_period = 24,
                    orbital_period = 364,
                    discovery_date = "1977-05-25",
                    is_colonized = true
                ),
                Planet(
                    id = 3,
                    name = "Hoth",
                    climate = "Frozen",
                    terrain = "Tundra, Ice Caves",
                    rotation_period = 23,
                    orbital_period = 549,
                    discovery_date = "1980-05-21",
                    is_colonized = false
                ),
                Planet(
                    id = 4,
                    name = "Dagobah",
                    climate = "Murky",
                    terrain = "Swamp, Jungles",
                    rotation_period = 23,
                    orbital_period = 341,
                    discovery_date = "1980-05-21",
                    is_colonized = false
                )
            ),
            onPlanetClick = {},
            onDeletePlanet = {}
        )
    }
}