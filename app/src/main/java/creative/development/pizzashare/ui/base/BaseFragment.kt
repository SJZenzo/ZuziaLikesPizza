package creative.development.pizzashare.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import creative.development.pizzashare.consts.Consts

abstract class BaseFragment<VB : ViewBinding, VM : BaseFragmentViewModel, AVM : BaseActivityViewModel> :
    Fragment() {

    abstract val viewModel: VM
    abstract val activityViewModel: AVM

    abstract val layoutResId: Int

    protected lateinit var binding: VB
        private set

    abstract fun onBind(view: View): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return context?.let { context ->
            FrameLayout(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                addView(
                    ViewStub(context, layoutResId).apply {
                        setOnInflateListener { _, inflated ->
                            onInflate(inflated)
                        }
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ((view as ViewGroup).children.first() as? ViewStub)?.inflate()
    }

    open fun onBackPressed() = true

    open fun VM.subscribe() {}
    open fun VB.subscribe() {}

    fun navigate(action: NavDirections) {
        activityViewModel.showLoader()
        binding.root.postDelayed({
            findNavController().navigate(action)
        }, Consts.MIN_DELAY_FRAGMENT_NAVIGATION)
    }

    fun popBackStack() {
        activityViewModel.showLoader()
        binding.root.postDelayed({
            findNavController().popBackStack()
        }, Consts.MIN_DELAY_FRAGMENT_NAVIGATION)
    }

    private fun onInflate(view: View) {
        binding = onBind(view)
        binding.subscribe()
        activityViewModel.hideLoader()
        viewModel.subscribe()
    }
}
