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
import com.example.smilie.screens.settings.SignUpViewModel

@Composable
fun RateYourDay(
    openAndPopUp: (String) -> Unit,
    rateViewModel: RateYourDayViewModel = hiltViewModel()
) {
    var metrics = mutableListOf<String>(
        "Amount of Sleep",
        "Quality of Sleep",
        "Exercise",
        "Productivity (School)",
        "Time with Friends",
        "Video Games",
        "Food",
        "Time spent on Assignments",
        "Overall"
    )

    var sliders = mutableListOf<MutableState<Float>>()
    metrics.indices.forEach {
        sliders.add(remember { mutableStateOf(5F) })
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
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
                for (i in metrics.indices) {
                    item {
                        Row() {
                            Text(
                                text = metrics[i],
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

            }) {
                Text(
                    text = "Submit"
                )
            }
        }
    }

}
