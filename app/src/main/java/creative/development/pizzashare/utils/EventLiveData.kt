package creative.development.pizzashare.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

open class EventLiveData<T> : MutableLiveData<T> {

    private var autoClearable = true

    constructor(value: T, autoClearable: Boolean = true) : super(value) {
        this.autoClearable = autoClearable
    }

    constructor() : super()

    fun clear() {
        value = null
    }

    private fun notNullObserver(observer: Observer<in T>) = Observer<T> {
        if (it != null) {
            observer.onChanged(it)
            if (autoClearable) clear()
        }
    }

    override fun observeForever(observer: Observer<in T>) {
        super.observeForever(notNullObserver(observer))
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, notNullObserver(observer))
    }
}
