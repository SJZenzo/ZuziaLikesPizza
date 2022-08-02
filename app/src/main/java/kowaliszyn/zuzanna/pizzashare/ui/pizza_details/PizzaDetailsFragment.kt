package kowaliszyn.zuzanna.pizzashare.ui.pizza_details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kowaliszyn.zuzanna.pizzashare.R
import kowaliszyn.zuzanna.pizzashare.data.model.Pizza
import kowaliszyn.zuzanna.pizzashare.databinding.FragmentPizzaDetailsBinding
import kowaliszyn.zuzanna.pizzashare.ui.base.BaseFragment
import kowaliszyn.zuzanna.pizzashare.utils.extensions.roundToPlaces

@AndroidEntryPoint
class PizzaDetailsFragment : BaseFragment<FragmentPizzaDetailsBinding, PizzaDetailsViewModel>() {

    override val viewModel: PizzaDetailsViewModel by viewModels()

    override fun onBind(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ) = FragmentPizzaDetailsBinding.inflate(layoutInflater, parent, attachToParent).apply {
        bindingSubscribe()
    }

    override fun PizzaDetailsViewModel.onViewModelSubscribe() {
        loadedPizzaDataEvent.observe(viewLifecycleOwner) { pizza ->
            binding.bindingRefill(pizza)
        }
    }

    private fun FragmentPizzaDetailsBinding.generatePizza(): Pizza {
        return viewModel.generatePizza(
            fragmentPizzaDetailsPizzaDiameterInput.editText?.text.toString().toFloatOrNull(),
            fragmentPizzaDetailsPizzaPriceInput.editText?.text.toString().toFloatOrNull(),
            fragmentPizzaDetailsPizzaConsumersNumberInput.editText?.text.toString().toIntOrNull()
        )
    }

    @SuppressLint("SetTextI18n")
    private fun FragmentPizzaDetailsBinding.calculate(pizzaFromRefill: Pizza? = null) {
        with(viewModel) {
            when (calculatingState) {
                PizzaDetailsViewModel.CalculatingState.CALCULATING -> {
                    binding.root.post { calculate() }
                    calculatingState = PizzaDetailsViewModel.CalculatingState.QUEUED
                }
                PizzaDetailsViewModel.CalculatingState.REFILL -> return
                else -> Unit
            }
            calculatingState = PizzaDetailsViewModel.CalculatingState.CALCULATING
        }
        val pizza = pizzaFromRefill ?: generatePizza()
        fragmentPizzaDetailsSurfaceInput.editText?.setText(
            "${pizza.surface.roundToPlaces(2)}$unitName"
        )
        fragmentPizzaDetailsPricePerUnitInput.editText?.setText(
            "${pizza.pricePerUnit.roundToPlaces(2)}$priceCurrency"
        )
        fragmentPizzaDetailsPricePerConsumerInput.editText?.setText(
            "${pizza.pricePerConsumer.roundToPlaces(2)}$priceCurrency"
        )
        viewModel.calculatingState = PizzaDetailsViewModel.CalculatingState.DEFAULT
    }

    private fun EditText.calculateAfterEdit() {
        doAfterTextChanged { binding.calculate() }
    }

    private fun EditText.secureEmptyValue(defValue: String) {
        setOnFocusChangeListener { _, hasFocus ->
            val isZero = text.toString().toDoubleOrNull() == 0.0
            if (hasFocus) {
                if (isZero) setText("")
            } else {
                if (text.toString().isEmpty() || isZero) setText(defValue)
            }
        }
    }

    private fun FragmentPizzaDetailsBinding.bindingSubscribe() {
        fragmentPizzaDetailsPricePerUnitInput.hint =
            context?.getString(
                R.string.fragment_pizza_details_price_per_unit_label
            )?.format(
                context?.getString(R.string.config_unit_name) ?: ""
            ) ?: ""
        fragmentPizzaDetailsPizzaDiameterInput.editText?.apply {
            calculateAfterEdit()
            secureEmptyValue(0.0.roundToPlaces(2))
        }
        fragmentPizzaDetailsPizzaPriceInput.editText?.apply {
            calculateAfterEdit()
            secureEmptyValue(0.0.roundToPlaces(2))
        }
        fragmentPizzaDetailsPizzaConsumersNumberInput.editText?.apply {
            calculateAfterEdit()
            secureEmptyValue("1")
        }
        fragmentPizzaDetailsSaveButton.setOnClickListener {
            viewModel.savePizza(generatePizza())
            findNavController().popBackStack()
        }
    }

    private fun FragmentPizzaDetailsBinding.bindingRefill(pizza: Pizza) {
        viewModel.calculatingState = PizzaDetailsViewModel.CalculatingState.REFILL
        fragmentPizzaDetailsPizzaDiameterInput.editText?.setText(
            pizza.diameter.roundToPlaces(2)
        )
        fragmentPizzaDetailsPizzaPriceInput.editText?.setText(
            pizza.price.roundToPlaces(2)
        )
        fragmentPizzaDetailsPizzaConsumersNumberInput.editText?.setText(
            pizza.consumersNumber.toString()
        )
        viewModel.calculatingState = PizzaDetailsViewModel.CalculatingState.DEFAULT
        calculate(pizza)
    }
}
