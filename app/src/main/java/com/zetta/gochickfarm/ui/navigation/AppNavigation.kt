package com.zetta.gochickfarm.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zetta.gochickfarm.ui.screen.activity.ActivityHistoryScreen
import com.zetta.gochickfarm.ui.screen.activity.OfflineSyncScreen
import com.zetta.gochickfarm.ui.screen.activity.ProfileScreen
import com.zetta.gochickfarm.ui.screen.animal.AddAnimalScreen
import com.zetta.gochickfarm.ui.screen.animal.AnimalDetailScreen
import com.zetta.gochickfarm.ui.screen.animal.AnimalListScreen
import com.zetta.gochickfarm.ui.screen.animal.UpdateAnimalStatusScreen
import com.zetta.gochickfarm.ui.screen.auth.LoginScreen
import com.zetta.gochickfarm.ui.screen.dashboard.DashboardScreen
import com.zetta.gochickfarm.ui.screen.feed.AddFeedScreen
import com.zetta.gochickfarm.ui.screen.feed.FeedListScreen
import com.zetta.gochickfarm.ui.screen.feeding.AddBreedingLogScreen
import com.zetta.gochickfarm.ui.screen.feeding.AddFeedingLogScreen
import com.zetta.gochickfarm.ui.screen.transaction.AddTransactionDetailScreen
import com.zetta.gochickfarm.ui.screen.transaction.AddTransactionScreen
import com.zetta.gochickfarm.ui.screen.transaction.TransactionDetailScreen
import com.zetta.gochickfarm.ui.screen.transaction.TransactionListScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Define screens that should show bottom navigation
    val bottomNavScreens = listOf(
        Screen.Dashboard.route,
        Screen.AnimalList.route,
        Screen.FeedList.route,
        Screen.TransactionList.route,
        Screen.Profile.route
    )

    val showBottomBar = currentDestination?.route in bottomNavScreens

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = modifier.padding(paddingValues)
        ) {
            // Login Screen
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // Dashboard Screen
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onNavigateToAddFeeding = {
                        navController.navigate(Screen.AddFeedingLog.createRoute())
                    },
                    onNavigateToBreeding = {
                        navController.navigate(Screen.AddBreedingLog.route)
                    },
                    onNavigateToTransaction = {
                        navController.navigate(Screen.AddTransaction.route)
                    }
                )
            }

            // Animal List Screen
            composable(Screen.AnimalList.route) {
                AnimalListScreen(
                    onNavigateToDetail = { animalId ->
                        navController.navigate(Screen.AnimalDetail.createRoute(animalId))
                    },
                    onNavigateToAddAnimal = {
                        navController.navigate(Screen.AddAnimal.route)
                    }
                )
            }

            // Animal Detail Screen
            composable(
                route = Screen.AnimalDetail.route,
                arguments = listOf(
                    navArgument("animalId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString("animalId") ?: ""
                AnimalDetailScreen(
                    animalId = animalId,
                    onNavigateBack = { navController.navigateUp() },
                    onNavigateToAddFeeding = {
                        navController.navigate(Screen.AddFeedingLog.createRoute(animalId))
                    },
                    onNavigateToUpdateStatus = {
                        navController.navigate(Screen.UpdateAnimalStatus.createRoute(animalId))
                    }
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
            composable(Screen.FeedList.route) {
                FeedListScreen(
                    onNavigateToAddFeed = {
                        navController.navigate(Screen.AddFeed.route)
                    }
                )
            }

            // Add Feed Screen
            composable(Screen.AddFeed.route) {
                AddFeedScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onSaveSuccess = { navController.navigateUp() }
                )
            }

            // Transaction List Screen
            composable(Screen.TransactionList.route) {
                TransactionListScreen(
                    onNavigateToDetail = { transactionId ->
                        navController.navigate(Screen.TransactionDetail.createRoute(transactionId))
                    },
                    onNavigateToAddTransaction = {
                        navController.navigate(Screen.AddTransaction.route)
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