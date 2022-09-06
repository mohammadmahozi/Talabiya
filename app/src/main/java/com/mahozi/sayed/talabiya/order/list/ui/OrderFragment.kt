package com.mahozi.sayed.talabiya.order.list.ui

import android.os.Bundle
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.databinding.FragmentOrderBinding
import com.mahozi.sayed.talabiya.order.OrderViewModel
import com.mahozi.sayed.talabiya.order.store.OrderEntity
import com.mahozi.sayed.talabiya.order.view.create.CreateOrderFragment
import com.mahozi.sayed.talabiya.order.view.create.DatePickerPopUp
import com.mahozi.sayed.talabiya.order.view.details.OrderDetailsFragment

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mOrderAdapter: OrderAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager

    private lateinit var orderViewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order, container, false)
        orderViewModel = ViewModelProviders.of(requireActivity()).get(OrderViewModel::class.java)
        mRecyclerView = view.findViewById(R.id.fragment_order_recycler_view)
        //this add a separator
        mRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        layoutManager = LinearLayoutManager(context)
        mRecyclerView.setLayoutManager(layoutManager)
        mOrderAdapter =
            OrderAdapter(object :
                OrderRecyclerViewListener {
                override fun onClick(orderEntity: OrderEntity) {
                    orderViewModel.setCurrentOrderEntity(orderEntity)
                    startOrderDetailsFragment()
                }

                override fun onLongClick(orderEntity: OrderEntity) {
                    val orderActionMode =
                        OrderActionMode(
                            object :
                                OrderActionMode.OnSelectionActionMode {
                                override fun finished() {
                                    mOrderAdapter.finishSelectionSession()
                                }

                                override fun delete(actionMode: ActionMode) {
                                    deleteOrder(actionMode)
                                }

                                override fun changeStatus(mode: ActionMode) {
                                    changeOrderStatus(mode)
                                }
                            })
                    requireActivity().startActionMode(orderActionMode)
                }
            })
        mRecyclerView.setAdapter(mOrderAdapter)
        orderViewModel.allOrderEntities.observe(this) { orderEntities ->
            mOrderAdapter.setDataList(
                orderEntities
            )
        }
        val addOrderFab = view.findViewById<FloatingActionButton>(R.id.add_order_fab)
        addOrderFab.setOnClickListener { view1: View? -> if (!mOrderAdapter.isActionModeOn) startCreateOrderFragment() }
        return view
    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = "Orders"
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

    fun deleteOrder(actionMode: ActionMode) {
        val selectedItemsPositions = ArrayList(
            mOrderAdapter.selectedItems
        )
        AlertDialog.Builder(requireActivity())
            .setTitle(R.string.confirm).setMessage(
                getString(
                    R.string.delete_all_these,
                    selectedItemsPositions.size.toString() + ""
                )
            )
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes) { dialog, whichButton ->
                for (pos in selectedItemsPositions) {
                    orderViewModel.deleteOrder(mOrderAdapter[pos])
                }
                actionMode.finish()
                mOrderAdapter.finishSelectionSession()
            }
            .setNegativeButton(android.R.string.no, null).show()
    }

    fun changeOrderStatus(actionMode: ActionMode) {
        val selectedItemsPositions = ArrayList(
            mOrderAdapter.selectedItems
        )
        val datePick = DatePickerPopUp { date ->
            for (pos in selectedItemsPositions) {
                val orderEntity = mOrderAdapter[pos]
                if (orderEntity.status == false) {
                    orderEntity.clearance_date = date
                    orderEntity.status = true
                    orderViewModel.updateOrder(orderEntity)
                }
            }
            actionMode.finish()
        }
        datePick.show(requireActivity().supportFragmentManager, "DatePicker")
    }
}