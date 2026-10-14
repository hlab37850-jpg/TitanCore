package com.titan.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.titan.core.ui.theme.NeonGreen
import com.titan.core.ui.theme.TextDim

@Composable
fun SystemModule(
    title: String,
    mainValue: String,
    subValue: String,
    graphData: List<Float>,
    graphMax: Float,
    graphColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0A0A0A))
            .border(1.dp, Color(0xFF1A1A1A))
            .padding(12.dp)
    ) {
        Text(text = title, color = TextDim, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier.height(4.dp))
        Text(text = mainValue, color = NeonGreen, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = subValue, color = TextDim, fontSize = 12.sp)
        Spacer(modifier.height(8.dp))
        LiveGraph(data = graphData, maxValue = graphMax, lineColor = graphColor)
    }
}
