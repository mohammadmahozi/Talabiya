package com.mahozi.sayed.talabiya.order.details.suborder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mahozi.sayed.talabiya.R


@Composable private fun Suborder(

) {

  
}

@Preview(showBackground = true)
@Composable private fun PreviewHeader() {
  Header(
    name = "Test",
    onHeaderClicked = {},
    onEditClicked = {}
  )
}
@Composable private fun Header(
   name: String,
   onHeaderClicked: () -> Unit,
   onEditClicked: () -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clickable(onClick = onHeaderClicked)
  ) {
    Text(text = name)
    
    Spacer(Modifier.weight(1F))
    
    IconButton(onClick = onEditClicked) {
      Icon(
        imageVector = Icons.Default.Edit,
        contentDescription = stringResource(R.string.edit_order)
      )
    }
  }
}