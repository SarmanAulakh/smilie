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
    var weight: Number = 0,
    var tempWeight: Number = 0
)