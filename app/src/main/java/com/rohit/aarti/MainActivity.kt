package com.rohit.aarti

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.rohit.aarti.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
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
fun IconImage(
    iconRes: Int,
    contentDescription: String,
    size: Dp,
    onClick: () -> Unit,
    backgroundColor: Color = Color(0xFF011A2C),
    cornerRadius: Dp = 8.dp,
    iconPadding: Dp = 8.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size)
            .background(backgroundColor, shape = RoundedCornerShape(cornerRadius))
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(size - iconPadding),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun FallingFlowersEffect(durationMillis: Int = 10000) {
    val flowers = remember { mutableStateListOf<FallingFlowerData>() }
    val flowerResources = listOf(R.drawable.ic_flower, R.drawable.ic_rose, R.drawable.ic_blue_rose)

    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() - startTime < durationMillis) {
            val randomFlowerResource = flowerResources.random()
            flowers.add(FallingFlowerData(resourceId = randomFlowerResource))
            delay(300)
        }
    }

    flowers.forEach { flowerData ->
        FallingFlower(flowerData) { flowers.remove(flowerData) }
    }
}

@Composable
fun FallingFlower(flowerData: FallingFlowerData, onReachedBottom: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "")
    val yOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = flowerData.fallDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels.dp
    val flowerY = yOffset * screenHeight
    if (flowerY >= screenHeight) onReachedBottom()
    Image(
        painter = painterResource(id = flowerData.resourceId),
        contentDescription = "Falling Flower",
        modifier = Modifier
            .offset(x = flowerData.startX.dp, y = flowerY)
            .size(40.dp),
        contentScale = ContentScale.Fit
    )
}

data class FallingFlowerData(
    val startX: Float = (0..300).random().toFloat(),
    val fallDuration: Int = (3000..5000).random(),
    val resourceId: Int
)

@Composable
fun VerticalIcons(
    startPadding: Dp = 16.dp,
    bottomPadding: Dp = 50.dp,
    iconSpacing: Dp = 16.dp,
    iconSize: Dp = 60.dp,
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
        listOf(
            "music" to R.drawable.ic_music,
            "conch_shell" to R.drawable.ic_conch_shell,
            "bell" to R.drawable.ic_bell,
            "flower" to R.drawable.ic_flower
        ).forEach { (iconType, iconRes) ->
            IconImage(
                iconRes = iconRes,
                contentDescription = "$iconType Icon",
                size = iconSize,
                onClick = { onIconTapped(iconType) },
                backgroundColor = Color(0xFFEBF0F5),
                cornerRadius = 8.dp
            )
        }
    }
}

@Composable
fun ImageGalleryScreen() {
    val imageSoundMap = mapOf(
        R.drawable.durga to R.raw.aarti_durga_maa_ki,
        R.drawable.hanuman to R.raw.shri_hanuman_chalisa,
        R.drawable.kirshan to R.raw.aarti_sri_ram_ji_ki,
        R.drawable.vishwakarma to R.raw.shree_vishwakarma_aarti,
        R.drawable.shiv to R.raw.omshiv,
        R.drawable.ganesh to R.raw.ganpati,
        R.drawable.ambe to R.raw.gauri,
        R.drawable.lilkrishna to R.raw.aartibihari,
        R.drawable.laxmi to R.raw.laxmi,
        R.drawable.saraswati to R.raw.saraswati,
        R.drawable.karwaimage to R.raw.karwa,
    )
    val imageList = imageSoundMap.keys.toList()
    var currentIndex by remember { mutableIntStateOf(0) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()
    var isPlaying by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var showFlowerEffect by remember { mutableStateOf(false) }
    var flowerEffectStartTime by remember { mutableLongStateOf(0L) }

    // Map to manage MediaPlayer instances
    val mediaPlayers = remember { mutableMapOf<String, MediaPlayer>() }

    fun playSound(sound: String) {
        mediaPlayers[sound]?.apply {
            if (isPlaying) {
                stop()
                reset()
            }
            release()
        }

        val newMediaPlayer = when (sound) {
            "bell" -> MediaPlayer.create(context, R.raw.bell_sound)
            "conch_shell" -> MediaPlayer.create(context, R.raw.conch_sound)
            "music" -> imageSoundMap[imageList[currentIndex]]?.let { MediaPlayer.create(context, it) }
            else -> null
        }

        newMediaPlayer?.apply {
            mediaPlayers[sound] = this
            start()
            isPlaying = true

            if (sound in listOf("bell", "conch_shell")) {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (isPlaying) {
                        stop()
                        reset()
                    }
                    release()
                    isPlaying = false
                    mediaPlayers.remove(sound)
                }, 30000)
            }
        }
    }

    fun toggleSound(sound: String) {
        val currentPlayer = mediaPlayers[sound]
        if (currentPlayer?.isPlaying == true) {
            currentPlayer.pause()
            isPlaying = false
        } else {
            playSound(sound)
        }
    }

    fun handleIconTapped(iconType: String) {
        if (iconType == "flower") {
            showFlowerEffect = !showFlowerEffect
            flowerEffectStartTime = if (showFlowerEffect) System.currentTimeMillis() else 0L
        } else {
            toggleSound(iconType)
        }
    }

    LaunchedEffect(showFlowerEffect) {
        if (showFlowerEffect) {
            delay(20000)
            showFlowerEffect = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderSection()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                // Update index circularly with both directions in mind
                                currentIndex = if (offsetX > 300f) {
                                    (currentIndex - 1 + imageList.size) % imageList.size
                                } else {
                                    (currentIndex + 1) % imageList.size
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
            AnimatedImage(imageList[currentIndex], offsetX)
            FixedImage(isPlaying)
            VerticalIcons(
                startPadding = 10.dp,
                bottomPadding = 150.dp,
                iconSpacing = 20.dp,
                iconSize = 70.dp,
                modifier = Modifier.align(Alignment.BottomStart),
                onIconTapped = ::handleIconTapped
            )
            if (showFlowerEffect) {
                FallingFlowersEffect(durationMillis = 20000)
            }
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

@SuppressLint("UnrememberedMutableState")
@Composable
fun FixedImage(isPlaying: Boolean) {
    val aartiImage = painterResource(id = R.drawable.aarti)
    val transition = rememberInfiniteTransition()
    val xOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val yOffset by derivedStateOf { 15f * kotlin.math.sin((xOffset / 30f) * kotlin.math.PI).toFloat() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp)
    ) {
        Image(
            painter = aartiImage,
            contentDescription = "Aarti Image",
            modifier = Modifier
                .align(Alignment.Center)
                .offset(
                    x = if (isPlaying) xOffset.dp else 0.dp,
                    y = if (isPlaying) yOffset.dp else 0.dp
                )
                .width(150.dp)
                .height(150.dp),
            contentScale = ContentScale.Fit
        )
    }
}