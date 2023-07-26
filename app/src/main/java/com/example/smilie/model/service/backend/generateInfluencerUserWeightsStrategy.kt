package com.example.smilie.model.service.backend

import com.example.smilie.model.Metric

class generateInfluencerUserWeightsStrategy : generateUserWeightsStrategy {
    override suspend fun generateWeights(metrics: ArrayList<Metric>): Array<Double> {
        var weights : Array<Double> = emptyArray()
        for (metric in metrics) {
            // TO:DO
            if (metric.name == "Productivity (School)" || metric.name == "Time spent on Assignments") {
                weights += 0.0
            } else if (metric.name == "Time on Phone") {
                weights += 2.5
            } else {
                weights += 2.0
            }
        }
        return weights
    }
}