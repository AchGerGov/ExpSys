package com.example.hardwareexpert.logic

import android.content.Context
import com.example.hardwareexpert.data.Rule
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class Answer(val cause: String, val advice: String, val source: String = "База знаний")

class ExpertSystem(private val context: Context) {
    private val rules: List<Rule> by lazy { loadRules() }
    private val gson = Gson()

    fun getSymptomsFor(device: String, brand: String): List<String> {
        return rules.filter { (it.device == "Any" || it.device == device) && (it.brand == "Any" || it.brand == brand) }
            .flatMap { it.symptoms }.distinct().sorted()
    }

    fun getBrandsFor(device: String): List<String> {
        val brands = rules.filter { it.device == device && it.brand != "Any" }.map { it.brand }.distinct().sorted()
        return listOf("Любой") + brands
    }

    private fun loadRules(): List<Rule> {
        val json = context.assets.open("rules.json").bufferedReader().use { it.readText() }
        return gson.fromJson(json, Array<Rule>::class.java).toList()
    }

    suspend fun diagnose(device: String, brand: String, symptoms: List<String>): List<Answer> = withContext(Dispatchers.Default) {
        val relevantRules = rules.filter { (it.device == "Any" || it.device == device) && (it.brand == "Any" || it.brand == brand) }
        val scoredRules = relevantRules.map { rule -> Pair(rule, rule.symptoms.count { it in symptoms }) }.filter { it.second > 0 }
        val sortedRules = scoredRules.sortedByDescending { it.second }
        
        val answers = mutableListOf<Answer>()
        val seenCauses = mutableSetOf<String>()
        for ((rule, _) in sortedRules) {
            if (rule.cause !in seenCauses) {
                answers.add(Answer(rule.cause, rule.advice))
                seenCauses.add(rule.cause)
            }
        }
        if (answers.isEmpty()) listOf(Answer("Причина не определена", "Ни одно правило не сработало. Уточните симптомы или обратитесь в сервисный центр.")) else answers
    }
}
