package com.example.smilie.screens

import android.widget.PopupMenu
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.geometry.Offset
import java.util.*


private val bg_color = Color(0xFFDCD0FF)
private val txt_color = Color(0xFF0D0D0D)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val userName = "Dohyun"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg_color),
        contentAlignment = Alignment.TopCenter
    ) {
        MaterialTheme {
            Column {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    item { Home(userName, 78) }

                    val metricData = listOf(
                        Metric("Amount of Sleep", 80),
                        Metric("Quality of Sleep", 35),
                        Metric("Time spent with Friends", 60),
                        Metric("Productivity", 40),
                        Metric("Exercise", 70),
                        Metric("Entertainment", 90),
                        Metric("Time spent Studying", 20),
                    )

                    item{ Title("$userName's Data") }

                    items(metricData) {metric ->
                        Box(
                            modifier = Modifier.padding(start = 0.dp)
                        ) {
                            Column {
                                Text(
                                    text = metric.name,
                                    style = TextStyle(
                                        color = txt_color,
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
    val progressBarColor = Color(0xFF87CEEB)
    val height: Dp = 25.dp
    val width = (value / 100f)
    Row() {
        LinearProgressIndicator(
            progress = width,
            modifier = Modifier
                .weight(10f)
                .height(height)
                .fillMaxWidth(0.75f),
            color = progressBarColor,
        )
        Text(
            text = value.toString(),
            style = TextStyle(
                color = txt_color,
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
            color = txt_color,
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
                    color = txt_color,
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
                        color = txt_color,
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
                    color = txt_color,
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
                        color = txt_color,
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
                    color = txt_color,
                    fontSize = (15 * link.type).sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
            )
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
data class Metric(val name: String, val value: Int)

// text: text to display, type: determines fontsize
data class TextType(val text: String, val type: Int)

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen()
}
