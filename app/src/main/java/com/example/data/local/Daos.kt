package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfileFlow(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfile(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET currentStreak = :streak, bestStreak = :best, lastCallDate = :date, todayMinutesPracticed = :todayMins WHERE id = 1")
    suspend fun updateStreakAndMinutes(streak: Int, best: Int, date: String, todayMins: Int)
}

@Dao
interface CallHistoryDao {
    @Query("SELECT * FROM call_history ORDER BY id DESC")
    fun getAllHistoryFlow(): Flow<List<CallHistoryEntity>>

    @Insert
    suspend fun insert(call: CallHistoryEntity): Long

    @Query("DELETE FROM call_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM call_history")
    suspend fun clearAll()
}

@Dao
interface SavedVocabDao {
    @Query("SELECT * FROM saved_vocabulary ORDER BY savedTimestamp DESC")
    fun getAllVocabFlow(): Flow<List<SavedVocabEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vocab: SavedVocabEntity)

    @Query("DELETE FROM saved_vocabulary WHERE word = :word")
    suspend fun deleteWord(word: String)

    @Query("DELETE FROM saved_vocabulary")
    suspend fun clearAll()
}
