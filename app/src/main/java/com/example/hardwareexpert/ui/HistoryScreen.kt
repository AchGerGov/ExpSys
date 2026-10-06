package com.example.hardwareexpert.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hardwareexpert.data.HistoryDao
import com.example.hardwareexpert.data.HistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(dao: HistoryDao, onBack: () -> Unit) {
    var items by remember { mutableStateOf<List<HistoryEntity>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val fmt = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }

    LaunchedEffect(Unit) {
        items = withContext(Dispatchers.IO) { dao.getAll() }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(NavyBlue)
            .padding(16.dp)
    ) {
        Text(
            "История запросов",
            color = AccentCyan,
            fontFamily = Consolas,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))

        if (items.isEmpty()) {
            Text(
                "Пока нет запросов.",
                color = TextWhite,
                fontFamily = Consolas
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyDeep),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                fmt.format(Date(item.time)),
                                color = AccentGlow,
                                fontFamily = Consolas,
                                fontSize = 12.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                item.query,
                                color = AccentCyan,
                                fontFamily = Consolas,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                item.cause,
                                color = TextWhite,
                                fontFamily = Consolas,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = {
                    scope.launch {
                        withContext(Dispatchers.IO) { dao.clear() }
                        items = emptyList()
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentCyan),
                border = BorderStroke(1.dp, AccentCyan)
            ) {
                Text("Очистить", fontFamily = Consolas)
            }

            Button(
                onClick = onBack,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentCyan,
                    contentColor = NavyBlue
                )
            ) {
                Text("Назад", fontFamily = Consolas, fontWeight = FontWeight.Bold)
            }
        }
    }
}
