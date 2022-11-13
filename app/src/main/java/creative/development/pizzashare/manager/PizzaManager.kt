package creative.development.pizzashare.manager

import creative.development.pizzashare.data.model.Pizza

interface PizzaManager {
    fun init()
    fun add(pizza: Pizza)
    fun set(index: Int, pizza: Pizza)
    fun archivate(index: Int)
    fun restoreFromArchive(index: Int)
    fun get(fromArchiveList: Boolean, index: Int): Pizza
    fun getAll(archiveOrNot: Boolean): List<Pizza>
    fun remove(index: Int)
}
