package kowaliszyn.zuzanna.pizzashare.manager

import kowaliszyn.zuzanna.pizzashare.data.model.Pizza

interface PizzaManager {
    fun add(pizza: Pizza)
    fun set(index: Int, pizza: Pizza)
    fun get(index: Int): Pizza
    fun get(): List<Pizza>
    fun remove(index: Int)
}
