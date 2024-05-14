package mir.anika1d.repgit.datastore.impl

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import mir.anika1d.repgit.core.data.model.repository.Owner
import mir.anika1d.repgit.datastore.api.ACManagerDataStore

class ManagerDataStore(context: Context) : ACManagerDataStore(context) {
    companion object {
        val JWT_TOKEN = stringPreferencesKey("jwt_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val ID_TOKEN = stringPreferencesKey("id_token")
        val OWNER_NAME = stringPreferencesKey("owner_name")
        val OWNER_AVATAR = stringPreferencesKey("owner_avatar")

    }

    private val ownerName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[OWNER_NAME]

    }.distinctUntilChanged()

    private val ownerAvatar: Flow<String?> = dataStore.data.map { preferences ->
        preferences[OWNER_AVATAR]
    }.distinctUntilChanged()
   private  val jwtToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[JWT_TOKEN]
    }.distinctUntilChanged()

      private  val refreshToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN]
    }

  private   val idToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[ID_TOKEN]
    }

    override suspend fun getJwtToken(): String? {
        val tmp = jwtToken.first()
        return if (tmp.isNullOrEmpty())
            null
        else tmp
    }

    override suspend fun saveIdToken(id: String?) {
        dataStore.edit { preferences ->
            preferences[ID_TOKEN] = id.orEmpty()
        }
    }


    override suspend fun saveJwtToken(token: String?) {
        dataStore.edit { preferences ->
            preferences[JWT_TOKEN] = token.orEmpty()
        }
    }

    override suspend fun getRefreshToken(): String? {
        val tmp = refreshToken.first()
        return if (tmp.isNullOrEmpty())
            null
        else tmp
    }

    override suspend fun saveRefreshToken(token: String?) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = token.orEmpty()
        }
    }

    override suspend fun getIdToken(): String? {
        val tmp = idToken.first()
        return if (tmp.isNullOrEmpty())
            null
        else tmp
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override suspend fun getOwner(): Owner? {
        val n = ownerName.first()
        val a = ownerAvatar.first()
        return if (n != null && a != null)
            Owner(name = n, avatarUrl = a)
        else null
    }

    override suspend fun saveOwner(owner: Owner?) {
        dataStore.edit { preferences ->
            preferences[OWNER_NAME] = owner?.name.orEmpty()
            preferences[OWNER_AVATAR] = owner?.avatarUrl.orEmpty()
        }
    }

}