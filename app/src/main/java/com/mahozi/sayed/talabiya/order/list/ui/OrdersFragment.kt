package com.mahozi.sayed.talabiya.order.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.ui.TalabiyaBar
import com.mahozi.sayed.talabiya.databinding.FragmentOrderBinding
import com.mahozi.sayed.talabiya.order.store.OrderEntity
import com.mahozi.sayed.talabiya.order.view.create.CreateOrderFragment
import com.mahozi.sayed.talabiya.order.details.OrderDetailsFragment
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import com.mahozi.sayed.talabiya.core.di.appGraph
import com.mahozi.sayed.talabiya.core.ui.components.AddFab
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.core.ui.theme.colors
import kotlinx.coroutines.CoroutineScope

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: OrdersVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)

        viewModel = OrdersVM(appGraph.ordersRepository, CoroutineScope(AndroidUiDispatcher.Main))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scope = CoroutineScope(viewLifecycleOwner.lifecycleScope.coroutineContext + AndroidUiDispatcher.Main)
        val models = scope.launchMolecule(clock = RecompositionClock.ContextClock) { viewModel.start() }

        binding.composeView.setContent {
            AppTheme {
                Scaffold(
                    topBar = {
                        TalabiyaBar(title = R.string.app_name)
                    },
                    floatingActionButton = {
                        AddFab {
                            viewModel.event(OrdersEvent.CreateOrderClicked)
                        }
                    },
                    floatingActionButtonPosition = FabPosition.End
                ) {
                    Box(
                        modifier = Modifier
                            .padding(it)
                            .padding(horizontal = 16.dp)
                    ) {
                        val model = models.collectAsState().value
                        Orders(model.orders)
                    }
                }
            }
        }
    }

    @Composable
    fun Orders(orders: List<OrderEntity>) {
        LazyColumn {
            items(orders) {
                OrderRow(order = it)
            }
        }
    }

    @Preview
    @Composable
    fun PreviewOrderRow() {
        OrderRow(order = OrderEntity("Tannoor", "200", "20").apply {
            id = 0
            status = false
        })
    }

    @Composable
    fun OrderRow(order: OrderEntity) {
        Row(
            Modifier
                .background(colors.rowBackground)
                .clickable { viewModel.event(OrdersEvent.OrderClicked(order)) }
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = order.id.toString(),
                modifier = Modifier
                    .padding(end = 48.dp)
            )
            Column {
                Text(
                    text = order.restaurantName,
                    Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = order.date,
                    color = colors.secondaryText,
                    fontSize = 12.sp,
                )
            }

            Spacer(Modifier.weight(1f))
            if (order.status) {
                Text(
                    text = stringResource(R.string.complete),
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun startCreateOrderFragment() {
        val createOrderFragment = CreateOrderFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.order_container, createOrderFragment, "CreateOrderFragment")
            .addToBackStack(null)
            .commit()
    }

    fun startOrderDetailsFragment() {
        val orderDetailsFragment = OrderDetailsFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.order_container, orderDetailsFragment, "OrderDetailsFragment")
            .addToBackStack(null)
            .commit()
    }
}