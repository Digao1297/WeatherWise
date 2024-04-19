package com.example.weatherwise.ui.feature

import android.Manifest
import android.R
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.launch

@Composable
fun RequestPermission(
    activity: ComponentActivity,
    onGo: @Composable (contentPadding: PaddingValues) -> Unit
) {
    val locationPermissionAlreadyGranted = ContextCompat.checkSelfPermission(
        LocalContext.current, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var shouldShowPermissionRationale by remember {
        mutableStateOf(
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity, Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    val shouldDirectUserToApplicationSettings by remember {
        mutableStateOf(false)
    }

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                val locationPermissionsGranted =
                    permissions.values.reduce { acc, isPermissionGranted ->
                        acc && isPermissionGranted
                    }

                if (!locationPermissionsGranted) {
                    shouldShowPermissionRationale =
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            activity, Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                }

            })

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START && !locationPermissionAlreadyGranted
                && !shouldShowPermissionRationale
            ) {
                locationPermissionLauncher.launch(locationPermissions)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }

    })

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    if (shouldShowPermissionRationale) {
        LaunchedEffect(Unit) {
            scope.launch {
                val userAction = snackBarHostState.showSnackbar(
                    message = "Por favor autorize a permissão de localização",
                    actionLabel = "Approve",
                    duration = SnackbarDuration.Indefinite,
                    withDismissAction = true,
                )
                when (userAction) {
                    SnackbarResult.ActionPerformed -> {
                        shouldShowPermissionRationale = false
                        locationPermissionLauncher.launch(locationPermissions)
                    }

                    SnackbarResult.Dismissed -> {
                        shouldShowPermissionRationale = false
                    }
                }
            }
        }
    }
    if (shouldDirectUserToApplicationSettings) {
        openApplicationSettings(LocalContext.current)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { contentPadding ->
        onGo(contentPadding)
    }

}

private fun openApplicationSettings(context: Context) {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", R.attr.packageNames.toString(), null)
    ).also {
        ContextCompat.startActivity(context, it, null)
    }
}

@Composable
fun ShowLocationPermissionRationale() {
    AlertDialog(onDismissRequest = {
        //Logic when dismiss happens
    }, title = {
        Text("Permission Required")
    }, text = {
        Text("You need to approve this permission in order to...")
    }, confirmButton = {
        TextButton(onClick = {
            //Logic when user confirms to accept permissions
        }) {
            Text("Confirm")
        }
    }, dismissButton = {
        TextButton(onClick = {
            //Logic when user denies to accept permissions
        }) {
            Text("Deny")
        }
    })
}








