package creative.development.pizzashare

import android.app.Application
import creative.development.pizzashare.manager.LazyViewManager
import creative.development.pizzashare.manager.PizzaManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApp : Application() {

    @Inject
    lateinit var pizzaManager: PizzaManager

    @Inject
    lateinit var lazyViewManager: LazyViewManager

    override fun onCreate() {
        super.onCreate()
        pizzaManager.init()
    }
}
