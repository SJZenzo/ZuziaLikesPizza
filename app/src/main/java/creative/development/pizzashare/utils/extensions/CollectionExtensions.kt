package creative.development.pizzashare.utils.extensions

import creative.development.pizzashare.utils.view.LazyView
import creative.development.pizzashare.utils.view.LazyViewGroup

fun List<LazyView<*>>.doAfterInflated(block: () -> Unit) =
    LazyViewGroup(*this.toTypedArray()).addOnInflatedListener(block)
