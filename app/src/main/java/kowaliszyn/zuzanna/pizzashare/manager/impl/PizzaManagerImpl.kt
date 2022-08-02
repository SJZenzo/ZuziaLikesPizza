package kowaliszyn.zuzanna.pizzashare.manager.impl

import kowaliszyn.zuzanna.pizzashare.data.model.Pizza
import kowaliszyn.zuzanna.pizzashare.manager.PizzaManager
import javax.inject.Inject

class PizzaManagerImpl @Inject constructor() : PizzaManager {

    private val pizzasList = mutableListOf<Pizza>()

    override fun add(pizza: Pizza) {
        pizzasList.add(0, pizza)
    }

    override fun set(index: Int, pizza: Pizza) {
        pizzasList[index] = pizza
    }

    override fun get(index: Int): Pizza {
        return pizzasList[index]
    }

    override fun get(): List<Pizza> {
        return pizzasList
    }

    override fun remove(index: Int) {
        pizzasList.removeAt(index)
    }
}
