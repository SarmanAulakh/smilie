package com.example.smilie.model.service.backend

import com.example.smilie.model.Metric

public interface AlgorithmFunctions {
    var importantMetrics: Array<Metric>
    var unImportantMetrics: Array<Metric>
    var overallAccurate: Boolean

    suspend fun trueOverallValue(metrics: Array<Metric>): Double

    suspend fun determiningImportance(metrics: Array<Metric>)

    suspend fun calculateWeights(metrics: Array<Metric>): Array<Metric>

    suspend fun smoothingFunction(weight: Double) : Number

    suspend fun generateWeights(metrics: Array<Metric>) : Array<Double>

}