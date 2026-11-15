package com.example.hardwareexpert.logic
object SearchHelper {
    private val synonyms = mapOf(
        "блютуз" to "bluetooth", "блюпуп" to "bluetooth", "вайфай" to "wi-fi", "вай фай" to "wi-fi",
        "вифи" to "wi-fi", "винт" to "жёсткий диск", "винчестер" to "жёсткий диск", "ссд" to "ssd",
        "хдд" to "hdd", "оперативка" to "оперативная память", "озу" to "оперативная память",
        "видюха" to "видеокарта", "материнка" to "материнская плата", "бп" to "блок питания",
        "куллер" to "кулер", "экран" to "монитор", "матрица" to "экран"
    )
    private val stopWords = listOf(
        "почему", "что", "как", "где", "когда", "зачем", "всё", "время", "постоянно", "иногда",
        "часто", "бывает", "на", "в", "с", "и", "а", "но", "или", "то", "же", "комп", "компе",
        "компьютер", "компьютере", "пк", "ноут", "ноуте", "ноутбук", "ноутбуке", "лэптоп",
        "у", "меня", "было", "такое", "очень", "это"
    )
    fun extractKeywords(query: String): List<String> {
        var normalized = query.lowercase().replace(Regex("[^а-яёa-z0-9\\-\\s]"), "")
        for ((key, value) in synonyms) { normalized = normalized.replace(key, value) }
        return normalized.split(Regex("\\s+")).filter { it.isNotBlank() && it !in stopWords && it.length > 1 }
    }
}
