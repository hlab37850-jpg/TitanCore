package com.titan.core.data

import android.net.TrafficStats

object NetworkReader {
    private var lastTx = TrafficStats.getTotalTxBytes()
    private var lastRx = TrafficStats.getTotalRxBytes()
    private var lastTime = System.currentTimeMillis()

    data class NetworkSpeed(val uploadKBs: Float, val downloadKBs: Float)

    fun getNetworkSpeed(): NetworkSpeed {
        val currentTx = TrafficStats.getTotalTxBytes()
        val currentRx = TrafficStats.getTotalRxBytes()
        val currentTime = System.currentTimeMillis()
        
        val timeDiffSec = (currentTime - lastTime) / 1000f
        if (timeDiffSec <= 0) return NetworkSpeed(0f, 0f)

        val txDiff = if (currentTx != TrafficStats.UNSUPPORTED && lastTx != TrafficStats.UNSUPPORTED) currentTx - lastTx else 0L
        val rxDiff = if (currentRx != TrafficStats.UNSUPPORTED && lastRx != TrafficStats.UNSUPPORTED) currentRx - lastRx else 0L

        val uploadKBs = (txDiff / 1024f) / timeDiffSec
        val downloadKBs = (rxDiff / 1024f) / timeDiffSec

        lastTx = currentTx
        lastRx = currentRx
        lastTime = currentTime

        return NetworkSpeed(uploadKBs, downloadKBs)
    }
}
