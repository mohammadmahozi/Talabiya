package com.mahozi.sayed.talabiya.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsStore @Inject constructor(
  private val dataStore: DataStore<Preferences>,
) {

  val setUpdatedPriceAsDefaultKey = booleanPreferencesKey("setUpdatedPriceAsDefaultKey")

  val setNewPriceAsDefault: Flow<Boolean>
    get() = dataStore.data.map {
      it[setUpdatedPriceAsDefaultKey] ?: false
    }

  suspend fun setNewPriceAsDefault(setNewPriceAsDefault: Boolean) {
    dataStore.edit {
      it[setUpdatedPriceAsDefaultKey] = setNewPriceAsDefault
    }
  }
}