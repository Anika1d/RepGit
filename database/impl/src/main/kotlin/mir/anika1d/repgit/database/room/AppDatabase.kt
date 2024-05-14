package mir.anika1d.repgit.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import mir.anika1d.repgit.database.dao.SearchQueryDao
import mir.anika1d.repgit.database.data.response.SearchQueryResponse

@Database(entities = [SearchQueryResponse::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchQueryDao(): SearchQueryDao
}