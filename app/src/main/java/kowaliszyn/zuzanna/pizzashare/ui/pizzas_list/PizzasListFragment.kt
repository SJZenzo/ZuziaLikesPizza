package kowaliszyn.zuzanna.pizzashare.ui.pizzas_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kowaliszyn.zuzanna.pizzashare.config.Consts
import kowaliszyn.zuzanna.pizzashare.databinding.FragmentPizzasListBinding
import kowaliszyn.zuzanna.pizzashare.ui.base.BaseFragment
import kowaliszyn.zuzanna.pizzashare.utils.extensions.roundToPlaces

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
        }
        countedTotalCostEvent.observe(viewLifecycleOwner) { totalCost ->
            binding.fragmentPizzasListTotalCostLabel.editText?.setText(
                "${totalCost.roundToPlaces(2)}$priceCurrency"
            )
        }
        onClickPizzasListItemEvent.observe(viewLifecycleOwner) { pizzaClickHolder ->
            goToPizzaDetails(pizzaClickHolder.pizzaIndex)
        }
    }
}
