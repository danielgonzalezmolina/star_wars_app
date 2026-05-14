package com.example.star_wars.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.star_wars.ui.base.common.Action
import com.example.star_wars.ui.base.common.BaseTopAppBar
import com.example.star_wars.ui.base.common.BaseTopAppBarState
import com.example.star_wars.ui.home.NavHostScreen
import com.example.star_wars.ui.home.Routes
import com.example.star_wars.ui.theme.StarWarsTheme
import kotlinx.coroutines.launch

sealed class DrawerItem(val route: String, val icon: ImageVector, val label: String) {
    object Inicio : DrawerItem("dashboard", Icons.Default.Dashboard, "Inicio")
    object Listado : DrawerItem(Routes.LISTAR, Icons.AutoMirrored.Filled.List, "Planetas")
    object Peliculas : DrawerItem(Routes.PELICULAS, Icons.Default.Movie, "Películas")
    object Ajustes : DrawerItem("settings", Icons.Default.Settings, "Ajustes")
    object SobreNosotros : DrawerItem(Routes.ABOUT, Icons.Default.Info, "Sobre Nosotros")
}

@Composable
fun MainNavigationContainer(
    navController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        DrawerItem.Inicio,
        DrawerItem.Listado,
        DrawerItem.Peliculas,
        DrawerItem.SobreNosotros
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = StarWarsTheme.customColors.mainBackground,
            ) {
                Spacer(Modifier.height(12.dp))
                Text(
                    "STAR WARS NAV",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = StarWarsTheme.customColors.textPrimary
                )
                HorizontalDivider(color = StarWarsTheme.customColors.accent.copy(alpha = 0.2f))

                items.forEach { item ->
                    val isSelected = currentRoute == item.route
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.label) },
                        selected = isSelected,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedTextColor = StarWarsTheme.customColors.textPrimary,
                            unselectedIconColor = StarWarsTheme.customColors.accent,
                            selectedTextColor = StarWarsTheme.customColors.accent,
                            selectedIconColor = StarWarsTheme.customColors.accent
                        )
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                val topBarActions = remember(currentRoute) {
                    when (currentRoute) {
                        Routes.PELICULAS, Routes.CREAR_PELICULA, Routes.EDITAR_PELICULA -> listOf(
                            Action.ActionImageVector(
                                name = "Añadir",
                                icon = Icons.Default.Add,
                                onClick = { navController.navigate(Routes.CREAR_PELICULA) }
                            ),
                            Action.ActionImageVector(
                                name = "Listar",
                                icon = Icons.AutoMirrored.Filled.List,
                                onClick = { navController.navigate(Routes.PELICULAS) }
                            )
                        )
                        else -> listOf(
                            Action.ActionImageVector(
                                name = "Añadir",
                                icon = Icons.Default.Add,
                                onClick = { navController.navigate(Routes.CREAR) }
                            ),
                            Action.ActionImageVector(
                                name = "Listar",
                                icon = Icons.AutoMirrored.Filled.List,
                                onClick = { navController.navigate(Routes.LISTAR) }
                            )
                        )
                    }
                }

                BaseTopAppBar(
                    state = BaseTopAppBarState(
                        title = "Star Wars App",
                        iconUpAction = rememberVectorPainter(Icons.Default.Menu),
                        upAction = { scope.launch { drawerState.open() } },
                        actions = topBarActions
                    )
                )
            },
            floatingActionButton = {
                if (currentRoute == Routes.LISTAR) {
                    FloatingActionButton(
                        onClick = { navController.navigate(Routes.CREAR) },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir Planeta")
                    }
                }
            },
            containerColor = StarWarsTheme.customColors.mainBackground
        ) { padding ->
            NavHostScreen(
                navController = navController,
                modifier = Modifier.padding(padding)
            )
        }
    }
}
