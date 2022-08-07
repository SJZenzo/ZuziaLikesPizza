package creative.development.pizzashare.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import creative.development.pizzashare.R

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    abstract val viewModel: VM
    protected lateinit var binding: VB
        private set

    protected val priceCurrency get() = context?.getString(R.string.config_price_currency) ?: ""
    protected val unitName get() = context?.getString(R.string.config_unit_name) ?: ""

    abstract fun onBind(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = onBind(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.subscribe()
    }

    open fun VM.subscribe() {
    }
}
