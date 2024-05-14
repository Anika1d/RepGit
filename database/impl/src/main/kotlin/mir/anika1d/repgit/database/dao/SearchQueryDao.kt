package mir.anika1d.repgit.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import mir.anika1d.repgit.database.data.response.SearchQueryResponse

@Dao
interface SearchQueryDao {
    @Query("SELECT * FROM search_queries WHERE name LIKE :name || '%' ORDER BY LENGTH(name) ,name ASC, created_at ASC LIMIT :limit")
    fun getSearchQueriesByName(name: String, limit: Int): Flow<List<SearchQueryResponse>>

    @Query("SELECT * FROM search_queries ORDER BY name ASC")
    fun getAllSortedByName(): Flow<List<SearchQueryResponse>>

    @Query("SELECT * FROM search_queries ORDER BY created_at ASC")
    fun getAllSortedByCreatedAt(): Flow<List<SearchQueryResponse>>

    @Query("SELECT * FROM search_queries WHERE name LIKE :name")
    fun getSearchQueriesByName(name: String): Flow<List<SearchQueryResponse>>

    @Query("INSERT OR IGNORE INTO  search_queries (name) VALUES(:name)")
    suspend fun insert(name: String)


    @Query("DELETE FROM search_queries WHERE name = :name")
    suspend fun deleteByName(name: String)
}