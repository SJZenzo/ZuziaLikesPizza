package creative.development.pizzashare.manager

import creative.development.pizzashare.data.model.Pizza

interface PizzaManager {
    fun init()
    fun add(pizza: Pizza)
    fun set(index: Int, pizza: Pizza)
    fun get(index: Int): Pizza
    fun getAll(): List<Pizza>
    fun remove(index: Int)
}
