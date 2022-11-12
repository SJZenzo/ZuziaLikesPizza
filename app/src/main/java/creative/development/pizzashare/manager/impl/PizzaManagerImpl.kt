package creative.development.pizzashare.manager.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import creative.development.pizzashare.consts.Consts
import creative.development.pizzashare.data.model.Pizza
import creative.development.pizzashare.manager.PizzaManager
import creative.development.pizzashare.utils.extensions.fromJson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class PizzaManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : PizzaManager {

    private val pizzasList = mutableListOf<Pizza>()
    private val pizzasListDataStoreJsonKey =
        stringPreferencesKey(Consts.PIZZAS_LIST_STORAGE_KEY_NAME)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val Context.pizzasListDataStore: DataStore<Preferences> by preferencesDataStore(
        name = Consts.PIZZAS_LIST_STORAGE_NAME
    )

    override fun init() {
        coroutineScope.launch {
            pizzasList.clear()
            pizzasList.addAll(context.loadPizzasList())
        }
    }

    override fun add(pizza: Pizza) {
        pizzasList.add(0, pizza)
        savePizzasList()
    }

    override fun set(index: Int, pizza: Pizza) {
        pizzasList[index] = pizza
        savePizzasList()
    }

    override fun setArchive(index: Int, archive: Boolean) {
        pizzasList[index].isArchive = archive
        savePizzasList()
    }

    override fun get(index: Int, archiveOrNot: Boolean): Pizza {
        return pizzasList.filter {
            it.isArchive == archiveOrNot
        }[index]
    }

    override fun getAll(archiveOrNot: Boolean): List<Pizza> {
        return pizzasList.filter { it.isArchive  == archiveOrNot }
    }

    override fun remove(index: Int) {
        pizzasList.removeAt(index)
        savePizzasList()
    }

    private fun savePizzasList() {
        coroutineScope.launch {
            context.savePizzasList(pizzasList)
        }
    }

    private suspend fun Context.loadPizzasList() =
        pizzasListDataStore.data.map { preferences ->
            preferences[pizzasListDataStoreJsonKey]?.let {
                gson.fromJson<List<Pizza>>(it)
            } ?: listOf()
        }.first()

    private suspend fun Context.savePizzasList(pizzasList: List<Pizza>) =
        pizzasListDataStore.edit { preferences ->
            preferences[pizzasListDataStoreJsonKey] = gson.toJson(pizzasList)
        }
}
