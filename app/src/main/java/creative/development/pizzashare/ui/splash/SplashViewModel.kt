package creative.development.pizzashare.ui.splash

import androidx.lifecycle.ViewModel
import creative.development.pizzashare.config.Consts
import creative.development.pizzashare.utils.EventLiveData
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
            Consts.DELAY_SPLASH_SCREEN_IN_MILLIS
        )
    }
}
