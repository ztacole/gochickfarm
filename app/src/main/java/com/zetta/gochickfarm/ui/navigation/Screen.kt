package com.zetta.gochickfarm.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object AnimalList : Screen("animal_list")
    object AnimalDetail : Screen("animal_detail/{animalId}") {
        fun createRoute(animalId: String) = "animal_detail/$animalId"
    }
    object AddAnimal : Screen("add_animal")
    object UpdateAnimalStatus : Screen("update_status/{animalId}") {
        fun createRoute(animalId: String) = "update_status/$animalId"
    }
    object AddFeedingLog : Screen("add_feeding_log/{animalId}") {
        fun createRoute(animalId: String = "") = if (animalId.isEmpty()) "add_feeding_log/" else "add_feeding_log/$animalId"
    }
    object AddBreedingLog : Screen("add_breeding_log")
    object FeedList : Screen("feed_list")
    object AddFeed : Screen("add_feed")
    object TransactionList : Screen("transaction_list")
    object AddTransaction : Screen("add_transaction")
    object TransactionDetail : Screen("transaction_detail/{transactionId}") {
        fun createRoute(transactionId: String) = "transaction_detail/$transactionId"
    }
    object AddTransactionDetail : Screen("add_transaction_detail/{transactionId}") {
        fun createRoute(transactionId: String) = "add_transaction_detail/$transactionId"
    }
    object ActivityHistory : Screen("activity_history")
    object Profile : Screen("profile")
    object OfflineSync : Screen("offline_sync")
}