package creative.development.pizzashare.ui.splash

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.activity.viewModels
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
        goToMainActivityEvent.observe(lifeCycleOwner) {
            startActivity<MainActivity>(finishCurrent = true)
        }
    }
}
