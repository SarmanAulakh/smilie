package com.example.smilie.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Slider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RateYourDay() {
//    var metrics = Array<String>(10){""}
    var metrics = mutableListOf<String>(
        "Amount of Sleep",
        "Quality of Sleep",
        "Exercise",
        "Productivity (School)",
        "Time with Friends",
        "Video Games",
        "Food",
        "Overall"
    )
    var sliders = mutableListOf<MutableState<Float>>()
    metrics.indices.forEach {
        //metrics[it] = "Metric "+(it+1).toString()
        sliders.add(remember { mutableStateOf(5F) })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "Rate Your Day",
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(10.dp)
            )
            LazyColumn(modifier = Modifier.padding(10.dp)) {

                //            item{Text(
                //                text = "Rate Your Day",
                //                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                //                fontWeight = FontWeight.Bold,
                //                color = Color.Black
                //            )}

                for (i in metrics.indices) {
                    item {
                        Row() {
                            Text(
                                text = metrics[i],
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = sliders[i].value.toInt().toString(),
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize
                            )
                        }

                        Slider(
                            value = sliders[i].value,
                            onValueChange = { sliders[i].value = it },
                            valueRange = 0f..10f,
                            steps = 9
                        )

                        Row() {
                            Text(text = "Terrible")
                            Spacer(Modifier.weight(1f))
                            Text(text = "Amazing")
                        }
                    }
                    item{Spacer(Modifier.height(20.dp))}
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(horizontal=15.dp,vertical=10.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(
                    text = "Done",
                    color = Color.Green
                )
            }
        }
    }

}

@Composable
@Preview
fun RateYourDayPreview() {
    RateYourDay()
}