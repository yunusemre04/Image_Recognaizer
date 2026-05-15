package com.example.imagerecognaizer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.imagerecognaizer.R
import com.example.imagerecognaizer.cameraUtils.CameraPreview
import com.example.imagerecognaizer.cameraUtils.RealTimeImageAnalyzer

@Composable
fun RealTimeClassifierScreen(navController: NavController) {
    val analyzingText = stringResource(R.string.analyzing)
    var result by remember { mutableStateOf(analyzingText) }

    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(onClick = {
            navController.navigate("home") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = false
            }
            launchSingleTop = true
        } }, modifier = Modifier.align(Alignment.TopStart).zIndex(2f)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
        }
        CameraPreview(imageAnalyzer = RealTimeImageAnalyzer(LocalContext.current) {
            result = it
        })

        Text(
            text = result.replaceFirstChar { it.uppercaseChar() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(16.dp),
            color = Color.White,
            fontSize = 20.sp
        )
    }
}
