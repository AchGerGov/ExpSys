package com.example.hardwareexpert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.hardwareexpert.data.AppDatabase
import com.example.hardwareexpert.data.HistoryEntity
import com.example.hardwareexpert.logic.Answer
import com.example.hardwareexpert.logic.ExpertSystem
import com.example.hardwareexpert.ui.*
import kotlinx.coroutines.launch

sealed interface Screen {
    data object DeviceSelection : Screen
    data class Home(val device: String) : Screen
    data class Result(val answers: List<Answer>) : Screen
    data object History : Screen
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val expert = ExpertSystem(applicationContext)
        val dao = AppDatabase.get(applicationContext).historyDao()

        setContent {
            HardwareExpertTheme {
                var screen by remember { mutableStateOf<Screen>(Screen.DeviceSelection) }
                val scope = rememberCoroutineScope()

                Scaffold(
                    containerColor = NavyBlue,
                    topBar = {
                        TopAppBar(
                            title = { Text("Hardware Expert", fontFamily = Consolas) },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = NavyDeep, titleContentColor = AccentCyan)
                        )
                    }
                ) { padding ->
                    Box(Modifier.padding(padding).fillMaxSize().background(NavyBlue)) {
                        when (val s = screen) {
                            Screen.DeviceSelection -> DeviceSelectionScreen(
                                onDeviceSelected = { device -> screen = Screen.Home(device) },
                                onHistory = { screen = Screen.History }
                            )
                            is Screen.Home -> HomeScreen(
                                expert = expert,
                                deviceType = s.device,
                                onDiagnose = { brand, symptoms ->
                                    scope.launch {
                                        val answers = expert.diagnose(s.device, brand, symptoms)
                                        dao.insert(HistoryEntity(
                                            query = symptoms.joinToString(", "),
                                            cause = answers.firstOrNull()?.cause ?: "Не определено",
                                            advice = answers.joinToString("\n\n") { it.advice },
                                            source = "База знаний"
                                        ))
                                        screen = Screen.Result(answers)
                                    }
                                },
                                onBack = { screen = Screen.DeviceSelection }
                            )
                            is Screen.Result -> ResultScreen(
                                answers = s.answers,
                                onBack = { screen = Screen.DeviceSelection }
                            )
                            Screen.History -> HistoryScreen(
                                dao = dao,
                                onBack = { screen = Screen.DeviceSelection }
                            )
                        }
                    }
                }
            }
        }
    }
}
