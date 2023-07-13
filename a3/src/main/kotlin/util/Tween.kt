package util

/*
 * Linear interpolation from start to end, using
 * time factor t (t should be in [0, 1])
 */
fun lerp(start: Double, end: Double, t: Double): Double {
    return start + (end - start) * t
}

// Tweening class
class Tween (
    var startValue: Double, // starting value for our tween (keyframe 1)
    var endValue: Double,   // ending value for our tween (keyframe 2)
    val time: Long,         // time is duration of the tween (in ms)
    val easing: (x: Double) -> Double = { x -> x }  // easing function (default is linear)
) {
    // tweened value
    var value: Double = startValue
        private set

    // use null as the not running flag
    var startTime: Long? = null

    // start the tween
    fun start() {
        // get start time for this tween
        startTime = System.nanoTime()
        // initialize tweened value
        value = startValue
    }

    val isRunning: Boolean
        get() = (startTime != null)

    fun update(currentTime: Long) {
        // check if animation is running
        if (startTime != null) {
            // elapsed time (converted to milliseconds)
            val dt = (currentTime - (startTime ?: 0)) / 1e6
            // proportion of time completed
            val t = dt / time
            // interpolate value
            value = lerp(startValue, endValue, easing(t))

            // if animation ended
            if (dt > time) {
                value = endValue // snap to final value
                startTime = null
            }
        }
    }
    fun end() {
        // set start time be null
        startTime = null
//        endValue = value
    }
}