package com.mahozi.sayed.talabiya.core.permission

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi


//TODO check permissions correctness
object Permissions {
  val RecordAudio = arrayOf(Manifest.permission.RECORD_AUDIO)

  val Storage = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
    arrayOf(
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )
  } else {
    arrayOf()
  }

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  const val Notification = Manifest.permission.POST_NOTIFICATIONS
}