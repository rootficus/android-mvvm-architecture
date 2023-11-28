package com.fionpay.agent.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fionpay.agent.data.model.request.Bank
import com.fionpay.agent.data.model.request.Modem
import com.fionpay.agent.data.model.response.GetBalanceManageRecord
import com.fionpay.agent.data.model.response.GetMessageManageRecord
import com.fionpay.agent.roomDB.model.ModemSetting
import com.fionpay.agent.roomDB.dao.FionDao
import com.fionpay.agent.roomDB.model.NotificationRecord
import com.fionpay.agent.roomDB.model.OperatorRecord
import com.fionpay.agent.roomDB.model.SMSRecord


@Database(
    entities = [SMSRecord::class, ModemSetting::class, OperatorRecord::class, NotificationRecord::class, GetBalanceManageRecord::class, GetMessageManageRecord::class, Modem::class, Bank::class],
    version = 1
)
abstract class FionDatabase : RoomDatabase() {
    abstract fun fioDao(): FionDao?
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