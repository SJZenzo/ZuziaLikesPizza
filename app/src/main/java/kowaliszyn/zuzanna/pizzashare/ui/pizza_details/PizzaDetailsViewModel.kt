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

    companion object {
        const val DEFAULT_DIAMETER = 0f
        const val DEFAULT_PRICE = 0f
        const val DEFAULT_SLICES = 8
        const val DEFAULT_CONSUMERS_NUMBER = 1
    }

    enum class CalculatingState {
        DEFAULT, CALCULATING, QUEUED, REFILL,
    }

    var calculatingState = CalculatingState.DEFAULT
    val loadedPizzaDataEvent = EventLiveData<Pizza>()
    val args = PizzaDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val pizzaIndex = args.pizzaIndex

    init {
        loadedPizzaDataEvent.value = args.pizzaDetails ?: generatePizza()
    }

    fun generatePizza(
        name: String? = null,
        diameter: Float? = null,
        price: Float? = null,
        sliceNumber: Int? = null,
        consumerNumber: Int? = null
    ) =
        Pizza(
            name ?: "",
            diameter ?: DEFAULT_DIAMETER,
            price ?: DEFAULT_PRICE,
            sliceNumber ?: DEFAULT_SLICES,
            consumerNumber?.takeIf { it > 0 } ?: DEFAULT_CONSUMERS_NUMBER
        )

    fun savePizza(pizza: Pizza) =
        if (pizzaIndex >= 0) pizzaManager.set(pizzaIndex, pizza)
        else pizzaManager.add(pizza)
}
