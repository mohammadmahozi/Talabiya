package com.mahozi.sayed.talabiya.resturant.option

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.ui.components.AddFab
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.resturant.store.RestaurantStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class OptionsState(
  val foodOptions: List<FoodOption>,
)

sealed interface OptionsEvent {
  object AddOption: OptionsEvent
}

class OptionsPresenter @Inject constructor(
  private val restaurantStore: RestaurantStore,
): Presenter<OptionsEvent, OptionsState> {

  @Composable override fun start(events: Flow<OptionsEvent>): OptionsState {
    val options by remember { restaurantStore.options }.collectAsState(initial = emptyList())
    CollectEvents(events) {
      when(it) {
        OptionsEvent.AddOption -> TODO()
      }
    }

    return OptionsState(options)
  }
}

@Composable fun OptionsScreen(
  state: OptionsState,
  onEvent: (OptionsEvent) -> Unit,
  modifier: Modifier = Modifier,
) {
  Scaffold(
    topBar = {
      TalabiyaBar(R.string.options)
    },
    floatingActionButton = {
      AddFab {
        onEvent(OptionsEvent.AddOption)
      }
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .padding(paddingValues)
        .padding(16.dp)
    ) {

    }
  }
}

@Composable private fun FoodOption(option: FoodOption) {
  Column {
    Text(text = option.name)

    val categories = option.categories.joinToString(", ")
    Text(
      text = categories,
      fontSize = 12.sp,
      color = AppTheme.colors.secondaryText)
  }
}