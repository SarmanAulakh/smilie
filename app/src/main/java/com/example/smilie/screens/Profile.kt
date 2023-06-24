package com.example.smilie.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smilie.R


@Composable
fun ProfileScreen(name: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 25.dp)
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "${name}'s profile",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            );
            Card(
                shape = CircleShape
            ) {
                Image(
                    painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = "Past Week's Metric Averages",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            );
            Text(
                text = "7 Hours of Sleep",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = Color.Black
            );
            LinearProgressIndicator(progress = 0.5f);
            Text(
                text = "50 Minutes Playing Video Games",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = Color.Black
            );
            LinearProgressIndicator(progress = 0.5f);
        }
    }
}

@Composable
@Preview
fun ProfileScreenPreview() {
    ProfileScreen(name = "Arnold")
}