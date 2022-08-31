package creative.development.pizzashare.ui.main

import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import creative.development.pizzashare.R
import creative.development.pizzashare.databinding.ActivityMainBinding
import creative.development.pizzashare.ui.base.BaseActivity
import creative.development.pizzashare.ui.base.BaseFragment
import creative.development.pizzashare.utils.TouchFocusCleaner
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    @Inject
    lateinit var touchFocusCleaner: TouchFocusCleaner

    override val viewModel: MainViewModel by viewModels()

    override val loader get() = binding.activityMainLoader

    private val navHostFragment
        get() = supportFragmentManager.findFragmentById(
            R.id.activity_main_nav_host_fragment
        ) as NavHostFragment
    private val currentFragment
        get() = navHostFragment.childFragmentManager.fragments.firstOrNull()

    override fun onBind(layoutInflater: LayoutInflater) =
        ActivityMainBinding.inflate(layoutInflater)

    override fun onBackPressed() {
        val result = (currentFragment as? BaseFragment<*, *, *>)?.onBackPressed() ?: true
        if (result) super.onBackPressed()
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        val result = super.dispatchTouchEvent(event)
        event?.let {
            currentFocus?.let { view ->
                touchFocusCleaner.clearFocusOnTouchOutside(event, view)
            }
        }
        return result
    }

    override fun ActivityMainBinding.subscribe() {
        MobileAds.initialize(this@MainActivity) { }
        val adRequest = AdRequest.Builder().build()
        activityMainAdView.loadAd(adRequest)
    }
}
