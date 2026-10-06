package com.example.hardwareexpert.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hardwareexpert.logic.Answer

@Composable
fun ResultScreen(answer: Answer, onBack: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(NavyBlue)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Причина:",
            color = AccentCyan,
            fontFamily = Consolas,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            answer.cause,
            color = TextWhite,
            fontFamily = Consolas,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(18.dp))

        Text(
            "Рекомендации:",
            color = AccentCyan,
            fontFamily = Consolas,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            answer.advice,
            color = TextWhite,
            fontFamily = Consolas,
            fontSize = 15.sp
        )

        Spacer(Modifier.height(18.dp))

        Text(
            "Источник: ${answer.source}",
            color = AccentGlow,
            fontFamily = Consolas,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shadow(
                    elevation = 10.dp,
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
            Text("Назад", fontFamily = Consolas, fontWeight = FontWeight.Bold)
        }
    }
}
