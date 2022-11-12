package creative.development.pizzashare.ui.pizzasList

import creative.development.pizzashare.data.holder.PizzasListItemDataHolder
import creative.development.pizzashare.data.model.Pizza
import creative.development.pizzashare.manager.PizzaManager
import creative.development.pizzashare.ui.base.BaseFragmentViewModel
import creative.development.pizzashare.utils.EventLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PizzasListViewModel @Inject constructor(
    private val pizzaManager: PizzaManager
) : BaseFragmentViewModel() {

    val loadedPizzasListEvent = EventLiveData<List<PizzaListItem>>()
    val countedTotalCostEvent = EventLiveData<Double>()
    val onClickPizzasListItemEvent = EventLiveData<PizzasListItemDataHolder>()
    val onRemovePizzasListItemEvent = EventLiveData<PizzasListItemDataHolder>()
    var isViewArchive: Boolean = false

    fun refreshPizzasList() {
        pizzaManager.getAll(isViewArchive).let { pizzasList ->
            loadedPizzasListEvent.value = pizzasList.map { pizza ->
                PizzaListItem(
                    pizza = pizza,
                    clickListener = { pizzaDataHolder ->
                        onClickPizzasListItemEvent.value = pizzaDataHolder
                    },
                    removeListener = { pizzaDataHolder ->
                        onRemovePizzasListItemEvent.value = pizzaDataHolder
                    }
                )
            }
            countedTotalCostEvent.value = pizzasList.sumOf { pizza ->
                pizza.price.toDouble()
            }
        }
    }

    fun removePizzaItem(pizzaIndex: Int) {
        pizzaManager.remove(pizzaIndex)
        refreshPizzasList()
    }

    fun archivePizzaItem(pizzaIndex: Int) {
        pizzaManager.setArchive(pizzaIndex, isViewArchive.not())
        refreshPizzasList()
    }

    fun changeViewArchiveOrNot() {
        isViewArchive = isViewArchive.not()
        refreshPizzasList()
    }
}
