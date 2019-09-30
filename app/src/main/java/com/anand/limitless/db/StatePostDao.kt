package com.anand.limitless.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anand.limitless.vo.StateName


@Dao
interface StatePostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<StateName>)

    @Query("SELECT * FROM state")
    fun postsByState(): DataSource.Factory<Int, StateName>

    @Query("SELECT MAX(indexInResponse) + 1 FROM state WHERE name = :subreddit")
    fun getNextIndexInSubreddit(subreddit: String) : Int
}