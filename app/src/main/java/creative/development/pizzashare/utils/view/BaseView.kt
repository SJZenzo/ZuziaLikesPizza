package creative.development.pizzashare.utils.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding
import creative.development.pizzashare.utils.extensions.getFromStyleable
import kotlin.reflect.KProperty

/**
 * LazyView for lazy inflating inherited views
 * It's HiltView - all inherited views must be @AndroidEntryPoint
 */
@Suppress("LeakingThis")
abstract class BaseView<VB : ViewBinding> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private object UninitializedPropertyValue

    inner class UpdatableProperty<T> {

        private var value: Any? = UninitializedPropertyValue

        constructor() : super()
        constructor(value: T) : super() {
            this.value = value
        }

        @Suppress("UNCHECKED_CAST")
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            if (value is UninitializedPropertyValue)
                throw UninitializedPropertyAccessException()
            return value as T
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            val isUninitialized = this.value == UninitializedPropertyValue
            if (isUninitialized || this.value != value) {
                this.value = value
                if (!isUninitialized) binding.update()
            }
        }
    }

    fun <T> updatableProperty(value: T) = UpdatableProperty(value)
    fun <T> lateInitProperty() = UpdatableProperty<T>()

    protected abstract val styleableResId: IntArray?

    protected lateinit var binding: VB
        private set

    fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        styleableResId?.let { styleableResId ->
            attrs.getFromStyleable(
                context,
                styleableResId,
                defStyleAttr,
                0,
                ::initAttrs
            )
        }
        binding = onBind(LayoutInflater.from(context), this, true)
        binding.subscribe()
        binding.update()
    }

    open fun initAttrs(typedArray: TypedArray?) {}

    abstract fun onBind(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): VB

    open fun VB.subscribe() {}
    open fun VB.update() {}
}
