package com.example.smilie.screens.profile

import android.graphics.Paint
import android.net.Uri
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
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
import com.example.smilie.R
import com.example.smilie.model.User
import com.example.smilie.model.UserTypes
import com.example.smilie.ui.components.ProfileImage
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.smilie.model.view.ProfileViewModel
import com.example.smilie.screens.TextType
import com.example.smilie.ui.navigation.Profile
import java.util.Calendar
import kotlin.math.roundToInt
import com.example.smilie.model.Metric

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    userId: String?,
    openAndPopUp: (String) -> Unit,
    metrics: ArrayList<Metric>?
) {
    profileViewModel.updateCurrentlyViewingUser(userId)
    val user = profileViewModel.currentlyViewingUser.value
    var barToggle by remember { mutableStateOf(false) }
    if (user == null) {
        Text(text = "loading...")
    } else {
        Log.d("SmilieDebug", "userId: ${user.id}")
        Box(
            modifier = Modifier
                .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
        ) {
            MaterialTheme {
                Column() {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding =  PaddingValues(horizontal = 10.dp)
                    ) {
//                        item {
//                            TextField(value = "blah", onValueChange = {})
//                        }
                        item {
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
                                    text = user.bio,
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                    fontWeight = FontWeight.Bold,
                                );
                                Text(
                                    text = "Type: " + user.userType,
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
                                            text = "Following",
                                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                            fontWeight = FontWeight.Bold,
                                        );
                                        LazyRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            state = rememberLazyListState(),
                                            horizontalArrangement = Arrangement
                                                .spacedBy(
                                                    space = 10.dp,
                                                    alignment = Alignment.CenterHorizontally
                                                )
                                        ) {
                                            for (following in profileViewModel.followingUsers.value) {
                                                item {
                                                    Column(
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {
                                                        Text(
                                                            text = following.username,
                                                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                                        );
                                                        Card(
                                                            modifier = Modifier
                                                                .size(50.dp),
                                                            shape = CircleShape,
                                                            onClick = { profileViewModel.onFriendClick(openAndPopUp, following.id) }
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

                        val helpfulLinks = listOf(
                            "betterhelp.org",
                            "LeagueofLeg.com"
                        )
                        val metricText = listOf(
                            "sleep",
                            "time spent with friends",
                            "productivity",
                        )

                        item {
                            com.example.smilie.screens.FoldableCards(
                                input = metricText,
                                title = "Recommended Metrics"
                            )
                        }
                        item {
                            com.example.smilie.screens.FoldableCards(
                                input = helpfulLinks,
                                title = "Helpful Links"
                            )
                        }

                        item { com.example.smilie.screens.Title("${user.username}'s Data") }

                        metrics?.forEach() {
                            item {
                                if (it.active) {
                                    Box(
                                        modifier = Modifier.padding(start = 0.dp)
                                    ) {
                                        Column {
                                            Text(
                                                text = it.name,
                                                style = TextStyle(
                                                    fontSize = 28.sp,
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                modifier = Modifier.padding(8.dp)
                                            )
                                            com.example.smilie.screens.Rectangle(it.values.last().value.toFloat())
                                        }
                                    }
                                }

                            }

                        }

//                        items(metricData) { metric ->
//                            Box(
//                                modifier = Modifier.padding(start = 0.dp)
//                            ) {
//                                Column {
//                                    Text(
//                                        text = metric.name,
//                                        style = TextStyle(
//                                            fontSize = 28.sp,
//                                            fontWeight = FontWeight.Bold
//                                        ),
//                                        modifier = Modifier.padding(8.dp)
//                                    )
//                                    com.example.smilie.screens.Rectangle(metric.value)
//                                }
//                            }
//                        }


                        item {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Switch(
                                    checked = barToggle,
                                    onCheckedChange = {
                                        barToggle = it
                                    }
                                )
                                Text(
                                    text = "Toggle Graph Type",
                                    style = TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(20.dp)
                                )
                            }
                        }

                        if (barToggle) {
                            item {
                                com.example.smilie.screens.PieChart(
                                    modifier = Modifier
                                        .size(400.dp),
                                    input = metrics
                                )
                            }
                        } else {
                            item {
                                com.example.smilie.screens.BarGraph(
                                    modifier = Modifier
                                        .size(500.dp),
                                    input = metrics
                                )
                            }
                        }

                        // Buffer to show things hidden from the task bar
                        item {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                            ) {
                            }
                        }
                    }
                }
            }
        }
    }
}
