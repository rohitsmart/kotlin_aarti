package com.rohit.aarti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
                .weight(1f) // Occupies remaining space for image display
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        change.consume()
                        if (dragAmount > 0) { // Swipe right to go to the previous image
                            if (currentIndex > 0) {
                                currentIndex--
                            }
                        } else { // Swipe left to go to the next image
                            if (currentIndex < imageList.size - 1) {
                                currentIndex++
                            }
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            val image = painterResource(id = imageList[currentIndex])
            DisplayImage(image)
        }

        FooterSection()
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
        val logo = painterResource(id = R.drawable.ic_logo) // Replace with your logo drawable
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
            .fillMaxSize(), // Makes the image fill the entire available space
        contentScale = ContentScale.Crop // Ensures the image fills the screen proportionately
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
        // Placeholder for footer items
        Text(text = "Home", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Settings", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Profile", style = MaterialTheme.typography.bodyLarge)
    }
}
