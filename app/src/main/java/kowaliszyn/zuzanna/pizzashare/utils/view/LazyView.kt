package kowaliszyn.zuzanna.pizzashare.utils.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.card.MaterialCardView
import kowaliszyn.zuzanna.pizzashare.R
import kowaliszyn.zuzanna.pizzashare.utils.extensions.*

/**
 * LazyView for lazy inflating inherited views
 */
abstract class LazyView<VB : ViewBinding> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), Updatable {

    var lazyBackgroundColor: Int by Updatable.UpdatableProperty.lateInit()
    var lazyBorderColor: Int by Updatable.UpdatableProperty.lateInit()
    var lazyBorderWidth: Int by Updatable.UpdatableProperty.lateInit()
    var lazyHeight: Int by Updatable.UpdatableProperty.lateInit()
    var lazyCornerRadius: Float by Updatable.UpdatableProperty.lateInit()
    var lazyElevation: Float by Updatable.UpdatableProperty.lateInit()
    var lazyAlpha: Float by Updatable.UpdatableProperty.lateInit()

    var isInflated = false

    protected abstract val defaultLazyHeight: Int
    protected abstract val layoutResId: Int

    protected lateinit var binding: VB
        private set

    protected open val defaultLazyBackgroundColor
        get() =
            ContextCompat.getColor(context, R.color.lazy_background_color)
    protected open val defaultLazyBorderColor
        get() =
            ContextCompat.getColor(context, R.color.lazy_border_color)
    protected open val defaultLazyBorderWidth
        get() =
            resources.getDimensionPixelSize(R.dimen.lazy_border_width)
    protected open val defaultLazyCornerRadius
        get() =
            resources.getDimension(R.dimen.lazy_corner_radius)
    protected open val defaultLazyElevation
        get() =
            resources.getDimension(R.dimen.lazy_elevation)
    protected open val defaultLazyAlpha
        get() =
            ResourcesCompat.getFloat(resources, R.dimen.lazy_alpha)

    private val lazyInflater = AsyncLayoutInflater(context)
    private val lazyCardView: MaterialCardView = MaterialCardView(context)

    private var onInflatedListeners: MutableList<() -> Unit> = mutableListOf()

    init {
        attrs.getFromStyleable(
            context,
            R.styleable.LazyView,
            defStyleAttr,
            0,
            ::initAttrs
        )
    }

    override fun update() {
        if (isInflated) binding.update()
        else lazyCardView.apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, lazyHeight)
            setCardBackgroundColor(lazyBackgroundColor)
            radius = lazyCornerRadius
            strokeColor = lazyBorderColor
            strokeWidth = lazyBorderWidth
            alpha = lazyAlpha
            elevation = lazyElevation
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (!isInEditMode) {
            update()
            addView(lazyCardView)
            asyncInflate()
        } else inflate()
    }

    fun addOnInflatedListener(onInflatedListener: () -> Unit) {
        if (isInflated) onInflatedListener()
        else onInflatedListeners.add(onInflatedListener)
    }

    fun removeOnInflatedListener(onInflatedListener: () -> Unit) {
        onInflatedListeners.remove(onInflatedListener)
    }

    fun inflate() {
        onInflated(
            LayoutInflater.from(context).inflate(
                layoutResId,
                this,
                false
            )
        )
    }

    fun asyncInflate() {
        lazyInflater.inflate(layoutResId, this) { view, _, _ ->
            onInflated(view)
        }
    }

    protected abstract fun onBind(view: View): VB

    protected abstract fun VB.subscribe()
    protected abstract fun VB.update()

    private fun onInflated(view: View) {
        removeAllViews()
        binding = onBind(view).apply {
            subscribe()
        }
        isInflated = true
        onInflatedListeners.forEach { onInflatedListener ->
            onInflatedListener.invoke()
        }
        update()
        addView(binding.root)
    }

    private fun initAttrs(typedArray: TypedArray?) {
        lazyBackgroundColor = typedArray.getColorOrDef(
            R.styleable.LazyView_lazy_background_color,
            defaultLazyBackgroundColor
        )
        lazyBorderColor = typedArray.getColorOrDef(
            R.styleable.LazyView_lazy_border_color,
            defaultLazyBorderColor
        )
        lazyBorderWidth = typedArray.getDimensionPixelSizeOrDef(
            R.styleable.LazyView_lazy_border_width,
            defaultLazyBorderWidth
        )
        lazyHeight = typedArray.getDimensionPixelSizeOrDef(
            R.styleable.LazyView_lazy_height,
            defaultLazyHeight
        )
        lazyCornerRadius = typedArray.getDimensionOrDef(
            R.styleable.LazyView_lazy_corner_radius,
            defaultLazyCornerRadius
        )
        lazyElevation = typedArray.getDimensionOrDef(
            R.styleable.LazyView_lazy_elevation,
            defaultLazyElevation
        )
        lazyAlpha = typedArray.getFloatOrDef(
            R.styleable.LazyView_lazy_alpha,
            defaultLazyAlpha
        )
    }
}
