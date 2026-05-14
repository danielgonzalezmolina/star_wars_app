package com.example.star_wars.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.star_wars.data.model.Film
import com.example.star_wars.data.model.Planet
import com.example.star_wars.ui.composables.FormSection
import com.example.star_wars.ui.composables.HeaderBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmEditScreen(
    initialFilm: Film,
    allPlanets: List<Planet>,
    selectedPlanetIds: Set<Int>,
    onTogglePlanet: (Int) -> Unit,
    onSave: (Film) -> Unit,
    modifier: Modifier = Modifier
) {
    var filmState by remember(initialFilm) { mutableStateOf(initialFilm) }
    val isTitleValid = filmState.title.isNotBlank()
    val isEpisodeValid = filmState.episode_id != 0
    val isCreating = initialFilm.episode_id == 0

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize().navigationBarsPadding().imePadding()) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HeaderBox()

            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (isCreating) "Registrar Película" else "Editar Película",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
                )

                FormSection("Identidad", Icons.Default.Info)

                OutlinedTextField(
                    value = filmState.episode_id.toString(),
                    onValueChange = { newValue ->
                        val number = newValue.filter { it.isDigit() }.toIntOrNull() ?: 0
                        filmState = filmState.copy(episode_id = number)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("ID de Episodio") },
                    isError = !isEpisodeValid,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = { Icon(Icons.Default.Numbers, null) }
                )

                OutlinedTextField(
                    value = filmState.title,
                    onValueChange = { filmState = filmState.copy(title = it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Título de la Película") },
                    isError = !isTitleValid,
                    supportingText = { if (!isTitleValid) Text("El título es obligatorio") },
                    leadingIcon = { Icon(Icons.Default.Movie, null) }
                )

                FormSection("Producción", Icons.Default.MovieFilter)

                OutlinedTextField(
                    value = filmState.director,
                    onValueChange = { filmState = filmState.copy(director = it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Director") },
                    leadingIcon = { Icon(Icons.Default.Person, null) }
                )

                OutlinedTextField(
                    value = filmState.release_date,
                    onValueChange = { filmState = filmState.copy(release_date = it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Fecha de Estreno (YYYY-MM-DD)") },
                    leadingIcon = { Icon(Icons.Default.Event, null) }
                )

                FormSection("Planetas", Icons.Default.Public)

                // Dropdown para seleccionar planetas
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = "Seleccionar planeta...",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        allPlanets.forEach { planet ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = selectedPlanetIds.contains(planet.id),
                                            onCheckedChange = null // Click handled by MenuItem
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(planet.name)
                                    }
                                },
                                onClick = {
                                    onTogglePlanet(planet.id)
                                }
                            )
                        }
                    }
                }

                if (selectedPlanetIds.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        selectedPlanetIds.forEach { id ->
                            val planetName = allPlanets.find { it.id == id }?.name ?: "Unknown"
                            InputChip(
                                selected = true,
                                onClick = { onTogglePlanet(id) },
                                label = { Text(planetName) },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remove",
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onSave(filmState) },
                    enabled = isTitleValid && isEpisodeValid,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(if (isCreating) Icons.Default.CloudUpload else Icons.Default.Save, null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (isCreating) "Guardar en Base de Datos" else "Actualizar Película")
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
