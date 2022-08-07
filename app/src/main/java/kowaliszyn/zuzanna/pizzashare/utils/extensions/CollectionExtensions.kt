package kowaliszyn.zuzanna.pizzashare.utils.extensions

import kowaliszyn.zuzanna.pizzashare.utils.view.LazyView
import kowaliszyn.zuzanna.pizzashare.utils.view.LazyViewGroup

fun List<LazyView<*>>.doAfterInflated(block: () -> Unit) =
    LazyViewGroup(*this.toTypedArray()).addOnInflatedListener(block)
