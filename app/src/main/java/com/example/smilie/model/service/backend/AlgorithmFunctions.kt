package com.example.smilie.model.service.backend

import com.example.smilie.model.Metric

public interface AlgorithmFunctions {
    var importantMetrics: ArrayList<Metric>
    var unImportantMetrics: ArrayList<Metric>
    var overallAccurate: Boolean

    suspend fun trueOverallValue(metrics: ArrayList<Metric>): Double

    suspend fun determiningImportance(metrics: ArrayList<Metric>)

    suspend fun calculateWeights(metrics: ArrayList<Metric>): ArrayList<Metric>

    suspend fun smoothingFunction(weight: Double) : Number


}