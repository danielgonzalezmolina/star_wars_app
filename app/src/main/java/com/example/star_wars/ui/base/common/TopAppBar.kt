package com.example.star_wars.ui.base.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
//import app.base.ui.common.Action
import kotlin.collections.filter

/**
 * Clase que representa el estado de la Top App Bar.
 * @property title         el título de la App Bar.
 * @property iconUpAction  el ícono utilizado para la navigationButton.
 * @property upAction      el evento de la navigationButton.
 * @property actions       la lista de acciones disponibles en la App Bar.
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopAppBar(state: BaseTopAppBarState) {
    val visibleActions = state.actions.filter { it.isVisible }
    val overflowActions = state.actions.filter { !it.isVisible }

    TopAppBar(
        title = {
            Text(
                text = state.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = state.upAction) {
                Icon(
                    painter = state.iconUpAction,
                    contentDescription = "Atrás",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            visibleActions.forEach { action ->
                IconButton(onClick = action.onClick) {
                    when (action) {
                        is Action.ActionImageVector -> {
                            Icon(
                                imageVector = action.icon,
                                contentDescription = action.contentDescription,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        is Action.ActionPainter -> {
                            Icon(
                                painter = action.icon,
                                contentDescription = action.contentDescription,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            if (overflowActions.isNotEmpty()) {
                TopAppBarDropDownMenu(overflowActions)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun TopAppBarDropDownMenu(actions: List<Action>) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Más opciones",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            actions.forEach { action ->
                DropdownMenuItem(
                    text = { Text(text = action.name) },
                    onClick = {
                        expanded = false
                        action.onClick()
                    }
                )
            }
        }
    }
}