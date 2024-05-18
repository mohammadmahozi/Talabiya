package com.mahozi.sayed.talabiya.resturant.option

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.Preview
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.AddFab
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.resturant.store.RestaurantStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.parcelize.Parcelize

@Parcelize
data class OptionsScreen(
  val restaurantId: Long,
): Screen

data class OptionsState(
  val foodOptions: List<FoodOption>,
)

sealed interface OptionsEvent {
  object AddOption: OptionsEvent
}

class OptionsPresenter @AssistedInject constructor(
  @Assisted private val restaurantId: Long,
  private val restaurantStore: RestaurantStore,
): Presenter<OptionsEvent, OptionsState> {

  @Composable override fun start(events: Flow<OptionsEvent>): OptionsState {
    val options by remember { restaurantStore.getOptions(restaurantId) }.collectAsState(initial = emptyList())
    CollectEvents(events) {
      when(it) {
        OptionsEvent.AddOption -> TODO()
      }
    }

    return OptionsState(options)
  }

  @AssistedFactory interface Factory {
    fun create(restaurantId: Long): OptionsPresenter
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
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = modifier
        .padding(paddingValues)
        .padding(16.dp)
    ) {
      items(state.foodOptions) {
        FoodOption(it)
      }
    }
  }
}

@Preview(showBackground = true)
@Composable private fun PreviewFoodOption() {
  Preview {
    FoodOption(
      FoodOption(
        0,
        "Name",
        listOf(FoodOption.Category("Name", true))
      )
    )
  }
}
@Composable private fun FoodOption(option: FoodOption) {
  Column {
    Text(text = option.name)

    val categories = option.categories.joinToString(", ") { it.name }
    Text(
      text = categories,
      fontSize = 12.sp,
      color = AppTheme.colors.secondaryText)
  }
}