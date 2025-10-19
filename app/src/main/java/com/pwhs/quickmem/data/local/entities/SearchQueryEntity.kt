package com.pwhs.quickmem.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import javax.annotation.Nonnull

@Entity(
    tableName = "recent_search",
    indices = [Index(value = ["query"], unique = true)]
)
data class SearchQueryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "query")
    @param:Nonnull
    val query: String,
    val timestamp: Long = System.currentTimeMillis()
)
