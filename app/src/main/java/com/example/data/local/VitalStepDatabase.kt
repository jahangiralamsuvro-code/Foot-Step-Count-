package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AchievementUnlockEntity
import com.example.data.model.DailyActivityRecord
import com.example.data.model.FavoriteQuoteEntity
import com.example.data.model.HydrationRecord
import com.example.data.model.WeightRecord

@Database(
    entities = [
        DailyActivityRecord::class,
        HydrationRecord::class,
        WeightRecord::class,
        AchievementUnlockEntity::class,
        FavoriteQuoteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VitalStepDatabase : RoomDatabase() {

    abstract fun vitalStepDao(): VitalStepDao

    companion object {
        @Volatile
        private var INSTANCE: VitalStepDatabase? = null

        fun getInstance(context: Context): VitalStepDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VitalStepDatabase::class.java,
                    "vitalstep_health.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
