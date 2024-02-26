package com.mir.repgit.screens.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mir.repgit.screens.main.MainSearchScreen
import com.mir.repgit.screens.repository.RepositoryScreen

object NavGraphRoutes {
    fun NavGraphBuilder.mainSearchScreen(){
        composable(Route.MAIN_SEARCH_SCREEN.path){
            MainSearchScreen()
        }
    }
    fun NavGraphBuilder.repositoryScreen(){
        composable(
            Route.REPOSITORY_SCREEN.path + "{owner}/{repo}",
            arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("repo") { type = NavType.StringType }
            )
        ){
                bundle->
            RepositoryScreen(
                owner = bundle.arguments?.getString("owner")!!,
                repo = bundle.arguments?.getString("repo")!!
            )
        }
    }
}