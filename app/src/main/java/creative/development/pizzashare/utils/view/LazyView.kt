package creative.development.pizzashare.utils.view

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import creative.development.pizzashare.R
import creative.development.pizzashare.utils.extensions.*

/**
 * LazyView for lazy inflating inherited views
 * It's HiltView - all inherited views must be @AndroidEntryPoint
 */
abstract class LazyView<VB : ViewBinding> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), Updatable {

    var lazyBackground: Drawable? by Updatable.UpdatableProperty.lateInit()
    var lazyHeight: Int by Updatable.UpdatableProperty.lateInit()
    var lazyAlpha: Float by Updatable.UpdatableProperty.lateInit()
    var lazyLightAnimationDuration: Int by Updatable.UpdatableProperty.lateInit()
    var lazyLightAlpha: Float by Updatable.UpdatableProperty.lateInit()

    var isInflated = false

    protected abstract val defaultLazyHeight: Int
    protected abstract val layoutResId: Int

    protected lateinit var binding: VB
        private set

    protected open val defaultLazyBackground
        get() = ContextCompat.getDrawable(context, R.drawable.bck_lazy_view)
    protected open val defaultLazyAlpha
        get() = ResourcesCompat.getFloat(resources, R.dimen.lazy_view_alpha)
    protected open val defaultLazyLightAnimationDuration
        get() = resources.getInteger(R.integer.lazy_light_animation_duration)
    protected open val defaultLazyLightAlpha
        get() = ResourcesCompat.getFloat(resources, R.dimen.lazy_view_alpha)

    private val lazyInflater = AsyncLayoutInflater(context)
    private val lightView: ImageView

    private var onInflatedListeners: MutableList<() -> Unit> = mutableListOf()
    private var animationWidth = 0
    private var animationStarted = false

    init {
        attrs.getFromStyleable(
            context,
            R.styleable.LazyView,
            defStyleAttr,
            0,
            ::initAttrs
        )
        lightView = ImageView(context).apply {
            layoutParams = LayoutParams(lazyHeight, lazyHeight)
            setImageDrawable(generateLightDrawable())
            isVisible = false
            alpha = lazyLightAlpha
        }
    }

    override fun update() {
        if (isInflated) {
            binding.update()
        } else {
            alpha = lazyAlpha
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (!isInEditMode) {
            update()
            background = lazyBackground
            addView(lightView)
            context.lazyViewManager?.add(this)
        } else inflate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (animationWidth < measuredWidth) {
            animationWidth = measuredWidth
            if (!animationStarted) {
                animationStarted = true
                lightView.animateLight()
            }
        }
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

    private fun generateLightDrawable(): ShapeDrawable {
        val lazyLightWidth = resources.getDimensionPixelSize(R.dimen.lazy_view_light_size).toFloat()
        val lazyLightHeight = lazyHeight.toFloat()
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(lazyLightWidth, 0f)
            lineTo(lazyLightHeight, lazyLightHeight)
            lineTo(lazyLightHeight - lazyLightWidth, lazyLightHeight)
            close()
        }
        val shape = PathShape(path, lazyLightHeight, lazyLightHeight)
        return ShapeDrawable(shape).apply {
            intrinsicWidth = lazyHeight
            intrinsicHeight = lazyHeight
            paint.color = ContextCompat.getColor(context, R.color.white)
        }
    }

    private fun View.animateLight() {
        isVisible = true
        translationX = -lazyHeight.toFloat()
        ObjectAnimator.ofFloat(
            this,
            "translationX",
            animationWidth.toFloat()
        ).apply {
            duration = lazyLightAnimationDuration.toLong()
            doOnEnd {
                animateLight()
            }
            start()
        }
    }

    private fun onInflated(view: View) {
        background = null
        removeAllViews()
        binding = onBind(view)
        isInflated = true
        binding.subscribe()
        onInflatedListeners.forEach { onInflatedListener ->
            onInflatedListener.invoke()
        }
        update()
        addView(binding.root)
    }

    private fun initAttrs(typedArray: TypedArray?) {
        lazyBackground = typedArray.getDrawableOrDef(
            R.styleable.LazyView_lazy_background,
            defaultLazyBackground
        )
        lazyHeight = typedArray.getDimensionPixelSizeOrDef(
            R.styleable.LazyView_lazy_height,
            defaultLazyHeight
        )
        lazyAlpha = typedArray.getFloatOrDef(
            R.styleable.LazyView_lazy_alpha,
            defaultLazyAlpha
        )
        lazyLightAlpha = typedArray.getFloatOrDef(
            R.styleable.LazyView_lazy_light_alpha,
            defaultLazyLightAlpha
        )
        lazyLightAnimationDuration = typedArray.getIntOrDef(
            R.styleable.LazyView_lazy_light_animation_duration,
            defaultLazyLightAnimationDuration
        )
    }
}
