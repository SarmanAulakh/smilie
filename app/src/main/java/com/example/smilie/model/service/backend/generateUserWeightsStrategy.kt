package com.example.smilie.model.service.backend

import com.example.smilie.model.Metric

interface generateUserWeightsStrategy {
    suspend fun generateWeights(metrics: ArrayList<Metric>): ArrayList<Double>
}