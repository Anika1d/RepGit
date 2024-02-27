package com.mir.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mir.database.dao.SearchQueryDao
import com.mir.database.data.response.SearchQueryResponse

@Database(entities = [SearchQueryResponse::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchQueryDao(): SearchQueryDao
}