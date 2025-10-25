package com.zetta.gochickfarm.ui.screen

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zetta.gochickfarm.ui.screen.auth.AuthScreen
import com.zetta.gochickfarm.ui.screen.home.HomeScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val navBackStackEntry = navController.currentBackStackEntry
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Auth.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(
                route = Screen.Auth.route,
                exitTransition = {
                    slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { -it })
                }
            ) { AuthScreen(
                onSignIn = { navController.navigate(
                    route = Screen.Home.route,
                    builder = {
                        popUpTo(Screen.Auth.route) {
                            inclusive = true
                        }
                    }
                ) }
            ) }
            composable(
                route = Screen.Home.route,
                enterTransition = {
                    slideInHorizontally(animationSpec = tween(300), initialOffsetX = { it })
                },
                exitTransition = {
                    fadeOut(tween(300), targetAlpha = 0f)
                }
            ) { HomeScreen(

            ) }
        }
    }
}