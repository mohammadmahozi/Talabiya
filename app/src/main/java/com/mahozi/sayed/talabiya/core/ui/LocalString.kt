package com.mahozi.sayed.talabiya.core.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource

fun localString(@StringRes id: Int, vararg args: String): LocalString = LocalString.Res(id, *args)
fun localString(@StringRes id: Int, @StringRes vararg args: Int): LocalString = LocalString.Res(id, *args)

@Stable
sealed class LocalString {
  data class Char(val string: String) : LocalString()
  data class Res(
    @StringRes val id: Int,
    val args: List<LocalStringArg> = emptyList()
  ) : LocalString() {
    constructor(@StringRes id: Int, vararg args: String) : this(
      id = id,
      args = args.toList().map { LocalStringArg.Char(it) }
    )

    constructor(@StringRes id: Int, @StringRes vararg args: Int) : this(
      id = id,
      args = args.toList().map { LocalStringArg.Res(it) }
    )
  }
}

@Stable
sealed interface LocalStringArg {
  data class Char(val value: String) : LocalStringArg
  data class Res(@StringRes val id: Int) : LocalStringArg
}

val String.localize: LocalString get() = LocalString.Char(this)

val Int.localize: LocalString get() = LocalString.Res(this)

val LocalString.value
  @Composable get() = when (this) {
    is LocalString.Char -> string
    is LocalString.Res -> {
      val resolvedArgs = args.map { textArg ->
        when (textArg) {
          is LocalStringArg.Char -> textArg.value
          is LocalStringArg.Res -> stringResource(textArg.id)
        }
      }

      if (args.isEmpty()) {
        stringResource(id)
      } else {
        stringResource(id, *resolvedArgs.toTypedArray())
      }
    }
  }