package com.mahozi.talabiya

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import com.mahozi.sayed.talabiya.core.Presenter
import kotlinx.coroutines.flow.Flow

suspend fun <Event, State> Presenter<Event, State>.test(
  events: Flow<Event>,
  validate: suspend TurbineTestContext<State>.() -> Unit,
) {
  moleculeFlow(RecompositionMode.Immediate) {
    start(events)
  }.test(validate = validate)
}