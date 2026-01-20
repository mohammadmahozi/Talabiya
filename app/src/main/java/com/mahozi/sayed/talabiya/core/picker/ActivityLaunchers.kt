package com.mahozi.sayed.talabiya.core.picker

import android.net.Uri
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.mahozi.sayed.talabiya.core.launcher.ActivityLauncher
import com.mahozi.sayed.talabiya.core.permission.Permissions
import com.mahozi.sayed.talabiya.core.permission.rememberPermissionLauncher


@Composable
fun rememberCameraImageLauncher(
  option: CameraImageOption,
  onResult: (Uri?) -> Unit,
): ActivityLauncher {
  val activity = LocalActivity.current
  var path by rememberSaveable { mutableStateOf(null as Uri?) }

  val takePictureLauncher =
    rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
      if (success) {
        onResult(path!!)
      } else {
        onResult(null)
      }
    }

  val permissionLauncher = rememberPermissionLauncher(
    permissions = Permissions.Storage,
    onGranted = {
      path = activity!!.createImageUri(option.fileName)
      takePictureLauncher.launch(path!!)
    },
  )

  return remember { ActivityLauncher { permissionLauncher.launch() } }
}

@Composable
fun rememberCameraVideoLauncher(
  option: CameraVideoOption,
  onResult: (Uri?) -> Unit
): ActivityLauncher {

  val activity = LocalActivity.current
  var path by rememberSaveable { mutableStateOf(null as Uri?) }

  val takePictureLauncher =
    rememberLauncherForActivityResult(ActivityResultContracts.CaptureVideo()) { success ->
      if (success) {
        onResult(path!!)
      } else {
        onResult(null)
      }
    }

  val permissionLauncher = rememberPermissionLauncher(
    permissions = Permissions.Storage,
    onGranted = {
      path = activity!!.createVideoUri(option.fileName)
      takePictureLauncher.launch(path!!)
    },
  )

  return remember { ActivityLauncher { permissionLauncher.launch() } }
}

@Composable
fun rememberGalleryLauncher(
  option: GalleryOption,
  onResult: (Uri?) -> Unit
): ActivityLauncher {

  val mediaPicker = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia(),
  ) { uri ->
    if (uri != null) onResult(uri)
  }

  val mediaType = when (option.type) {
    MediaType.Image -> ActivityResultContracts.PickVisualMedia.ImageOnly
    MediaType.Video -> ActivityResultContracts.PickVisualMedia.VideoOnly
    MediaType.ImageAndVideo -> ActivityResultContracts.PickVisualMedia.ImageAndVideo
  }

  val request = PickVisualMediaRequest
    .Builder()
    .setMediaType(mediaType)
    .build()

  return ActivityLauncher { mediaPicker.launch(request) }
}