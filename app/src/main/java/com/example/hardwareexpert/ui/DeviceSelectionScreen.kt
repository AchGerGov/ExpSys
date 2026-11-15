package com.example.hardwareexpert.ui
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DeviceSelectionScreen(onDeviceSelected: (String) -> Unit, onHistory: () -> Unit) {
    Column(Modifier.fillMaxSize().background(NavyBlue).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Выберите тип устройства", color = AccentCyan, fontFamily = Consolas, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(40.dp))
        DeviceButton(text = "Компьютер (ПК)", onClick = { onDeviceSelected("PC") })
        Spacer(Modifier.height(20.dp))
        DeviceButton(text = "Ноутбук", onClick = { onDeviceSelected("Laptop") })
        Spacer(Modifier.height(60.dp))
        OutlinedButton(onClick = onHistory, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentCyan), border = BorderStroke(1.dp, AccentCyan)) {
            Text("История запросов", fontFamily = Consolas)
        }
    }
}

@Composable
private fun DeviceButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth().height(80.dp).shadow(12.dp, shape = RoundedCornerShape(16.dp), ambientColor = AccentGlow, spotColor = AccentGlow), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = NavyDeep, contentColor = AccentCyan), border = BorderStroke(2.dp, AccentCyan)) {
        Text(text, fontFamily = Consolas, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}
