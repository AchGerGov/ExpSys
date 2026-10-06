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
    data object Home : Screen
    data class Result(val answer: Answer) : Screen
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
                var screen by remember { mutableStateOf<Screen>(Screen.Home) }
                val scope = rememberCoroutineScope()

                Scaffold(
                    containerColor = NavyBlue,
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    "Hardware Expert",
                                    fontFamily = Consolas
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = NavyDeep,
                                titleContentColor = AccentCyan
                            )
                        )
                    }
                ) { padding ->
                    Box(
                        Modifier
                            .padding(padding)
                            .fillMaxSize()
                            .background(NavyBlue)
                    ) {
                        when (val s = screen) {

                            Screen.Home -> HomeScreen(
                                expert = expert,
                                onDiagnose = { symptoms ->
                                    scope.launch {
                                        val answer = expert.diagnose(symptoms)
                                        dao.insert(
                                            HistoryEntity(
                                                query = symptoms.joinToString(", "),
                                                cause = answer.cause,
                                                advice = answer.advice,
                                                source = answer.source
                                            )
                                        )
                                        screen = Screen.Result(answer)
                                    }
                                },
                                onHistory = { screen = Screen.History }
                            )

                            is Screen.Result -> ResultScreen(
                                answer = s.answer,
                                onBack = { screen = Screen.Home }
                            )

                            Screen.History -> HistoryScreen(
                                dao = dao,
                                onBack = { screen = Screen.Home }
                            )
                        }
                    }
                }
            }
        }
    }
}
