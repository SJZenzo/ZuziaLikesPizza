package creative.development.pizzashare.manager

import creative.development.pizzashare.utils.view.LazyView

interface LazyViewManager {
    fun add(view: LazyView<*>)
}
