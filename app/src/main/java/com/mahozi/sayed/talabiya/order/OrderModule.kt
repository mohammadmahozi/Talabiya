package com.mahozi.sayed.talabiya.order

import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.navmodel.backstack.BackStack
import com.mahozi.sayed.talabiya.core.data.TalabiyaDatabase
import com.mahozi.sayed.talabiya.core.main.MainScope
import com.mahozi.sayed.talabiya.order.store.OrderDao
import dagger.Module
import dagger.Provides

@Module
object OrderModule {

    @MainScope
    @Provides fun provideOrderDao(database: TalabiyaDatabase): OrderDao = database.orderDao()

    @MainScope
    @Provides fun provideOrderBackstack(buildContext: BuildContext): BackStack<OrderRoute> = BackStack(
        initialElement = OrderRoute.Orders,
        savedStateMap = buildContext.savedStateMap
    )
}