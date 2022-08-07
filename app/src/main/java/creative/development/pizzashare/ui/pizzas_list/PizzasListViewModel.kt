package creative.development.pizzashare.ui.pizzas_list

import androidx.lifecycle.ViewModel
import creative.development.pizzashare.data.holder.PizzasListItemClickHolder
import creative.development.pizzashare.manager.PizzaManager
import creative.development.pizzashare.utils.EventLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PizzasListViewModel @Inject constructor(
    private val pizzaManager: PizzaManager
) : ViewModel() {

    val loadedPizzasListEvent = EventLiveData<List<PizzaListItem>>()
    val countedTotalCostEvent = EventLiveData<Double>()
    val onClickPizzasListItemEvent = EventLiveData<PizzasListItemClickHolder>()

    fun refreshPizzasList() {
        pizzaManager.get().let { pizzasList ->
            loadedPizzasListEvent.value = pizzasList.map { pizza ->
                PizzaListItem(
                    pizza = pizza,
                    clickListener = { pizzaClickHolder ->
                        onClickPizzasListItemEvent.value = pizzaClickHolder
                    },
                    removeListener = { pizzaIndex ->
                        pizzaManager.remove(pizzaIndex)
                        refreshPizzasList()
                    }
                )
            }
            countedTotalCostEvent.value = pizzasList.sumOf { pizza ->
                pizza.price.toDouble()
            }
        }
    }
}
