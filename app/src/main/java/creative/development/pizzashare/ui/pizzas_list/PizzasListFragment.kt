package creative.development.pizzashare.ui.pizzas_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import creative.development.pizzashare.config.Consts
import creative.development.pizzashare.databinding.FragmentPizzasListBinding
import creative.development.pizzashare.ui.base.BaseFragment
import creative.development.pizzashare.utils.extensions.roundToPlaces
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
        onClickPizzasListItemEvent.observe(viewLifecycleOwner) { pizzaClickHolder ->
            goToPizzaDetails(pizzaClickHolder.pizzaIndex)
        }
    }
}
