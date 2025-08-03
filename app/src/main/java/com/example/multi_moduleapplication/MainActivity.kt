package com.example.multi_moduleapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.multi_moduleapplication.screens.CharacterDetailsScreen
import com.example.multi_moduleapplication.screens.CharacterEpisodesScreen
import com.example.multi_moduleapplication.screens.HomeScreen
import com.example.multi_moduleapplication.ui.theme.MultimoduleApplicationTheme
import com.example.multi_moduleapplication.ui.theme.RickAction
import com.example.multi_moduleapplication.ui.theme.RickPrimary
import com.example.network.KtorClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


sealed class NavDestinations(val title: String, val route: String, val icon: ImageVector) {
    object Home : NavDestinations(title = "Home", route = "home_screen", icon = Icons.Filled.Home)
    object Episodes : NavDestinations(
        title = "Episodes",
        route = "episodes_screen",
        icon = Icons.Filled.PlayArrow
    )
    object Search :
        NavDestinations(title = "Search", route = "search_screen", icon = Icons.Filled.Search)
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var ktorClient: KtorClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val navBarItem = listOf(
                NavDestinations.Home, NavDestinations.Episodes, NavDestinations.Search
            )
            var selectedIndex by remember { mutableIntStateOf(0) }

            MultimoduleApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            windowInsets = NavigationBarDefaults.windowInsets,
                            containerColor = RickPrimary
                        ) {
                            navBarItem.forEachIndexed { index, screen ->
                                NavigationBarItem(
                                    selected = selectedIndex == index,
                                    onClick = {
                                        selectedIndex = index
                                        navController.navigate(screen.route) {
                                            // Pop up to the start destination of the graph to
                                            // avoid building up a large stack of destinations
                                            // on the back stack as users select items
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            // Avoid multiple copies of the same destination when
                                            // reselecting the same item
                                            launchSingleTop = true
                                            // Restore state when reselecting a previously selected item
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(imageVector = screen.icon, contentDescription = null)
                                    },

                                    label = { Text(screen.title) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = RickAction,
                                        selectedTextColor = RickAction,
                                        indicatorColor = Color.Transparent

                                    )
                                )

                            }

                        }
                    },
                    ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        ktorClient = ktorClient,
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    ktorClient: KtorClient,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = "home_screen",
        modifier = Modifier
            .background(color = RickPrimary)
            .padding(innerPadding)
    ) {

        composable("home_screen") {
            HomeScreen(characterSelected = { characterId ->
                navController.navigate("character_details/$characterId")
            })
        }

        composable(
            route = "character_details/{characterId}",
            arguments = listOf(navArgument("characterId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val characterId =
                backStackEntry.arguments?.getInt("characterId") ?: -1
            CharacterDetailsScreen(
                characterId = characterId,
                onEpisodeClicked = { navController.navigate("character_episodes/$it") }
            ) {
                navController.navigateUp()
            }
        }
        composable(
            route = "character_episodes/{characterId}",
            arguments = listOf(navArgument("characterId") {
                type =
                    NavType.IntType
            })
        ) { backStackEntry ->
            val characterId =
                backStackEntry.arguments?.getInt("characterId") ?: -1
            CharacterEpisodesScreen(
                characterID = characterId,
                ktorClient = ktorClient,
                onBackClicked = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = NavDestinations.Episodes.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Episodes", fontSize = 62.sp, color = RickAction)
            }
        }

        composable(route = NavDestinations.Search.route) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Search", fontSize = 62.sp, color = RickAction)
            }
        }
    }

}

