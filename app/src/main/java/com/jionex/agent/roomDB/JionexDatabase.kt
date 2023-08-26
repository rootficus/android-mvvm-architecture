package com.jionex.agent.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jionex.agent.data.model.response.GetBalanceManageRecord
import com.jionex.agent.roomDB.model.ModemSetting
import com.jionex.agent.roomDB.dao.JionexDao
import com.jionex.agent.roomDB.model.OperatorRecord
import com.jionex.agent.roomDB.model.SMSRecord


@Database(
    entities = [SMSRecord::class, ModemSetting::class, OperatorRecord::class, GetBalanceManageRecord::class],
    version = 1
)
abstract class JionexDatabase : RoomDatabase() {
    abstract fun rapidxDao(): JionexDao?
    companion object {
        @Volatile
        private var INSTANCE: JionexDatabase? = null

        fun getDatabase(context: Context): JionexDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JionexDatabase::class.java,
                    "JionexDatabase.db"
                ).allowMainThreadQueries().build()//.addMigrations(migration_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }
}