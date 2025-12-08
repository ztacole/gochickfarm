package com.zetta.gochickfarm.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object AnimalList : Screen("animal_list?species={species}") {
        fun withSpecies(species: String) = "animal_list?species=$species"
    }
    object AnimalDetail : Screen("animal_detail/{animalId}") {
        fun createRoute(animalId: Int) = "animal_detail/$animalId"
    }
    object AddFeedingLog : Screen("add_feeding_log/{animalId}/{animalTag}") {
        fun createRoute(animalId: String, animalTag: String) = "add_feeding_log/$animalId/$animalTag"
    }
    object AddBreedingLog : Screen("add_breeding_log")
    object FeedList : Screen("feed_list")
    object AddFeed : Screen("add_feed")
    object TransactionList : Screen("transaction_list")
    object AddTransaction : Screen("add_transaction")
    object TransactionDetail : Screen("transaction_detail/{transactionId}") {
        fun createRoute(transactionId: Int) = "transaction_detail/$transactionId"
    }
    object ActivityHistory : Screen("activity_history")
    object Profile : Screen("profile")
    object OfflineSync : Screen("offline_sync")
}