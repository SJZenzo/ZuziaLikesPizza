package creative.development.pizzashare.ui.splash

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import creative.development.pizzashare.R
import creative.development.pizzashare.config.Consts
import creative.development.pizzashare.databinding.ActivitySplashBinding
import creative.development.pizzashare.ui.base.BaseActivity
import creative.development.pizzashare.ui.main.MainActivity
import creative.development.pizzashare.utils.extensions.startActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    override val viewModel: SplashViewModel by viewModels()

    override fun onBind(layoutInflater: LayoutInflater) =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun SplashViewModel.subscribe() {
        startAnimationEvent.observe(lifeCycleOwner) {
            animate(0)
        }
        goToMainActivityEvent.observe(lifeCycleOwner) {
            startActivity<MainActivity>(finishCurrent = true)
        }
    }

    private fun animate(step: Int) {
        when (step) {
            0 -> binding.activitySplashBackgroundImage
            1 -> binding.activitySplashTitleFirstLine
            2 -> binding.activitySplashTitleSecondLine
            else -> throw IllegalArgumentException()
        }.apply {
            measuredWidth.takeIf { it > 0 }?.let { width ->
                val titleMargin = resources.getDimensionPixelOffset(
                    R.dimen.activity_splash_app_title_margin
                ).toFloat()
                val splashBackgroundAlpha = ResourcesCompat.getFloat(
                    resources,
                    R.dimen.activity_splash_background_alpha
                )
                when (step) {
                    0 -> {
                        alpha = 1f
                        ObjectAnimator.ofFloat(
                            this,
                            "alpha",
                            splashBackgroundAlpha
                        )
                    }
                    1 -> {
                        translationX = -width - titleMargin
                        isVisible = true
                        ObjectAnimator.ofFloat(
                            this,
                            "translationX",
                            0f
                        )
                    }
                    2 -> {
                        translationX = width + titleMargin
                        isVisible = true
                        ObjectAnimator.ofFloat(
                            this,
                            "translationX",
                            0f
                        )
                    }
                    else -> throw IllegalArgumentException()
                }.apply {
                    duration =
                        if (step == 0) Consts.DURATION_SPLASH_SCREEN_ANIMATION_FIRST_STEP_IN_MILLIS
                        else Consts.DURATION_SPLASH_SCREEN_ANIMATION_OTHER_STEPS_IN_MILLIS
                    doOnEnd {
                        if (step == 0) {
                            animate(1)
                            post {
                                animate(2)
                            }
                        } else if (step == 2) {
                            viewModel.endAnimation()
                        }
                    }
                    start()
                }
            } ?: post {
                animate(step)
            }
        }
    }
}
