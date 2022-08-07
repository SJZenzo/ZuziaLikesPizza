package creative.development.pizzashare.utils.view

/**
 * LazyViewGroup for grouped checking is all lazy view in group was inflated
 */
class LazyViewGroup(vararg lazyViews: LazyView<*>) {

    private var isAllInflated = false
    private val lazyViews: List<LazyView<*>> = lazyViews.toList()
    private var onInflatedListeners: MutableList<() -> Unit> = mutableListOf()

    fun addOnInflatedListener(onInflatedListener: () -> Unit) {
        if (isAllInflated) onInflatedListener()
        else onInflatedListeners.add(onInflatedListener)
    }

    fun removeOnInflatedListener(onInflatedListener: () -> Unit) {
        onInflatedListeners.remove(onInflatedListener)
    }

    init {
        lazyViews.forEach { lazyView ->
            lazyView.addOnInflatedListener(::checkIsAllInflated)
        }
    }

    fun checkIsAllInflated() {
        isAllInflated = lazyViews.all { lazyView ->
            lazyView.isInflated
        }
        if (isAllInflated) {
            onInflatedListeners.forEach { onInflatedListener ->
                onInflatedListener()
            }
        }
    }
}
