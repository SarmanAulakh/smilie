package com.example.smilie.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan),
        contentAlignment = Alignment.TopCenter
    ) {
        MaterialTheme {
            Column {
//                Title(text = "User Feedback Page")
//
//                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    item { Home("Dohyun", 7.8) }

                    val metricData = listOf(
                        Metric("Amount of Sleep", 80),
                        Metric("Quality of Sleep", 35),
                        Metric("Time spent with Friends", 60),
                        Metric("Productivity", 40),
                        Metric("Exercise", 70),
                        Metric("Entertainment", 90),
                        Metric("Time spent Studying", 20),
                    )

                    items(metricData) {metric ->
                        Box(
                            modifier = Modifier.padding(start = 0.dp)
                        ) {
                            Column {
                                Text(
                                    text = metric.name,
                                    style = TextStyle(
                                        color = Color.DarkGray,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(8.dp)
                                )
                                Rectangle(metric.value)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Rectangle(value: Int) {
//    val hexcolor = "B200ED"
//    val color = Color(android.graphics.Color.parseColor(hexcolor))
    val color = Color.Magenta
    val height: Dp = 25.dp
    val width = (value / 100f)
    Row() {
        LinearProgressIndicator(
            progress = width,
            modifier = Modifier
                .weight(10f)
                .height(height)
                .fillMaxWidth(0.75f),
            color = color,
        )
        Text(
            text = value.toString(),
            style = TextStyle(
                color = Color.DarkGray,
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
//            .background(color)
//            .padding(20.dp)
//    )
}

@Composable
fun Title(text: String) {
    Text(
        text = text,
        style = TextStyle(
            color = Color.DarkGray,
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
fun Home(name: String, value: Double) {
    Column(modifier = Modifier.padding(16.dp)) {
        val textData = listOf(
            TextType("Hello $name! Welcome back!", 2),
            TextType("Over the last week, you've average a ${value.toString()}/10 !", 1),
            TextType("Keep up the work!", 1)
        )
        for (textval in textData) {
            Text(
                text = textval.text,
                style = TextStyle(
                    color = Color.DarkGray,
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
            TextType("Popular Metrics:", 2),
            TextType("- sleep", 1),
            TextType("- time spent with friends", 1),
            TextType("- productivity", 1),
        )
        for (metric in metricText) {
            Text(
                text = metric.text,
                style = TextStyle(
                    color = Color.DarkGray,
                    fontSize = (15 * metric.type).sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
            )
        }

        val helpfulLinks = listOf(
            TextType("Helpful Links:", 2),
            TextType("- betterhelp.org", 1),
            TextType("- LeagueofLeg.com", 1),
        )
        for (link in helpfulLinks) {
            Text(
                text = link.text,
                style = TextStyle(
                    color = Color.DarkGray,
                    fontSize = (15 * link.type).sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
            )
        }
    }

}
data class Metric(val name: String, val value: Int)

// text: text to display, type: determines fontsize
data class TextType(val text: String, val type: Int)

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen()
}
