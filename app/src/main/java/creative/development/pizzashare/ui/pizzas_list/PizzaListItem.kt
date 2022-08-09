package creative.development.pizzashare.ui.pizzas_list

import android.annotation.SuppressLint
import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import creative.development.pizzashare.R
import creative.development.pizzashare.data.holder.PizzasListItemClickHolder
import creative.development.pizzashare.data.model.Pizza
import creative.development.pizzashare.databinding.ItemPizzasListBinding
import creative.development.pizzashare.utils.extensions.roundToPlaces

class PizzaListItem(
    private val pizza: Pizza,
    private val clickListener: (PizzasListItemClickHolder) -> Unit,
    private val removeListener: (Int) -> Unit
) : BindableItem<ItemPizzasListBinding>() {

    override fun getLayout() = R.layout.item_pizzas_list

    @SuppressLint("SetTextI18n")
    override fun bind(binding: ItemPizzasListBinding, position: Int) {
        binding.itemPizzasListNameLabel.text = pizza.name
        binding.itemPizzasListDiameterLabel.text =
            "${pizza.diameter.roundToPlaces(2)}${binding.root.context.getString(R.string.config_unit_name)}"
        binding.itemPizzasListPriceLabel.text =
            "${pizza.price.roundToPlaces(2)}${binding.root.context.getString(R.string.config_price_currency)}"
        binding.itemPizzasListConsumersNumberLabel.text =
            "${pizza.consumersNumber}"
        binding.root.setOnClickListener {
            clickListener(PizzasListItemClickHolder(position, pizza))
        }
        binding.itemPizzasListRemoveButton.setOnClickListener {
            removeListener(position)
        }
    }

    override fun initializeViewBinding(view: View): ItemPizzasListBinding {
        return ItemPizzasListBinding.bind(view)
    }
}
