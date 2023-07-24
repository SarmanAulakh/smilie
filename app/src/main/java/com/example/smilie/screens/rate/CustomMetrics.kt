package com.example.smilie.screens.rate

import android.util.Log
import android.widget.ToggleButton
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smilie.model.Metric
import com.example.smilie.model.Value
import com.example.smilie.model.service.backend.AllMetrics
import com.example.smilie.screens.settings.SignUpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomMetrics(
    openAndPopUp: (String) -> Unit,
    rateViewModel: RateYourDayViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var public = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "Create Custom Metric",
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp)
            )
//            Text(
//                text = "Metric Name",
//                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
//                fontWeight = FontWeight.Bold
//            )
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = { Text(text = "Enter Metric Name")},
                placeholder = { Text(text = "Your Custom Metric") }
            )
            Row() {
                Text(text = "Public")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = public.value,
                    onCheckedChange = {public.value = it}
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            Spacer(Modifier.weight(1f))
            Row() {
                Button(onClick = {
                    rateViewModel.onCancelClick(openAndPopUp, true)
                }) {
                    Text(
                        text = "Cancel"
                    )
                }
                Spacer(Modifier.weight(1f))
                Button(onClick = {
                    rateViewModel.onCreateComplete(openAndPopUp, name.text, public.value)
                }) {
                    Text(
                        text = "Create"
                    )
                }
            }
        }
    }
}