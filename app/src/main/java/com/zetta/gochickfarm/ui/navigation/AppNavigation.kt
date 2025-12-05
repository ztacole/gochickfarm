package com.zetta.gochickfarm.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.zetta.gochickfarm.ui.screen.activity.ActivityHistoryScreen
import com.zetta.gochickfarm.ui.screen.activity.OfflineSyncScreen
import com.zetta.gochickfarm.ui.screen.activity.ProfileScreen
import com.zetta.gochickfarm.ui.screen.animal.add.AddAnimalScreen
import com.zetta.gochickfarm.ui.screen.animal.detail.AnimalDetailScreen
import com.zetta.gochickfarm.ui.screen.animal.detail.AnimalDetailViewModel
import com.zetta.gochickfarm.ui.screen.animal.list.AnimalListScreen
import com.zetta.gochickfarm.ui.screen.animal.list.AnimalListViewModel
import com.zetta.gochickfarm.ui.screen.animal.update.UpdateAnimalStatusScreen
import com.zetta.gochickfarm.ui.screen.auth.Login
import com.zetta.gochickfarm.ui.screen.dashboard.Dashboard
import com.zetta.gochickfarm.ui.screen.feed.add.AddFeedScreen
import com.zetta.gochickfarm.ui.screen.feed.list.FeedListScreen
import com.zetta.gochickfarm.ui.screen.feeding.AddBreedingLogScreen
import com.zetta.gochickfarm.ui.screen.feeding.AddFeedingLogScreen
import com.zetta.gochickfarm.ui.screen.transaction.add.AddTransactionDetailScreen
import com.zetta.gochickfarm.ui.screen.transaction.add.AddTransactionScreen
import com.zetta.gochickfarm.ui.screen.transaction.detail.TransactionDetailScreen
import com.zetta.gochickfarm.ui.screen.transaction.list.TransactionListScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: Screen,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Define screens that should show bottom navigation
    val bottomNavScreens = listOf(
        Screen.Dashboard.route,
        Screen.AnimalList.route,
        Screen.FeedList.route,
        Screen.TransactionList.route
    )

    val fabScreens = listOf(
        FabItem(
            route = Screen.AnimalList.route,
            label = "Add Animal",
            onClick = { navController.navigate(Screen.AddAnimal.route) }
        ),
        FabItem(
            route = Screen.FeedList.route,
            label = "Add Feed",
            onClick = { navController.navigate(Screen.AddFeed.route) }
        ),
        FabItem(
            route = Screen.TransactionList.route,
            label = "Add Transaction",
            onClick = { navController.navigate(Screen.AddTransaction.route) }
        )
    )

    val showBottomBar = currentDestination?.route in bottomNavScreens
    val showFab = currentDestination?.route in fabScreens.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    onNavigate = {
                        navController.navigate(it) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    currentDestination = currentDestination,
                )
            }
        },
        floatingActionButton = {
            if (showFab) {
                val fabItem = fabScreens.find { it.route == currentDestination?.route }
                FloatingActionButton(
                    onClick = fabItem?.onClick ?: {}
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = fabItem?.label)
                }
            }
        }
    ) { innerPadding ->
        val modifierPadding = modifier.padding(PaddingValues(start = innerPadding.calculateLeftPadding(LayoutDirection.Ltr), end = innerPadding.calculateRightPadding(LayoutDirection.Ltr), top = 0.dp, bottom = innerPadding.calculateBottomPadding()))
        NavHost(
            navController = navController,
            startDestination = startDestination.route,
            modifier = modifierPadding,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(200))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) + fadeOut(animationSpec = tween(200))
            }
        ) {
            // Login Screen
            composable(Screen.Login.route) {
                Login(
                    onNavigateToDashboard = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // Dashboard Screen
            composable(
                Screen.Dashboard.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                Dashboard(
                    onNavigateToAddFeeding = {
                        navController.navigate(Screen.AddFeedingLog.route)
                    },
                    onNavigateToBreeding = {
                        navController.navigate(Screen.AddBreedingLog.route)
                    },
                    onNavigateToTransaction = {
                        navController.navigate(Screen.AddTransaction.route)
                    },
                    onNavigateToGoatSearch = { species ->
//                        navController.currentBackStackEntry?.savedStateHandle?.set("category", category)
                        navController.navigate(Screen.AnimalList.withSpecies(species)) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToChickenSearch = { species ->
                        navController.navigate(Screen.AnimalList.withSpecies(species)) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Animal List Screen
            composable(
                Screen.AnimalList.route,
                arguments = listOf(
                    navArgument("species") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                ),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                val viewModel: AnimalListViewModel = koinViewModel()
                AnimalListScreen(
                    onNavigateToDetail = { animalId ->
                        navController.navigate(Screen.AnimalDetail.createRoute(animalId))
                    },
                    viewModel = viewModel
                )
            }

            // Animal Detail Screen
            composable(
                route = Screen.AnimalDetail.route,
                arguments = listOf(
                    navArgument("animalId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val viewModel: AnimalDetailViewModel = koinViewModel()
                AnimalDetailScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onNavigateToAddFeeding = { animalId ->
                        navController.navigate(Screen.AddFeedingLog.createRoute(animalId))
                    },
                    viewModel = viewModel
                )
            }

            // Add Animal Screen
            composable(Screen.AddAnimal.route) {
                AddAnimalScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onSaveSuccess = { navController.navigateUp() }
                )
            }

            // Update Animal Status Screen
            composable(
                route = Screen.UpdateAnimalStatus.route,
                arguments = listOf(
                    navArgument("animalId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString("animalId") ?: ""
                UpdateAnimalStatusScreen(
                    animalId = animalId,
                    onNavigateBack = { navController.navigateUp() },
                    onSaveSuccess = { navController.navigateUp() }
                )
            }

            // Add Feeding Log Screen
            composable(
                route = Screen.AddFeedingLog.route,
                arguments = listOf(
                    navArgument("animalId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString("animalId")
                AddFeedingLogScreen(
                    preSelectedAnimalId = animalId,
                    onNavigateBack = { navController.navigateUp() },
                    onSaveSuccess = { navController.navigateUp() }
                )
            }

            // Add Breeding Log Screen
            composable(Screen.AddBreedingLog.route) {
                AddBreedingLogScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onSaveSuccess = { navController.navigateUp() }
                )
            }

            // Feed List Screen
            composable(
                Screen.FeedList.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                FeedListScreen()
            }

            // Add Feed Screen
            composable(Screen.AddFeed.route) {
                AddFeedScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onSaveSuccess = { navController.navigateUp() }
                )
            }

            // Transaction List Screen
            composable(
                Screen.TransactionList.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                TransactionListScreen(
                    onNavigateToDetail = { transactionId ->
                        navController.navigate(Screen.TransactionDetail.createRoute(transactionId))
                    }
                )
            }

            // Add Transaction Screen
            composable(Screen.AddTransaction.route) {
                AddTransactionScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onSaveSuccess = { navController.navigateUp() }
                )
            }

            // Transaction Detail Screen
            composable(
                route = Screen.TransactionDetail.route,
                arguments = listOf(
                    navArgument("transactionId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getString("transactionId") ?: ""
                TransactionDetailScreen(
                    transactionId = transactionId,
                    onNavigateBack = { navController.navigateUp() },
                    onNavigateToAddDetail = {
                        navController.navigate(Screen.AddTransactionDetail.createRoute(transactionId))
                    }
                )
            }

            // Add Transaction Detail Screen
            composable(
                route = Screen.AddTransactionDetail.route,
                arguments = listOf(
                    navArgument("transactionId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getString("transactionId") ?: ""
                AddTransactionDetailScreen(
                    transactionId = transactionId,
                    onNavigateBack = { navController.navigateUp() },
                    onSaveSuccess = { navController.navigateUp() }
                )
            }

            // Activity History Screen
            composable(Screen.ActivityHistory.route) {
                ActivityHistoryScreen(
                    onNavigateBack = { navController.navigateUp() }
                )
            }

            // Profile Screen
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToSync = {
                        navController.navigate(Screen.OfflineSync.route)
                    },
                    onNavigateToActivityHistory = {
                        navController.navigate(Screen.ActivityHistory.route)
                    },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // Offline Sync Screen
            composable(Screen.OfflineSync.route) {
                OfflineSyncScreen(
                    onNavigateBack = { navController.navigateUp() }
                )
            }
        }
    }
}

data class FabItem(
    val route: String,
    val label: String,
    val onClick: () -> Unit
)