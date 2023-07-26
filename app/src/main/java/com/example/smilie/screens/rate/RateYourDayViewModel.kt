package com.example.smilie.screens.rate

import AlgorithmFunctionsImpl
import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewModelScope
import com.example.smilie.model.Metric
import com.example.smilie.model.UserTypes
import com.example.smilie.model.Value
import com.example.smilie.model.service.backend.AccountBackend
import com.example.smilie.model.service.backend.MetricBackend
import com.example.smilie.model.view.SmilieViewModel
import com.example.smilie.ui.navigation.ADD_METRICS_SCREEN
import com.example.smilie.ui.navigation.CUSTOM_METRICS_SCREEN
import com.example.smilie.ui.navigation.Home
import com.example.smilie.ui.navigation.REMOVE_METRICS_SCREEN
import com.example.smilie.ui.navigation.RateYourDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject
import com.example.smilie.model.service.backend.generateDefaultUserWeightsStrategy
import com.example.smilie.model.service.backend.generateInfluencerUserWeightsStrategy
import com.example.smilie.model.service.backend.generateStudentUserWeightsStrategy
import androidx.appcompat.app.AlertDialog



@HiltViewModel
class RateYourDayViewModel @Inject constructor(
    private val metricBackend: MetricBackend,
    private val accountBackend: AccountBackend
) : SmilieViewModel() {
    fun onRemoveClick(openAndPopUp: (String) -> Unit) {
        launchCatching {
            openAndPopUp(REMOVE_METRICS_SCREEN)
        }
    }
    fun onAddClick(openAndPopUp: (String) -> Unit) {
        launchCatching {
            openAndPopUp(ADD_METRICS_SCREEN)
        }
    }
    fun onCustomClick(openAndPopUp: (String) -> Unit) {
        launchCatching {
            openAndPopUp(CUSTOM_METRICS_SCREEN)
        }
    }

    fun onCancelClick(openAndPopUp: (String) -> Unit, fromCustom: Boolean) {
        if(fromCustom) {
            launchCatching {
                openAndPopUp(ADD_METRICS_SCREEN)
            }
        } else {
            launchCatching {
                openAndPopUp(RateYourDay.route)
            }
        }
    }

    fun onEditComplete(openAndPopUp: (String) -> Unit, metrics: ArrayList<Metric>, edit: MutableList<MutableState<Boolean>>) {
        Log.d("SmilieDebug", "Editing metrics")
        viewModelScope.launch {
            var inputMet = ArrayList<Metric>()
            for(i in metrics.indices) {
                if(metrics[i].active != edit[i].value) {
                    inputMet.add(Metric(metrics[i].id, metrics[i].name, metrics[i].public, edit[i].value, metrics[i].values))
                }
            }
            if(inputMet.isNotEmpty() && !metricBackend.editMetrics(id=accountBackend.currentUserId, metrics=inputMet)) {
                Log.d("SmilieDebug", "Failed to edit metrics")
            } else {
                Log.d("SmilieDebug", "Edited successfully")
            }
        }
        launchCatching {
            openAndPopUp(RateYourDay.route)
        }
    }

    fun onSubmit(openAndPopUp: (String) -> Unit, metrics: ArrayList<Metric>, vals: MutableList<MutableState<Float>> ) {
        Log.d("SmilieDebug", "Submitting metrics")

        var overall : Float = 0F

        // check if numbers are valid
        for (i in metrics.indices) {
            if (metrics[i].name == "Overall") {
                overall = vals[i].value
            }
        }

        var index = 0
        while(index < vals.size) {
            if (vals[index].value != overall) break
            index += 1
        }
        // if not every value is the same as overall
        if (index != vals.size) {
            Log.d("first check", "checking if all values are the same")
            index = 0
            while (index < vals.size) {
                if (vals[index].value > overall) break
                index += 1
            }

            // checking if every value is less than or equal to overall
            Log.d("second check", "checking if all values are less than or equal")
            if (index != vals.size) {
                index = 0
                while (index < vals.size) {
                    if (vals[index].value < overall) break
                    index += 1
                }

                // checking if every value is >= overall
                if (index == vals.size) {
                    // notification and return
                }
            } else {
                Log.d("lessThanOrEqual", "all values are less than or equal")
                // notification and return
                return

            }
        }



        val algo = when (accountBackend.currentUser!!.userType) {
            UserTypes.STUDENT -> generateStudentUserWeightsStrategy()
            UserTypes.INFLUENCER -> generateInfluencerUserWeightsStrategy()
            else -> {
                generateDefaultUserWeightsStrategy()
            }
        }
        var weights: ArrayList<Double> = ArrayList<Double>()


        viewModelScope.launch {
            if (metrics[0].values.size >= 1) {
                val weightCalculator = AlgorithmFunctionsImpl(metricBackend, accountBackend)
                for (i in metrics.indices) {
                    val temp = Value(vals[i].value, Instant.now().toString())
                    metrics[i].values += temp
                }
                val newMetrics = weightCalculator.calculateWeights(metrics)
                Log.d("logging new metrics", "these are the new metrics:$newMetrics")
                for (metric in newMetrics) {
                    weights += metric.values.last().weight.toDouble()
                }
            } else if (metrics[0].values.size == 0 ) {
                weights = algo.generateWeights(metrics)
                for (i in metrics.indices) {
                    metrics[i].values += Value(vals[i].value, "temp", weights[i], 0.0)
                }
                val weightCalculator = AlgorithmFunctionsImpl(metricBackend, accountBackend)
                for (i in metrics.indices) {
                    val temp = Value(vals[i].value, Instant.now().toString())
                    metrics[i].values += temp
                }
                val newMetrics = weightCalculator.calculateWeights(metrics)
                weights.clear()
                for (metric in newMetrics) {
                    weights += metric.values.last().weight.toDouble()
                }

            }
            for(i in metrics.indices) {

                // to addMetricEntry add weight field so that it can also be added to metricBackend
                if(!metricBackend.addMetricEntry(accountBackend.currentUserId, metrics[i].id.toString(), vals[i].value, weights[i])) {
                    Log.d("SmilieDebug", "Failed to submit metric entry")
                } else {
                    Log.d("SmilieDebug", "Submitted successfully")
                }
            }
        }
        launchCatching {
            openAndPopUp(Home.route)
        }
    }

    fun onCreateComplete(openAndPopUp: (String) -> Unit, name: String, public: Boolean) {
        viewModelScope.launch {
            var inputMet = ArrayList<Metric>()
            inputMet.add( Metric(null, name, public, false, ArrayList<Value>()) )
            if(!metricBackend.editMetrics(id=accountBackend.currentUserId, metrics=inputMet)) {
                Log.d("SmilieDebug", "Failed to create custom metric")
            } else {
                Log.d("SmilieDebug", "Created successfully")
            }
        }
        launchCatching {
            openAndPopUp(ADD_METRICS_SCREEN)
        }

    }
}