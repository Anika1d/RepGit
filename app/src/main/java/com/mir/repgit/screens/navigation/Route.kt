package com.mir.repgit.screens.navigation

sealed class Route(val path:String) {
    data object MAIN_SEARCH_SCREEN: Route("main_search_screen/")
    data object REPOSITORY_SCREEN: Route("repository_screen/")
}