package com.mahozi.sayed.talabiya.core.permission

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.launcher.ActivityLauncher
import com.mahozi.sayed.talabiya.core.ui.components.TlbTextButton


@Composable
fun rememberPermissionLauncher(
  permissions: Array<String>,
  onGranted: () -> Unit,
  rationalTitle: String = stringResource(R.string.permission_required),
  rationalText: String = stringResource(R.string.permission_required_text),
  settingsTitle: String = rationalTitle,
  settingsText: String = rationalText,
): ActivityLauncher {
  val activity = LocalActivity.current!!

  var showRationale by remember { mutableStateOf(false) }
  var showSettings by remember { mutableStateOf(false) }

  val permissionLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) { result ->
    val granted = result.values.all { it }
    val shouldShowRational = result.keys.any { activity.shouldShowRequestPermissionRationale(it) }

    if (granted) {
      onGranted()
    } else if (shouldShowRational) {
      showRationale = true
    } else {
      showSettings = true
    }
  }

  if (showRationale) {
    RationalDialog(
      title = rationalTitle,
      text = rationalText,
      onDismissRequest = { showRationale = false },
      onOpenSettings = { showSettings = true }
    )
  }

  if (showSettings) {
    SettingsDialog(
      title = settingsTitle,
      text = settingsText,
      onDismissRequest = { showSettings = false },
      onOpenSettings = {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
          data = Uri.fromParts("package", activity.packageName, null)
        }
        activity.startActivity(intent)
      }
    )
  }

  return remember { ActivityLauncher { permissionLauncher.launch(permissions) } }
}

@Composable
private fun RationalDialog(
  title: String,
  text: String,
  onDismissRequest: () -> Unit,
  onOpenSettings: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismissRequest,
    title = { Text(title) },
    text = { Text(text) },
    confirmButton = {
      TlbTextButton(
        onClick = { onOpenSettings(); onDismissRequest() },
        text = stringResource(R.string.allow)
      )
    },
    dismissButton = {
      TlbTextButton(
        onClick = onDismissRequest,
        text = stringResource(R.string.cancel)
      )
    }
  )
}

@Composable
private fun SettingsDialog(
  title: String,
  text: String,
  onDismissRequest: () -> Unit,
  onOpenSettings: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismissRequest,
    title = { Text(title) },
    text = { Text(text) },
    confirmButton = {
      TlbTextButton(
        onClick = { onOpenSettings(); onDismissRequest() },
        text = stringResource(R.string.settings)
      )
    },
    dismissButton = {
      TlbTextButton(
        onClick = onDismissRequest,
        text = stringResource(R.string.cancel)
      )
    }
  )
}