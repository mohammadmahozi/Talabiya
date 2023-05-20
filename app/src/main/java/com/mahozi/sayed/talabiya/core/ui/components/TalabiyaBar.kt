package com.mahozi.sayed.talabiya.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme

@Preview @Composable fun PreviewBar() {
    AppTheme {
        TalabiyaBar(title = R.string.app_name)
    }
}

@Composable fun TalabiyaBar(@StringRes title: Int) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = title),
                color = Color.White
            )
        },
    )
}