package com.bharatisethiya.explorableexplanations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FactCheck
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bharatisethiya.explorableexplanations.ui.screens.ContextScreen
import com.bharatisethiya.explorableexplanations.ui.screens.FilterScreen
import com.bharatisethiya.explorableexplanations.ui.screens.HomeScreen
import com.bharatisethiya.explorableexplanations.ui.screens.ScenarioScreen
import com.bharatisethiya.explorableexplanations.ui.theme.ExplorableTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ExplorableTheme { ExplorableApp() } }
    }
}

private data class Destination(val route: String, val label: String, val icon: ImageVector)

private val destinations = listOf(
    Destination("read", "Read", Icons.Outlined.Home),
    Destination("scenario", "Scenario", Icons.Outlined.Tune),
    Destination("filter", "Filter", Icons.Outlined.AutoGraph),
    Destination("context", "Context", Icons.AutoMirrored.Outlined.FactCheck),
)

@Composable
private fun ExplorableApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val current = backStackEntry?.destination
    Scaffold(
        bottomBar = {
            NavigationBar {
                destinations.forEach { destination ->
                    NavigationBarItem(
                        selected = current?.hierarchy?.any { it.route == destination.route } == true,
                        onClick = {
                            navController.navigateToTopLevel(destination.route)
                        },
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { padding ->
        NavHost(navController, startDestination = "read") {
            composable("read") { HomeScreen(padding, onOpen = navController::navigateToTopLevel) }
            composable("scenario") { ScenarioScreen(padding) }
            composable("filter") { FilterScreen(padding) }
            composable("context") { ContextScreen(padding) }
        }
    }
}

private fun NavHostController.navigateToTopLevel(route: String) {
    if (currentDestination?.route == route) return

    if (route == "read") {
        popBackStack("read", inclusive = false)
    } else {
        navigate(route) {
            popUpTo("read")
            launchSingleTop = true
        }
    }
}
