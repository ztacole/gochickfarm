package com.zetta.gochickfarm.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Grass
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Dashboard : BottomNavItem(
        route = Screen.Dashboard.route,
        icon = Icons.Rounded.Home,
        label = "Dashboard"
    )
    object Animals : BottomNavItem(
        route = Screen.AnimalList.route,
        icon = Icons.Rounded.Pets,
        label = "Hewan"
    )
    object Feeds : BottomNavItem(
        route = Screen.FeedList.route,
        icon = Icons.Rounded.Grass,
        label = "Pakan"
    )
    object Transactions : BottomNavItem(
        route = Screen.TransactionList.route,
        icon = Icons.Rounded.MonetizationOn,
        label = "Transaksi"
    )
    object Profile : BottomNavItem(
        route = Screen.Profile.route,
        icon = Icons.Rounded.Person,
        label = "Profil"
    )
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentDestination: androidx.navigation.NavDestination?
) {
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Animals,
        BottomNavItem.Feeds,
        BottomNavItem.Transactions,
        BottomNavItem.Profile
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination to avoid building up a large stack
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}