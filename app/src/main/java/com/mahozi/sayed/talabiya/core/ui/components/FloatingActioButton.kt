package com.mahozi.sayed.talabiya.core.ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme

@Preview @Composable fun PreviewAddFab() {
    AppTheme {
        AddFab { }
    }
}
@Composable
fun AddFab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = { onClick() },
        contentColor = Color.White,
        content = {
            Icon(
                painterResource(R.drawable.ic_add_white_24dp),
                stringResource(R.string.create_order)
            )
        },
      modifier = modifier
    )
}

@Preview
@Composable
private fun PreviewConfirmFab() {
    AppTheme {
        ConfirmFab {
        }
    }
}
@Composable
fun ConfirmFab(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = modifier.defaultMinSize(minWidth = 56.dp, minHeight = 56.dp),
        enabled = enabled,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
          containerColor = AppTheme.colors.material.secondary
        )

    ){
        Icon(
            Icons.Default.Check,
            stringResource(R.string.create_order)
        )
    }
}