package com.mahozi.sayed.talabiya.core.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mahozi.sayed.talabiya.core.ActionsState
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.DbBackupManager
import com.mahozi.sayed.talabiya.core.DbBackupResult
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.SettingsStore
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class MainPresenter @Inject constructor(
  private val dbBackupManager: DbBackupManager,
  private val settingsStore: SettingsStore,
) : Presenter<MainEvent, MainUiModel> {

  @Composable
  override fun start(events: Flow<MainEvent>): MainUiModel {
    var creatingBackup by remember { mutableStateOf(false) }

    val actions = remember { ActionsState<MainAction>() }

    CollectEvents(events) { event ->
      when (event) {
        MainEvent.CreateBackup -> {
          launch {
            creatingBackup = true
            val result = dbBackupManager.createBackup()
            creatingBackup = false
            when (result) {
              is DbBackupResult.Error -> { /* TODO handle error */ }
              DbBackupResult.NoDir -> actions.send(MainAction.OpenDirectoryPicker)
              DbBackupResult.Success -> { /*TODO handle success*/ }
            }
          }
        }
        is MainEvent.SaveBackupDir -> {
          launch {
            settingsStore.setDbBackupDir(event.dir)
          }
        }
      }
    }

    return MainUiModel(
      creatingBackup = creatingBackup,
      actions = actions
    )
  }
}