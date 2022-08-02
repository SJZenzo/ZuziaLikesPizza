package kowaliszyn.zuzanna.pizzashare.ui.pizzas_list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kowaliszyn.zuzanna.pizzashare.data.holder.PizzasListItemClickHolder
import kowaliszyn.zuzanna.pizzashare.manager.PizzaManager
import kowaliszyn.zuzanna.pizzashare.utils.EventLiveData
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
