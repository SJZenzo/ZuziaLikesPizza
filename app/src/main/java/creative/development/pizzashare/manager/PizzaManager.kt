package creative.development.pizzashare.manager

import creative.development.pizzashare.data.model.Pizza

interface PizzaManager {
    fun init()
    fun add(pizza: Pizza)
    fun set(index: Int, pizza: Pizza)
    fun setArchive(index: Int, archive: Boolean)
    fun get(index: Int): Pizza
    fun getAll(archiveOrNot: Boolean): List<Pizza>
    fun remove(index: Int)
}
