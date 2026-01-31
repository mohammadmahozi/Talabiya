package com.mahozi.sayed.talabiya.user.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.datetime.AppDateTimeFormatter
import com.mahozi.sayed.talabiya.core.datetime.LocalDateTimeFormatter
import com.mahozi.sayed.talabiya.core.datetime.ProvideDateTimeFormatter
import com.mahozi.sayed.talabiya.core.extensions.locale
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.HorizontalSpacer
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaTopBarDefaults
import com.mahozi.sayed.talabiya.core.ui.components.TlbCard
import com.mahozi.sayed.talabiya.core.ui.components.TlbIcon
import com.mahozi.sayed.talabiya.core.ui.components.TlbTab
import com.mahozi.sayed.talabiya.core.ui.components.TlbTabRow
import com.mahozi.sayed.talabiya.core.ui.components.TlbText
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.core.ui.theme.onSurfaceVariant
import com.mahozi.sayed.talabiya.payment.Payment
import com.mahozi.sayed.talabiya.payment.PaymentStatus
import com.mahozi.sayed.talabiya.payment.background
import com.mahozi.sayed.talabiya.payment.title
import com.mahozi.sayed.talabiya.user.details.payment.create.CreateUserPaymentScreen
import com.mahozi.sayed.talabiya.user.details.ui.UserDetailsEvent
import com.mahozi.sayed.talabiya.user.details.ui.UserDetailsState
import com.mahozi.sayed.talabiya.user.details.ui.UserDetailsTab
import com.mahozi.sayed.talabiya.userorder.UserOrder
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class UserDetailsScreen(
  val userId: Long,
  val name: String,
) : Screen

@Composable
fun UserDetailsScreen(
  state: UserDetailsState,
  onEvent: (UserDetailsEvent) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      TalabiyaBar(
        title = { Text(text = state.userName) },
        navigationIcon = { TalabiyaTopBarDefaults.BackIcon(onBack)}
      )
    },
  ) { paddingValues ->
    Column(
      modifier = modifier
        .padding(paddingValues)
    ) {
      TlbTabRow(
        selectedTabIndex = state.tab.ordinal,
      ) {
        TlbTab(
          selected = state.tab == UserDetailsTab.CreatePayment,
          text = { Text(stringResource(R.string.pay)) },
          onClick = { onEvent(UserDetailsEvent.SelectTab(UserDetailsTab.CreatePayment)) }
        )
        TlbTab(
          selected = state.tab == UserDetailsTab.Payments,
          text = { Text(stringResource(R.string.payments)) },
          onClick = { onEvent(UserDetailsEvent.SelectTab(UserDetailsTab.Payments)) }
        )
        TlbTab(
          selected = state.tab == UserDetailsTab.Orders,
          text = { Text(stringResource(R.string.orders)) },
          onClick = { onEvent(UserDetailsEvent.SelectTab(UserDetailsTab.Orders)) }
        )
      }

      Column(
        modifier = Modifier
          .padding(16.dp)
      ) {
        when (state.tab) {
          UserDetailsTab.CreatePayment -> {
            CreateUserPaymentScreen(
              state = state.createUserPaymentState,
              onEvent = { onEvent(UserDetailsEvent.CreatePaymentEvent(it)) }
            )
          }
          UserDetailsTab.Payments -> {
            Payments(state.payments)
          }
          UserDetailsTab.Orders -> {
            UserOrders(state.orders)
          }
        }
      }
    }
  }
}

@Composable
private fun Payments(
  payments: List<Payment>
) {
  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    items(payments) { payment ->
      Payment(payment)
    }
  }
}

@Composable
private fun Payment(
  payment: Payment
) {

  TlbCard {
    Row(
      verticalAlignment = Alignment.Top,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = "#${payment.id}",
        style = AppTheme.type.labelSmall.onSurfaceVariant,
        modifier = Modifier
          .background(
            color = AppTheme.colors.surfaceContainerHighest,
            shape = CircleShape
          )
          .padding(2.dp)
      )
      Text(
        text = payment.amount.format(),
        style = AppTheme.type.bodyLarge,
        fontWeight = FontWeight.Bold,
      )
      HorizontalSpacer(1F)
      Text(
        text = stringResource(payment.status.title),
        modifier = Modifier
          .background(
            color = payment.status.background,
            shape = AppTheme.shapes.medium,
          )
          .padding(8.dp)
      )
    }

    Text(
      text = LocalDateTimeFormatter.current.formatShortDateWithDay(payment.createdAt),
      style = AppTheme.type.bodyMedium.onSurfaceVariant,
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPayment() {
  AppTheme {
    val payment = Payment(
      id = 1L,
      amount = 100.money,
      createdAt = Instant.now(),
      status = PaymentStatus.Completed,
      userId = 0L,
    )
    ProvideDateTimeFormatter(AppDateTimeFormatter(LocalContext.current.locale)) {
      Payment(payment)
    }
  }
}

@Composable
private fun UserOrders(
  orders: List<UserOrder>
) {
  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    items(orders) { order ->
      UserOrder(order)
    }
  }
}

@Composable
private fun UserOrder(order: UserOrder) {
  TlbCard {
    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = order.orderId.toString(),
      )

      Column {
        Row(
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = order.restaurant,
          )
          HorizontalSpacer(1F)
          TlbText(
            text = "0",
            style = AppTheme.type.bodySmall,
            leadingIcon = {
              TlbIcon(
                painter = painterResource(R.drawable.arrow_forward_rounded),
                contentDescription = stringResource(R.string.expense)
              )
            }
          )
          HorizontalSpacer(16.dp)
          TlbText(
            text = "0",
            style = AppTheme.type.bodySmall,
            leadingIcon = {
              TlbIcon(
                painter = painterResource(R.drawable.arrow_back_rounded),
                contentDescription = stringResource(R.string.income)
              )
            }
          )
        }
        val formatter = LocalDateTimeFormatter.current
        Text(
          text = formatter.formatShortDateWithDay(order.createdAt),
          style = AppTheme.type.bodyMedium.onSurfaceVariant,
        )
      }
    }
  }
}

