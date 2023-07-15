package com.example.smilie.screens.profile

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smilie.R
import com.example.smilie.model.User
import com.example.smilie.screens.sign_up.SignUpViewModel
import com.example.smilie.ui.components.ProfileImage


@Composable
fun ProfileScreen(
    user: User?,
) {
    if (user == null) {
        Text(text = "loading...")
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 25.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "${user.username}'s profile",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                );
                ProfileImage(imageUri = Uri.parse(user.imageUrl), setImageUri = {})
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp),
                    text = "I like long walks on the beach. I try to get enough sleep but that doesn't always work",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                );
                Text(
                    text = "Type: Student",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                );
                Text(
                    text = "Current Quality of Life Rating: 9/10",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                );

                // Week metric Averages
                Column() {
                    Text(
                        text = "This Week's Metric Averages",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                    );
                    Text(
                        text = "7 Hours of Sleep",
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    );
                    LinearProgressIndicator(progress = 0.5f);
                    Text(
                        text = "600 Minutes Playing Video Games",
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    );
                    LinearProgressIndicator(progress = 0.95f);
                }

                // Current Metrics
                Column() {
                    Text(
                        text = "Current Metrics",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                    );
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Text(
                            text = "Hours of Sleep",
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        );
                        Text(
                            text = "Minutes Playing Video Games",
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        );
                    }
                }

                // Past Metrics
                Column() {
                    Text(
                        text = "Past Metrics",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                    );
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Text(
                            text = "Minutes of Exercise",
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        );
                        Text(
                            text = "Minutes Watching YouTube",
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        );
                    }
                }

                // Recommended Metrics
                Column() {
                    Text(
                        text = "Recommended Metrics",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                    );
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Text(
                            text = "Minutes Studying",
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        );
                        Text(
                            text = "Hours Working",
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        );
                    }
                }

                // Friends list
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Friends",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontWeight = FontWeight.Bold,
                        );
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Brian Peng",
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                );
                                Card(
                                    modifier = Modifier
                                        .size(50.dp),
                                    shape = CircleShape,
                                ) {
                                    Image(
                                        painterResource(R.drawable.ic_profile),
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop
                                    )
                                };
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "William Tam",
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                );
                                Card(
                                    modifier = Modifier
                                        .size(50.dp),
                                    shape = CircleShape,
                                ) {
                                    Image(
                                        painterResource(R.drawable.ic_profile),
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop
                                    )
                                };
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Sarman Aulakh",
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                );
                                Card(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .align(alignment = Alignment.CenterHorizontally),
                                    shape = CircleShape,
                                ) {
                                    Image(
                                        painterResource(R.drawable.ic_profile),
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop
                                    )
                                };
                            }
                        }
                    }
                }
            }
        }
    }

}
