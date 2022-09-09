package com.mahozi.sayed.talabiya.core.ui.components

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.ui.theme.colors

@Preview @Composable fun PreviewAddFab() {
    AddFab { }
}
@Composable
fun AddFab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        backgroundColor = colors.primary,
        contentColor = Color.White,
        content = {
            Icon(
                painterResource(R.drawable.ic_add_white_24dp),
                stringResource(R.string.create_order)
            )
        })
}