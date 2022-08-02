package kowaliszyn.zuzanna.pizzashare.ui.splash

import androidx.lifecycle.ViewModel
import kowaliszyn.zuzanna.pizzashare.config.Config
import kowaliszyn.zuzanna.pizzashare.utils.EventLiveData
import java.util.*
import kotlin.concurrent.timerTask

class SplashViewModel : ViewModel() {

    val goToMainActivityEvent = EventLiveData<Unit>()

    init {
        startTimer()
    }

    fun startTimer() {
        Timer().schedule(
            timerTask {
                goToMainActivityEvent.postValue(Unit)
            },
            Config.DELAY_SPLASH_SCREEN_IN_MILLIS
        )
    }
}
