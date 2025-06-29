package com.ashok.decode.arUco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ashok.decode.arUco.myui.MainScreen
import com.ashok.decode.arUco.ui.theme.DecodeArUcoTheme
import com.ashok.decode.arUco.viewmodel.MainViewModel
import com.journeyapps.barcodescanner.ScanContract

class MainActivity : ComponentActivity() {


    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register barcode scanner
        viewModel.barcodeLauncher = registerForActivityResult(ScanContract()) {
            viewModel.onBarcodeScanned(it.contents)
        }

        // Register permission handler
        viewModel.permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            viewModel.onPermissionResult(isGranted)
        }

        setContent {
            DecodeArUcoTheme {
                MainScreen(viewModel)
            }
        }

    }
}
