package kowaliszyn.zuzanna.pizzashare.utils.view

import kotlin.reflect.KProperty

open class LateInitProperty<T> {

    protected object Uninitialized

    protected var value: Any? = Uninitialized

    @Suppress("UNCHECKED_CAST")
    open operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (value is Uninitialized)
            throw UninitializedPropertyAccessException()
        return value as T
    }

    open operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}
