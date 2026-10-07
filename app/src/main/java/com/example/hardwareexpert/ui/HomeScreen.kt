package com.example.hardwareexpert.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hardwareexpert.logic.ExpertSystem

@Composable
fun HomeScreen(
    expert: ExpertSystem,
    onDiagnose: (List<String>) -> Unit,
    onHistory: () -> Unit
) {
    val allSymptoms = remember { expert.allSymptoms() }
    val selected = remember { mutableStateListOf<String>() }
    var query by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    // Фильтрация симптомов по поисковой строке
    val filtered = remember(query, allSymptoms) {
        if (query.isBlank()) allSymptoms
        else allSymptoms.filter { it.contains(query, ignoreCase = true) }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(NavyBlue)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // ─── Заголовок ─────────────────────────────────────────────
        Text(
            "Отметьте наблюдаемые симптомы",
            color = AccentCyan,
            fontFamily = Consolas,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Всего симптомов: ${allSymptoms.size} · выбрано: ${selected.size}",
            color = AccentGlow,
            fontFamily = Consolas,
            fontSize = 12.sp
        )

        Spacer(Modifier.height(10.dp))

        // ─── Поисковая строка ──────────────────────────────────────
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = {
                Text(
                    "Поиск симптома…",
                    fontFamily = Consolas,
                    color = TextWhite.copy(alpha = 0.5f)
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Поиск",
                    tint = AccentCyan
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Очистить",
                            tint = AccentCyan
                        )
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedBorderColor = AccentCyan,
                unfocusedBorderColor = AccentGlow.copy(alpha = 0.5f),
                cursorColor = AccentCyan,
                focusedContainerColor = NavyDeep,
                unfocusedContainerColor = NavyDeep
            )
        )

        Spacer(Modifier.height(8.dp))

        // ─── Кнопки управления списком ─────────────────────────────
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SmallActionButton(
                text = "Сбросить выбор",
                enabled = selected.isNotEmpty(),
                onClick = { selected.clear() },
                modifier = Modifier.weight(1f)
            )
            SmallActionButton(
                text = "Снять все",
                enabled = query.isNotBlank() && filtered.any { it in selected },
                onClick = { selected.removeAll(filtered.toSet()) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(8.dp))

        // ─── Список симптомов ──────────────────────────────────────
        if (filtered.isEmpty()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Ничего не найдено по запросу «$query»",
                    color = TextWhite.copy(alpha = 0.6f),
                    fontFamily = Consolas,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(filtered, key = { it }) { s ->
                    SymptomRow(
                        symptom = s,
                        checked = s in selected,
                        onToggle = {
                            if (s in selected) selected.remove(s) else selected.add(s)
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        // ─── Прогресс-бар ──────────────────────────────────────────
        if (loading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = AccentCyan,
                trackColor = NavyDeep
            )
            Spacer(Modifier.height(8.dp))
        }

        // ─── Кнопка "Диагностировать" ──────────────────────────────
        Button(
            onClick = {
                if (selected.isNotEmpty()) {
                    loading = true
                    onDiagnose(selected.toList())
                }
            },
            enabled = selected.isNotEmpty() && !loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = AccentGlow,
                    spotColor = AccentGlow
                ),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentCyan,
                contentColor = NavyBlue
            )
        ) {
            Text(
                if (selected.isEmpty()) "Выберите симптомы"
                else "Диагностировать (${selected.size})",
                fontFamily = Consolas,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.height(8.dp))

        // ─── Кнопка "История" ──────────────────────────────────────
        OutlinedButton(
            onClick = onHistory,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentCyan),
            border = BorderStroke(1.dp, AccentCyan)
        ) {
            Text("История запросов", fontFamily = Consolas)
        }
    }
}

// ─────────────────────────────────────────────────────────────────
// Вспомогательные composable-функции
// ─────────────────────────────────────────────────────────────────

@Composable
private fun SymptomRow(
    symptom: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .background(
                color = if (checked) NavyDeep else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(
                checkedColor = AccentCyan,
                uncheckedColor = TextWhite,
                checkmarkColor = NavyBlue
            )
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = symptom,
            color = TextWhite,
            fontFamily = Consolas,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun SmallActionButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = AccentCyan,
            disabledContentColor = AccentCyan.copy(alpha = 0.3f)
        ),
        border = BorderStroke(
            1.dp,
            if (enabled) AccentCyan else AccentCyan.copy(alpha = 0.3f)
        ),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
    ) {
        Text(
            text,
            fontFamily = Consolas,
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}
