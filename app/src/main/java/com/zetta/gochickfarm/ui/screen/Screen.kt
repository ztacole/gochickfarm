package com.zetta.gochickfarm.ui.screen

sealed class Screen(val route: String) {
    object Auth: Screen("auth")
    object Home: Screen("home")
}