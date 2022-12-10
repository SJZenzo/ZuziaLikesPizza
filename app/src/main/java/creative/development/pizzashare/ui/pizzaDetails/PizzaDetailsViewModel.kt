package creative.development.pizzashare.ui.pizzaDetails

import androidx.lifecycle.SavedStateHandle
import creative.development.pizzashare.consts.Consts
import creative.development.pizzashare.data.model.Pizza
import creative.development.pizzashare.manager.PizzaManager
import creative.development.pizzashare.ui.base.BaseFragmentViewModel
import creative.development.pizzashare.utils.EventLiveData
import creative.development.pizzashare.utils.extensions.coerceRangeOrDef
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PizzaDetailsViewModel @Inject constructor(
    private val pizzaManager: PizzaManager,
    savedStateHandle: SavedStateHandle
) : BaseFragmentViewModel() {

    companion object {
        const val DEFAULT_DIAMETER = 0f
        const val DEFAULT_PRICE = 0f
        const val DEFAULT_SLICES = 8
        const val DEFAULT_CONSUMERS_NUMBER = 1
        const val MAX_DIAMETER = 999.99
        const val MAX_PRICE = 999.99
        const val MAX_SLICES = 99
        const val MAX_CONSUMERS_NUMBER = 99
    }

    val loadedPizzaDataEvent = EventLiveData<Pizza>()
    var wasEdited = false

    private val args = PizzaDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val pizzaIndex = args.pizzaIndex
    private val isArchive = args.isArchive
    private val originalPizza =
        if (pizzaIndex != Consts.NEW_PIZZA_INDEX) {
            pizzaManager.get(isArchive, pizzaIndex)
        } else generatePizza(compareWithOriginal = false)

    init {
        loadedPizzaDataEvent.value = originalPizza
    }

    fun generatePizza(
        name: String? = null,
        diameter: Float? = null,
        price: Float? = null,
        sliceNumber: Int? = null,
        consumerNumber: Int? = null,
        compareWithOriginal: Boolean = true
    ) =
        Pizza(
            name ?: "",
            diameter.coerceRangeOrDef(
                DEFAULT_DIAMETER..MAX_DIAMETER.toFloat(),
                DEFAULT_DIAMETER
            ),
            price.coerceRangeOrDef(
                DEFAULT_PRICE..MAX_PRICE.toFloat(),
                DEFAULT_PRICE
            ),
            sliceNumber.coerceRangeOrDef(
                DEFAULT_SLICES..MAX_SLICES,
                DEFAULT_SLICES
            ),
            consumerNumber.coerceRangeOrDef(
                DEFAULT_CONSUMERS_NUMBER..MAX_CONSUMERS_NUMBER,
                DEFAULT_CONSUMERS_NUMBER
            )
        ).also { pizza ->
            if (compareWithOriginal) pizza.compareWithOriginal()
        }

    fun savePizza(pizza: Pizza) =
        if (pizzaIndex >= 0) pizzaManager.set(pizzaIndex, pizza)
        else pizzaManager.add(pizza)

    private fun Pizza.compareWithOriginal() {
        if (originalPizza != this) {
            wasEdited = true
        }
    }
}
