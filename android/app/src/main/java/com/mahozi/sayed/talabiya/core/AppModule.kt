package com.mahozi.sayed.talabiya.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.mahozi.sayed.talabiya.core.di.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn


@ContributesTo(AppScope::class)
interface AppModule {

  @SingleIn(AppScope::class)
  @Provides
  fun provideDataStore(context: Context): DataStore<Preferences> =
    PreferenceDataStoreFactory.create(
      produceFile = { context.preferencesDataStoreFile("Settings") }
    )
}