package com.example.smilie.screens.rate

import AlgorithmFunctionsImpl
import android.util.Log
import androidx.compose.runtime.MutableState
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
import com.example.smilie.model.service.backend.AlgorithmFunctions
import com.example.smilie.model.service.backend.InfluencerAlgorithmFunctions
import com.example.smilie.model.service.backend.StudentAlgorithmFunctions





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

    suspend fun onSubmit(openAndPopUp: (String) -> Unit, metrics: ArrayList<Metric>, vals: MutableList<MutableState<Float>> ) {
        Log.d("SmilieDebug", "Submitting metrics")

        val algo = when (accountBackend.currentUser!!.userType) {
            UserTypes.STUDENT -> StudentAlgorithmFunctions(metricBackend, accountBackend)
            UserTypes.INFLUENCER -> InfluencerAlgorithmFunctions(metricBackend, accountBackend)
            else -> {
                AlgorithmFunctionsImpl(metricBackend, accountBackend)
            }
        }
        var weights: Array<Double> = emptyArray<Double>()
        // right here
        if (metrics[0].values.size >= 1) {
            for (i in metrics.indices) {
                val temp = Value(vals[i].value, Instant.now().toString())
                metrics[i].values += temp
            }
            val newMetrics = algo.calculateWeights(metrics.toArray() as Array<Metric>)
            for (metric in newMetrics) {
                weights += metric.values.last().weight.toDouble()
            }
        } else if (metrics[0].values.size == 0 ) {
            weights = algo.generateWeights(metrics.toArray() as Array<Metric>)
        }


        // run calculate metrics


        // nested for loop that creates weight array of correct ordering


        // return the weights that are calculated
        // sleeping metric: metrics contains like [entry1, entry2, entry3....]
        // vals[sleepingMetric]: entryn but only values

        viewModelScope.launch {
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