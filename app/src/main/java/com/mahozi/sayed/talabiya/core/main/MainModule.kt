package com.mahozi.sayed.talabiya.core.main

import android.content.Context
import com.mahozi.sayed.talabiya.core.extensions.locale
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import java.time.Clock
import java.util.Locale


@Module
@ContributesTo(MainScope::class)
object MainModule {

  @Provides
  fun provideDatabase(context: Context): Locale = context.locale

  @Provides
  fun provideClock() = Clock.systemDefaultZone()

}