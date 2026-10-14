package com.titan.core.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.titan.core.ui.components.SystemModule
import com.titan.core.ui.theme.NeonCyan
import com.titan.core.ui.theme.NeonGreen
import com.titan.core.ui.theme.NeonRed
import com.titan.core.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {
    val cpu by viewModel.cpuUsage.collectAsState()
    val ram by viewModel.ramInfo.collectAsState()
    val net by viewModel.netSpeed.collectAsState()
    val sensor by viewModel.sensorData.collectAsState()
    val cpuHist by viewModel.cpuHistory.collectAsState()
    val netHist by viewModel.netHistory.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "// TITAN_CORE //",
            color = NeonCyan,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // CPU Module
        SystemModule(
            title = "CPU_LOAD",
            mainValue = String.format("%.1f%%", cpu),
            subValue = "KERNEL_THREAD_ACTIVE",
            graphData = cpuHist,
            graphMax = 100f,
            graphColor = NeonGreen
        )
        Spacer(modifier = Modifier.height(12.dp))

        // RAM Module
        val ramPercent = if (ram.total > 0) (ram.used.toFloat() / ram.total) * 100 else 0f
        SystemModule(
            title = "MEMORY_ALLOC",
            mainValue = "${ram.used}MB / ${ram.total}MB",
            subValue = String.format("LOAD: %.1f%% | FREE: %dMB", ramPercent, ram.free),
            graphData = List(30) { ramPercent }, // Static line for RAM as it changes slowly
            graphMax = 100f,
            graphColor = NeonCyan
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Network Module
        val totalNet = net.downloadKBs + net.uploadKBs
        SystemModule(
            title = "NET_TRAFFIC",
            mainValue = String.format("%.1f KB/s", totalNet),
            subValue = "TX: ${String.format("%.1f", net.uploadKBs)} | RX: ${String.format("%.1f", net.downloadKBs)}",
            graphData = netHist,
            graphMax = maxOf(netHist.maxOrNull() ?: 10f, 10f),
            graphColor = NeonRed
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Sensor Module
        Text(
            text = "GYRO_MATRIX",
            color = Color(0x66FFFFFF),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("X: ${String.format("%.2f", sensor.first)}", color = NeonGreen, fontSize = 14.sp)
            Text("Y: ${String.format("%.2f", sensor.second)}", color = NeonGreen, fontSize = 14.sp)
            Text("Z: ${String.format("%.2f", sensor.third)}", color = NeonGreen, fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "[ SYSTEM NOMINAL ]",
            color = Color(0x66FFFFFF),
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
