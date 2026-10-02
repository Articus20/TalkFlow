package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val username: String,
    val goalsDescription: String,
    val dailyTargetMinutes: Int = 10,
    val currentStreak: Int = 0, // Starts at 0!
    val bestStreak: Int = 0,
    val lastCallDate: String = "", // YYYY-MM-DD
    val todayMinutesPracticed: Int = 0,
    val todayDateString: String = ""
)

@Entity(tableName = "call_history")
data class CallHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val scenarioId: String,
    val scenarioTitle: String,
    val fluencyScore: Int,
    val durationSeconds: Long,
    val dateFormatted: String, // e.g. "Oct 2, 2026"
    val timestamp: Long = System.currentTimeMillis(),
    val turnsCount: Int
)

@Entity(tableName = "saved_vocabulary")
data class SavedVocabEntity(
    @PrimaryKey val word: String,
    val translationSpanish: String,
    val scenarioId: String,
    val savedTimestamp: Long = System.currentTimeMillis()
)
