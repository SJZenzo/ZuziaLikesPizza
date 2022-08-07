package kowaliszyn.zuzanna.pizzashare.utils.extensions

import android.view.ViewGroup
import androidx.core.view.children
import kowaliszyn.zuzanna.pizzashare.utils.view.LazyView

fun ViewGroup.doAfterChildrenInflated(block: () -> Unit) =
    children.mapNotNull { child ->
        child as? LazyView<*>
    }.toList().doAfterInflated {
        block()
    }
