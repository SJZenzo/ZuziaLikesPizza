package creative.development.pizzashare.utils.view

import kotlin.reflect.KProperty

interface Updatable {

    class UpdatableProperty<T> : LateInitProperty<T> {

        class MissingUpdatableBindingOwnerScopeException : RuntimeException()

        companion object {
            fun <T> lateInit() = UpdatableProperty<T>()
        }

        private constructor() : super()
        constructor(value: T) : super() {
            this.value = value
        }

        @Suppress("UNCHECKED_CAST")
        override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            if (thisRef !is Updatable)
                throw MissingUpdatableBindingOwnerScopeException()
            return super.getValue(thisRef, property)
        }

        override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            if (thisRef !is Updatable)
                throw MissingUpdatableBindingOwnerScopeException()
            val isUninitialized = this.value == Uninitialized
            if (isUninitialized || this.value != value) {
                super.setValue(thisRef, property, value)
                if (!isUninitialized) thisRef.update()
            }
        }
    }

    fun update()
}
