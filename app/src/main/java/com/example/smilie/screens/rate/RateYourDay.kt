package com.example.smilie.screens.rate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Slider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smilie.model.Metric
import java.time.Instant
import kotlin.math.roundToInt

@Composable
fun RateYourDay(
    openAndPopUp: (String) -> Unit,
    metrics: ArrayList<Metric>?,
    rateViewModel: RateYourDayViewModel = hiltViewModel()
) {

    var sliders = mutableListOf<MutableState<Float>>()
    var metricsToRate = ArrayList<Metric>()
    var overall = Metric()
    if (metrics != null) {
        metrics.forEach {
            if(it.name == "Overall") {
                overall = it
            } else if(it.active) {
                sliders.add(remember { mutableStateOf(5F) })
                metricsToRate.add(it)
            }
        }
        sliders.add(remember { mutableStateOf(5F) })
        metricsToRate.add(overall)

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            if(metricsToRate.isNotEmpty() &&
                metricsToRate[0].values.isNotEmpty() &&
                metricsToRate[0].values.last().date.split('T')[0] == Instant.now().toString().split('T')[0]) {

                Text(
                    text = "You've already rated today. Come back tomorrow and rate again!",
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(10.dp)
                )
            } else {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Rate Your Day",
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(10.dp)
                    )
                    Row(){
                        Button(
                            onClick = { rateViewModel.onRemoveClick(openAndPopUp) },
                            //modifier = Modifier.height(30.dp).width(100.dp)
                        ) {
                            Text(
                                text = "Remove"
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        Button(
                            onClick = { rateViewModel.onAddClick(openAndPopUp) },
                            //modifier = Modifier.height(30.dp).width(100.dp)
                        ) {
                            Text(
                                text = "Add"
                            )
                        }
                    }

                    LazyColumn(modifier = Modifier.padding(10.dp)) {
                        for (i in metricsToRate.indices) {
                            item {
                                Row() {
                                    Text(
                                        text = metricsToRate[i].name,
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.weight(1f))
                                    Text(
                                        text = sliders[i].value.toInt().toString(),
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                                    )
                                }

                                Slider(
                                    value = sliders[i].value,
                                    onValueChange = { sliders[i].value = it.roundToInt().toFloat() },
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
                        item{Spacer(Modifier.height(30.dp))}
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Spacer(Modifier.weight(1f))
                    Button(onClick = {
                        rateViewModel.onSubmit(openAndPopUp, metricsToRate, sliders)
                    }) {
                        Text(
                            text = "Submit"
                        )
                    }
                }
            }
        }
    }
}
