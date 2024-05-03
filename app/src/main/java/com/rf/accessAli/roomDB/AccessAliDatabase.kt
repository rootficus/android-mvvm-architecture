package com.rf.accessAli.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rf.accessAli.roomDB.dao.AccessAliDao
import com.rf.accessAli.roomDB.model.SMSRecord


@Database(
    entities = [SMSRecord::class],
    version = 1
)
abstract class AccessAliDatabase : RoomDatabase() {
    abstract fun accessAliDao(): AccessAliDao?

    companion object {
        @Volatile
        private var INSTANCE: AccessAliDatabase? = null

        fun getDatabase(context: Context): AccessAliDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AccessAliDatabase::class.java,
                    "AccessAliDatabase.db"
                ).allowMainThreadQueries().build()//.addMigrations(migration_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }
}