package com.rf.macgyver.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rf.macgyver.roomDB.dao.MagDao
import com.rf.macgyver.roomDB.model.Converters
import com.rf.macgyver.roomDB.model.DailyReporting
import com.rf.macgyver.roomDB.model.IPQuestionTypeConverter
import com.rf.macgyver.roomDB.model.IncidentReport
import com.rf.macgyver.roomDB.model.InspectionForm
import com.rf.macgyver.roomDB.model.LoginDetails
import com.rf.macgyver.roomDB.model.QuestionDataTypeConverter


@Database(
    entities = [LoginDetails::class, DailyReporting::class, InspectionForm::class, IncidentReport::class],
    version = 1
)
@TypeConverters(Converters::class , QuestionDataTypeConverter :: class,  IPQuestionTypeConverter :: class)
abstract class MagDatabase : RoomDatabase() {
    abstract fun magDao(): MagDao?

    companion object {
        @Volatile
        private var INSTANCE: MagDatabase? = null

        fun getDatabase(context: Context): MagDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MagDatabase::class.java,
                    "MagDatabase.db"
                ).allowMainThreadQueries().build()//.addMigrations(migration_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }
}