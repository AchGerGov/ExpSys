package com.example.hardwareexpert.data
data class Rule(
    val device: String,
    val brand: String,
    val symptoms: List<String>,
    val cause: String,
    val advice: String
)
