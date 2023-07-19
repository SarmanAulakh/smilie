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
import androidx.compose.material3.Switch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RemoveMetrics(
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
    var remove = mutableListOf<MutableState<Boolean>>()
    metrics.indices.forEach {
        remove.add(remember { mutableStateOf(false) })
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(){
                Text(
                    text = "Edit Metrics",
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(10.dp)
                )
            }

            LazyColumn(modifier = Modifier.padding(10.dp)) {

                for (i in metrics.indices) {
                    item {
                        if (metrics[i] != "Overall") {
                            Row() {
                                Text(
                                    text = metrics[i],
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.weight(1f))
                            }

                            Row() {
                                Text(text = "Remove")
                                Spacer(Modifier.weight(1f))
                                Switch(
                                    checked = remove[i].value,
                                    onCheckedChange = {remove[i].value = it}
                                )
                            }
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
                rateViewModel.onEditComplete(openAndPopUp)
            }) {
                Text(
                    text = "Done"
                )
            }
        }
    }

}
