package com.example.hardwareexpert.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun HomeScreen(
    expert: ExpertSystem,
    onDiagnose: (List<String>) -> Unit,
    onHistory: () -> Unit
) {
    val symptoms = remember { expert.allSymptoms() }
    val selected = remember { mutableStateListOf<String>() }
    var loading by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(NavyBlue)
            .padding(16.dp)
    ) {
        Text(
            "Отметьте наблюдаемые симптомы",
            color = AccentCyan,
            fontFamily = Consolas,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(symptoms) { s ->
                val checked = s in selected
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (checked) selected.remove(s) else selected.add(s)
                        }
                        .background(
                            color = if (checked) NavyDeep else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = null, // обрабатывается Row.clickable
                        colors = CheckboxDefaults.colors(
                            checkedColor = AccentCyan,
                            uncheckedColor = TextWhite,
                            checkmarkColor = NavyBlue
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = s,
                        color = TextWhite,
                        fontFamily = Consolas,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        if (loading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = AccentCyan,
                trackColor = NavyDeep
            )
            Spacer(Modifier.height(8.dp))
        }

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
                "Диагностировать",
                fontFamily = Consolas,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = onHistory,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentCyan),
            border = BorderStroke(1.dp, AccentCyan)
        ) {
            Text("История запросов", fontFamily = Consolas)
        }
    }
}
