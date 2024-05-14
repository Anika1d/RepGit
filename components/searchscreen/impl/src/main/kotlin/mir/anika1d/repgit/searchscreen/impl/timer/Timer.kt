package mir.anika1d.repgit.searchscreen.impl.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Timer(private val timeEvent: Long) {

    private val _time: MutableLiveData<Long> = MutableLiveData(0)
    private val _stateTimer: MutableLiveData<TimerState> = MutableLiveData(TimerState.End)
    val time: LiveData<Long>
        get() = _time
    val stateTimer: LiveData<TimerState>
        get() = _stateTimer

    private var event: (() -> Lazy<Unit>)? = null

    private var job: Job = CoroutineScope(Dispatchers.Default).launch {
        while (stateTimer.value == TimerState.Start) {
            checkTime()
            delay(1000)
        }
    }

    private suspend fun setJob() {
        job = CoroutineScope(Dispatchers.Default).launch {
            while (stateTimer.value == TimerState.Start) {
                checkTime()
                delay(1000)
            }
        }
    }

    suspend fun start(event: () -> Lazy<Unit>) {
        this.event = event
        reset()

    }

    private suspend fun reset() {
        _stateTimer.value = TimerState.Start
        _time.value = System.currentTimeMillis()
        if (job.isCancelled || !job.isActive) {
            setJob()
        }
    }


    private fun checkTime() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - _time.value!! >= timeEvent) {
            _stateTimer.postValue(TimerState.Wait)
            event?.invoke()?.value
            _stateTimer.postValue(TimerState.End)
        }
    }


    fun stop() {
        _stateTimer.postValue(TimerState.End)
        _time.postValue( System.currentTimeMillis())
    }

    fun setNewTime() {
        _time.value = System.currentTimeMillis()
    }

    fun destroy() {
        stop()
        job.cancel()
    }


}


sealed class TimerState {
    data object Start : TimerState()
    data object Wait : TimerState()
    data object End : TimerState()
}

