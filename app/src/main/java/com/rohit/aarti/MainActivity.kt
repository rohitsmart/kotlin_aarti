package com.rohit.aarti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rohit.aarti.ui.theme.MyApplicationTheme
import com.rohit.aarti.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AppScaffold()
            }
        }
    }
}

@Composable
fun AppScaffold() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { Footer() }
    ) { innerPadding ->
        MainScreen(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Section with Image Placeholder
        Image(
            painter = painterResource(id = R.drawable.image_placeholder), // Placeholder image
            contentDescription = "Top Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Middle Section with Icons and Carousel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconColumn()
            ImageCarousel()
        }

        // Animated Aarti Icon
        AnimatedAartiIcon()
    }
}

@Composable
fun IconColumn() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = { /* Handle bell icon click */ }) {
            Icon(
                painter = painterResource(id = R.drawable.icon_bell), // Bell icon placeholder
                contentDescription = "Bell Icon",
                tint = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconButton(onClick = { /* Handle aarti icon click */ }) {
            Icon(
                painter = painterResource(id = R.drawable.icon_aarti), // Aarti icon placeholder
                contentDescription = "Aarti Icon",
                tint = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconButton(onClick = { /* Handle conch shell icon click */ }) {
            Icon(
                painter = painterResource(id = R.drawable.icon_conch), // Conch shell icon placeholder
                contentDescription = "Conch Shell Icon",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun ImageCarousel() {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(5) { index ->
            Image(
                painter = painterResource(id = R.drawable.image_placeholder), // Placeholder for carousel images
                contentDescription = "Carousel Image $index",
                modifier = Modifier
                    .size(100.dp)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun AnimatedAartiIcon() {
    var isGlowing by remember { mutableStateOf(false) }

    IconButton(onClick = { isGlowing = !isGlowing }) {
        Icon(
            painter = painterResource(id = R.drawable.icon_aarti), // Glowing Aarti icon placeholder
            contentDescription = "Glowing Aarti Icon",
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp),
            tint = if (isGlowing) Color.Yellow else Color.Gray
        )
    }
}

@Composable
fun Footer() {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary // Use containerColor instead of backgroundColor
    ) {
        Spacer(Modifier.weight(1f, true))
        Text(
            text = "Settings",
            color = Color.White,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        AppScaffold()
    }
}
