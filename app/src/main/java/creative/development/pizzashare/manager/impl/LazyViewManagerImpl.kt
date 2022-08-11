package creative.development.pizzashare.manager.impl

import creative.development.pizzashare.manager.LazyViewManager
import creative.development.pizzashare.utils.view.LazyView
import javax.inject.Inject

class LazyViewManagerImpl @Inject constructor() : LazyViewManager {

    private val prevView: LazyView<*>? = null

    override fun add(view: LazyView<*>) {
        prevView?.addOnInflatedListener {
            view.asyncInflate()
        } ?: view.asyncInflate()
    }
}
