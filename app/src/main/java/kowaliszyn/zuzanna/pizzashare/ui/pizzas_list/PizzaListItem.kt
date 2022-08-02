package kowaliszyn.zuzanna.pizzashare.ui.pizzas_list

import android.annotation.SuppressLint
import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import kowaliszyn.zuzanna.pizzashare.R
import kowaliszyn.zuzanna.pizzashare.data.holder.PizzasListItemClickHolder
import kowaliszyn.zuzanna.pizzashare.data.model.Pizza
import kowaliszyn.zuzanna.pizzashare.databinding.ItemPizzasListBinding
import kowaliszyn.zuzanna.pizzashare.utils.extensions.roundToPlaces

class PizzaListItem(
    private val pizza: Pizza,
    private val clickListener: (PizzasListItemClickHolder) -> Unit,
    private val removeListener: (Int) -> Unit
) : BindableItem<ItemPizzasListBinding>() {

    override fun getLayout() = R.layout.item_pizzas_list

    @SuppressLint("SetTextI18n")
    override fun bind(binding: ItemPizzasListBinding, position: Int) {
        binding.itemPizzasListDiameterLabel.text =
            "${pizza.diameter.roundToPlaces(2)}${binding.root.context.getString(R.string.config_unit_name)}"
        binding.itemPizzasListPriceLabel.text =
            "${pizza.price.roundToPlaces(2)}${binding.root.context.getString(R.string.config_price_currency)}"
        binding.itemPizzasListConsumersNumberLabel.text =
            "${pizza.consumersNumber} consumers"
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
