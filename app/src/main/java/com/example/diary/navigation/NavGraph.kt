package com.example.diary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetUpNavGraph(
    startDestination: String,
    navController: NavHostController
){
    NavHost(
    startDestination =startDestination,
        navController =navController
    ){
        authenticationRoute()
        homeRoute()
        writeRoute()
    }
}
fun NavGraphBuilder.authenticationRoute(){
    composable(route= Screen.Authentication.route){

    }
}
fun NavGraphBuilder.homeRoute(){
    composable(route= Screen.Home.route){

    }
}
fun NavGraphBuilder.writeRoute(){
    composable(route= Screen.Write.route){

    }
}