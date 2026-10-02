package com.example.data.model

data class Persona(
    val id: String,
    val name: String,
    val firstName: String,
    val role: String,
    val accent: String
)

data class PanicSuggestion(
    val userSpanish: String,
    val translatedEnglish: String,
    val conservative: String,
    val natural: String,
    val advanced: String
)

data class DialogTurn(
    val botEnglish: String,
    val botSpanish: String,
    val panic: PanicSuggestion
)

enum class ScenarioLevel(val label: String) {
    BASIC("BASIC"),
    INTERMEDIATE("INTERMEDIATE"),
    ADVANCED("ADVANCED")
}

data class VocabWord(
    val word: String,
    val translationSpanish: String
)

data class NativeIdiom(
    val phrase: String,
    val usage: String
)

data class PracticeScenario(
    val id: String,
    val title: String,
    val level: ScenarioLevel,
    val durationMinutes: Int,
    val category: String,
    val persona: Persona,
    val targetKeywords: List<String>,
    val extraKeywords: List<String>,
    val vocabulary: List<VocabWord>,
    val idioms: List<NativeIdiom>,
    val turns: List<DialogTurn>,
    val closingEnglish: String,
    val closingSpanish: String
)

enum class CoachingStrictness(val title: String, val subtitle: String) {
    GENTLE("Gentle", "Hints first"),
    BALANCED("Balanced", "Correct live"),
    STRICT("Strict", "Challenge me")
}

enum class CallEngineState {
    THINKING,
    SPEAKING,
    LISTENING,
    WAITING,
    DONE
}

data class PhonemeScore(
    val symbol: String,
    val exampleWord: String,
    val percentage: Int
)

data class CallFeedback(
    val scenarioId: String,
    val scenarioTitle: String,
    val fluencyScore: Int,
    val deltaFromLast: Int,
    val paceScore: Int,
    val clarityScore: Int,
    val vocabScore: Int,
    val confidenceScore: Int,
    val keywordScore: Int,
    val headline: String,
    val subtitle: String,
    val phonemes: List<PhonemeScore>,
    val nativeIdioms: List<NativeIdiom>,
    val targetWords: List<VocabWord>,
    val durationSeconds: Long,
    val turnsAnswered: Int,
    val isFirstCallStreakStarter: Boolean
)
