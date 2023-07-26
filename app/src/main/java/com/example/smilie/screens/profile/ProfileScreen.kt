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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clipToBounds
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
import coil.compose.AsyncImage
import com.example.smilie.model.view.ProfileViewModel
import com.example.smilie.screens.TextType
import com.example.smilie.ui.navigation.Profile
import java.util.Calendar
import kotlin.math.roundToInt
import com.example.smilie.model.Metric
import com.example.smilie.model.getMetricLast
import com.example.smilie.screens.settings.SettingsManager
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    userId: String?,
    openAndPopUp: (String) -> Unit,
    settingsManager: SettingsManager
) {
//    profileViewModel.updateCurrentlyViewingUser(userId)
    val user = profileViewModel.currentlyViewingUser.value
    val metrics = profileViewModel.metricData.value
    var barToggle by remember { mutableStateOf(false) }
    var showFullFriendList by remember { mutableStateOf(false) }
    if (user == null) {
        Text(text = "loading...")
    } else {
        Log.d("SmilieDebug", "userId: ${user.id}")
        Box(
            modifier = Modifier
                .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
        ) {
            if (!showFullFriendList) {
                Column() {
                    if (profileViewModel.signedInUserId != user.id) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top=8.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Button(onClick = {
                                showFullFriendList = false;
                                profileViewModel.updateCurrentlyViewingUser(userId = null);
                            }) {
                                Text(text = "Back")
                            }
                        }
                    }
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding =  PaddingValues(horizontal = 10.dp)
                    ) {
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
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    ProfileImage(imageUri = Uri.parse(user.imageUrl), setImageUri = {})
                                }
                                Text(
                                    modifier = Modifier
                                        .padding(start = 10.dp),
                                    text = user.bio,
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                    fontWeight = FontWeight.Bold,
                                );
                                Text(
                                    text = "Type: ",
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                    fontWeight = FontWeight.Bold,
                                );
                                Text(
                                    text = user.userType.name,
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                );
                                Text(
                                    text = "Current Quality of Life Rating:",
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                    fontWeight = FontWeight.Bold,
                                );
                                var overallAvg = 0f
                                var count = 0f

                                metrics?.forEach {
                                    if(it.name == "Overall") {
                                        it.values.forEach {itit ->
                                            overallAvg += itit.value.toFloat()
                                            count += 1f
                                        }
                                    }
                                }
                                Text(
                                    text = (overallAvg/count).toInt().toString() + "/10",
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                );

                                // for most important metrics
                                Column(modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 8.dp, bottom = 24.dp)) {
                                    Text(
                                        text = "Most Important Metrics:",
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    );
                                    if (metrics != null) {
                                        for (metric in metrics.sortedByDescending { it.values.sumOf { value -> value.weight.toDouble() } }
                                            .take(3)) {
                                            Column(modifier =
                                            Modifier.padding(bottom = 12.dp)) {
                                                Text(
                                                    text = metric.name,
                                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                                )
                                            }
                                        }
                                    }
                                // Current Metrics
                                Column(modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 8.dp, bottom = 24.dp)) {
                                    Text(
                                        text = "Current Metrics:",
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    );
                                    if (metrics != null) {
                                        for (metric in metrics.filter { it.public }) {
                                            Column(modifier =
                                                Modifier.padding(bottom = 12.dp)) {
                                                Text(
                                                    text = metric.name,
                                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                                );
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(8.dp)
                                                        .background(Color.Gray)
                                                ) {
                                                    LinearProgressIndicator(progress = getMetricLast(metric.values), modifier = Modifier
                                                        .fillMaxSize()
                                                        .clipToBounds());
                                                }
                                            }
                                        }
                                    }
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
                                        Row(modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "Following",
                                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                                fontWeight = FontWeight.Bold,
                                            );
                                            Button(onClick = { showFullFriendList = true }) {
                                                Text(text = "View All")
                                            }
                                        }
                                        LazyRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            state = rememberLazyListState(),
                                            horizontalArrangement = Arrangement
                                                .spacedBy(
                                                    space = 10.dp,
//                                                    alignment = Alignment.CenterHorizontally
                                                )
                                        ) {
                                            for (following in profileViewModel.followingUsers.value) {
                                                item {
                                                    Column(
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        modifier = Modifier.clickable(onClick = { profileViewModel.updateCurrentlyViewingUser(following.id)})
                                                    ) {
                                                        Text(
                                                            text = following.username,
                                                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                                        );
                                                        Card(
                                                            shape = CircleShape,
                                                            modifier = Modifier
                                                                .padding(8.dp)
                                                                .size(50.dp)
                                                        ) {
                                                            AsyncImage(
                                                                model = Uri.parse(following.imageUrl),
                                                                contentDescription = "",
                                                                modifier = Modifier
                                                                    .wrapContentSize(),
                                                                contentScale = ContentScale.Crop
                                                            )
                                                        }
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
                            metrics?.get(0)?.name,
                            metrics?.get(1)?.name,
                            metrics?.get(2)?.name
                        )

                        item {
                            com.example.smilie.screens.FoldableCards(
                                input = metricText,
                                title = "Recommended Metrics"
                            )
                        }
//                        item {
//                            com.example.smilie.screens.FoldableCards(
//                                input = helpfulLinks,
//                                title = "Helpful Links"
//                            )
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
                                    input = metrics,
                                    settingsManager = settingsManager
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
            } else {
                Column(modifier = Modifier.padding(10.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Button(onClick = {
                            showFullFriendList = false;
                            profileViewModel.updateCurrentlyViewingUser(userId = null);
                        }) {
                            Text(text = "Back")
                        }
                    }

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Following List", fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                    for (following in profileViewModel.followingUsers.value) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable(onClick = {
                                    showFullFriendList = false;
                                    profileViewModel.updateCurrentlyViewingUser(
                                        following.id
                                    )
                                })
                        ) {
                            Card(
                                shape = CircleShape,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(50.dp)
                            ) {
                                AsyncImage(
                                    model = Uri.parse(following.imageUrl),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .wrapContentSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Text(
                                text = following.username + " - " + following.userType.toString(),
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                            );
                        }
                    }
                }
            }
        }
    }
}
