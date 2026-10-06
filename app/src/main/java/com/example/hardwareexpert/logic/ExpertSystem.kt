package com.example.hardwareexpert.logic

import android.content.Context
import com.example.hardwareexpert.BuildConfig
import com.example.hardwareexpert.data.Rule
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

data class Answer(
    val cause: String,
    val advice: String,
    val source: String
)

class ExpertSystem(private val context: Context) {

    private val rules: List<Rule> by lazy { loadRules() }

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    /** Список всех симптомов, которые встречаются в базе правил. */
    fun allSymptoms(): List<String> =
        rules.flatMap { it.symptoms }.distinct().sorted()

    private fun loadRules(): List<Rule> {
        val json = context.assets
            .open("rules.json")
            .bufferedReader()
            .use { it.readText() }
        return gson.fromJson(json, Array<Rule>::class.java).toList()
    }

    /**
     * Прямой вывод (forward chaining):
     * 1. Ищем правила, где совпало ≥ 1 симптома.
     * 2. Ранжируем по числу совпадений (при равенстве — по компактности правила).
     * 3. Если полное совпадение — ответ из базы.
     * 4. Иначе обращаемся к LLM.
     */
    suspend fun diagnose(symptoms: List<String>): Answer = withContext(Dispatchers.IO) {

        val scored = rules
            .map { rule ->
                val matched = rule.symptoms.count { it in symptoms }
                Triple(rule, matched, rule.symptoms.size)
            }
            .filter { it.second > 0 }
            .sortedWith(
                compareByDescending<Triple<Rule, Int, Int>> { it.second }
                    .thenBy { it.third }
            )

        val best = scored.firstOrNull()

        when {
            best != null && best.second == best.third ->
                Answer(best.first.cause, best.first.advice, "База знаний")

            best != null && best.second >= 2 ->
                Answer(
                    best.first.cause,
                    best.first.advice + "\n\n(Внимание: совпадение неполное, уточните симптомы.)",
                    "База знаний (частично)"
                )

            else -> {
                val llm = askLLM(symptoms)
                if (llm.isNotBlank() && !llm.startsWith("Ошибка")) {
                    Answer("Ответ нейросети", llm, "Нейросеть (DeepSeek)")
                } else {
                    Answer(
                        "Ответ не найден",
                        llm.ifBlank {
                            "Попробуйте уточнить симптомы или проверьте интернет-соединение."
                        },
                        "—"
                    )
                }
            }
        }
    }

    private fun askLLM(symptoms: List<String>): String {
        val key = BuildConfig.LLM_API_KEY
        if (key.isBlank()) {
            return "LLM_API_KEY не задан в local.properties"
        }

        val prompt = """
            Ты — эксперт по диагностике компьютерного железа (ПК и ноутбуков).
            Пользователь сообщает симптомы: ${symptoms.joinToString(", ")}.
            Ответь кратко и простым языком:
            1) Вероятная причина.
            2) 2-4 конкретных действия по устранению.
            Не более 100 слов.
        """.trimIndent()

        val jsonBody = """
            {
              "model": "deepseek-chat",
              "messages": [
                {"role": "user", "content": ${gson.toJson(prompt)}}
              ],
              "temperature": 0.5
            }
        """.trimIndent()

        val request = Request.Builder()
            .url("https://api.deepseek.com/chat/completions")
            .addHeader("Authorization", "Bearer $key")
            .addHeader("Content-Type", "application/json")
            .post(jsonBody.toRequestBody("application/json".toMediaType()))
            .build()

        return try {
            client.newCall(request).execute().use { resp ->
                val body = resp.body?.string().orEmpty()
                if (!resp.isSuccessful) {
                    return "Ошибка API: ${resp.code}\n$body"
                }
                JsonParser.parseString(body)
                    .asJsonObject["choices"]
                    ?.asJsonArray
                    ?.get(0)
                    ?.asJsonObject["message"]
                    ?.asJsonObject["content"]
                    ?.asString
                    .orEmpty()
            }
        } catch (e: Exception) {
            "Ошибка соединения: ${e.message}"
        }
    }
}
