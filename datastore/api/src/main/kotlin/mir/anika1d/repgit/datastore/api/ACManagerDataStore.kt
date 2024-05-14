package mir.anika1d.repgit.datastore.api

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import mir.anika1d.repgit.core.data.model.repository.Owner

abstract class ACManagerDataStore(private val context: Context) {
    open val dataStore: DataStore<Preferences>
        get() = context.dataStore
    abstract suspend fun getJwtToken(): String?
    abstract suspend fun saveJwtToken(token: String?)

    abstract suspend fun getRefreshToken(): String?
    abstract suspend fun saveRefreshToken(token: String?)

    abstract suspend fun getIdToken(): String?
    abstract suspend fun saveIdToken(id: String?)
    abstract suspend fun clear()
    abstract suspend fun getOwner(): Owner?
    abstract suspend fun saveOwner(owner: Owner?)

}

internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "rep_git")
