package creative.development.pizzashare.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() {

    abstract val viewModel: VM
    protected lateinit var binding: VB
        private set

    protected val lifeCycleOwner get() = this as LifecycleOwner

    abstract fun onBind(layoutInflater: LayoutInflater): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = onBind(LayoutInflater.from(this))
        setContentView(binding.root)
        viewModel.subscribe()
    }

    open fun VM.subscribe() {
    }
}
