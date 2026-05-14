package com.example.star_wars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.star_wars.ui.home.NavHostScreen
import com.example.star_wars.ui.home.Routes
import com.example.star_wars.ui.theme.StarWarsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            StarWarsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(), topBar = {
                        TopAppBar(title = { Text("Star Wars") }, actions = {
                            IconButton(onClick = {
                                navController.navigate(Routes.CREAR)
                            }) {
                                Icon(
                                    Icons.Default.Add, contentDescription = "Acción Añadir"
                                )
                            }

                            IconButton(onClick = {
                                navController.navigate(Routes.LISTAR)
                            }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.List,
                                    contentDescription = "Listar"
                                )
                            }

                            OverflowMenu(
                                { navController.navigate(Routes.ABOUT) }


                            )
                        })
                    },
                    floatingActionButton = {
                        if (currentRoute == Routes.LISTAR) {
                            FloatingActionButton(
                                onClick = {
                                    navController.navigate(Routes.CREAR)
                                },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Añadir Planeta")
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.End
                ) { innerPadding ->
                    NavHostScreen(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding,)
                    )
                }
            }
        }
    }
}

@Composable
fun OverflowMenu(
    onAboutUsClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { expanded = true }) {
            Icon(
                Icons.Default.MoreVert, contentDescription = "Más opciones"
            )
        }

        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("About us") }, onClick = {
                expanded = false
                onAboutUsClick()
            })

        }
    }
}