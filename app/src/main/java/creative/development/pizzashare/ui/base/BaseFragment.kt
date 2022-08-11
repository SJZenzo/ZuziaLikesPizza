package creative.development.pizzashare.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    abstract val viewModel: VM
    protected lateinit var binding: VB
        private set

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

    open fun onBackPressed() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.subscribe()
    }

    open fun VM.subscribe() {
    }
}
