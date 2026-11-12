package com.example.hardwareexpert.ui
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hardwareexpert.logic.ExpertSystem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(expert: ExpertSystem, deviceType: String, onDiagnose: (String, List<String>) -> Unit, onBack: () -> Unit) {
    var selectedBrand by remember { mutableStateOf("Любой") }
    val availableBrands = remember(deviceType) { expert.getBrandsFor(deviceType) }
    val allSymptoms = remember(deviceType, selectedBrand) { expert.getSymptomsFor(deviceType, if (selectedBrand == "Любой") "Any" else selectedBrand) }
    val selected = remember { mutableStateListOf<String>() }
    var query by remember { mutableStateOf("") }
    var brandMenuExpanded by remember { mutableStateOf(false) }
    val filtered = remember(query, allSymptoms) { if (query.isBlank()) allSymptoms else allSymptoms.filter { it.contains(query, ignoreCase = true) } }

    Column(Modifier.fillMaxSize().background(NavyBlue).padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = AccentCyan) }
            Text(if (deviceType == "PC") "Диагностика ПК" else "Диагностика ноутбука", color = AccentCyan, fontFamily = Consolas, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(8.dp))
        if (deviceType == "Laptop") {
            Box {
                OutlinedButton(onClick = { brandMenuExpanded = true }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentCyan), border = BorderStroke(1.dp, AccentGlow)) {
                    Text("Бренд: $selectedBrand", fontFamily = Consolas)
                }
                DropdownMenu(expanded = brandMenuExpanded, onDismissRequest = { brandMenuExpanded = false }, modifier = Modifier.background(NavyDeep)) {
                    availableBrands.forEach { brand ->
                        DropdownMenuItem(text = { Text(brand, color = TextWhite, fontFamily = Consolas) }, onClick = { selectedBrand = brand; brandMenuExpanded = false; selected.clear() })
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }
        Text("Всего симптомов: ${allSymptoms.size} · выбрано: ${selected.size}", color = AccentGlow, fontFamily = Consolas, fontSize = 12.sp)
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(value = query, onValueChange = { query = it }, placeholder = { Text("Поиск симптома…", fontFamily = Consolas, color = TextWhite.copy(alpha = 0.5f)) }, leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Поиск", tint = AccentCyan) }, trailingIcon = { if (query.isNotEmpty()) { IconButton(onClick = { query = "" }) { Icon(Icons.Default.Clear, contentDescription = "Очистить", tint = AccentCyan) } } }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite, focusedBorderColor = AccentCyan, unfocusedBorderColor = AccentGlow.copy(alpha = 0.5f), cursorColor = AccentCyan, focusedContainerColor = NavyDeep, unfocusedContainerColor = NavyDeep))
        Spacer(Modifier.height(8.dp))
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            items(filtered, key = { it }) { s ->
                val checked = s in selected
                Row(Modifier.fillMaxWidth().clickable { if (checked) selected.remove(s) else selected.add(s) }.background(if (checked) NavyDeep else Color.Transparent, RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = checked, onCheckedChange = null, colors = CheckboxDefaults.colors(checkedColor = AccentCyan, uncheckedColor = TextWhite, checkmarkColor = NavyBlue))
                    Spacer(Modifier.width(8.dp))
                    Text(s, color = TextWhite, fontFamily = Consolas, fontSize = 14.sp)
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = { if (selected.isNotEmpty()) onDiagnose(if (selectedBrand == "Любой") "Any" else selectedBrand, selected.toList()) }, enabled = selected.isNotEmpty(), modifier = Modifier.fillMaxWidth().height(52.dp).shadow(12.dp, RoundedCornerShape(12.dp), ambientColor = AccentGlow, spotColor = AccentGlow), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = AccentCyan, contentColor = NavyBlue)) {
            Text(if (selected.isEmpty()) "Выберите симптомы" else "Диагностировать (${selected.size})", fontFamily = Consolas, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
