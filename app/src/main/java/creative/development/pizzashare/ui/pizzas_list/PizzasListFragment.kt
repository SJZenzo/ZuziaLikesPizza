package creative.development.pizzashare.ui.pizzas_list

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import creative.development.pizzashare.R
import creative.development.pizzashare.consts.Consts
import creative.development.pizzashare.data.holder.PizzasListItemDataHolder
import creative.development.pizzashare.databinding.FragmentPizzasListBinding
import creative.development.pizzashare.ui.base.BaseFragment
import creative.development.pizzashare.utils.extensions.roundToPlaces
import creative.development.pizzashare.utils.extensions.showConfirmDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PizzasListFragment : BaseFragment<FragmentPizzasListBinding, PizzasListViewModel>() {

    override val viewModel: PizzasListViewModel by viewModels()
    private val pizzasListAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onResume() {
        super.onResume()
        viewModel.refreshPizzasList()
    }

    override fun onBind(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ) = FragmentPizzasListBinding.inflate(layoutInflater, parent, attachToParent).apply {
        subscribe()
    }

    private fun goToPizzaDetails(pizzaIndex: Int = Consts.NEW_PIZZA_INDEX) {
        val action = PizzasListFragmentDirections.actionFragmentPizzasListToFragmentPizzaDetails(
            pizzaIndex
        )
        findNavController().navigate(action)
    }

    private fun FragmentPizzasListBinding.subscribe() {
        fragmentPizzasListAddPizzaFloatingButton.setOnClickListener {
            goToPizzaDetails()
        }
        fragmentPizzasListRecyclerView.adapter = pizzasListAdapter
    }

    private fun showRemovePizzasListItemConfirmationDialog(
        pizzaDataHolder: PizzasListItemDataHolder
    ) {
        val content = getString(
            R.string.dialog_remove_pizzas_list_item_content,
            pizzaDataHolder.pizza.name
        ).let { content ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(
                    content,
                    Html.FROM_HTML_MODE_COMPACT
                )
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(content)
            }
        }
        context?.showConfirmDialog(
            title = getString(R.string.dialog_remove_pizzas_list_item_title),
            content = content,
            approveButtonText = getString(R.string.dialog_remove_pizzas_list_agree_button_text)
        ) {
            viewModel.removePizzaItem(pizzaDataHolder.pizzaIndex)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun PizzasListViewModel.subscribe() {
        loadedPizzasListEvent.observe(viewLifecycleOwner) { pizzaItemsList ->
            pizzasListAdapter.clear()
            pizzasListAdapter.addAll(pizzaItemsList)
            binding.layoutEmptyPizzasList.root.isVisible = pizzaItemsList.isEmpty()
        }
        countedTotalCostEvent.observe(viewLifecycleOwner) { totalCost ->
            binding.fragmentPizzasListTotalCostLabel.apply {
                addOnInflatedListener {
                    editText?.setText(totalCost.roundToPlaces(2))
                }
            }
        }
        onClickPizzasListItemEvent.observe(viewLifecycleOwner) { pizzaDataHolder ->
            goToPizzaDetails(pizzaDataHolder.pizzaIndex)
        }
        onRemovePizzasListItemEvent.observe(viewLifecycleOwner) { pizzaDataHolder ->
            showRemovePizzasListItemConfirmationDialog(pizzaDataHolder)
        }
    }
}
