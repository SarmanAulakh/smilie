package com.example.smilie.model

import com.google.gson.annotations.SerializedName

data class Metric(
    val id: String? = null,
    val name: String = "",
    var public: Boolean = false,
    var active: Boolean = false,
    val values: ArrayList<Value> = ArrayList<Value>()
)

data class Value(
    val value: Number = 0,
    val date: String = "",
    val weight: Number = 0
)

fun getMetricLast(values: ArrayList<Value>): Float  {
    if (values.isEmpty()) return 0.0f

    return String.format("%.1f", values.last().value.toFloat() / 10).toFloat();
}