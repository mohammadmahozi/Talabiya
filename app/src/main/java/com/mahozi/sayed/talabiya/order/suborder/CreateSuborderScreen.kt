package com.mahozi.sayed.talabiya.order.suborder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.AddFab
import com.mahozi.sayed.talabiya.core.ui.components.SearchBar
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.resturant.menu.MenuItem
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize data class CreateSuborderScreen(
  val orderId: Long,
  val userid: Long,
): Screen

@Preview
@Composable private fun PreviewCreateSuborderScreen() {
  val menu = listOf(
    MenuItem(
      0,
      "Item 1",
      "Pastery",
      14.money
    )
  )
  AppTheme {
    CreateSuborderScreen(
      state = CreateSuborderState(menu),
      onEvent = {}
    )
  }
}

@Composable fun CreateSuborderScreen(
  state: CreateSuborderState,
  onEvent: (CreateSuborderEvent) -> Unit,
  modifier: Modifier = Modifier,
) {
  Scaffold(
    topBar = {
      SearchBar(
        title = R.string.menu,
        query = "",
        onSearchClicked = {},
        onQueryChanged = {},
      )
    },
    floatingActionButton = {
      AddFab {

      }
    }
  ) { paddingValues ->
    Row {
      val lazyState = rememberLazyListState()
      val scope = rememberCoroutineScope()

      LazyColumn(
        state = lazyState,
        modifier = modifier
          .weight(1F)
          .padding(paddingValues)
      ) {
        items(state.menuItems) {
          MenuItem(item = it, onItemClicked = {})
          Divider()
        }
      }

      Divider(modifier = Modifier
        .fillMaxHeight()
        .width(1.dp)
      )

      //TODO A big is causing state value inside the lambda to always be the first value (empty menu items)
      // So we will only show the index when it is no empty. Investigate later
      if (state.menuItems.isNotEmpty()) {
        AlphabetIndex(
          onLetterSelected = { letter ->
            val index = state.menuItems.map { item -> item.name.first().toString() }.indexOf(letter)
            if (index != -1) {
              scope.launch {
                lazyState.animateScrollToItem(index)
              }
            }
          }
        )
      }
    }
  }
}

@Composable private fun MenuItem(
  item: MenuItem,
  onItemClicked: (MenuItem) -> Unit,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clickable { onItemClicked(item) }
      .padding(vertical = 12.dp, horizontal = 16.dp)
  ) {

    Text(
      text = item.name,
      style = AppTheme.types.title
    )

    Spacer(Modifier.weight(1F))

    Text(
      text = item.price.format(),
      style = AppTheme.types.title
    )
  }
}

@Composable private fun AlphabetIndex(
  onLetterSelected: (letter: String) -> Unit
) {
  val letters = stringResource(R.string.alphabet).split(" ")
  var selectedLetter by remember { mutableStateOf("") }
  var itemHeight by remember { mutableStateOf(0) }

  fun updateSelectedLetter(offset: Offset) {
    val indexOfTouchedLetter = (offset.y / itemHeight).toInt()
    selectedLetter = letters.getOrNull(indexOfTouchedLetter) ?: ""
    if (selectedLetter.isNotEmpty()) {
      onLetterSelected(selectedLetter)
    }
  }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxHeight()
      .onGloballyPositioned { coordinates ->
        val columnHeight = coordinates.size.height
        itemHeight = columnHeight / letters.size
      }
      .pointerInput(Unit) {
        detectTapGestures(
          onPress = { offset ->
            updateSelectedLetter(offset)
            awaitRelease()
            selectedLetter = ""
          }
        )
      }
      .pointerInput(Unit) {
        detectDragGestures(
          onDrag = { change, _ ->
            updateSelectedLetter(change.position)
          },
          onDragEnd = {
            selectedLetter = ""
          }
        )
      }
  ) {
    letters.forEach {
      Text(
        text = it,
        fontSize = 11.sp,
        modifier = Modifier
          .weight(1F)
          .padding(horizontal = 2.dp)
      )
    }
  }

  if (selectedLetter.isNotEmpty()) {
    Popup(
      alignment = Alignment.Center
    ) {
      Text(text = selectedLetter)
    }
  }
}

@Preview(showBackground = true)
@Composable private fun PreviewAddOrderItem() {
  AppTheme {
    AddOrderItem(1)
  }
}
@Composable private fun AddOrderItem(
  quantity: Int,
) {
    Column {
      Row {
        Text(
          text = stringResource(R.string.quantity),
          style = AppTheme.types.title
        )

        Spacer(Modifier.weight(1F))

        Row {
          Icon(
            painter = painterResource(R.drawable.ic_remove),
            contentDescription = null,
            modifier = Modifier
              .background(color = AppTheme.colors.backgroundSecondary, AppTheme.shapes.circle)
          )

          Text(
            text = quantity.toString(),
            style = AppTheme.types.title
          )

          Icon(
            painter = painterResource(R.drawable.ic_add_white_24dp),
            contentDescription = null,
            modifier = Modifier
              .background(color = AppTheme.colors.backgroundSecondary, shape = AppTheme.shapes.circle)
          )
        }
      }
  }
}

