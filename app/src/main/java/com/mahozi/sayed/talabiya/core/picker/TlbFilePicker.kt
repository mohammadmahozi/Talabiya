package com.mahozi.sayed.talabiya.core.picker

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.mahozi.sayed.talabiya.R
import java.io.File


data class CameraImageOption(
  val fileName: String = "img_${System.currentTimeMillis()}.png",
)

data class CameraVideoOption(
  val fileName: String = "vid_${System.currentTimeMillis()}"
)

data class GalleryOption(
  val type: MediaType = MediaType.Image,
  val allowedImageTypes: List<String> = listOf("image/*"),
  val allowedVideoTypes: List<String> = listOf("video/*"),
)

data class FileOption(
  val allowedTypes: List<String> = listOf("*/*")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TlbFilePicker(
  onDismissRequest: () -> Unit,
  onResult: (Uri?) -> Unit,
  modifier: Modifier = Modifier,
  cameraImage: CameraImageOption? = null,
  cameraVideo: CameraVideoOption? = null,
  gallery: GalleryOption? = null,
  file: FileOption? = null,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
    modifier = modifier,
  ) {
    Column {
      if (gallery != null) {
        val galleryLauncher = rememberGalleryLauncher(
          option = gallery,
          onResult = { onResult(it) }
        )
        Option(
          stringId = R.string.gallery,
          icon = R.drawable.ic_image,
          onClick = { galleryLauncher.launch() }
        )
      }

      if (cameraImage != null) {
        val takeImageLauncher = rememberCameraImageLauncher(
          option = cameraImage,
          onResult = {
            onResult(it)
          }
        )

        Option(
          stringId = R.string.camera,
          icon = R.drawable.ic_camera,
          onClick = { takeImageLauncher.launch() }
        )
      }

      if (cameraVideo != null) {
        val captureVideoLauncher = rememberCameraVideoLauncher(
          option = cameraVideo,
          onResult = { onResult(it) }
        )

        Option(
          stringId = R.string.video,
          icon = R.drawable.ic_capture_video,
          onClick = { captureVideoLauncher.launch() }
        )
      }

      if (file != null) {
        val filePicker = rememberLauncherForActivityResult(
          contract = ActivityResultContracts.OpenDocument(),
        ) { uri ->
          if (uri != null) {
            onResult(uri)
            onDismissRequest()
          }
        }

        Option(
          stringId = R.string.file,
          icon = R.drawable.ic_file,
          onClick = {
            filePicker.launch(file.allowedTypes.toTypedArray())
          }
        )
      }
    }
  }
}

@Composable
private fun Option(
  @StringRes stringId: Int,
  @DrawableRes icon: Int,
  onClick: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
  ) {
    Row(
      modifier = Modifier.padding(16.dp)
    ) {
      Icon(
        painter = painterResource(icon),
        null,
        tint = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.size(24.dp)
      )

      Spacer(
        modifier = Modifier
          .width(32.dp)
      )

      Text(
        text = stringResource(stringId),
        color = MaterialTheme.colorScheme.onSurface,
      )
    }

    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainer)
  }
}

internal val authority: String
  get() = "com.mahozi.talabiya.fileprovider"

internal fun Context.createImageUri(
  fileName: String = "img_${System.currentTimeMillis()}.png"
): Uri {
  val type = Environment.DIRECTORY_PICTURES
  val base = "${Environment.getExternalStoragePublicDirectory(type)}/Talabiya"
  val dir = File(base)
  if(!dir.exists()) dir.mkdirs()
  val file = File(dir, fileName)
  return FileProvider.getUriForFile(this, authority, file)
}

internal fun Context.createVideoUri(
  fileName: String = "vid_${System.currentTimeMillis()}.mp4"
): Uri {
  val type = Environment.DIRECTORY_MOVIES
  val base = "${Environment.getExternalStoragePublicDirectory(type)}/Talabiya"
  val dir = File(base)
  if(!dir.exists()) dir.mkdirs()
  val file = File(dir, fileName)
  return FileProvider.getUriForFile(this, authority, file)
}
