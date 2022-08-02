package kowaliszyn.zuzanna.pizzashare.ui.splash

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kowaliszyn.zuzanna.pizzashare.databinding.ActivitySplashBinding
import kowaliszyn.zuzanna.pizzashare.ui.base.BaseActivity
import kowaliszyn.zuzanna.pizzashare.ui.main.MainActivity
import kowaliszyn.zuzanna.pizzashare.utils.extensions.startActivity

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    override val viewModel: SplashViewModel by viewModels()

    override fun onBind(layoutInflater: LayoutInflater) =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun SplashViewModel.onViewModelSubscribe() {
        goToMainActivityEvent.observe(lifeCycleOwner) {
            startActivity<MainActivity>(finishCurrent = true)
        }
    }
}
