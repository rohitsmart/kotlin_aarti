package com.rohit.aarti

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun IconImage(iconRes: Int, contentDescription: String, size: Dp = 36.dp, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = iconRes),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(size)
            .clickable { onClick() }
    )
}

@Composable
fun VerticalIcons(
    startPadding: Dp = 16.dp,
    bottomPadding: Dp = 50.dp,
    iconSpacing: Dp = 16.dp,
    iconSize: Dp = 48.dp,
    onIconTapped: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(start = startPadding, bottom = bottomPadding)
            .wrapContentHeight(align = Alignment.Bottom),
        verticalArrangement = Arrangement.spacedBy(iconSpacing),
        horizontalAlignment = Alignment.Start
    ) {
        IconImage(R.drawable.ic_music, "Music Icon", size = iconSize) { onIconTapped("music") }
        IconImage(R.drawable.ic_conch_shell, "Conch Shell Icon", size = iconSize) { onIconTapped("conch_shell") }
        IconImage(R.drawable.ic_bell, "Bell Icon", size = iconSize) { onIconTapped("bell") }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ImageGalleryScreen() {
    val imageSoundMap = mapOf(
        R.drawable.durga to R.raw.aarti_durga_maa_ki,
        R.drawable.hanuman to R.raw.shri_hanuman_chalisa,
        R.drawable.kirshan to R.raw.aarti_sri_ram_ji_ki,
        R.drawable.vishwakarma to R.raw.shree_vishwakarma_aarti
    )
    val imageList = imageSoundMap.keys.toList()

    var currentIndex by remember { mutableIntStateOf(0) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    val transition = updateTransition(targetState = currentIndex, label = "")
    val coroutineScope = rememberCoroutineScope()

    var isPlaying by remember { mutableStateOf(false) }
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    fun playSound(sound: String) {
        if (isPlaying) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }

        mediaPlayer = when (sound) {
            "bell" -> MediaPlayer.create(context, R.raw.bell_sound)
            "conch_shell" -> MediaPlayer.create(context, R.raw.conch_sound)
            "music" -> {
                val currentImageRes = imageList[currentIndex]
                val soundRes = imageSoundMap[currentImageRes]
                soundRes?.let {
                    MediaPlayer.create(context, it)
                }
            }
            else -> null
        }

        mediaPlayer?.apply {
            start()
            isPlaying = true

            Handler(Looper.getMainLooper()).postDelayed({
                stop()
                isPlaying = false
            }, 30000)
        }
    }

    fun toggleSound(sound: String) {
        if (isPlaying) {
            mediaPlayer?.pause()
            isPlaying = false
        } else {
            playSound(sound)
        }
    }

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
                bottomPadding = 100.dp,
                iconSpacing = 20.dp,
                iconSize = 58.dp,
                modifier = Modifier.align(Alignment.BottomStart),
                onIconTapped = { sound ->
                    toggleSound(sound)
                }
            )
        }
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
