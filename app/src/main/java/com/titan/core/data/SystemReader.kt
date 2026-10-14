package com.titan.core.data

import java.io.RandomAccessFile

object SystemReader {
    
    private var lastCpuIdle = 0L
    private var lastCpuTotal = 0L

    fun getCpuUsage(): Float {
        return try {
            val reader = RandomAccessFile("/proc/stat", "r")
            val load = reader.readLine()
            reader.close()
            
            val toks = load.split("\\s+".toRegex())
            if (toks.size > 7) {
                val idle1 = toks[4].toLong()
                val cpu1 = toks[2].toLong() + toks[3].toLong() + toks[5].toLong() + toks[6].toLong() + toks[7].toLong()
                
                val idle2 = toks[4].toLong()
                val cpu2 = toks[2].toLong() + toks[3].toLong() + toks[5].toLong() + toks[6].toLong() + toks[7].toLong()
                
                val totalDiff = (cpu2 + idle2) - (cpu1 + idle1)
                val idleDiff = idle2 - idle1
                
                if (totalDiff > 0) {
                    (100f * (totalDiff - idleDiff) / totalDiff)
                } else 0f
            } else 0f
        } catch (e: Exception) {
            0f
        }
    }

    data class RamInfo(val total: Long, val used: Long, val free: Long)

    fun getRamInfo(): RamInfo {
        return try {
            val reader = RandomAccessFile("/proc/meminfo", "r")
            var total = 0L
            var free = 0L
            var buffers = 0L
            var cached = 0L
            
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                when {
                    line!!.startsWith("MemTotal:") -> total = line!!.split("\\s+".toRegex())[1].toLong()
                    line!!.startsWith("MemFree:") -> free = line!!.split("\\s+".toRegex())[1].toLong()
                    line!!.startsWith("Buffers:") -> buffers = line!!.split("\\s+".toRegex())[1].toLong()
                    line!!.startsWith("Cached:") -> cached = line!!.split("\\s+".toRegex())[1].toLong()
                }
            }
            reader.close()
            
            val actualFree = free + buffers + cached
            val actualUsed = total - actualFree
            RamInfo(total / 1024, actualUsed / 1024, actualFree / 1024) // Convert to MB
        } catch (e: Exception) {
            RamInfo(0, 0, 0)
        }
    }
}
