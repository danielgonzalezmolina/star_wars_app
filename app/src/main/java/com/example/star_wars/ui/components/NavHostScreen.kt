package com.example.star_wars.ui.home

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
// IMPORTANTE: Usar el modelo de la base de datos
import com.example.star_wars.data.model.Planet
import com.example.star_wars.ui.screen.AboutUsScreen
import com.example.star_wars.ui.screen.PlanetEditScreen
import com.example.star_wars.ui.screen.PlanetListScreen
import com.example.star_wars.ui.screen.PlanetViewModel

object Routes {
    const val LISTAR = "listPlanet"
    const val CREAR = "createPlanet"
    const val EDITAR = "editPlanet"
    const val ABOUT = "aboutPlanet"
}

@Composable
fun NavHostScreen(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: PlanetViewModel = hiltViewModel()
) {
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