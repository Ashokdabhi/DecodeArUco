package com.ashok.decode.arUco

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(private val app: Application) : AndroidViewModel(app) {
    private val _scanResult = MutableStateFlow("")
    val scanResult: StateFlow<String> = _scanResult

    lateinit var barcodeLauncher: ActivityResultLauncher<ScanOptions>
    lateinit var permissionLauncher: ActivityResultLauncher<String>

    fun onBarcodeScanned(result: String?) {
        if (result != null) {
            _scanResult.value = result
            Toast.makeText(app, "Scan complete\n$result", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(app, "Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchQRScanner() {
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt("Scan a QR Code")
            setCameraId(0)
            setBeepEnabled(false)
            setOrientationLocked(false)
        }
        barcodeLauncher.launch(options)
    }

    fun checkCameraPermission() {
        val permission = android.Manifest.permission.CAMERA
        when {
            app.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED -> {
                launchQRScanner()
            }

            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }

    fun onPermissionResult(granted: Boolean) {
        if (granted) {
            launchQRScanner()
        } else {
            Toast.makeText(app, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun vibrateDevice() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                app.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            app.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        val vibrationEffect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
        } else {
            Log.e("VIBRATION", "Unsupported OS version")
            return
        }

        vibrator.cancel()
        vibrator.vibrate(vibrationEffect)
    }
}