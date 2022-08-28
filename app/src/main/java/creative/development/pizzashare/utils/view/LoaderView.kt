package creative.development.pizzashare.utils.view

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import creative.development.pizzashare.R
import creative.development.pizzashare.consts.Consts
import creative.development.pizzashare.databinding.ViewLoaderBinding
import creative.development.pizzashare.utils.extensions.getBooleanOrDef

/**
 * Loader view - displayed during switching screens in app
 */
class LoaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseView<ViewLoaderBinding>(context, attrs, defStyleAttr) {

    override val styleableResId: IntArray? = R.styleable.LoaderView

    private var animationAutoStart = false
    private var animationStarted = false

    init {
        init(attrs, defStyleAttr)
    }

    override fun onBind(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ) = ViewLoaderBinding.inflate(layoutInflater, parent, attachToParent)

    override fun initAttrs(typedArray: TypedArray?) {
        animationAutoStart =
            typedArray.getBooleanOrDef(R.styleable.LoaderView_android_autoStart, false)
    }

    override fun ViewLoaderBinding.subscribe() {
        binding.viewLoaderIcon.apply {
            pivotX =
                resources.getDimensionPixelSize(R.dimen.loader_icon_size) * Consts.PIVOT_X_LOADER_ANIMATION
            pivotY =
                resources.getDimensionPixelSize(R.dimen.loader_icon_size) * Consts.PIVOT_Y_LOADER_ANIMATION
        }
        if (animationAutoStart) start()
    }

    fun start() {
        animationStarted = true
        binding.viewLoaderIcon.animateIcon()
    }

    fun stop() {
        animationStarted = false
        binding.viewLoaderIcon.clearAnimation()
    }

    private fun View.animateIcon() {
        rotation = 0.0f
        ObjectAnimator.ofFloat(
            this,
            "rotation",
            Consts.ROTATION_NUMBER_LOADER_ANIMATION * 360f
        ).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = Consts.DURATION_LOADER_ANIMATION
            doOnEnd {
                if (animationStarted) postDelayed({ animateIcon() }, Consts.DELAY_LOADER_ANIMATION)
            }
            start()
        }
    }
}
