import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlin.math.*
import java.lang.Math
import com.example.smilie.model.User
import com.example.smilie.model.Metric
import com.example.smilie.model.service.backend.MetricBackend
import com.example.smilie.model.service.backend.AccountBackend
import com.example.smilie.model.service.backend.UserBackend
import com.example.smilie.model.view.SmilieViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainUserFunctions @Inject constructor(
    private val userBackend: UserBackend,
    private val metricBackend: MetricBackend,
    private val accountBackend: AccountBackend,
    private val auth: FirebaseAuth
) : SmilieViewModel() {

    var userData: MutableState<User?> = mutableStateOf(null)
    var userIdToken: MutableState<String?> = mutableStateOf(null)
    var metricData: MutableState<ArrayList<Metric>?> = mutableStateOf(null)

//    init {
//        determiningImportance()
//        smoothingFunction()
//        smoothWeights()
//        trueOverallValue()
//    }

    var importantMetrics: Array<Metric> = arrayOf()
    var unImportantMetrics: Array<Metric> = arrayOf()
    var overallAccurate: Boolean = false

    fun trueOverallValue(metrics: Array<Metric>): Double {
        var avg: Double = 0.0
        for (metric in metrics) {
            if (metric.name == "Overall") continue
            avg += (metric.values[metric.values.size - 1].value).toInt()
        }

        return avg / metrics.size - 1
    }

    suspend fun determiningImportance(metrics: Array<Metric>) {
        val trueAverage = trueOverallValue(metrics)
        val overall = metricBackend.getMetricValueByName(accountBackend.currentUserId, "Overall")
        if (overall == -1) {
            Log.d("Overall doesn't exist", "Error: no overall value")
            return
        }
        if (trueAverage > overall!!.toDouble()) {
            for (metric in metrics) {
                if (metric.values[metric.values.size - 1].value.toInt() < overall!!.toDouble()) {
                    unImportantMetrics += metric
                } else {
                    importantMetrics += metric
                }
            }
        } else if (trueAverage < overall!!.toDouble()) {
            for (metric in metrics) {
                if (metric.values[metric.values.size - 1].value.toInt() > overall!!.toDouble()) {
                    unImportantMetrics += metric
                } else {
                    importantMetrics += metric
                }
            }
        } else {
            // special calculations, use overallAccurate to check
            overallAccurate = true
        }

    }

    fun smoothWeights(weights: DoubleArray): Array<Double> {
        var updatedWeights = emptyArray<Double>()
        for (weight in weights) {
            updatedWeights += smoothingFunction(weight)
        }
        return updatedWeights
    }

    fun smoothingFunction(weight: Double): Double {
        return 5 * (1 / (1 + exp(-0.5 * weight))) - 2.5
    }
}