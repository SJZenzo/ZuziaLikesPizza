package kowaliszyn.zuzanna.pizzashare.ui.main

import android.view.LayoutInflater
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kowaliszyn.zuzanna.pizzashare.databinding.ActivityMainBinding
import kowaliszyn.zuzanna.pizzashare.ui.base.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val viewModel: MainViewModel by viewModels()

    override fun onBind(layoutInflater: LayoutInflater) =
        ActivityMainBinding.inflate(layoutInflater)
}
