import android.util.Log
import kotlin.math.*
import com.example.smilie.model.Metric
import com.example.smilie.model.service.backend.MetricBackend
import com.example.smilie.model.service.backend.AccountBackend
import com.example.smilie.model.service.backend.AlgorithmFunctions
import com.example.smilie.model.service.backend.UserBackend
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
open class AlgorithmFunctionsImpl @Inject constructor(
    open val metricBackend: MetricBackend,
    open val accountBackend: AccountBackend,
) : AlgorithmFunctions {

//    var userData: MutableState<User?> = mutableStateOf(null)
//    var userIdToken: MutableState<String?> = mutableStateOf(null)
//    var metricData: MutableState<ArrayList<Metric>?> = mutableStateOf(null)

//    init {
//        determiningImportance()
//        smoothingFunction()
//        smoothWeights()
//        trueOverallValue()
//    }

    override var importantMetrics: Array<Metric> = arrayOf()
    override var unImportantMetrics: Array<Metric> = arrayOf()
    override var overallAccurate: Boolean = false

    // This function has to account for overall
    override suspend fun trueOverallValue(metrics: Array<Metric>): Double {
        var avg: Double = 0.0
        for (metric in metrics) {
            if (metric.name == "Overall") continue
            avg += (metric.values.last().value).toInt()
        }

        return avg / metrics.size - 1
    }

    // this function has to account for overall
    override suspend fun determiningImportance(metrics: Array<Metric>) {
        val trueAverage = trueOverallValue(metrics)
        val overall = metricBackend.getMetricValueByName(accountBackend.currentUserId, "Overall")
        if (overall == -1) {
            Log.d("Overall doesn't exist", "Error: no overall value")
            return
        }
        if (trueAverage > overall!!.toDouble()) {
            for (metric in metrics) {
                if (metric.name == "Overall") continue
                if (metric.values.last().value.toInt() < overall!!.toDouble()) {
                    unImportantMetrics += metric
                } else if (metric.values.last().value.toInt() > overall!!.toDouble()) {
                    importantMetrics += metric
                } else {
                    val rng = Random.nextInt(1, 2)
                    if (rng == 1) {
                        unImportantMetrics += metric
                    } else {
                        importantMetrics += metric
                    }
                }
            }
        } else if (trueAverage < overall!!.toDouble()) {
            for (metric in metrics) {
                if (metric.name == "Overall") continue
                if (metric.values.last().value.toInt() > overall!!.toDouble()) {
                    unImportantMetrics += metric
                } else if (metric.values.last().value.toInt() < overall!!.toDouble()) {
                    importantMetrics += metric
                } else {
                    val rng = Random.nextInt(1, 2)
                    if (rng == 1) {
                        unImportantMetrics += metric
                    } else {
                        importantMetrics += metric
                    }
                }
            }
        } else {
            // special calculations, use overallAccurate to check
            overallAccurate = true
            for (metric in metrics) {
                if (metric.name == "Overall") continue
                if (metric.values.last().value.toInt() > overall!!.toDouble()) {
                    unImportantMetrics += metric
                } else if (metric.values.last().value.toInt() < overall!!.toDouble()) {
                    importantMetrics += metric
                } else {
                    val rng = Random.nextInt(1, 2)
                    if (rng == 1) {
                        unImportantMetrics += metric
                    } else {
                        importantMetrics += metric
                    }
                }
            }
        }

    }

    // this function DOES NOT have to account for overall
    // return an array of metrics
    override suspend fun calculateWeights(metrics: Array<Metric>): Array<Metric> {
        val overall = metricBackend.getMetricValueByName(accountBackend.currentUserId, "Overall")
        var sumImportant = 0
        var sumUnimportant = 0
        val totalNumberOfMetrics = unImportantMetrics.size + importantMetrics.size
//        val metrics = metricBackend.getMetricsById(accountBackend.currentUserId)
        determiningImportance(metrics)
        if (overallAccurate) {
            // initial weighting of reciprocal of ratio between number of important to unimportant metrics
            for (metric in unImportantMetrics) {
                sumUnimportant += metric.values.last().value.toInt()
                metric.values.last().weight = 1 / (importantMetrics.size / unImportantMetrics.size)
            }

            for (metric in importantMetrics) {
                sumImportant += metric.values.last().value.toInt()
                metric.values.last().weight = 1 / (unImportantMetrics.size / importantMetrics.size)
            }

            // multiplying weights by percentage of value each metric corresponds to in its given set to
            // get true metric weighting
            for (metric in unImportantMetrics) {
                var temp = metric.values.last().weight.toDouble() * metric.values.last().value.toDouble() / sumUnimportant
                metric.values.last().weight = temp
            }

            for (metric in importantMetrics) {
                var temp = metric.values.last().weight.toDouble() * metric.values.last().value.toDouble() / sumUnimportant
                metric.values.last().weight = temp
            }

            for (metric in metrics) {
                if (metric.name == "Overall") continue
                var found = false
                for (metric1 in importantMetrics) {
                    if (metric.name == metric1.name) {
                        val temp = metric.values.last().weight.toDouble() + smoothingFunction(metric1.values.last().tempWeight.toDouble()).toDouble()
                        metric.values.last().weight = smoothingFunction(metric1.values.last().tempWeight.toDouble())
                        found = true
                    }
                }
                if (found) continue
                for (metric1 in unImportantMetrics) {
                    if (metric.name == metric1.name) {
                        val temp = metric.values.last().weight.toDouble() - smoothingFunction(metric1.values.last().tempWeight.toDouble()).toDouble()
                        metric.values.last().weight = smoothingFunction(metric1.values.last().tempWeight.toDouble())
                        found = true
                    }
                }
            }
            return metrics
        }

        var minDiff = 11
        var maxDiff = 0
        var closest: ArrayList<Metric> = ArrayList<Metric>()
        var furthest: ArrayList<Metric> = ArrayList<Metric>()

        // figuring out least and most important metrics
        for (metric in importantMetrics) {
           if (abs(metric.values.last().value.toInt() - overall!!.toInt()) < minDiff) {
               closest.clear()
               closest += metric
               minDiff = abs(metric.values.last().value.toInt() - overall!!.toInt())
           } else if (abs(metric.values.last().value.toInt() - overall!!.toInt()) == minDiff) {
               closest += metric
           }
        }

        for (metric in unImportantMetrics) {
            if (abs(metric.values.last().value.toInt() - overall!!.toInt()) > maxDiff) {
                furthest.clear()
                furthest += metric
                maxDiff = abs(metric.values.last().value.toInt() - overall!!.toInt())
            } else if (abs(metric.values.last().value.toInt() - overall!!.toInt()) == maxDiff) {
                furthest += metric
            }
        }

        val mostImportantMetric: Metric = closest.random()
        val leastImportantMetric: Metric = furthest.random()
        var total: Double = 0.0 // gonna be used to keep track of total for final coef calculation

        mostImportantMetric.values.last().tempWeight = 1
        leastImportantMetric.values.last().tempWeight = 1 / maxDiff

        // val highTempWeight = mostImportantMetric.values.last().tempWeight
        val lowTempWeight = leastImportantMetric.values.last().tempWeight

        for (metric in importantMetrics) {
            if (metric.name == mostImportantMetric.name) continue
            var temp = (mostImportantMetric.values.last().value.toDouble() +
                    lowTempWeight.toDouble() *
                    leastImportantMetric.values.last().value.toDouble()) /
                    metric.values.last().value.toDouble()
            if (temp > 1) temp = 1/temp.toDouble()
            metric.values.last().tempWeight = temp
            total += metric.values.last().value.toDouble() * metric.values.last().tempWeight.toDouble()
        }

        for (metric in unImportantMetrics) {
            if (metric.name == leastImportantMetric.name) continue
            var temp = (mostImportantMetric.values.last().value.toDouble() +
                    lowTempWeight.toDouble() *
                    leastImportantMetric.values.last().value.toDouble()) /
                    metric.values.last().value.toDouble()
            if (temp > 1) temp = 1/temp.toDouble()
            metric.values.last().tempWeight = temp
            total += metric.values.last().value.toDouble() * metric.values.last().tempWeight.toDouble()
        }

        val coef: Double = totalNumberOfMetrics * overall!!.toDouble() / total

        for (metric in importantMetrics) {
            val temp = metric.values.last().tempWeight.toDouble() * coef
            metric.values.last().tempWeight = temp
        }

        for (metric in unImportantMetrics) {
            val temp = metric.values.last().tempWeight.toDouble() * coef
            metric.values.last().tempWeight = temp
        }


        // need a function to set each metrics weight

        // should update weights properly
        for (metric in metrics) {
            if (metric.name == "Overall") continue
            var found = false
            for (metric1 in importantMetrics) {
                if (metric.name == metric1.name) {
                    val temp = metric.values.last().weight.toDouble() + smoothingFunction(metric1.values.last().tempWeight.toDouble()).toDouble()
                    metric.values.last().weight = smoothingFunction(metric1.values.last().tempWeight.toDouble())
                    found = true
                }
            }
            if (found) continue
            for (metric1 in unImportantMetrics) {
                if (metric.name == metric1.name) {
                    val temp = metric.values.last().weight.toDouble() - smoothingFunction(metric1.values.last().tempWeight.toDouble()).toDouble()
                    metric.values.last().weight = smoothingFunction(metric1.values.last().tempWeight.toDouble())
                }
            }
        }

        return metrics


    }

    override suspend fun smoothingFunction(weight: Double) : Number {
        return (5 * (1 / (1 + exp(-0.5 * weight))) - 2.5)
    }

    override suspend fun generateWeights(metrics: Array<Metric>) : Array<Double> {
        var weights : Array<Double> = emptyArray()
        for (metric in metrics) {
            weights += 2.0
        }
        return weights
    }
}

