package com.example.smilie.model.service.backend

import android.util.Log
import android.widget.Toast
import com.example.smilie.SmilieHiltApp
import com.example.smilie.model.Metric
import com.example.smilie.model.Value
import com.example.smilie.model.service.MetricService
import java.net.SocketTimeoutException

class MetricBackendImpl(
    private val application: SmilieHiltApp,
    private val metricService: MetricService,
): MetricBackend {
    override suspend fun getMetricsById(id: String): ArrayList<Metric> {
        Log.d("SmilieDebug", "getById: $id")
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

    override suspend fun editMetrics(id: String, metrics: ArrayList<Metric>): ArrayList<Metric> {

        val retMetrics = metricService.put(id, metrics)

        if(retMetrics.isEmpty()) {
            Toast.makeText(
                application,
                "Metric data not returned",
                Toast.LENGTH_SHORT
            ).show()
        }

        return retMetrics
    }

    override suspend fun addMetricEntry(id: String, metricId: String, metricVal: Number): Value {
        val entryData = Value(metricVal,"",0f)

        val retEntry = metricService.put(id, metricId, entryData)

        if(retEntry.weight.toFloat() < 0f) {
            Toast.makeText(
                application,
                "Value data not returned",
                Toast.LENGTH_SHORT
            ).show()
        }

        return retEntry
    }
}