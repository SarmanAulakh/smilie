package com.example.smilie.screens

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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.grpc.util.OutlierDetectionLoadBalancer.OutlierDetectionLoadBalancerConfig.FailurePercentageEjection
import java.lang.Math.PI
import java.util.*

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val userName = "Dohyun"

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
//                    item { Home(userName, 78) }

                    val metricData = listOf(
                        Metric("Amount of Sleep", 80),
                        Metric("Quality of Sleep", 35),
                        Metric("Time spent with Friends", 60),
                        Metric("Productivity", 40),
                        Metric("Exercise", 70),
                        Metric("Entertainment", 90),
                        Metric("Time spent Studying", 20),
                    )

//                    item{ Title("$userName's Data") }

//                    items(metricData) {metric ->
//                        Box(
//                            modifier = Modifier.padding(start = 0.dp)
//                        ) {
//                            Column {
//                                Text(
//                                    text = metric.name,
//                                    style = TextStyle(
//                                        fontSize = 28.sp,
//                                        fontWeight = FontWeight.Bold
//                                    ),
//                                    modifier = Modifier.padding(8.dp)
//                                )
//                                Rectangle(metric.value)
//                            }
//                        }
//                    }

                    val pieChartIns = listOf(
                        ChartInput(value = 29, description = "Python"),
                        ChartInput(value = 21, description = "Swift"),
                        ChartInput(value = 32, description = "JS"),
                        ChartInput(value = 18, description = "Java"),
                        ChartInput(value = 12, description = "Ruby"),
                        ChartInput(value = 38, description = "Kotlin"),
                        ChartInput(value = 10, description = "C++"),
                    )

                    item {
                        PieChart(
                            modifier = Modifier
                                .size(500.dp),
                            input = pieChartIns
                        )
                    }

                    item {
                        BarGraph(modifier = Modifier
                            .size(500.dp),
                            input = pieChartIns
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Rectangle(value: Int) {
    val height: Dp = 25.dp
    val width = (value / 100f)
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
//    Box(
//        modifier = Modifier
//            .wrapContentWidth(align = Alignment.Start)
//            .height(height)
//            .fillMaxWidth(width)
//            .padding(20.dp)
//    )
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
fun Home(name: String, value: Int) {
    Column(modifier = Modifier.padding(16.dp)) {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = getGreeting(currentHour)

        val expandedState = remember { mutableStateOf(false) }
        val anchorPosition = remember { mutableStateOf<Offset?>(null) }

        val textData = listOf(
            TextType("$greeting $name! Welcome back!", 2),
            TextType("Over the last week, you've average a ${value.toString()}/100 !", 1),
            TextType("Keep up the work!", 1)
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
                    .padding(top = 16.dp)
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
            )
        }

        val metricText = listOf(
            TextType("- sleep", 2),
            TextType("- time spent with friends", 2),
            TextType("- productivity", 2),
        )

        Box {
            Button(
                onClick = {
                    expandedState.value = !expandedState.value
                    anchorPosition.value = Offset.Zero
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "Popular Metrics",
                    style = TextStyle(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
        }

        for (metric in metricText) {
            Text(
                text = metric.text,
                style = TextStyle(
                    fontSize = (15 * metric.type).sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
            )
        }

        val helpfulLinks = listOf(
            TextType("- betterhelp.org", 2),
            TextType("- LeagueofLeg.com", 2),
        )

        Box {
            Button(
                onClick = {
                    expandedState.value = !expandedState.value
                    anchorPosition.value = Offset.Zero
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "Helpful Links",
                    style = TextStyle(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
        }

        for (link in helpfulLinks) {
            Text(
                text = link.text,
                style = TextStyle(
                    fontSize = (15 * link.type).sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
            )
        }
    }
}
@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    radius:Float = 500f,
    input:List<ChartInput>,
) {
    val colorList = listOf<Color>(
        Color(0xAAFF0000),
        Color(0xAAFF7F00),
        Color(0xAAFFFF00),
        Color(0xAA00FF00),
        Color(0xAA0000FF),
        Color(0xAA4B0082),
        Color(0xAA9400D3)
    )

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

            val totalValue = input.sumOf { it.value }
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
    Column() {
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
                    text = pieChartInput.description,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Left
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
fun BarGraph(
    modifier: Modifier = Modifier,
    input:List<ChartInput>,
) {
    val colorList = listOf<Color>(
        Color(0xAAFF0000),
        Color(0xAAFF7F00),
        Color(0xAAFFFF00),
        Color(0xAA00FF00),
        Color(0xAA0000FF),
        Color(0xAA4B0082),
        Color(0xAA9400D3)
    )
    val maxValue = input.maxOf { it.value }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val listSum by remember { mutableStateOf(input.sumOf { it.value }) }
        input.forEachIndexed {index, chartInput ->
            val percentage = chartInput.value / listSum.toFloat()
            Bar(
                modifier = Modifier
                    .height((120 * percentage * input.size).dp)
                    .width(40.dp),
                primaryColor = colorList[index],
                percentage = percentage,
                description = chartInput.description
            )
        }
    }
}

@Composable
fun Bar(
    modifier: Modifier = Modifier,
    primaryColor: Color,
    percentage: Float,
    description: String
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
            val barHeight3DPart = height - barHeight
            val barWidth3DPart = (width - barWidth) * (height * 0.002f)

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
                    colors = listOf(Color(0xFFFFFFFF), primaryColor)
                )
            )
            path = Path().apply {
                moveTo(barWidth, height - barHeight)
                lineTo(barWidth3DPart + barWidth, 0f)
                lineTo(barWidth3DPart + barWidth, barHeight)
                lineTo(barWidth, height)
                close()
            }
            drawPath(
                path,
                brush = Brush.linearGradient(
                    colors = listOf(primaryColor, Color(0xFFFFFFFF))
                )
            )
        }
    }
}

data class ChartInput(
    val value:Int,
    val description:String
)

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
data class Metric(val name: String, val value: Int)

// text: text to display, type: determines fontsize
data class TextType(val text: String, val type: Int)

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen()
}
