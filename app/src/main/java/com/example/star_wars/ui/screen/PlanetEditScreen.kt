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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.star_wars.R
import com.example.star_wars.data.model.Planet
import com.example.star_wars.ui.composables.FormSection
import com.example.star_wars.ui.composables.HeaderBox
import com.example.star_wars.ui.theme.StarWarsTheme

@Composable
fun PlanetEditScreen(
    initialPlanet: Planet,
    onSave: (Planet) -> Unit,
    modifier: Modifier = Modifier
) {
    var planetState by remember(initialPlanet) { mutableStateOf(initialPlanet) }

    val isNameValid = planetState.name.isNotBlank()
    val isCreating = initialPlanet.id == 0

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
                    text = if (isCreating) "Registrar Planeta" else "Editar Planeta",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
                )

                FormSection("Identidad", Icons.Default.Info)

                OutlinedTextField(
                    value = planetState.name,
                    onValueChange = { planetState = planetState.copy(name = it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre del Planeta") },
                    isError = !isNameValid,
                    supportingText = { if (!isNameValid) Text("El nombre es obligatorio") },
                    leadingIcon = { Icon(Icons.Default.Public, null) }
                )

                FormSection("Datos Técnicos", Icons.Default.Straighten)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = planetState.rotation_period.toString(),
                        onValueChange = { newValue ->
                            val number = newValue.filter { it.isDigit() }.toIntOrNull() ?: 0
                            planetState = planetState.copy(rotation_period = number)
                        },
                        modifier = Modifier.weight(1f),
                        label = { Text("Rotación (h)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = planetState.orbital_period.toString(),
                        onValueChange = { newValue ->
                            val number = newValue.filter { it.isDigit() }.toIntOrNull() ?: 0
                            planetState = planetState.copy(orbital_period = number)
                        },
                        modifier = Modifier.weight(1f),
                        label = { Text("Órbita (d)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                FormSection("Ambiente y Estado", Icons.Default.Cloud)

                OutlinedTextField(
                    value = planetState.climate,
                    onValueChange = { planetState = planetState.copy(climate = it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Clima") },
                    leadingIcon = { Icon(Icons.Default.Thermostat, null) }
                )

                // INDICADOR VISUAL: ¿Está colonizado? (Boolean)
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Groups, null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(12.dp))
                            Text("¿Está colonizado?")
                        }
                        Switch(
                            checked = planetState.is_colonized,
                            onCheckedChange = { planetState = planetState.copy(is_colonized = it) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onSave(planetState) },
                    enabled = isNameValid,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(if (isCreating) Icons.Default.CloudUpload else Icons.Default.Save, null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (isCreating) "Guardar en Base de Datos" else "Actualizar Registro")
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true, name = "Modo Creación")
@Composable
fun PreviewCreate() {
    StarWarsTheme {
        PlanetEditScreen(
            initialPlanet = Planet.empty(),
            onSave = {}
        )
    }
}

@Preview(showBackground = true, name = "Modo Edición")
@Composable
fun PreviewEdit() {
    StarWarsTheme {
        PlanetEditScreen(
            initialPlanet = Planet(
                id = 1,
                name = "Coruscant",
                rotation_period = 24,
                orbital_period = 368,
                climate = "Temperate",
                terrain = "Cityscape, Mountains",
                discovery_date = "1977-05-25",
                is_colonized = true,
                user_id = 1
            ),
            onSave = {}
        )
    }
}