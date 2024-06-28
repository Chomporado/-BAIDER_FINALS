package com.example.myapplication


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CenteredJumbleWordQuizGame()
                }
            }
        }
    }
}

@Composable
fun CenteredJumbleWordQuizGame() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        JumbleWordQuizGame()
    }
}

@Composable
fun JumbleWordQuizGame() {
    var currentRound by remember { mutableStateOf(1) }
    var score by remember { mutableStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    var currentWordIndex by remember { mutableStateOf(0) }
    val words = listOf(
        Word("IGUANA", R.drawable.iguana),
        Word("APPLE", R.drawable.apple),
        Word("BANANA", R.drawable.banana),
        Word("ORANGE", R.drawable.orange),
        Word("LEMON", R.drawable.lemon)
    )
    val shuffledWord = remember(currentWordIndex) { words[currentWordIndex].scrambledWord }
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Round $currentRound / 5",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .border(2.dp, Color.Black)
        ) {
            Image(
                painter = painterResource(id = words[currentWordIndex].imageRes),
                contentDescription = "Word Image",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
        Text(
            text = "Unscramble the word:",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = shuffledWord,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("Enter your guess") },
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Button(
            onClick = {
                if (userInput.equals(words[currentWordIndex].word, ignoreCase = true)) {
                    score++
                }
                if (currentRound < 5) {
                    currentRound++
                    currentWordIndex = (currentWordIndex + 1) % words.size
                    userInput = ""
                } else {
                    // Game Over
                    // Show final score
                    showDialog.value = true
                }
            }
        ) {
            Text("Submit")
        }
        Text(
            text = "Score: $score",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
        if (showDialog.value) {
            GameOverDialog(score = score)
        }
    }
}

@Composable
fun GameOverDialog(score: Int) {
    Dialog(onDismissRequest = {}) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .padding(16.dp)
                .border(5.dp, Color.Black)
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Game Over!",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Your final score: $score",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = {
                        // Restart the game
                        // Reset score and round
                    }
                ) {
                    Text("Restart")
                }
            }
        }
    }
}



data class Word(val word: String, val imageRes: Int) {
    val scrambledWord: String
        get() = word.toCharArray().toList().shuffled().joinToString("")
}
