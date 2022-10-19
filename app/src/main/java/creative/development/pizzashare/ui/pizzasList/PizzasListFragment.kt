package creative.development.pizzashare.ui.pizzasList

import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import creative.development.pizzashare.R
import creative.development.pizzashare.consts.Consts
import creative.development.pizzashare.data.holder.PizzasListItemDataHolder
import creative.development.pizzashare.databinding.FragmentPizzasListBinding
import creative.development.pizzashare.ui.base.BaseFragment
import creative.development.pizzashare.ui.main.MainViewModel
import creative.development.pizzashare.utils.extensions.roundToPlaces
import creative.development.pizzashare.utils.extensions.showDeleteDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PizzasListFragment :
    BaseFragment<FragmentPizzasListBinding, PizzasListViewModel, MainViewModel>() {

    override val viewModel: PizzasListViewModel by viewModels()
    override val activityViewModel: MainViewModel by activityViewModels()

    override val layoutResId get() = R.layout.fragment_pizzas_list

    private val pizzasListAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onResume() {
        super.onResume()
        viewModel.refreshPizzasList()
    }

    override fun onBind(view: View) = FragmentPizzasListBinding.bind(view)

    override fun FragmentPizzasListBinding.subscribe() {
        fragmentPizzasListAddPizzaFloatingButton.setOnClickListener {
            goToPizzaDetails()
        }
        fragmentPizzasListRecyclerView.adapter = pizzasListAdapter
    }

    @SuppressLint("SetTextI18n")
    override fun PizzasListViewModel.subscribe() {
        loadedPizzasListEvent.observe(viewLifecycleOwner) { pizzaItemsList ->
            pizzasListAdapter.clear()
            pizzasListAdapter.addAll(pizzaItemsList)
            binding.layoutEmptyPizzasList.root.isVisible = pizzaItemsList.isEmpty()
        }
        countedTotalCostEvent.observe(viewLifecycleOwner) { totalCost ->
            binding.fragmentPizzasListTotalCostLabel.editText?.setText(
                totalCost.roundToPlaces(2)
            )
        }
        onClickPizzasListItemEvent.observe(viewLifecycleOwner) { pizzaDataHolder ->
            goToPizzaDetails(pizzaDataHolder.pizzaIndex)
        }
        onRemovePizzasListItemEvent.observe(viewLifecycleOwner) { pizzaDataHolder ->
            showRemovePizzasListItemConfirmationDialog(pizzaDataHolder)
        }
    }

    private fun goToPizzaDetails(pizzaIndex: Int = Consts.NEW_PIZZA_INDEX) {
        val action = PizzasListFragmentDirections.actionFragmentPizzasListToFragmentPizzaDetails(
            pizzaIndex
        )
        navigate(action)
    }

    private fun showRemovePizzasListItemConfirmationDialog(
        pizzaDataHolder: PizzasListItemDataHolder
    ) {
        context?.showDeleteDialog(
            title = getString(R.string.dialog_remove_pizzas_list_item_title),
            content = getString(
                R.string.dialog_remove_pizzas_list_item_content,
                pizzaDataHolder.pizza.name
            ),
            approveButtonText = getString(R.string.dialog_remove_pizzas_list_agree_button_text),
        ) {
            viewModel.removePizzaItem(pizzaDataHolder.pizzaIndex)
        }
    }
}
