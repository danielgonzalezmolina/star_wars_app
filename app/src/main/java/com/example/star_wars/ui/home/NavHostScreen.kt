package com.example.star_wars.ui.home

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.star_wars.data.model.Planet
import com.example.star_wars.ui.screen.AboutUsScreen
import com.example.star_wars.ui.screen.FilmEditScreen
import com.example.star_wars.ui.screen.FilmEditViewModel
import com.example.star_wars.ui.screen.FilmListScreen
import com.example.star_wars.ui.screen.FilmListViewModel
import com.example.star_wars.ui.screen.PlanetEditScreen
import com.example.star_wars.ui.screen.PlanetEditViewModel
import com.example.star_wars.ui.screen.PlanetListScreen
import com.example.star_wars.ui.screen.PlanetListViewModel

object Routes {
    const val LISTAR = "listPlanet"
    const val CREAR = "createPlanet"
    const val EDITAR = "editPlanet/{planetId}"
    const val PELICULAS = "listFilms"
    const val CREAR_PELICULA = "createFilm"
    const val EDITAR_PELICULA = "editFilm/{filmId}"
    const val ABOUT = "aboutPlanet"

    fun editPlanet(id: Int) = "editPlanet/$id"
    fun editFilm(id: Int) = "editFilm/$id"
}

@Composable
fun NavHostScreen(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LISTAR,
        modifier = modifier,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)) }
    ) {
        composable(Routes.LISTAR) {
            val viewModel: PlanetListViewModel = hiltViewModel()
            PlanetListScreen(
                planetList = viewModel.planetList,
                onPlanetClick = { planet ->
                    navController.navigate(Routes.editPlanet(planet.id))
                },
                onDeletePlanet = { planet ->
                    viewModel.deletePlanet(planet)
                }
            )
        }

        composable(Routes.CREAR) {
            val viewModel: PlanetEditViewModel = hiltViewModel()
            
            LaunchedEffect(Unit) {
                viewModel.loadPlanet(0)
            }

            EditScreenContent(viewModel, navController)
        }

        composable(
            route = Routes.EDITAR,
            arguments = listOf(navArgument("planetId") { type = NavType.IntType })
        ) { backStackEntry ->
            val planetId = backStackEntry.arguments?.getInt("planetId") ?: 0
            val viewModel: PlanetEditViewModel = hiltViewModel()

            LaunchedEffect(planetId) {
                viewModel.loadPlanet(planetId)
            }

            EditScreenContent(viewModel, navController)
        }

        composable(Routes.PELICULAS) {
            val viewModel: FilmListViewModel = hiltViewModel()
            FilmListScreen(
                filmList = viewModel.filmList,
                onFilmClick = { filmWithPlanets ->
                    navController.navigate(Routes.editFilm(filmWithPlanets.film.episode_id))
                }
            )
        }

        composable(Routes.CREAR_PELICULA) {
            val viewModel: FilmEditViewModel = hiltViewModel()
            LaunchedEffect(Unit) { viewModel.loadFilm(0) }
            FilmEditScreenContent(viewModel, navController)
        }

        composable(
            route = Routes.EDITAR_PELICULA,
            arguments = listOf(navArgument("filmId") { type = NavType.IntType })
        ) { backStackEntry ->
            val filmId = backStackEntry.arguments?.getInt("filmId") ?: 0
            val viewModel: FilmEditViewModel = hiltViewModel()
            LaunchedEffect(filmId) { viewModel.loadFilm(filmId) }
            FilmEditScreenContent(viewModel, navController)
        }

        composable(Routes.ABOUT) { AboutUsScreen() }
    }
}

@Composable
private fun FilmEditScreenContent(
    viewModel: FilmEditViewModel,
    navController: NavHostController
) {
    val selectedFilm = viewModel.selectedFilm
    val isNew = selectedFilm?.episode_id == 0

    if (viewModel.showDuplicateError) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissError() },
            title = { Text("Error") },
            text = { Text("La película '${viewModel.duplicateTitle}' o su ID ya existen.") },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissError() }) {
                    Text("Aceptar")
                }
            }
        )
    }

    if (selectedFilm != null) {
        FilmEditScreen(
            initialFilm = selectedFilm,
            allPlanets = viewModel.allPlanets,
            selectedPlanetIds = viewModel.selectedPlanetIds,
            onTogglePlanet = { viewModel.togglePlanetSelection(it) },
            onSave = { film ->
                viewModel.saveFilm(film, isNew) { success ->
                    if (success) navController.popBackStack()
                }
            }
        )
    }
}

@Composable
private fun EditScreenContent(
    viewModel: PlanetEditViewModel,
    navController: NavHostController
) {
    val selectedPlanet = viewModel.selectedPlanet

    if (viewModel.showDuplicateError) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissError() },
            title = { Text("Error de Integridad") },
            text = { Text("El planeta '${viewModel.duplicateName}' ya existe en la base de datos. Por favor, utilice un ID diferente.") },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissError() }) {
                    Text("Aceptar")
                }
            }
        )
    }

    if (selectedPlanet != null) {
        PlanetEditScreen(
            initialPlanet = selectedPlanet,
            onSave = { planet ->
                viewModel.savePlanet(planet) { success ->
                    if (success) navController.popBackStack()
                }
            }
        )
    }
}
