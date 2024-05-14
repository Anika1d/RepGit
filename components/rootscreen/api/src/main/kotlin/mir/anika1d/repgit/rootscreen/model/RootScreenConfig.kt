package mir.anika1d.repgit.rootscreen.model


import kotlinx.serialization.Serializable
import mir.anika1d.repgit.profilescreen.api.model.ProfileConfig
import mir.anika1d.repgit.repodetailscreen.api.model.DetailsRepoConfig

@Serializable
sealed class RootScreenConfig {
    @Serializable
    data class DetailsRepoScreen(val detailsRepoConfig: DetailsRepoConfig) : RootScreenConfig()

    @Serializable
    data object AuthScreen : RootScreenConfig()

    @Serializable
    data class ProfileScreen(val profileConfig: ProfileConfig) : RootScreenConfig()

    @Serializable
    data object SearchScreen : RootScreenConfig()

    @Serializable
    data object DownloadScreen : RootScreenConfig()
}
