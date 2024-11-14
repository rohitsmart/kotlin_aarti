package com.rohit.aarti

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.updateTransition
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.rohit.aarti.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

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
    var currentIndex by remember { mutableIntStateOf(0) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    val transition = updateTransition(targetState = currentIndex, label = "")
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HeaderSection()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX > 300f) {
                                    currentIndex = (currentIndex - 1 + imageList.size) % imageList.size
                                } else if (offsetX < -300f) {
                                    currentIndex = (currentIndex + 1) % imageList.size
                                }
                                offsetX = 0f
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount
                    }
                }
        ) {
            transition.AnimatedVisibility(
                visible = { true },
                modifier = Modifier.offset { IntOffset(offsetX.toInt(), 0) }
            ) {
                AnimatedImage(imageList[currentIndex], offsetX)
            }
            FixedImage()
            VerticalIcons(
                startPadding = 10.dp,
                bottomPadding = 30.dp,
                iconSpacing = 20.dp,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
        FooterSection()
    }
}

@Composable
fun VerticalIcons(
    startPadding: Dp = 16.dp,
    bottomPadding: Dp = 26.dp,
    iconSpacing: Dp = 16.dp,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(start = startPadding, bottom = bottomPadding)
            .wrapContentHeight(align = Alignment.Bottom),
        verticalArrangement = Arrangement.spacedBy(iconSpacing),
        horizontalAlignment = Alignment.Start
    ) {
        IconImage(R.drawable.ic_music, "Music Icon")
        IconImage(R.drawable.ic_conch_shell, "Conch Shell Icon")
        IconImage(R.drawable.ic_bell, "Bell Icon")
    }
}

@Composable
fun AnimatedImage(imageRes: Int, offsetX: Float) {
    val painter = painterResource(id = imageRes)
    DisplayImage(painter, offsetX)
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
fun DisplayImage(image: Painter, offsetX: Float) {
    Image(
        painter = image,
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.toInt(), 0) },
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

@Composable
fun FixedImage() {
    val aartiImage = painterResource(id = R.drawable.aarti)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp)
    ) {
        Image(
            painter = aartiImage,
            contentDescription = "Aarti Image",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(100.dp)
                .height(100.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun IconImage(iconRes: Int, description: String) {
    val iconPainter = painterResource(id = iconRes)
    Image(
        painter = iconPainter,
        contentDescription = description,
        modifier = Modifier.size(40.dp),
        contentScale = ContentScale.Fit
    )
}