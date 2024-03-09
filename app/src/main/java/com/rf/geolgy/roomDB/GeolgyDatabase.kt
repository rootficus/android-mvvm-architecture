package com.rf.geolgy.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rf.geolgy.roomDB.dao.GeolgyDao
import com.rf.geolgy.roomDB.model.ModemSetting
import com.rf.geolgy.roomDB.model.NotificationRecord
import com.rf.geolgy.roomDB.model.OperatorRecord
import com.rf.geolgy.roomDB.model.SMSRecord


@Database(
    entities = [SMSRecord::class, ModemSetting::class, OperatorRecord::class, NotificationRecord::class],
    version = 1
)
abstract class GeolgyDatabase : RoomDatabase() {
    abstract fun fioDao(): GeolgyDao?

    companion object {
        @Volatile
        private var INSTANCE: GeolgyDatabase? = null

        fun getDatabase(context: Context): GeolgyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GeolgyDatabase::class.java,
                    "FionDatabase.db"
                ).allowMainThreadQueries().build()//.addMigrations(migration_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }
}