package creative.development.pizzashare.ui.splash

import androidx.lifecycle.ViewModel
import creative.development.pizzashare.consts.Consts
import creative.development.pizzashare.manager.PizzaManager
import creative.development.pizzashare.utils.EventLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timerTask

@HiltViewModel
class SplashViewModel @Inject constructor(
    pizzaManager: PizzaManager
) : ViewModel() {

    val goToMainActivityEvent = EventLiveData<Unit>()
    val startAnimationEvent = EventLiveData<Unit>()

    private val timer = Timer()

    init {
        startAnimation()
        pizzaManager.init()
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
