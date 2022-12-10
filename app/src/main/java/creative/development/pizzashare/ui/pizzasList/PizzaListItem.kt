package creative.development.pizzashare.ui.pizzasList

import android.annotation.SuppressLint
import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import creative.development.pizzashare.R
import creative.development.pizzashare.data.holder.PizzasListItemDataHolder
import creative.development.pizzashare.data.model.Pizza
import creative.development.pizzashare.databinding.ItemPizzasListBinding
import creative.development.pizzashare.utils.extensions.getCurrencyFormat
import creative.development.pizzashare.utils.extensions.getSurfaceFormat
import creative.development.pizzashare.utils.extensions.roundToPlaces

class PizzaListItem(
    private val pizza: Pizza,
    private val clickListener: (PizzasListItemDataHolder) -> Unit,
    private val removeListener: (PizzasListItemDataHolder) -> Unit
) : BindableItem<ItemPizzasListBinding>() {

    override fun getLayout() = R.layout.item_pizzas_list

    @SuppressLint("SetTextI18n")
    override fun bind(binding: ItemPizzasListBinding, position: Int) {
        with(binding) {
            itemPizzasListNameLabel.text = pizza.name
            itemPizzasListDiameterLabel.text =
                pizza.diameter.roundToPlaces(2).getSurfaceFormat(root.context)
            itemPizzasListPriceLabel.text =
                pizza.price.roundToPlaces(2).getCurrencyFormat(root.context)
            itemPizzasListConsumersNumberLabel.text =
                "${pizza.consumersNumber}"
            itemPizzasListConsumersNumberLabelSmallScreens.text =
                "${pizza.consumersNumber}"
            root.setOnClickListener {
                clickListener(PizzasListItemDataHolder(position, pizza.isArchive, pizza))
            }
            itemPizzasListRemoveButton.setOnClickListener {
                removeListener(PizzasListItemDataHolder(position, pizza.isArchive, pizza))
            }
        }
    }

    override fun initializeViewBinding(view: View): ItemPizzasListBinding {
        return ItemPizzasListBinding.bind(view)
    }
}
