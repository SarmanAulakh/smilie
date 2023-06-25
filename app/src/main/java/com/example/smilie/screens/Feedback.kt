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
import com.example.smilie.R


@Composable
fun FeedbackScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan),
        contentAlignment = Alignment.TopCenter
    ) {
        MaterialTheme {
            Column {
                Title(text = "User Feedback Page")

                Spacer(modifier = Modifier.height(16.dp))

                val metricData = listOf(
                    Metric("Metric 1", 80),
                    Metric("Metric 2", 60),
                    Metric("Metric 3", 40),
                    Metric("Metric 4", 70),
                    Metric("Metric 5", 90),
                    Metric("Metric 6", 35),
                    Metric("Metric 7", 20),
                    Metric("Metric 8", 90),
                    Metric("Metric 9", 45),
                    Metric("Metric 10", 75)
                )

                for (metric in metricData) {
                    Box(
                        modifier = Modifier.padding(start = 20.dp)
                    ) {
                        MaterialTheme {
                            Column {
                                Text(
                                    text = metric.name,
                                    style = TextStyle(
                                        color = Color.DarkGray,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(10.dp)
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
    val height: Dp = 64.dp
    val width = (value / 100f)

    Box(
        modifier = Modifier
            .wrapContentWidth(align = Alignment.Start)
            .height(height)
            .fillMaxWidth(width)
            .background(color)
            .padding(20.dp)
    )
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
data class Metric(val name: String, val value: Int)

@Composable
@Preview
fun FeedbackScreenPreview() {
    FeedbackScreen()
}