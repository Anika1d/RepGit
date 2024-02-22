package com.mir.repgit.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mir.repgit.screens.navigation.NavGraphRoutes.mainSearchScreen
import com.mir.repgit.screens.navigation.NavGraphRoutes.repositoryScreen
import com.mir.repgit.values.LocalNavController

@Composable
fun MainNavigation(
    navController: NavHostController = rememberNavController(),
) {
    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        
    NavHost(navController = navController, startDestination = Route.MAIN_SEARCH_SCREEN.path){
        mainSearchScreen()
        repositoryScreen()
    }
    }
}
