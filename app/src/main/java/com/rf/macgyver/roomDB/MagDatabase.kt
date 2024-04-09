package com.rf.macgyver.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rf.macgyver.roomDB.dao.UtellDao
import com.rf.macgyver.roomDB.model.ModemSetting
import com.rf.macgyver.roomDB.model.NotificationRecord
import com.rf.macgyver.roomDB.model.OperatorRecord
import com.rf.macgyver.roomDB.model.SMSRecord


@Database(
    entities = [SMSRecord::class, ModemSetting::class, OperatorRecord::class, NotificationRecord::class],
    version = 1
)
abstract class MagDatabase : RoomDatabase() {
    abstract fun fioDao(): UtellDao?

    companion object {
        @Volatile
        private var INSTANCE: MagDatabase? = null

        fun getDatabase(context: Context): MagDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MagDatabase::class.java,
                    "FionDatabase.db"
                ).allowMainThreadQueries().build()//.addMigrations(migration_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }
}