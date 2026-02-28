package com.mahozi.sayed.talabiya.core.main

import android.content.Context
import com.mahozi.sayed.talabiya.core.extensions.locale
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import java.time.Clock
import java.util.Locale


@ContributesTo(MainScope::class)
interface MainModule {

  @Provides
  fun provideDatabase(context: Context): Locale = context.locale

  @Provides
  fun provideClock(): Clock = Clock.systemDefaultZone()

}