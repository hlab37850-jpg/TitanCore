package com.titan.core.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.titan.core.data.NetworkReader
import com.titan.core.data.SystemReader
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application), SensorEventListener {

    private val sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val _cpuUsage = MutableStateFlow(0f)
    val cpuUsage: StateFlow<Float> = _cpuUsage

    private val _ramInfo = MutableStateFlow(SystemReader.RamInfo(0,0,0))
    val ramInfo: StateFlow<SystemReader.RamInfo> = _ramInfo

    private val _netSpeed = MutableStateFlow(NetworkReader.NetworkSpeed(0f, 0f))
    val netSpeed: StateFlow<NetworkReader.NetworkSpeed> = _netSpeed

    private val _sensorData = MutableStateFlow(Triple(0f, 0f, 0f))
    val sensorData: StateFlow<Triple<Float, Float, Float>> = _sensorData

    // History for graphs (Last 30 seconds)
    private val _cpuHistory = MutableStateFlow(List(30) { 0f })
    val cpuHistory: StateFlow<List<Float>> = _cpuHistory

    private val _netHistory = MutableStateFlow(List(30) { 0f })
    val netHistory: StateFlow<List<Float>> = _netHistory

    init {
        startTelemetry()
        registerSensors()
    }

    private fun startTelemetry() {
        viewModelScope.launch {
            while (isActive) {
                val cpu = SystemReader.getCpuUsage()
                _cpuUsage.value = cpu
                _cpuHistory.value = _cpuHistory.value.drop(1) + cpu

                _ramInfo.value = SystemReader.getRamInfo()

                val net = NetworkReader.getNetworkSpeed()
                _netSpeed.value = net
                _netHistory.value = _netHistory.value.drop(1) + (net.downloadKBs + net.uploadKBs)

                delay(1000)
            }
        }
    }

    private fun registerSensors() {
        val gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        gyro?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            _sensorData.value = Triple(event.values[0], event.values[1], event.values[2])
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }
}
