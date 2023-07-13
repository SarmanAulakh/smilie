package com.example.smilie.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.LinearProgressIndicator

@Composable
fun FeedbackScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        MaterialTheme {
            Column {
//                Title1(text = "User Feedback Page")
//
//                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    val metricData = listOf(
                        Metric1("Metric 1", 80),
                        Metric1("Metric 2", 60),
                        Metric1("Metric 3", 40),
                        Metric1("Metric 4", 70),
                        Metric1("Metric 5", 90),
                        Metric1("Metric 6", 35),
                        Metric1("Metric 7", 20),
                        Metric1("Metric 8", 90),
                        Metric1("Metric 9", 45),
                        Metric1("Metric 10", 75)
                    )

                    items(metricData) {metric ->
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
                                Rectangle1(metric.value)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Rectangle1(value: Int) {
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
fun Title1(text: String) {
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
data class Metric1(val name: String, val value: Int)

@Composable
@Preview(showBackground = true)
fun FeedbackScreenPreview() {
    FeedbackScreen()
}
