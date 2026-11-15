package com.example.hardwareexpert.data

data class Rule(
    val device: String,  // "PC", "Laptop", "Any"
    val brand: String,   // "Any", "ASUS", "Dell", "Apple", "Lenovo", "MSI", "Acer", "HP"
    val symptoms: List<String>,
    val cause: String,
    val advice: String
)
