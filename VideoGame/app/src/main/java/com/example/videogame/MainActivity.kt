package com.example.videogame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.AppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WelcomeScreen()
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen() {
    var initGame by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "APUESTA Y PIERDE",
                modifier = Modifier.padding(bottom = 40.dp)
            )
            if(initGame) {
                TicTacToeGame()
            }
            else {
                Button(onClick = { initGame = true }) {
                    Text("Slay")
                }
            }
        }
    }
}

@Composable
fun TicTacToeGame() {
    var boardState by remember { mutableStateOf(Array(9) { "" }) }
    var playerTurn by remember { mutableStateOf(true) }
    var statusText by remember { mutableStateOf("Your turn (X)") }
    var isGameFinished by remember { mutableStateOf(false) }

    fun checkForWinner(): String? {
        val winPositions = listOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8),
            listOf(2, 4, 6)
        )

        for (positions in winPositions) {
            val a = boardState[positions[0]]
            val b = boardState[positions[1]]
            val c = boardState[positions[2]]

            if (a != "" && a == b && b == c) {
                return a
            }
        }
        return null
    }

    fun makeAIMove() {
        if (isGameFinished) return

        val emptyCells = boardState.mapIndexed { index, value -> if (value == "") index else -1 }
            .filter { it != -1 }

        if (emptyCells.isNotEmpty()) {
            val chosenSpot = emptyCells.random()
            val newBoard = boardState.copyOf()
            newBoard[chosenSpot] = "O"
            boardState = newBoard

            val winner = checkForWinner()
            if (winner != null) {
                statusText = "$winner wins!"
                isGameFinished = true
            } else if (!boardState.contains("")) {
                statusText = "It's a tie!"
                isGameFinished = true
            } else {
                statusText = "Your turn (X)"
                playerTurn = true
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = statusText,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium
        )

        for (row in 0 until 3) {
            Row {
                for (col in 0 until 3) {
                    val index = row * 3 + col
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(2.dp, MaterialTheme.colorScheme.outline)
                            .clickable(enabled = playerTurn && !isGameFinished && boardState[index] == "") {
                                val newBoard = boardState.copyOf()
                                newBoard[index] = "X"
                                boardState = newBoard

                                val winner = checkForWinner()
                                if (winner != null) {
                                    statusText = "You win!"
                                    isGameFinished = true
                                } else if (!boardState.contains("")) {
                                    statusText = "It's a tie!"
                                    isGameFinished = true
                                } else {
                                    statusText = "AI's turn (O)"
                                    playerTurn = false
                                    makeAIMove()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = boardState[index],
                            color = when (boardState[index]) {
                                "X" -> MaterialTheme.colorScheme.primary
                                "O" -> MaterialTheme.colorScheme.secondary
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                boardState = Array(9) { "" }
                playerTurn = true
                statusText = "Your turn (X)"
                isGameFinished = false
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                "Restart Game",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        WelcomeScreen()
    }
}