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
//                Home("Dohyun", 7.8)
//                Title(text = "User Feedback Page")
//
//                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
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
    val welcomeMessage = buildAnnotatedString {
        withStyle(style = SpanStyle()) {}
    }

    Column() {
        Text(
            text = "",
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

        Row() {
            Column() {
                Text(
                    text = "Popular Metrics:\n - sleep\n - time spent with friends\n productivity\n ...",
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                )
            }
            Column() {
                Text(
                    text = "Helpful Links:\n - betterhelp.com\n - ...",
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                )
            }
        }
    }

}
data class Metric(val name: String, val value: Int)

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen()
}
