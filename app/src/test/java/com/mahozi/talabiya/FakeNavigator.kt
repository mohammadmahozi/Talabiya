package com.mahozi.talabiya

import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.core.navigation.Screen

class FakeNavigator: Navigator {

  private val screens = mutableListOf<Screen>()

  fun currentScreen(): Screen? {
    return screens.lastOrNull()
  }

  override fun goto(screen: Screen) {
    screens.add(screen)
  }

  override fun back(screen: Screen?) {
    screens.remove(screen)
  }

  override fun replaceAll(screen: Screen) {
    screens.clear()
    screens.add(screen)
  }
}