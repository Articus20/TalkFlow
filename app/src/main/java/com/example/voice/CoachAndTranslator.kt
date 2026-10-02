package com.example.voice

import java.util.Locale

data class CoachTip(
    val tip: String,
    val isPositive: Boolean
)

object CoachAndTranslator {

    private val grammarRules = listOf(
        Regex("\\bi am agree\\b", RegexOption.IGNORE_CASE) to "Say \"I agree\" — no \"am\" needed.",
        Regex("\\bmore (better|easier|bigger|faster|cheaper)\\b", RegexOption.IGNORE_CASE) to "Use just \"better / easier / bigger / faster\" — omit \"more\".",
        Regex("\\b(he|she|it) don'?t\\b", RegexOption.IGNORE_CASE) to "Use \"doesn't\" with he / she / it.",
        Regex("\\bsince \\d+ (years?|months?|days?|weeks?)\\b", RegexOption.IGNORE_CASE) to "Use \"for\" with duration: e.g. \"for 3 years\".",
        Regex("\\binformations\\b", RegexOption.IGNORE_CASE) to "\"Information\" is uncountable — never add an 's'.",
        Regex("\\bdepends? of\\b", RegexOption.IGNORE_CASE) to "Say \"depend on\", never \"depend of\".",
        Regex("\\bi have \\d+ years\\b", RegexOption.IGNORE_CASE) to "In English say \"I am 25 years old\" rather than \"I have\".",
        Regex("\\bmake a question\\b", RegexOption.IGNORE_CASE) to "Say \"ask a question\" instead of \"make a question\".",
        Regex("\\bi am (work|study|live)\\b", RegexOption.IGNORE_CASE) to "Use \"I work / study / live\" for habitual routines."
    )

    private val fillerRegex = Regex("\\b(um+|uh+|er+|ehh*|like|you know|i mean)\\b", RegexOption.IGNORE_CASE)

    fun analyzeTurn(text: String, strictness: String, targetKeywords: List<String>): CoachTip {
        val trimmed = text.trim()
        val words = trimmed.split(Regex("\\s+")).filter { it.isNotBlank() }
        val fillerCount = fillerRegex.findAll(trimmed).count()

        for ((regex, hint) in grammarRules) {
            if (regex.containsMatchIn(trimmed)) {
                return CoachTip(hint, false)
            }
        }

        if (fillerCount >= 2) {
            return CoachTip("Try pausing silently instead of saying \"um\" or \"like\".", false)
        }

        val unusedKw = targetKeywords.filter { kw -> !trimmed.contains(kw, ignoreCase = true) }

        return when (strictness.lowercase(Locale.ROOT)) {
            "gentle" -> {
                if (words.size < 4) CoachTip("Hint: start with \"I think...\" or share your main idea.", true)
                else CoachTip("Great rhythm! Keep speaking naturally.", true)
            }
            "strict" -> {
                if (words.size < 7) CoachTip("Challenge: expand with a concrete example (7+ words).", false)
                else if (unusedKw.isNotEmpty()) CoachTip("Challenge: incorporate target keyword \"${unusedKw.first()}\".", false)
                else CoachTip("Sharp delivery. Confident and articulate.", true)
            }
            else -> { // Balanced
                if (words.size >= 5) CoachTip("Clear and natural delivery.", true)
                else CoachTip("Good start — add a quick reason to expand.", true)
            }
        }
    }

    private val spanishDictionary = mapOf(
        "hola" to "hello",
        "gracias" to "thank you",
        "por favor" to "please",
        "quiero" to "I want",
        "necesito" to "I need",
        "puedo" to "can I",
        "tengo" to "I have",
        "pienso" to "I think",
        "creo" to "I believe",
        "cafe" to "coffee",
        "leche" to "milk",
        "avena" to "oat",
        "caliente" to "hot",
        "frio" to "cold",
        "boleto" to "ticket",
        "tren" to "train",
        "manana" to "tomorrow",
        "ayer" to "yesterday",
        "hoy" to "today",
        "habitacion" to "room",
        "problema" to "issue",
        "precio" to "price",
        "descuento" to "discount",
        "contrato" to "contract",
        "trabajo" to "work",
        "equipo" to "team",
        "remoto" to "remote",
        "tiempo" to "time",
        "prioridad" to "priority",
        "urgente" to "urgent",
        "compromiso" to "commitment",
        "reembolso" to "refund",
        "acuerdo" to "agreement"
    )

    fun translateSpanishToEnglish(spanishInput: String): String {
        val input = spanishInput.trim()
        if (input.isBlank()) return ""

        // Check for common Spanish phrases
        val lower = input.lowercase(Locale.ROOT)
        if (lower.contains("cuesta priorizar")) {
            return "I find it hard to prioritize when everything feels urgent."
        }
        if (lower.contains("aire acondicionado")) {
            return "The air conditioning in my room has not been working."
        }
        if (lower.contains("boleto a") || lower.contains("boleto para")) {
            return "I would like to book a ticket for tomorrow morning, please."
        }
        if (lower.contains("cafe con leche")) {
            return "I would like a latte with oat milk, please."
        }
        if (lower.contains("precio menor") || lower.contains("descuento")) {
            return "Could we explore a volume discount or adjusted monthly rate?"
        }
        if (lower.contains("aumenta la productividad")) {
            return "It significantly increases deep-work focus and overall productivity."
        }

        // Word-by-word approximation fallback
        val cleaned = lower.replace(Regex("[^a-záéíóúñ\\s]"), "")
        val words = cleaned.split(Regex("\\s+"))
        val translated = words.map { word ->
            val unaccented = word
                .replace('á', 'a').replace('é', 'e')
                .replace('í', 'i').replace('ó', 'o')
                .replace('ú', 'u').replace('ñ', 'n')
            spanishDictionary[unaccented] ?: word
        }
        val sentence = translated.joinToString(" ")
        return sentence.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString() } + "."
    }
}
