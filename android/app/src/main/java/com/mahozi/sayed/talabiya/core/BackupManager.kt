package com.mahozi.sayed.talabiya.core

import android.content.Context
import android.provider.DocumentsContract
import androidx.core.net.toUri
import com.mahozi.sayed.talabiya.core.data.DataModule
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.Clock
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class DbBackupManager @Inject constructor(
  private val context: Context,
  private val settingsStore: SettingsStore,
  private val dispatcher: CoroutineDispatcher,
  private val clock: Clock,
) {

  suspend fun createBackup(): DbBackupResult {
    return withContext(dispatcher) {
      val backupDir = settingsStore.getDbBackupDir() ?: return@withContext DbBackupResult.NoDir
      val dbFile = context.getDatabasePath(DataModule.DB_NAME)

      val dateTime = LocalDateTime.now(clock).truncatedTo(ChronoUnit.MINUTES).toString()

      val docUri = DocumentsContract.buildDocumentUriUsingTree(
        backupDir.toUri(),
        DocumentsContract.getTreeDocumentId(backupDir.toUri())
      )
      try {
        val dbBackupUri = DocumentsContract.createDocument(
          /* content = */ context.contentResolver,
          /* parentDocumentUri = */ docUri,
          /* mimeType = */ "application/octet-stream",
          /* displayName = */ "Talabiya-backup-$dateTime.db"
        )
        context.contentResolver.openOutputStream(dbBackupUri!!).use { outputStream ->
          dbFile.inputStream().use { input -> input.copyTo(outputStream!!) }
        }
        DbBackupResult.Success
      } catch (e: Exception) {
        if (e is CancellationException) throw e
        DbBackupResult.Error(e)
      }
    }
  }
}

sealed interface DbBackupResult {
  data object Success : DbBackupResult
  data object NoDir : DbBackupResult
  data class Error(val throwable: Throwable) : DbBackupResult
}