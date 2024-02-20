package com.rf.utellRestaurant.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rf.utellRestaurant.roomDB.dao.UtellDao
import com.rf.utellRestaurant.roomDB.model.ModemSetting
import com.rf.utellRestaurant.roomDB.model.NotificationRecord
import com.rf.utellRestaurant.roomDB.model.OperatorRecord
import com.rf.utellRestaurant.roomDB.model.SMSRecord


@Database(
    entities = [SMSRecord::class, ModemSetting::class, OperatorRecord::class, NotificationRecord::class],
    version = 1
)
abstract class FionDatabase : RoomDatabase() {
    abstract fun fioDao(): UtellDao?

    companion object {
        @Volatile
        private var INSTANCE: FionDatabase? = null

        fun getDatabase(context: Context): FionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FionDatabase::class.java,
                    "FionDatabase.db"
                ).allowMainThreadQueries().build()//.addMigrations(migration_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }
}