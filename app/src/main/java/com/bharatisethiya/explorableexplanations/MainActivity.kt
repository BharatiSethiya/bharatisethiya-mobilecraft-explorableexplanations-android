package com.bharatisethiya.explorableexplanations

import android.os.Bundle
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FactCheck
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    Scaffold(
        bottomBar = {
            if (isLandscape) {
                CompactLandscapeNavigation(
                    currentRoute = current?.route,
                    onNavigate = navController::navigateToTopLevel,
                )
            } else {
                NavigationBar {
                    destinations.forEach { destination ->
                        NavigationBarItem(
                            selected = current?.hierarchy?.any { it.route == destination.route } == true,
                            onClick = { navController.navigateToTopLevel(destination.route) },
                            icon = { Icon(destination.icon, contentDescription = destination.label) },
                            label = { Text(destination.label) },
                        )
                    }
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

@Composable
private fun CompactLandscapeNavigation(currentRoute: String?, onNavigate: (String) -> Unit) {
    Surface(tonalElevation = 3.dp) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(64.dp)
                .navigationBarsPadding()
                .selectableGroup(),
        ) {
            destinations.forEach { destination ->
                val selected = currentRoute == destination.route
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .selectable(
                            selected = selected,
                            role = Role.Tab,
                            onClick = { onNavigate(destination.route) },
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        modifier = Modifier
                            .offset(y = 6.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(destination.icon, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(destination.label)
                    }
                }
            }
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
