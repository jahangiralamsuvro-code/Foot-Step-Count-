package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AchievementUnlockEntity
import com.example.data.model.DailyActivityRecord
import com.example.data.model.FavoriteQuoteEntity
import com.example.data.model.HydrationRecord
import com.example.data.model.WeightRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface VitalStepDao {

    // Daily Activity
    @Query("SELECT * FROM daily_activity WHERE date = :date LIMIT 1")
    fun getActivityForDate(date: String): Flow<DailyActivityRecord?>

    @Query("SELECT * FROM daily_activity WHERE date = :date LIMIT 1")
    suspend fun getActivityForDateSync(date: String): DailyActivityRecord?

    @Query("SELECT * FROM daily_activity ORDER BY date DESC")
    fun getAllActivityHistory(): Flow<List<DailyActivityRecord>>

    @Query("SELECT * FROM daily_activity ORDER BY date DESC LIMIT 7")
    fun getLast7DaysActivity(): Flow<List<DailyActivityRecord>>

    @Query("SELECT * FROM daily_activity ORDER BY date DESC LIMIT 30")
    fun getLast30DaysActivity(): Flow<List<DailyActivityRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateActivity(record: DailyActivityRecord)

    // Hydration
    @Query("SELECT * FROM hydration_records WHERE date = :date ORDER BY timestamp ASC")
    fun getHydrationForDate(date: String): Flow<List<HydrationRecord>>

    @Query("SELECT COALESCE(SUM(amountMl), 0) FROM hydration_records WHERE date = :date")
    fun getTotalHydrationForDate(date: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHydration(record: HydrationRecord): Long

    @Query("DELETE FROM hydration_records WHERE id = :id")
    suspend fun deleteHydration(id: Long)

    @Query("DELETE FROM hydration_records WHERE date = :date")
    suspend fun clearHydrationForDate(date: String)

    // Weight Records
    @Query("SELECT * FROM weight_records ORDER BY timestamp ASC")
    fun getAllWeightRecords(): Flow<List<WeightRecord>>

    @Query("SELECT * FROM weight_records ORDER BY timestamp DESC LIMIT 1")
    fun getLatestWeightRecord(): Flow<WeightRecord?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightRecord(record: WeightRecord): Long

    @Query("DELETE FROM weight_records WHERE id = :id")
    suspend fun deleteWeightRecord(id: Long)

    // Achievements
    @Query("SELECT * FROM unlocked_achievements")
    fun getAllUnlockedAchievements(): Flow<List<AchievementUnlockEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlockAchievement(entity: AchievementUnlockEntity)

    // Favorite Quotes
    @Query("SELECT quoteId FROM favorite_quotes")
    fun getFavoriteQuoteIds(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteQuote(entity: FavoriteQuoteEntity)

    @Query("DELETE FROM favorite_quotes WHERE quoteId = :quoteId")
    suspend fun removeFavoriteQuote(quoteId: Int)

    // Clear All Data
    @Query("DELETE FROM daily_activity")
    suspend fun clearAllActivity()

    @Query("DELETE FROM hydration_records")
    suspend fun clearAllHydration()

    @Query("DELETE FROM weight_records")
    suspend fun clearAllWeight()

    @Query("DELETE FROM unlocked_achievements")
    suspend fun clearAllAchievements()

    @Query("DELETE FROM favorite_quotes")
    suspend fun clearAllFavorites()
}
