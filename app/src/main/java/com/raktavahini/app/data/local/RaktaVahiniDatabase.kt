package com.raktavahini.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raktavahini.app.data.model.DonationLog
import com.raktavahini.app.data.model.Donor

@Database(
    entities = [Donor::class, DonationLog::class],
    version = 2,
    exportSchema = false
)
abstract class RaktaVahiniDatabase : RoomDatabase() {

    abstract fun donorDao(): DonorDao
    abstract fun donationLogDao(): DonationLogDao

    companion object {
        @Volatile
        private var INSTANCE: RaktaVahiniDatabase? = null

        fun getDatabase(context: Context): RaktaVahiniDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RaktaVahiniDatabase::class.java,
                    "rakta_vahini_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
