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
import com.example.smilie.model.Metric

@Composable
fun RemoveMetrics(
    openAndPopUp: (String) -> Unit,
    metrics: ArrayList<Metric>?,
    rateViewModel: RateYourDayViewModel = hiltViewModel()
) {
    if(metrics != null) {
        var dontRemove = mutableListOf<MutableState<Boolean>>()
        var metricsToRemove = ArrayList<Metric>()
        metrics.forEach {
            if(it.active) {
                dontRemove.add(remember { mutableStateOf(true) })
                metricsToRemove.add(it)
            }
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

                    for (i in metricsToRemove.indices) {
                        if (metricsToRemove[i].name != "Overall") {
                            item {
                                Row() {
                                    Text(
                                        text = metricsToRemove[i].name,
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.weight(1f))
                                }

                                Row() {
                                    Text(text = "Remove")
                                    Spacer(Modifier.weight(1f))
                                    Switch(
                                        checked = !dontRemove[i].value,
                                        onCheckedChange = {dontRemove[i].value = !it}
                                    )
                                }
                            }
                            item{Spacer(Modifier.height(20.dp))}
                        }
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
                    rateViewModel.onEditComplete(openAndPopUp, metricsToRemove, dontRemove)
                }) {
                    Text(
                        text = "Done"
                    )
                }
            }
        }
    }

}
