package mir.anika1d.repgit.database.data.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_queries")
data class SearchQueryResponse(
    @PrimaryKey val name: String,
    @ColumnInfo(
        defaultValue = "CURRENT_TIMESTAMP",
        name = "created_at",

    ) val createdAt: String ?=null
)