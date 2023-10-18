package com.mahozi.sayed.talabiya.order.suborder

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
      LazyColumn(
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

      AlphabetIndex()
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable private fun RowScope.AlphabetIndex() {
  val letters = stringResource(R.string.alphabet).split(" ")
  var touchedLetter by remember { mutableStateOf("") }
  var itemHeight by remember { mutableStateOf(0) }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxHeight()
      .onGloballyPositioned { coordinates ->
        val columnHeight = coordinates.size.height
        itemHeight = columnHeight / letters.size
        Log.d("gggg", "AlphabetIndex: column $columnHeight item $itemHeight")

      }.pointerInput(Unit) {
        detectTapGestures { offset ->
          Log.d("gggg", "AlphabetIndex: offset ${offset.y}")

          val indexOfTouchedLetter = (offset.y / itemHeight).toInt()
          touchedLetter = letters[indexOfTouchedLetter]
        }

        detectDragGestures { change, _ ->
          val indexOfTouchedLetter = (change.position.y / itemHeight).toInt()
          touchedLetter = letters[indexOfTouchedLetter]
        }
      }
  ) {
    letters.forEach {
      Text(
        text = it,
        fontSize = 11.sp,
        modifier = Modifier
          .weight(1F)
          .padding(horizontal = 2.dp)
//          .pointerInteropFilter { motionEvent ->
//            return@pointerInteropFilter when (motionEvent.action) {
//              MotionEvent.ACTION_DOWN -> {
//                Log.d("gggg", "AlphabetIndex: down")
//                touchedLetter = it
//                true
//              }
//              MotionEvent.ACTION_MOVE -> {
//                Log.d("gggg", "AlphabetIndex: move $it")
//                false
//
//              }
//              MotionEvent.ACTION_UP -> {
//                Log.d("gggg", "AlphabetIndex: up")
//                touchedLetter = ""
//                true
//              }
//              else -> false
//            }
//          }
      )
    }
  }

  if (touchedLetter.isNotEmpty()) {
    Log.d("gggg", "AlphabetIndex: pop")
    
    Popup(
      alignment = Alignment.Center
    ) {
      Text(text = touchedLetter)
    }
  }
}

//@Composable fun O() {
//  val items = remember { LoremIpsum().values.first().split(" ").sortedBy { it.lowercase() } }
//  val headers = remember { items.map { it.first().uppercase() }.toSet().toList() }
//  Row {
//    val listState = rememberLazyListState()
//    LazyColumn(
//      state = listState,
//      modifier = Modifier.weight(1f)
//    ) {
//      items(items) {
//        Text(it)
//      }
//    }
//    val offsets = remember { mutableStateMapOf<Int, Float>() }
//    var selectedHeaderIndex by remember { mutableStateOf(0) }
//    val scope = rememberCoroutineScope()
//
//    fun updateSelectedIndexIfNeeded(offset: Float) {
//      val index = offsets
//        .mapValues { abs(it.value - offset) }
//        .entries
//        .minByOrNull { it.value }
//        ?.key ?: return
//      if (selectedHeaderIndex == index) return
//      selectedHeaderIndex = index
//      val selectedItemIndex = items.indexOfFirst { it.first().uppercase() == headers[selectedHeaderIndex] }
//      scope.launch {
//        listState.scrollToItem(selectedItemIndex)
//      }
//    }
//
//    Column(
//      verticalArrangement = Arrangement.SpaceEvenly,
//      modifier = Modifier
//        .fillMaxHeight()
//        .background(Color.Gray)
//        .pointerInput(Unit) {
//          detectTapGestures {
//            updateSelectedIndexIfNeeded(it.y)
//          }
//        }
//        .pointerInput(Unit) {
//          detectVerticalDragGestures { change, _ ->
//            updateSelectedIndexIfNeeded(change.position.y)
//          }
//        }
//    ) {
//      headers.forEachIndexed { i, header ->
//        Text(
//          header,
//          modifier = Modifier.onGloballyPositioned { coordinates ->
//            offsets[i] = coordinates.boundsInParent().center.y
//          }
//        )
//      }
//    }
//  }
//}