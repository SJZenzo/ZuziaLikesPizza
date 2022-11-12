package creative.development.pizzashare.ui.pizzasList

import android.annotation.SuppressLint
import android.graphics.Color
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
import creative.development.pizzashare.utils.extensions.showDialog
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
            goToPizzaDetails(viewModel.isViewArchive)
        }
        btnArchives.setOnClickListener {
            viewModel.changeViewArchiveOrNot()
            if (viewModel.isViewArchive)
                it.setBackgroundResource(R.drawable.ic_unarchive)
            else it.setBackgroundResource(R.drawable.ic_archives)
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
            goToPizzaDetails(viewModel.isViewArchive, pizzaDataHolder.pizzaIndex)
        }
        onRemovePizzasListItemEvent.observe(viewLifecycleOwner) { pizzaDataHolder ->
            showRemovePizzasListItemConfirmationDialog(pizzaDataHolder)
        }
    }

    private fun goToPizzaDetails(isArchiveOrNot: Boolean, pizzaIndex: Int = Consts.NEW_PIZZA_INDEX) {
        val action = PizzasListFragmentDirections.actionFragmentPizzasListToFragmentPizzaDetails(
            pizzaIndex,
            isArchiveOrNot
        )
        navigate(action)

    }



    private fun showRemovePizzasListItemConfirmationDialog(
        pizzaDataHolder: PizzasListItemDataHolder
    ) {
        context?.showDialog(title = getString(R.string.dialog_remove_pizzas_list_item_title),
            content = getString(
                R.string.dialog_remove_pizzas_list_item_content, pizzaDataHolder.pizza.name
            ),
            approveButtonText = getString(R.string.dialog_remove_pizzas_list_agree_button_text),
            extraButtonText = if (!viewModel.isViewArchive)
                getString(R.string.dialog_remove_pizzas_list_archive_button_text)
            else getString(R.string.dialog_remove_pizzas_list_unarchive_button_text),
            onApproveAction = {
                viewModel.removePizzaItem(
                    pizzaDataHolder.pizzaIndex
                )
            },
            onExtraAction = {
                viewModel.archivePizzaItem(
                    pizzaDataHolder.pizzaIndex
                )
            })
    }
}
