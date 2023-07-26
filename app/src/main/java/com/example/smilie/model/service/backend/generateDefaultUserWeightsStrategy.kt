package com.example.smilie.model.service.backend

import com.example.smilie.model.Metric

class generateDefaultUserWeightsStrategy : generateUserWeightsStrategy {
    override suspend fun generateWeights(metrics: ArrayList<Metric>) : ArrayList<Double> {
        var weights : ArrayList<Double> = ArrayList<Double>()
        for (metric in metrics) {
            weights += 2.0
        }
        return weights
    }
}