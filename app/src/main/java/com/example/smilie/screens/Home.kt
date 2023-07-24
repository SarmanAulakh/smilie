package com.example.smilie.screens

import android.icu.text.CaseMap.Title
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.smilie.model.User
import com.google.android.gms.common.util.AndroidUtilsLight
import io.grpc.util.OutlierDetectionLoadBalancer.OutlierDetectionLoadBalancerConfig.FailurePercentageEjection
import java.lang.Math.PI
import java.util.*
import kotlin.math.roundToInt

// Using accessible colors (https://venngage.com/tools/accessible-color-palette-generator)
val colorList = listOf(
    Color(0xFF90d8b2),
    Color(0xFF8dd2dd),
    Color(0xFF8babf1),
    Color(0xFF8b95f6),
    Color(0xFF9b8bf4),
    Color(0xFFf8b8d0),
    Color(0xFFf194b8)
)

@Composable
fun HomeScreen(modifier: Modifier = Modifier, user: User?) {

    if (user == null) {
        println("loading....")
    } else {

        val userName = user.username
        var barToggle by remember { mutableStateOf(false) }

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
                        item { Home(userName, 7.8f) }

                        val helpfulLinks = listOf(
                            "betterhelp.org",
                            "LeagueofLeg.com"
                        )
                        val metricText = listOf(
                            "sleep",
                            "time spent with friends",
                            "productivity",
                        )

                        item { FoldableCards(input= metricText, title = "Recommended Metrics") }
                        item { FoldableCards(input = helpfulLinks, title = "Helpful Links") }

                        val metricData = listOf(
                            Metric("Amount of Sleep", 8f),
                            Metric("Quality of Sleep", 3f),
                            Metric("Time spent with Friends", 6f),
                            Metric("Productivity", 4f),
                            Metric("Exercise", 7f),
                            Metric("Entertainment", 9f),
                            Metric("Time spent Studying", 2f),
                        )

                        item { Title("$userName's Data") }

                        items(metricData) { metric ->
                            Box(
                                modifier = Modifier.padding(start = 0.dp)
                            ) {
                                Column {
                                    Text(
                                        text = metric.name,
                                        style = TextStyle(
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(8.dp)
                                    )
                                    Rectangle(metric.value)
                                }
                            }
                        }


                        item {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Switch(
                                    checked = barToggle,
                                    onCheckedChange = {
                                        barToggle = it
                                    }
                                )
                                Text(
                                    text = "Toggle Graph Type",
                                    style = TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(20.dp)
                                )
                            }
                        }

                        if (barToggle) {
                            item {
                                PieChart(
                                    modifier = Modifier
                                        .size(400.dp),
                                    input = metricData
                                )
                            }
                        } else {
                            item {
                                BarGraph(
                                    modifier = Modifier
                                        .size(500.dp),
                                    input = metricData
                                )
                            }
                        }

                        // Buffer to show things hidden from the task bar
                        item {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                            ) {
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Rectangle(value: Float) {
    val height: Dp = 25.dp
    val width = (value / 10f)
    Row() {
        LinearProgressIndicator(
            progress = width,
            modifier = Modifier
                .weight(10f)
                .height(height)
                .fillMaxWidth(0.75f),
        )
        Text(
            text = value.toString(),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = 25.dp)
        )
    }
}

@Composable
fun Title(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally)
    )
}

@Composable
fun Home(name: String, value: Float) {
    Column(modifier = Modifier.padding(16.dp)) {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = getGreeting(currentHour)

        val textData = listOf(
            TextType("$greeting, $name!", 2),
            TextType("Welcome back!", 2),
            TextType("Over the last week, you've average a ${value.toString()}/10 !", 1)
        )

        for (textval in textData) {
            Text(
                text = textval.text,
                style = TextStyle(
                    fontSize = (18 * textval.type).sp,
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
    input: List<String>,
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

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    radius:Float = 500f,
    input:List<Metric>,
) {
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    var inputList by remember {
        mutableStateOf(input)
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(true) {
                }
        ) {
            val width = size.width
            val height = size.height
            circleCenter = Offset(x = width / 2f, y = height / 2f)
            val totalValue = input.sumOf { (it.value).roundToInt() }
            val anglePerValue = 360f/totalValue
            var currentStartAngle = 0f
            inputList.forEachIndexed {  index, pieChartInput ->
                val scale = 1.0f
                val angleToDraw = pieChartInput.value * anglePerValue
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
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        inputList.forEachIndexed { index, pieChartInput ->
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
        }
    }
}

@Composable
fun BarGraph(
    modifier: Modifier = Modifier,
    input:List<Metric>,
) {
    val maxValue = input.maxOf { it.value }
    Column(
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
//            val listSum by remember { mutableStateOf(input.sumOf { it.value }) }
            input.forEachIndexed {index, chartInput ->
                val percentage = chartInput.value / maxValue.toFloat()
                Bar(
                    modifier = Modifier
                        .height((60 * percentage * input.size).dp)
                        .width(40.dp),
                    primaryColor = colorList[index],
                    percentage = percentage,
                    description = chartInput.name,
                    value = chartInput.value
                )
            }
        }
    }

    Spacer(modifier = Modifier.width(30.dp))
}


// Referenced from K Apps YouTube Video
// https://youtu.be/AxTUSBg1tRg
@Composable
fun Bar(
    modifier: Modifier = Modifier,
    primaryColor: Color,
    percentage: Float,
    description: String,
    value: Float
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
            val barHeight = height/8 * 7

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
                        color = primaryColor.toArgb()
                        textSize = 14.dp.toPx()
                        isFakeBoldText = true
                    }
                )
            }

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "$description",
                    0f,
                    5f,
                    android.graphics.Paint().apply {
                        color = primaryColor.toArgb()
                        textSize = 14.dp.toPx()
                        isFakeBoldText = true
                    }
                )
            }
        }
    }
}

private fun getGreeting(hour: Int): String {
    return when (hour) {
        in 0..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }
}

private fun handleMetricClick(metric: TextType) {
    // Handle the metric click here
    println("Selected metric: ${metric.text}")
}
data class Metric(val name: String, val value: Float)

// text: text to display, type: determines fontsize
data class TextType(val text: String, val type: Int)
