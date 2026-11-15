package com.example.hardwareexpert.ui
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hardwareexpert.logic.Answer

@Composable
fun ResultScreen(answers: List<Answer>, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(NavyBlue).padding(16.dp)) {
        Text("Результаты диагностики", color = AccentCyan, fontFamily = Consolas, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(answers) { answer ->
                Card(colors = CardDefaults.cardColors(containerColor = NavyDeep), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Причина:", color = AccentGlow, fontFamily = Consolas, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(answer.cause, color = TextWhite, fontFamily = Consolas, fontSize = 16.sp)
                        Spacer(Modifier.height(12.dp))
                        Text("Рекомендации:", color = AccentGlow, fontFamily = Consolas, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(answer.advice, color = TextWhite, fontFamily = Consolas, fontSize = 14.sp)
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth().height(50.dp).shadow(10.dp, RoundedCornerShape(12.dp), ambientColor = AccentGlow, spotColor = AccentGlow), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = AccentCyan, contentColor = NavyBlue)) {
            Text("Назад", fontFamily = Consolas, fontWeight = FontWeight.Bold)
        }
    }
}
