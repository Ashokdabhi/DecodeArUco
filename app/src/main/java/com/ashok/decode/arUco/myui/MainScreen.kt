package com.ashok.decode.arUco.myui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ashok.decode.arUco.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val result = viewModel.scanResult.collectAsState()


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { viewModel.checkCameraPermission() }) {
                Text("Scan QR Code")

            }

            Spacer(modifier = Modifier.height(16.dp))

            if (result.value.isNotEmpty()) {
                Text("Scan complete: ${result.value}")
                viewModel.vibrateDevice()
            }
        }
    }
}

