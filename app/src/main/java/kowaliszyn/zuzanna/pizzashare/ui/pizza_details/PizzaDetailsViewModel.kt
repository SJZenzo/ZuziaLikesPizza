package kowaliszyn.zuzanna.pizzashare.ui.pizza_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kowaliszyn.zuzanna.pizzashare.data.model.Pizza
import kowaliszyn.zuzanna.pizzashare.manager.PizzaManager
import kowaliszyn.zuzanna.pizzashare.utils.EventLiveData
import javax.inject.Inject

@HiltViewModel
class PizzaDetailsViewModel @Inject constructor(
    private val pizzaManager: PizzaManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    enum class CalculatingState {
        DEFAULT, CALCULATING, QUEUED, REFILL,
    }

    var calculatingState = CalculatingState.DEFAULT
    val loadedPizzaDataEvent = EventLiveData<Pizza>()
    val args = PizzaDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val pizzaIndex = args.pizzaIndex

    init {
        loadedPizzaDataEvent.value = args.pizzaDetails
    }

    fun generatePizza(diameter: Float?, price: Float?, consumerNumber: Int?) =
        Pizza(
            diameter ?: 0f,
            price ?: 0f,
            consumerNumber?.takeIf { it > 0 } ?: 1
        )

    fun savePizza(pizza: Pizza) =
        if (pizzaIndex >= 0) pizzaManager.set(pizzaIndex, pizza)
        else pizzaManager.add(pizza)
}
