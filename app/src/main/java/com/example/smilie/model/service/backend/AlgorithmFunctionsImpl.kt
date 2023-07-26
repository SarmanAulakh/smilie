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


open class AlgorithmFunctionsImpl @Inject constructor(
    open val metricBackend: MetricBackend,
    open val accountBackend: AccountBackend,
) : AlgorithmFunctions {


    override var importantMetrics: ArrayList<Metric> = ArrayList<Metric>()
    override var unImportantMetrics: ArrayList<Metric> = ArrayList<Metric>()
    override var overallAccurate: Boolean = false

    // This function has to account for overall
    override suspend fun trueOverallValue(metrics: ArrayList<Metric>): Double {
        var avg: Double = 0.0
        for (metric in metrics) {
            if (metric.name == "Overall") continue
            Log.d("metricVal", "this is the metric val" + metric.values.last().value)
            avg += (metric.values.last().value).toInt()
        }

        return avg / (metrics.size - 1)
    }

    // this function has to account for overall
    override suspend fun determiningImportance(metrics: ArrayList<Metric>) {
        val trueAverage = trueOverallValue(metrics)
//        val overall = metricBackend.getMetricValueByName(accountBackend.currentUserId, "Overall")
        var overall = 0
        for (metric in metrics) {
            if (metric.name == "Overall") {
                overall = metric.values.last().value.toInt()
                break
            }
        }
        if (overall == -1) {
            Log.d("Overall doesn't exist", "Error: no overall value")
            return
        }
        Log.d("trueAverage", "this is true average $trueAverage")
        Log.d("overall", "this is overall $overall")
        if (trueAverage > overall!!.toDouble()) {
            for (metric in metrics) {
                if (metric.name == "Overall") continue
                if (metric.values.last().value.toInt() > trueAverage) {
                    unImportantMetrics += metric
                } else if (metric.values.last().value.toInt() < trueAverage) {
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
                if (metric.values.last().value.toInt() < trueAverage) {
                    unImportantMetrics += metric
                } else if (metric.values.last().value.toInt() > trueAverage) {
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
                if (metric.values.last().value.toInt() > trueAverage) {
                    unImportantMetrics += metric
                } else if (metric.values.last().value.toInt() < trueAverage) {
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
    override suspend fun calculateWeights(metrics: ArrayList<Metric>): ArrayList<Metric> {
//        val overall = metricBackend.getMetricValueByName(accountBackend.currentUserId, "Overall")
        var overall = 0
        for (metric in metrics) {
            if (metric.name == "Overall") {
                overall = metric.values.last().value.toInt()
                break
            }
        }
        var sumImportant = 0
        var sumUnimportant = 0

//        val metrics = metricBackend.getMetricsById(accountBackend.currentUserId)
        determiningImportance(metrics)
        val totalNumberOfMetrics = unImportantMetrics.size + importantMetrics.size
        Log.d("important metrics", "these are the important metrics $importantMetrics")
        Log.d("unimportant metrics", "these are the unimportant metrics $unImportantMetrics")
        Log.d("overallAccurate", "this is overallAccurate $overallAccurate")
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

            Log.d("important metrics2", "these are the important metrics2 $importantMetrics")
            Log.d("unimportant metrics2", "these are the unimportant metrics2 $unImportantMetrics")

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

            Log.d("important metrics2", "these are the important metrics2 $importantMetrics")
            Log.d("unimportant metrics2", "these are the unimportant metrics2 $unImportantMetrics")

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

        Log.d("important metrics3", "these are the important metrics3 $importantMetrics")
        Log.d("unimportant metrics3", "these are the unimportant metrics3 $unImportantMetrics")
        var minDiff = 11
        var maxDiff = 0
        var closest: ArrayList<Metric> = ArrayList<Metric>()
        var furthest: ArrayList<Metric> = ArrayList<Metric>()

        // figuring out least and most important metrics
        Log.d("Overall rating", "this is the overall rating$overall")
        for (metric in importantMetrics) {
           if (abs(metric.values.last().value.toInt() - overall!!.toInt()) < minDiff) {
               closest.clear()
               closest += metric
               minDiff = abs(metric.values.last().value.toInt() - overall!!.toInt())
           } else if (abs(metric.values.last().value.toInt() - overall!!.toInt()) == minDiff) {
               closest += metric
           }
        }

        Log.d("closest", "these are the closest metrics3 $closest")

        for (metric in unImportantMetrics) {
            if (abs(metric.values.last().value.toInt() - overall!!.toInt()) > maxDiff) {
                furthest.clear()
                furthest += metric
                maxDiff = abs(metric.values.last().value.toInt() - overall!!.toInt())
            } else if (abs(metric.values.last().value.toInt() - overall!!.toInt()) == maxDiff) {
                furthest += metric
            }
        }

        Log.d("furthest", "these are the furthest metrics3 $furthest")

        val mostImportantMetric: Metric = closest.random()
        val leastImportantMetric: Metric = furthest.random()
        var total: Double = 0.0 // gonna be used to keep track of total for final coef calculation

        Log.d("mostimportant", "these are the mostimportant metrics3 $mostImportantMetric")
        Log.d("leastimportant", "these are the leastimportant metrics3 $leastImportantMetric")
        mostImportantMetric.values.last().tempWeight = 1
        leastImportantMetric.values.last().tempWeight = 1 / maxDiff

        Log.d("maxzDiff", "This is maxdiff $maxDiff")

        // val highTempWeight = mostImportantMetric.values.last().tempWeight
        val lowTempWeight : Double = 1.0 / maxDiff

        for (metric in importantMetrics) {
            if (metric.name == mostImportantMetric.name) {
                metric.values.last().tempWeight = 1
                continue
            }
            var temp = (mostImportantMetric.values.last().value.toDouble() +
                    lowTempWeight.toDouble() *
                    leastImportantMetric.values.last().value.toDouble()) /
                    metric.values.last().value.toDouble()
            if (temp > 1) temp = 1/temp.toDouble()
            Log.d("temp", "this is temp $temp")
            metric.values.last().tempWeight = temp
            Log.d("updating", "please tell me this is updating " + metric.values.last().tempWeight)
            total += metric.values.last().value.toDouble() * metric.values.last().tempWeight.toDouble()
        }

        for (metric in unImportantMetrics) {
            if (metric.name == leastImportantMetric.name) {
                Log.d("leastimportantupdate", "updating leas important metric tempweight")
                Log.d("lowTempWeight", "This is lowtempweight $lowTempWeight")
                metric.values.last().tempWeight = lowTempWeight
                continue
            }
            var temp = (mostImportantMetric.values.last().value.toDouble() +
                    lowTempWeight.toDouble() *
                    leastImportantMetric.values.last().value.toDouble()) /
                    metric.values.last().value.toDouble()
            if (temp > 1) temp = 1/temp.toDouble()
            Log.d("temp", "this is temp2 $temp")
            metric.values.last().tempWeight = temp
            Log.d("updating", "please tell me this is updating " + metric.values.last().tempWeight)
            total += metric.values.last().value.toDouble() * metric.values.last().tempWeight.toDouble()
        }

        Log.d("important metrics4", "these are the important metrics4 $importantMetrics")
        Log.d("unimportant metrics4", "these are the unimportant metrics4 $unImportantMetrics")

        val coef: Double = totalNumberOfMetrics * overall!!.toDouble() / total

        Log.d("coeff", "this is coeff $coef")

        for (metric in importantMetrics) {
            val temp = metric.values.last().tempWeight.toDouble() * coef
            metric.values.last().tempWeight = temp
        }

        for (metric in unImportantMetrics) {
            val temp = metric.values.last().tempWeight.toDouble() * coef
            metric.values.last().tempWeight = temp
        }

        Log.d("important metrics5", "these are the important metrics5 $importantMetrics")
        Log.d("unimportant metrics5", "these are the unimportant metrics5 $unImportantMetrics")
        // need a function to set each metrics weight

        // should update weights properly
        for (metric in metrics) {
            if (metric.name == "Overall") continue
            var found = false
            for (metric1 in importantMetrics) {
                if (metric.name == metric1.name) {
                    val temp = metric.values[metric.values.size - 2].weight.toDouble() + smoothingFunction(metric.values.last().tempWeight.toDouble()).toDouble()
                    metric.values.last().weight = temp
                    found = true
                }
            }
            if (found) continue
            for (metric1 in unImportantMetrics) {
                if (metric.name == metric1.name) {
                    val temp = metric.values[metric.values.size - 2].weight.toDouble() - smoothingFunction(metric1.values.last().tempWeight.toDouble()).toDouble()
                    metric.values.last().weight = temp
                }
            }
        }

        return metrics


    }

    override suspend fun smoothingFunction(weight: Double) : Number {
        return abs(2 * (1 / (1 + exp(-0.5 * weight))) - 1)
    }


}

