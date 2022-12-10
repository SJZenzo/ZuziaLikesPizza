package creative.development.pizzashare.ui.pizzasList

import creative.development.pizzashare.R
import creative.development.pizzashare.data.holder.PizzasListItemDataHolder
import creative.development.pizzashare.manager.PizzaManager
import creative.development.pizzashare.ui.base.BaseFragmentViewModel
import creative.development.pizzashare.utils.EventLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PizzasListViewModel @Inject constructor(
    private val pizzaManager: PizzaManager
) : BaseFragmentViewModel() {

    private var isViewArchive: Boolean = false
    private var viewTypeAll: Boolean = false
    val loadedPizzasListEvent = EventLiveData<List<PizzaListItem>>()
    val countedTotalCostEvent = EventLiveData<Double>()
    val onClickPizzasListItemEvent = EventLiveData<PizzasListItemDataHolder>()
    val onRemovePizzasListItemEvent = EventLiveData<PizzasListItemDataHolder>()

    fun refreshPizzasList() {
        pizzaManager.getAll(isViewArchive, viewTypeAll).let { pizzasList ->
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
        if (isViewArchive)
            pizzaManager.restoreFromArchive(pizzaIndex)
        else pizzaManager.archivate(pizzaIndex)
        refreshPizzasList()
    }

    fun changeViewArchiveOrNot() {
        isViewArchive = isViewArchive.not()
        refreshPizzasList()
    }

    fun getBtnArchiveIconResId(): Int {
        return if (isViewArchive)
            R.drawable.ic_unarchive
        else R.drawable.ic_archives
    }

    fun getBtnAddIsEnable(): Boolean {
        return !isViewArchive
    }

    fun setListView(index: Int){
        viewTypeAll = index != 0
        refreshPizzasList()
    }
    fun getListChosen(): Int {
        return if (viewTypeAll) 0
        else 1
    }


}