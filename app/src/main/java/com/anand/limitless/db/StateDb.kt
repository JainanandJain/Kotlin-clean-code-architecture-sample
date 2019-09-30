package com.anand.limitless.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anand.limitless.vo.StateName


/**
 * Database schema used by the DbRedditPostRepository
 */
@Database(
    entities = arrayOf(StateName::class),
    version = 1,
    exportSchema = false
)
abstract class StateDb : RoomDatabase() {
    companion object {
        fun create(context: Context, useInMemory : Boolean): StateDb {
            val databaseBuilder = if(useInMemory) {
                Room.inMemoryDatabaseBuilder(context, StateDb::class.java)
            } else {
                Room.databaseBuilder(context, StateDb::class.java, "state.db")
            }
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun posts(): StatePostDao
}