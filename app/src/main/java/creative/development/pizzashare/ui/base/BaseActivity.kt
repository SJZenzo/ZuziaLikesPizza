package creative.development.pizzashare.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import creative.development.pizzashare.utils.view.LoaderView

abstract class BaseActivity<VB : ViewBinding, VM : BaseActivityViewModel> : AppCompatActivity() {

    abstract val viewModel: VM
    open val loader: LoaderView? = null

    protected lateinit var binding: VB
        private set

    protected val lifeCycleOwner get() = this as LifecycleOwner

    abstract fun onBind(layoutInflater: LayoutInflater): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = onBind(LayoutInflater.from(this))
        binding.subscribe()
        loader?.changeLoaderVisibility(false)
        setContentView(binding.root)
        viewModel.apply {
            changeLoaderVisibilityEvent.observe(lifeCycleOwner) { isLoaderVisible ->
                loader?.changeLoaderVisibility(isLoaderVisible)
            }
            subscribe()
        }
    }

    open fun VM.subscribe() {}
    open fun VB.subscribe() {}

    private fun LoaderView.changeLoaderVisibility(isLoaderVisible: Boolean) {
        if (isLoaderVisible) start()
        else stop()
        isVisible = isLoaderVisible
    }
}
