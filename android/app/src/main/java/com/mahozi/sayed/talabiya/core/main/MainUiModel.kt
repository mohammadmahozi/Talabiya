package com.mahozi.sayed.talabiya.core.main

import com.mahozi.sayed.talabiya.core.ActionsState

data class MainUiModel(
  val creatingBackup: Boolean,
  val actions: ActionsState<MainAction>
)

sealed interface MainEvent {
  data object CreateBackup : MainEvent
  data class SaveBackupDir(val dir: String) : MainEvent
}

sealed interface MainAction {
  data object OpenDirectoryPicker : MainAction
}
