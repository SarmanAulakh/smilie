package com.example.smilie.model

import com.google.gson.annotations.SerializedName

data class Metric(
    val id: String = "",
    val name: String = "",
    val public: Boolean = false,
    val active: Boolean = false,
    val values: ArrayList<Value> = ArrayList<Value>()
)

data class Value(
    val value: Number = 0,
    val date: String = "",
    val weight: Number = 0
)