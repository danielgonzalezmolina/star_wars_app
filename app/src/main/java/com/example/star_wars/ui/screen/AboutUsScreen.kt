package com.example.star_wars.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun AboutUsScreen() {
    val scrollState = rememberScrollState()
    val colorScheme = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Wiki Planetas",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = colorScheme.primary
            )

            Text(
                text = "Versión 1.1.0",
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            CardInfo(
                title = "Info",
                content = "Esta aplicación permite la gestión y consulta de planetas del universo Star Wars. Desarrollada como práctica para la asignatura de Desarrollo de Interfaces."
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Desarrolladores",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            AvisoDesarrollador()

            Spacer(modifier = Modifier.height(16.dp))

            // LISTA DE DESARROLLADORES CON ROLES
            DeveloperCard(name = "Javier García", role = "Películas", isLeader = true, icon = Icons.Default.Camera)
            Spacer(modifier = Modifier.height(12.dp))
            DeveloperCard(name = "Álvaro Llamas", role = "Especies", icon = Icons.Default.Man)
            Spacer(modifier = Modifier.height(12.dp))
            DeveloperCard(name = "David Casasola", role = "Personajes", icon = Icons.Default.Face)
            Spacer(modifier = Modifier.height(12.dp))
            DeveloperCard(name = "Daniel González", role = "Planetas", icon = Icons.Default.Public)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Contacto",
                style = MaterialTheme.typography.titleMedium,
                color = colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(8.dp))

            SocialRow(icon = Icons.Default.Email, text = "Enviar Email")

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "© 2026 IES Portada Alta",
                color = colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun AvisoDesarrollador() {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorScheme.errorContainer.copy(alpha = 0.2f)),
        border = BorderStroke(1.dp, colorScheme.error.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Warning, null, tint = colorScheme.error, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Aclaración: Esta ya no es una práctica en grupo. El desarrollador actual es Daniel González.",
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
fun DeveloperCard(
    name: String,
    role: String,
    icon: ImageVector,
    isLeader: Boolean = false
) {
    val colorScheme = MaterialTheme.colorScheme
    val borderColor = if (isLeader) colorScheme.primary else colorScheme.primary.copy(alpha = 0.3f)
    val bgColor = if (isLeader) colorScheme.primary.copy(alpha = 0.1f) else colorScheme.surfaceVariant.copy(alpha = 0.3f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(if (isLeader) 2.dp else 1.dp, borderColor),
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (isLeader) colorScheme.primary else colorScheme.primaryContainer)
                        .padding(8.dp),
                    tint = if (isLeader) colorScheme.onPrimary else colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isLeader) colorScheme.primary else colorScheme.onSurface
                )
                Text(
                    text = role,
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }
        }
    }
}



@Composable
fun CardInfo(title: String, content: String) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(1.dp, colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Composable
fun SocialRow(icon: ImageVector, text: String) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(colorScheme.primary.copy(alpha = 0.1f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, color = colorScheme.onBackground, style = MaterialTheme.typography.bodyMedium)
    }
}



@Preview(showBackground = true )
@Composable
fun AboutUsPreview(){
    AboutUsScreen()
}