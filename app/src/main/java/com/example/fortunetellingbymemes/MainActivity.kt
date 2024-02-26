package com.example.fortunetellingbymemes

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fortunetellingbymemes.data.MemesRepository
import com.example.fortunetellingbymemes.model.Memes
import com.example.fortunetellingbymemes.ui.theme.FortuneTellingByMemesTheme
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FortuneTellingByMemesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val memesRepository = MemesRepository()

    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    var showMemesScreen by rememberSaveable { mutableStateOf(false) }
    var data: Memes? by rememberSaveable { mutableStateOf(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            query = text,
            onQueryChange = { text = it},
            onSearch = { active = false},
            active = active,
            onActiveChange = { active = it},
            placeholder = { Text("Введите запрос") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        ) {

        }
        Button(
            modifier = Modifier.padding(20.dp),
            enabled = !showMemesScreen,
            onClick = {
                scope.launch {
                    data= memesRepository.getMemes()?.data
                    showMemesScreen = true
                }
            }) {
            Text(text = "Получить предсказание")
        }

        if (showMemesScreen && data != null) {
            MemesShowScreen(data!!) {
                showMemesScreen = it
            }
        }
    }
}

@Composable
fun MemesShowScreen(
    data: Memes,
    onShowMemesScreenChange: (Boolean) -> Unit
) {
    var currentMemeIndex by rememberSaveable { mutableIntStateOf((0..99).random()) }
    val imageUrl = remember { mutableStateOf(data.memes[currentMemeIndex].url) }

    val alpha = remember { Animatable(1f) }

    LaunchedEffect(imageUrl.value) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.Top,

    ) {
        GlideImage(
            modifier = Modifier
                .height(450.dp)
                .fillMaxWidth()
                .padding(vertical = 40.dp)
                .graphicsLayer(alpha = alpha.value),
            imageModel = { imageUrl.value },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
        )
        Row(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                onClick = { onShowMemesScreenChange(false) },
            ) {
                Image(
                    painter = painterResource(id = R.drawable.thumb_up_ibrofz5ox3so),
                    contentDescription = null,
                    contentScale = ContentScale.Inside
                )
            }
            Button(onClick = {
                val newIndex = (0..99).random()
                imageUrl.value = data.memes[newIndex].url
                currentMemeIndex = newIndex
            }) {
                Image(
                    painter = painterResource(id = R.drawable.hands_urv68l8h14s6),
                    contentDescription = null,
                    contentScale = ContentScale.Inside
                )
            }
        }
    }
}
