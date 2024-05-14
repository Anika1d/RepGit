package mir.anika1d.core.resources

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class AppResources {
    enum class Drawable(@DrawableRes val id: Int) {
        SEARCH(R.drawable.search),
        BRANCHES(R.drawable.braches),
        FORKS(R.drawable.forks),
        IMAGE_FAIL(R.drawable.image_fail),
        DEFAULT_AVATAR(R.drawable.avatar_default),
        ISSUES(R.drawable.issues),
        NO_ETHERNET(R.drawable.no_ethernet),
        STAR(R.drawable.star),
        WATCH(R.drawable.watch),
        GITHUB_ICON(R.drawable.github_icon),
        DOWNLOAD(R.drawable.download_arrow),
        CHEST(R.drawable.chest),
        REPOSITORY(R.drawable.repository),
        FOLLOWERS(R.drawable.followers),
        PROFILE(R.drawable.profile)
    }

    sealed class Values {
        enum class Strings(@StringRes val id: Int) {
            APP_NAME(R.string.app_name),
            DOWNLOAD_REPOSITORIES(R.string.download_repositories),
            CHECK_CONNECTION(R.string.check_connection),
            DOWNLOAD_START(R.string.download_started),
            DOWNLOAD_END(R.string.download_ended),
            RESULT(R.string.result),
            ISSUES(R.string.issues),
            ISSUES_NOT_FOUND(R.string.issues_not_found),
            SIGN_IN_WRONG(R.string.sign_in_wrong),
            PLEASE_WAIT(R.string.pls_wait),
            TRY_AGAIN(R.string.wrong_try_again),
            FOLLOWERS(R.string.followers),
            PLEASE_AUTH(R.string.pls_auth),
            SIGN_IN(R.string.sign_in)

        }
    }
}
