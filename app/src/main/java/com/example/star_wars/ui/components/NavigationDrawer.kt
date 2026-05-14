package com.example.star_wars.ui.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.star_wars.R
import com.example.star_wars.data.model.Planet
import com.example.star_wars.ui.base.common.BaseTopAppBar
import com.example.star_wars.ui.base.common.BaseTopAppBarState
import com.example.star_wars.ui.home.Routes
import com.example.star_wars.ui.screen.AboutUsScreen
import com.example.star_wars.ui.screen.PlanetEditScreen
import com.example.star_wars.ui.screen.PlanetListScreen
import com.example.star_wars.ui.screen.PlanetViewModel
import com.example.star_wars.ui.theme.StarWarsTheme
import kotlinx.coroutines.launch

sealed class DrawerItem(val route: String, val icon: ImageVector, val label: String) {
    object Inicio : DrawerItem("dashboard", Icons.Default.Dashboard, "Inicio")
    object Listado : DrawerItem(Routes.LISTAR, Icons.AutoMirrored.Filled.List, "Planetas")
    object Ajustes : DrawerItem("settings", Icons.Default.Settings, "Ajustes")
    object SobreNosotros : DrawerItem(Routes.ABOUT, Icons.Default.Info, "Sobre Nosotros")
}

@Composable
fun MainNavigationContainer(
    navController: NavHostController = rememberNavController(),
    viewModel: PlanetViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val items = listOf(
        DrawerItem.Inicio,
        DrawerItem.Listado,
        DrawerItem.SobreNosotros
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = StarWarsTheme.customColors.accent,
            ) {
                Spacer(Modifier.height(12.dp))
                Text(
                    "STAR WARS NAV",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = StarWarsTheme.customColors.accent
                )
                HorizontalDivider(color = StarWarsTheme.customColors.accent.copy(alpha = 0.2f))

                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.label) },
                        selected = false,
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
                            unselectedIconColor = StarWarsTheme.customColors.accent
                        )
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                BaseTopAppBar(
                    state = BaseTopAppBarState(
                        title = "Star Wars App",
                        iconUpAction = rememberVectorPainter(Icons.Default.Menu),
                        upAction = { scope.launch { drawerState.open() } }
                    )
                )
            },
            containerColor = StarWarsTheme.customColors.mainBackground
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Routes.LISTAR,
                modifier = Modifier.padding(padding),
                enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)) },
                exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)) },
                popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)) },
                popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)) }
            ) {
                composable(Routes.LISTAR) {
                    PlanetListScreen(
                        planetList = viewModel.planetList,
                        onPlanetClick = { planet ->
                            viewModel.onPlanetSelected(planet)
                            navController.navigate(Routes.EDITAR)
                        },
                        onDeletePlanet = { planet ->
                            viewModel.deletePlanet(planet)
                        }
                    )
                }

                composable(Routes.CREAR) {
                    LaunchedEffect(Unit) { viewModel.onPlanetSelected(null) }

                    PlanetEditScreen(
                        initialPlanet = Planet(),
                        onSave = { nuevo ->
                            viewModel.savePlanet(nuevo)
                            navController.popBackStack()
                        }
                    )
                }

                composable(Routes.EDITAR) {
                    val planetParaEditar = viewModel.selectedPlanet ?: Planet()

                    PlanetEditScreen(
                        initialPlanet = planetParaEditar,
                        onSave = { editado ->
                            viewModel.savePlanet(editado)
                            navController.popBackStack()
                        }
                    )
                }

                composable(Routes.ABOUT) { AboutUsScreen() }
            }
        }
    }
}
