package com.example.smilie.screens

import android.net.Uri
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.smilie.model.User
import java.util.*
import com.example.smilie.model.Metric
import com.example.smilie.model.view.HomeViewModel
import kotlin.collections.ArrayList
import com.example.smilie.screens.settings.SettingsManager



// Using accessible colors (https://venngage.com/tools/accessible-color-palette-generator)
private val colorList = listOf(
    Color(0xFF90d8b2),
    Color(0xFF8dd2dd),
    Color(0xFF8babf1),
    Color(0xFF8b95f6),
    Color(0xFF9b8bf4),
    Color(0xFFf8b8d0),
    Color(0xFFf194b8),
    Color(0xFFFAAF90),
    Color(0xFFFCC9B5),
    Color(0xFFD9E4FF),
    Color(0xFFB3C7F7)
)

@Composable
fun HomeScreen(
    user: User?,
    metrics: ArrayList<Metric>?,
    allUsers: ArrayList<User>?,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    if (user == null || user.equals(null)) {
        println("loading....")
    } else {

        val userName = user.username

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            MaterialTheme {
                Column {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
                        var overallAvg = 0f
                        var count = 0f

                        metrics?.forEach {
                            if(it.name == "Overall") {
                                it.values.forEach {itit ->
                                    overallAvg += itit.value.toFloat()
                                    count += 1f
                                }
                            }
                        }
                        item { Home(userName, overallAvg/count) }

                        if (allUsers != null) {
                            var nonFriendList: List<User> = if (user.following == null) allUsers else allUsers.filter { !user.following.contains(it.id) }

                            item { Title("Recommended People to Follow") }

                            itemsIndexed(nonFriendList) { _, u ->
                                Recommendations(user =u, addFollowing = homeViewModel::addFollowing)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Title(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally)
    )
}

@Composable
fun Home(name: String, value: Float) {
    Column(modifier = Modifier.padding(16.dp)) {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = getGreeting(currentHour)
        val response = generateResponse(value)

        val textData = listOf(
            TextType("$greeting, $name!", 36),
            TextType("Welcome back!", 36),
            TextType("Over the last week, you've average a ${value.toInt()}/10 !", 24),
            TextType("$response", 24)
        )

        for (textval in textData) {
            Text(
                text = textval.text,
                style = TextStyle(
                    fontSize = (textval.type).sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
                    .wrapContentWidth(align = Alignment.Start)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldableCards(
    input: List<String?>,
    title: String
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if(expandedState) 180f else 0f
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .padding(
                top = 24.dp,
                end = 12.dp
            ),
        shape = RoundedCornerShape(4.dp),
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier
                        .weight(6f)
                        .padding(
                            top = 10.dp,
                            bottom = 10.dp
                        ),
                    text = title,
                    style = TextStyle(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Left
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .alpha(10f)
                        .weight(1f)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }
                ) {
                    Icon(
                       imageVector = Icons.Default.ArrowDropDown,
                       contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if(expandedState) {
                for (item in input) {
                    if (item != null) {
                        Text(
                            modifier = Modifier
                                .padding(
                                    start = 18.dp,
                                    top = 5.dp,
                                    bottom = 5.dp
                                ),
                            text = item,
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    radius:Float = 500f,
    input:ArrayList<Metric>?,
) {
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val width = size.width
            val height = size.height
            circleCenter = Offset(x = width / 2f, y = height / 2f)
//            val totalValue = input.sumOf { (it.value).roundToInt() }
            var totalValue:Float = 0f
            input?.forEach {
                if (it.active && it.values.size > 0 && it.name != "Overall") {
                    totalValue += it.values.last().value.toFloat()
                }
            }

            val anglePerValue = 360f/totalValue
            var currentStartAngle = 0f
            var index = 0
            input?.forEach { pieChartInput ->
                if (pieChartInput.active && pieChartInput.name != "Overall") {
                    val scale = 1.0f
                    val last = if (pieChartInput.values.size > 0) pieChartInput.values.last().value.toFloat() else 0.0f;
                    val angleToDraw = last * anglePerValue
                    scale(scale){
                        drawArc(
                            color = colorList[index],
                            startAngle = currentStartAngle,
                            sweepAngle = angleToDraw,
                            useCenter = true,
                            size = Size(
                                width = radius*2f,
                                height = radius*2f
                            ),
                            topLeft = Offset(
                                (width - radius * 2f) / 2f,
                                (height - radius * 2f) / 2f
                            )
                        )
                        currentStartAngle += angleToDraw
                    }
                    index += 1
                }
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        var index = 0
        input?.forEach { pieChartInput ->
            if (pieChartInput.active && pieChartInput.name != "Overall") {
                Row(){
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(colorList[index])
                            .align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = pieChartInput.name,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Left
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                }
                index += 1
            }

        }
    }
}

@Composable
fun BarGraph(
    modifier: Modifier = Modifier,
    input:ArrayList<Metric>?,
    settingsManager: SettingsManager
) {

    if (input != null && input.size > 0) {
        var maxValue:Float = 0f
        input?.forEach {
            if (it.active && it.values.size > 0 && it.values.last().value.toFloat() > maxValue) {
                maxValue = it.values.last().value.toFloat()
            }
        }

        Column(
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
//            val listSum by remember { mutableStateOf(input.sumOf { it.value }) }
                var index = 0
                input?.forEach { chartInput ->
                    if (chartInput.active && chartInput.name != "Overall") {
                        var last = if (chartInput.values.size > 0) chartInput.values.last().value.toFloat() else 0.0f;
                        val percentage =last / maxValue;
                        Bar(
                            modifier = Modifier
                                .height((60 * percentage * input.size).dp)
                                .width(40.dp),
                            primaryColor = colorList[index],
                            percentage = percentage,
                            description = chartInput.name,
                            value = String.format("%.1f",last).toFloat(),
                            index = index,
                            settingsManager = settingsManager
                        )
                        index += 1
                    }
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(top=24.dp)
            ) {
                var index = 0
                input?.forEach { chartInput ->
                    if (chartInput.active && chartInput.name != "Overall") {
                        Row() {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(colorList[index])
                                    .align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = chartInput.name,
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Left
                                ),
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                            )
                        }
                        index += 1
                    }
                }
            }
        }
    }
}


// Referenced from K Apps YouTube Video
// https://youtu.be/AxTUSBg1tRg
@Composable
fun Bar(
    modifier: Modifier = Modifier,
    primaryColor: Color,
    percentage: Float,
    description: String,
    value: Float,
    index: Int,
    settingsManager: SettingsManager
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val width = size.width
            val height = size.height
            val barWidth = width/5 * 3
            val barHeight = (height/8 * 7)

            var path = Path().apply {
                moveTo(0f, height)
                lineTo(barWidth, height)
                lineTo(barWidth, height - barHeight)
                lineTo(0f, height - barHeight)
                close()
            }
            drawPath(
                path,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xAAFFFFFF), primaryColor)
                )
            )

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "$value",
                    barWidth/5f,
                    height + 55f,
                    android.graphics.Paint().apply {

                        color = if (settingsManager.isDark.value) Color(0xFFFFFFFF).toArgb() else Color(0xFF000000).toArgb()
                        textSize = 14.dp.toPx()
                        isFakeBoldText = true
                    }
                )
            }
        }
    }
}

@Composable
fun Recommendations(
    user: User,
    addFollowing: (userId: String) -> Unit,
) {
    var buttonState = remember { mutableStateOf(true) }
    var buttonText = remember { mutableStateOf("Follow") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(50.dp)
            ) {
                AsyncImage(
                    model = Uri.parse(user.imageUrl),
                    contentDescription = "",
                    modifier = Modifier
                        .wrapContentSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Text(text = user.username, modifier = Modifier.padding(horizontal = 12.dp))
        }
        Button(
            // requires backend calls to add a person to the following list
            onClick = {
                buttonState.value = false
                buttonText.value = "Following!"
                addFollowing(user.id)
            },
            enabled = buttonState.value
        ) {
            Text(
                text = buttonText.value,
                fontSize = MaterialTheme.typography.labelLarge.fontSize,
            )
        }
    }
}

private fun generateResponse(input: Float): String {
    if (input <= 3) {
        return "Oof, that's rough.. You got this!!"
    } else if (input <= 7) {
        return "You are doing great! Keep up the work!!"
    } else {
        return "This is AWESOME!! Continue to keep up your lifestyle!!"
    }
}

private fun getGreeting(hour: Int): String {
    return when (hour) {
        in 0..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }
}

// text: text to display, type: determines fontsize
data class TextType(val text: String, val type: Int)
