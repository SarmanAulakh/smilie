package com.example.smilie.model.service.backend

import com.example.smilie.model.Metric
import com.example.smilie.model.Value

interface MetricBackend {
    suspend fun getMetricsById(id: String): ArrayList<Metric>

    suspend fun editMetrics(id: String, metrics: ArrayList<Metric>): Boolean

    suspend fun addMetricEntry(id: String, metricId: String, metricVal: Number): Boolean

    suspend fun getMetricValueByName(id: String, name: String): Number?
}