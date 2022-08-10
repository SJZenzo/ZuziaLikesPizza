package creative.development.pizzashare.ui.pizza_details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import creative.development.pizzashare.R
import creative.development.pizzashare.data.model.Pizza
import creative.development.pizzashare.databinding.FragmentPizzaDetailsBinding
import creative.development.pizzashare.ui.base.BaseFragment
import creative.development.pizzashare.utils.extensions.*
import creative.development.pizzashare.utils.view.InputView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PizzaDetailsFragment : BaseFragment<FragmentPizzaDetailsBinding, PizzaDetailsViewModel>() {

    override val viewModel: PizzaDetailsViewModel by viewModels()

    private val errorsSet: MutableSet<String> = mutableSetOf()

    override fun onBind(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ) = FragmentPizzaDetailsBinding.inflate(layoutInflater, parent, attachToParent).apply {
        subscribe()
    }

    override fun PizzaDetailsViewModel.subscribe() {
        loadedPizzaDataEvent.observe(viewLifecycleOwner) { pizza ->
            binding.fragmentPizzaDetailsContainer.doAfterChildrenInflated {
                binding.refill(pizza)
            }
        }
    }

    private fun FragmentPizzaDetailsBinding.onInputStateChangeListener(
        isValid: Boolean,
        errorMessage: String
    ) {
        if (isValid) errorsSet.remove(errorMessage)
        else errorsSet.add(errorMessage)
        fragmentPizzaDetailsErrorLabel.apply {
            if (errorsSet.isEmpty()) {
                visibility = View.GONE
                text = ""
            } else {
                text = errorsSet.first()
                fragmentPizzaDetailsErrorLabel.visibility = View.VISIBLE
            }
        }
    }

    private fun FragmentPizzaDetailsBinding.generatePizza(): Pizza {
        return viewModel.generatePizza(
            fragmentPizzaDetailsPizzaNameInput.text,
            fragmentPizzaDetailsPizzaDiameterInput.unformattedText.toFloatOrNull(),
            fragmentPizzaDetailsPizzaPriceInput.unformattedText.toFloatOrNull(),
            fragmentPizzaDetailsPizzaSlicesNumberInput.text.toIntOrNull(),
            fragmentPizzaDetailsPizzaConsumersNumberInput.text.toIntOrNull()
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
        fragmentPizzaDetailsSummarySurfaceValue.text =
            pizza.surface.roundToPlaces(2).getSurfaceFormat(root.context)
        fragmentPizzaDetailsSummaryPricePerUnitValue.text =
            pizza.pricePerUnit.roundToPlaces(2).getCurrencyFormat(root.context)
        fragmentPizzaDetailsSummaryPricePerSliceValue.text =
            pizza.pricePerSlice.roundToPlaces(2).getCurrencyFormat(root.context)
        fragmentPizzaDetailsSummaryPricePerConsumerValue.text =
            pizza.pricePerConsumer.roundToPlaces(2).getCurrencyFormat(root.context)
        viewModel.calculatingState = PizzaDetailsViewModel.CalculatingState.DEFAULT
    }

    private fun InputView.calculateAfterEdit() {
        addOnInflatedListener {
            editText?.doAfterTextChanged { this@PizzaDetailsFragment.binding.calculate() }
        }
    }

    private fun InputView.secureEmptyValue(
        defValue: String,
        formatTo2Decimal: Boolean = false
    ) {
        parser = { text, hasFocus ->
            when {
                hasFocus && text.isZero() -> ""
                text.isEmpty() || text.isZero() -> {
                    defValue
                }
                else -> text
            }.let { parsedText ->
                if (formatTo2Decimal) {
                    parsedText.doubleValue?.roundToPlaces(2) ?: parsedText
                } else parsedText
            }
        }
        validator = { text ->
            text.isNotEmpty() && !text.isZero()
        }
    }

    private fun FragmentPizzaDetailsBinding.savePizzaDetails() {
        fragmentPizzaDetailsPizzaNameInput.validate()
        fragmentPizzaDetailsPizzaDiameterInput.validate()
        fragmentPizzaDetailsPizzaPriceInput.validate()
        val isNameValid = fragmentPizzaDetailsPizzaNameInput.isValidate
        val isDiameterValid = fragmentPizzaDetailsPizzaDiameterInput.isValidate
        val isPriceValid = fragmentPizzaDetailsPizzaPriceInput.isValidate
        when {
            isNameValid && isDiameterValid && isPriceValid -> {
                viewModel.savePizza(generatePizza())
                findNavController().popBackStack()
            }
            !isNameValid -> {
                fragmentPizzaDetailsPizzaNameInput.editText?.requestFocus()
            }
            !isDiameterValid -> {
                fragmentPizzaDetailsPizzaDiameterInput.editText?.requestFocus()
            }
            !isPriceValid -> {
                fragmentPizzaDetailsPizzaPriceInput.editText?.requestFocus()
            }
        }
    }

    private fun FragmentPizzaDetailsBinding.subscribe() {
        fragmentPizzaDetailsSummaryPricePerUnitLabel.text =
            context?.getString(
                R.string.fragment_pizza_details_summary_price_per_unit
            )?.format("".getSurfaceFormat(root.context).trim()) ?: ""
        fragmentPizzaDetailsPizzaNameInput.apply {
            addOnStateChangeListener { isValid ->
                onInputStateChangeListener(
                    isValid,
                    context.getString(R.string.fragment_pizza_details_pizza_name_error)
                )
            }
        }
        fragmentPizzaDetailsPizzaDiameterInput.apply {
            calculateAfterEdit()
            secureEmptyValue(PizzaDetailsViewModel.DEFAULT_DIAMETER.toString(), true)
            addOnStateChangeListener { isValid ->
                onInputStateChangeListener(
                    isValid,
                    context.getString(R.string.fragment_pizza_details_pizza_diameter_error)
                )
            }
        }
        fragmentPizzaDetailsPizzaPriceInput.apply {
            calculateAfterEdit()
            secureEmptyValue(PizzaDetailsViewModel.DEFAULT_PRICE.toString(), true)
            addOnStateChangeListener { isValid ->
                onInputStateChangeListener(
                    isValid,
                    context.getString(R.string.fragment_pizza_details_pizza_price_error)
                )
            }
        }
        fragmentPizzaDetailsPizzaSlicesNumberInput.apply {
            calculateAfterEdit()
            secureEmptyValue(PizzaDetailsViewModel.DEFAULT_SLICES.toString())
        }
        fragmentPizzaDetailsPizzaConsumersNumberInput.apply {
            calculateAfterEdit()
            secureEmptyValue(PizzaDetailsViewModel.DEFAULT_CONSUMERS_NUMBER.toString())
        }
        fragmentPizzaDetailsSaveButton.apply {
            setOnClickListener {
                requestFocus()
                savePizzaDetails()
            }
        }
    }

    private fun FragmentPizzaDetailsBinding.refill(pizza: Pizza) {
        viewModel.calculatingState = PizzaDetailsViewModel.CalculatingState.REFILL
        fragmentPizzaDetailsPizzaNameInput.editText?.setText(pizza.name)
        fragmentPizzaDetailsPizzaDiameterInput.editText?.setText(
            pizza.diameter.roundToPlaces(2)
        )
        fragmentPizzaDetailsPizzaPriceInput.editText?.setText(
            pizza.price.roundToPlaces(2)
        )
        fragmentPizzaDetailsPizzaSlicesNumberInput.editText?.setText(
            pizza.slices.toString()
        )
        fragmentPizzaDetailsPizzaConsumersNumberInput.editText?.setText(
            pizza.consumersNumber.toString()
        )
        viewModel.calculatingState = PizzaDetailsViewModel.CalculatingState.DEFAULT
        fragmentPizzaDetailsSummary.addOnInflatedListener {
            calculate(pizza)
        }
    }
}
