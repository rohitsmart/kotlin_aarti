package com.rohit.aarti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rohit.aarti.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ImageGalleryScreen()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ImageGalleryScreen() {
    val imageList = listOf(
        R.drawable.durga,
        R.drawable.hanuman,
        R.drawable.kirshan,
        R.drawable.vishwakarma
    )

    var currentIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HeaderSection()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        change.consume()
                        if (dragAmount > 0) { // Swipe right to go to the previous image
                            currentIndex = (currentIndex - 1 + imageList.size) % imageList.size
                        } else { // Swipe left to go to the next image
                            currentIndex = (currentIndex + 1) % imageList.size
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            AnimatedImage(imageList[currentIndex])
        }

        FooterSection()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedImage(imageRes: Int) {
    AnimatedContent(
        targetState = imageRes,
        transitionSpec = {
            // Slide in/out animation for smooth transitions
            slideInHorizontally(
                animationSpec = tween(durationMillis = 500)
            ) togetherWith slideOutHorizontally(
                animationSpec = tween(durationMillis = 500)
            )
        }, label = ""
    ) { targetImage ->
        val painter = painterResource(id = targetImage)
        DisplayImage(painter)
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val logo = painterResource(id = R.drawable.ic_logo)
        Image(
            painter = logo,
            contentDescription = "Logo",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Har Har", style = MaterialTheme.typography.headlineLarge)
    }
}

@Composable
fun DisplayImage(image: Painter) {
    Image(
        painter = image,
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun FooterSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = "Home", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Settings", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Profile", style = MaterialTheme.typography.bodyLarge)
    }
}
