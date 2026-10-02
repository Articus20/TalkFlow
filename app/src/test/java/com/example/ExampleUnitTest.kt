package com.example

import com.example.data.model.ScenarioData
import com.example.data.model.ScenarioLevel
import com.example.util.CostaRicaTime
import com.example.voice.CoachAndTranslator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testCostaRicaTimeIntegrity() {
        val crTime = CostaRicaTime.getCurrentFormattedTime()
        assertNotNull(crTime)
        assertTrue(crTime.isNotEmpty())

        val greeting = CostaRicaTime.getGreeting()
        assertTrue(greeting.startsWith("GOOD"))

        val todayKey = CostaRicaTime.getTodayDateKey()
        assertTrue(todayKey.matches(Regex("\\d{4}-\\d{2}-\\d{2}")))
    }

    @Test
    fun testScenarioDataCatalog() {
        val scenarios = ScenarioData.allScenarios
        assertEquals(6, scenarios.size)

        val interview = ScenarioData.findById("interview")
        assertEquals("Job interviews", interview.title)
        assertEquals(ScenarioLevel.INTERMEDIATE, interview.level)
        assertEquals("Alex Rivera", interview.persona.name)
        assertTrue(interview.turns.isNotEmpty())
        assertNotNull(interview.turns.first().panic.conservative)
        assertNotNull(interview.turns.first().panic.natural)
        assertNotNull(interview.turns.first().panic.advanced)
    }

    @Test
    fun testGrammarCoachRules() {
        val tip1 = CoachAndTranslator.analyzeTurn("I am agree with you", "Balanced", listOf("impact"))
        assertFalse(tip1.isPositive)
        assertTrue(tip1.tip.contains("I agree"))

        val tip2 = CoachAndTranslator.analyzeTurn("This is more better than before", "Balanced", listOf("impact"))
        assertFalse(tip2.isPositive)
        assertTrue(tip2.tip.contains("omit \"more\""))

        val tip3 = CoachAndTranslator.analyzeTurn("I prioritize tasks by urgency and impact", "Balanced", listOf("impact"))
        assertTrue(tip3.isPositive)
    }

    @Test
    fun testSpanishToEnglishTranslation() {
        val translated1 = CoachAndTranslator.translateSpanishToEnglish("Me cuesta priorizar cuando todo parece urgente")
        assertTrue(translated1.contains("prioritize"))

        val translated2 = CoachAndTranslator.translateSpanishToEnglish("necesito un boleto de tren")
        assertTrue(translated2.contains("ticket") || translated2.contains("train"))
    }
}
