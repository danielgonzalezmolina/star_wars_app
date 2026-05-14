package com.example.star_wars.ui.base.common

import androidx.compose.ui.graphics.vector.ImageVector

sealed class Action(
    val name: String,
    val isVisible: Boolean,
    val contentDescription: String?,
    val onClick: () -> Unit
) {
    class ActionImageVector(
        name: String,
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        isVisible: Boolean = true,
        contentDescription: String? = null,
        onClick: () -> Unit
    ) : Action(name, isVisible, contentDescription, onClick)

    class ActionPainter(
        name: String,
        val icon: androidx.compose.ui.graphics.painter.Painter,
        isVisible: Boolean = true,
        contentDescription: String? = null,
        onClick: () -> Unit
    ) : Action(name, isVisible, contentDescription, onClick)
}

data class BaseTopAppBarState(
    val title: String,
    val iconUpAction: androidx.compose.ui.graphics.painter.Painter,
    val upAction: () -> Unit,
    val actions: List<Action> = emptyList()
)

/**sealed class Action(
val name: String,
val onClick: () -> Unit,
val isVisible: Boolean = true
) {

class ActionVector(
name: String,
val icon: ImageVector,
onClick: () -> Unit,
isVisible: Boolean = true
) : Action(name,  onClick, isVisible)
class ActionPainter(
name: String,
val icon: Painter,
onClick: () -> Unit,
isVisible: Boolean = true
) : Action(name,  onClick, isVisible)
}**/