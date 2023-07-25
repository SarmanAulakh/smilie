package com.example.smilie.model.service.backend

import android.util.Log
import android.widget.Toast
import com.example.smilie.SmilieHiltApp
import com.example.smilie.model.Metric
import com.example.smilie.model.Value
import com.example.smilie.model.service.MetricService
import java.net.SocketTimeoutException
import java.time.Instant

class MetricBackendImpl(
    private val application: SmilieHiltApp,
    private val metricService: MetricService,
): MetricBackend {
    override suspend fun getMetricsById(id: String): ArrayList<Metric> {
        Log.d("SmilieDebug", "getMetricsById: $id")
        if (id == "") {
            return ArrayList<Metric>()
        }

        val metrics = metricService.getAll(id)

        if (metrics.isEmpty()) {
            Toast.makeText(
                application,
                "Metric data not found",
                Toast.LENGTH_SHORT
            ).show()
        }

        return metrics
    }

    override suspend fun editMetrics(id: String, metrics: ArrayList<Metric>): Boolean {
        Log.d("SmilieDebug", "editMetrics: $id")
        val retMetrics = metricService.put(id, metrics)

        if(!retMetrics) {
            Toast.makeText(
                application,
                "Edit metrics failed",
                Toast.LENGTH_SHORT
            ).show()
        }

        return retMetrics
    }

    override suspend fun addMetricEntry(id: String, metricId: String, metricVal: Number): Boolean {
        val entryData = Value(metricVal, Instant.now().toString(),0f)
        val retEntry = metricService.put(id, metricId, entryData)

        if(!retEntry) {
            Toast.makeText(
                application,
                "Add metric entry failed",
                Toast.LENGTH_SHORT
            ).show()
        }

        return retEntry
    }

    override suspend fun getMetricValueByName(id: String, name: String): Number {
        val metrics: ArrayList<Metric> = getMetricsById(id)
        if (metrics.isEmpty()) return -1
        for (metric in metrics) {
            if (metric.name != name) continue
            return metric.values[metric.values.size - 1].value
        }

        return -1
    }
}