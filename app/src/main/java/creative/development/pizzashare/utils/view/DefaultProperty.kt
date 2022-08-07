package creative.development.pizzashare.utils.view

import kotlin.reflect.KProperty

class DefaultProperty<T : Any>(private val lazyInitBlock: () -> T) : LateInitProperty<T>() {

    @Suppress("UNCHECKED_CAST")
    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (value is Uninitialized) {
            value = lazyInitBlock()
        }
        return super.getValue(thisRef, property)
    }
}
