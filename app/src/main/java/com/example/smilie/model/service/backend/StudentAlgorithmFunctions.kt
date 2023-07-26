package com.example.smilie.model.service.backend

import AlgorithmFunctionsImpl
import android.util.Log
import kotlin.math.*
import com.example.smilie.model.Metric
import com.example.smilie.model.service.backend.MetricBackend
import com.example.smilie.model.service.backend.AccountBackend
import com.example.smilie.model.service.backend.AlgorithmFunctions
import com.example.smilie.model.service.backend.UserBackend
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

class StudentAlgorithmFunctions @Inject constructor(
    override val metricBackend: MetricBackend,
    override val accountBackend: AccountBackend,
    override var importantMetrics: Array<Metric> = emptyArray(),
    override var unImportantMetrics: Array<Metric> = emptyArray(),
    override var overallAccurate: Boolean = false,
                                                    ) : AlgorithmFunctionsImpl(metricBackend, accountBackend) {
    override suspend fun generateWeights(metrics: Array<Metric>): Array<Double> {
        var weights : Array<Double> = emptyArray()
        for (metric in metrics) {
            if (metric.name == "Productivity (School)" || metric.name == "Time spent on Assignments") {
                weights += 2.5
            } else {
                weights += 2.0
            }

        }
        return weights
    }
}