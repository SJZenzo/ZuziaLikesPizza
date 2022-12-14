package creative.development.pizzashare.ui.splash

import creative.development.pizzashare.consts.Consts
import creative.development.pizzashare.ui.base.BaseActivityViewModel
import creative.development.pizzashare.utils.EventLiveData
import java.util.*
import kotlin.concurrent.timerTask

class SplashViewModel : BaseActivityViewModel() {

    val goToMainActivityEvent = EventLiveData<Unit>()
    val startAnimationEvent = EventLiveData<Unit>()

    private val timer = Timer()

    init {
        startAnimation()
    }

    fun endAnimation() {
        timer.schedule(
            timerTask {
                goToMainActivityEvent.postValue(Unit)
            },
            Consts.DELAY_SPLASH_SCREEN_IN_MILLIS
        )
    }

    private fun startAnimation() {
        timer.schedule(
            timerTask {
                startAnimationEvent.postValue(Unit)
            },
            Consts.DELAY_SPLASH_SCREEN_ANIMATION_IN_MILLIS
        )
    }
}
