package creative.development.pizzashare.ui.main

import android.view.LayoutInflater
import androidx.activity.viewModels
import creative.development.pizzashare.databinding.ActivityMainBinding
import creative.development.pizzashare.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val viewModel: MainViewModel by viewModels()

    override fun onBind(layoutInflater: LayoutInflater) =
        ActivityMainBinding.inflate(layoutInflater)
}
