package creative.development.pizzashare.ui.pizzaDetails

import android.annotation.SuppressLint
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import creative.development.pizzashare.R
import creative.development.pizzashare.data.model.Pizza
import creative.development.pizzashare.databinding.FragmentPizzaDetailsBinding
import creative.development.pizzashare.ui.base.BaseFragment
import creative.development.pizzashare.ui.main.MainViewModel
import creative.development.pizzashare.utils.extensions.*
import creative.development.pizzashare.utils.view.InputView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PizzaDetailsFragment :
    BaseFragment<FragmentPizzaDetailsBinding, PizzaDetailsViewModel, MainViewModel>() {

    override val viewModel: PizzaDetailsViewModel by viewModels()
    override val activityViewModel: MainViewModel by activityViewModels()

    override val layoutResId get() = R.layout.fragment_pizza_details

    private val errorsSet: MutableSet<String> = mutableSetOf()

    override fun onBind(view: View) = FragmentPizzaDetailsBinding.bind(view)

    override fun onBackPressed(): Boolean {
        return if (viewModel.wasEdited) {
            showBackWithoutSavingConfirmationDialog()
            false
        } else true
    }

    override fun PizzaDetailsViewModel.subscribe() {
        loadedPizzaDataEvent.observe(viewLifecycleOwner) { pizza ->
            binding.refill(pizza)
        }
    }

    override fun FragmentPizzaDetailsBinding.subscribe() {
        fragmentPizzaDetailsSummaryPricePerUnitLabel.text =
            context?.getString(
                R.string.fragment_pizza_details_summary_price_per_unit
            )?.format(getString(R.string.unit_for_pizza_surface)) ?: ""
        fragmentPizzaDetailsPizzaNameInput.apply {
            calculateAfterEdit()
            addOnStateChangeListener { isValid ->
                onInputStateChangeListener(
                    isValid,
                    context.getString(R.string.fragment_pizza_details_pizza_name_error)
                )
            }
        }
        fragmentPizzaDetailsPizzaDiameterInput.apply {
            calculateAfterEdit()
            secureEmptyValue(
                defValue = PizzaDetailsViewModel.DEFAULT_DIAMETER.toString(),
                formatTo2Decimal = true,
                maxValue = PizzaDetailsViewModel.MAX_DIAMETER
            )
            addOnStateChangeListener { isValid ->
                onInputStateChangeListener(
                    isValid,
                    context.getString(R.string.fragment_pizza_details_pizza_diameter_error)
                )
            }
        }
        fragmentPizzaDetailsPizzaPriceInput.apply {
            calculateAfterEdit()
            secureEmptyValue(
                defValue = PizzaDetailsViewModel.DEFAULT_PRICE.toString(),
                formatTo2Decimal = true,
                maxValue = PizzaDetailsViewModel.MAX_PRICE
            )
            addOnStateChangeListener { isValid ->
                onInputStateChangeListener(
                    isValid,
                    context.getString(R.string.fragment_pizza_details_pizza_price_error)
                )
            }
        }
        fragmentPizzaDetailsPizzaSlicesNumberInput.apply {
            calculateAfterEdit()
            secureEmptyValue(
                defValue = PizzaDetailsViewModel.DEFAULT_SLICES.toString(),
                maxValue = PizzaDetailsViewModel.MAX_SLICES
            )
        }
        fragmentPizzaDetailsPizzaConsumersNumberInput.apply {
            calculateAfterEdit()
            secureEmptyValue(
                defValue = PizzaDetailsViewModel.DEFAULT_CONSUMERS_NUMBER.toString(),
                maxValue = PizzaDetailsViewModel.MAX_CONSUMERS_NUMBER
            )
        }
        fragmentPizzaDetailsSaveButton.apply {
            setOnClickListener {
                requestFocus()
                savePizzaDetails()
            }
        }
    }

    private fun showBackWithoutSavingConfirmationDialog() {
        context?.showConfirmDialog(
            title = getString(R.string.dialog_back_without_saving_title),
            content = getString(R.string.dialog_back_without_saving_content),
            approveButtonText = getString(R.string.dialog_back_without_saving_button_text)
        ) {
            findNavController().popBackStack()
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
            fragmentPizzaDetailsPizzaDiameterInput.unformattedText.doubleValue?.toFloat(),
            fragmentPizzaDetailsPizzaPriceInput.unformattedText.doubleValue?.toFloat(),
            fragmentPizzaDetailsPizzaSlicesNumberInput.text.toIntOrNull(),
            fragmentPizzaDetailsPizzaConsumersNumberInput.text.toIntOrNull()
        )
    }

    @SuppressLint("SetTextI18n")
    private fun FragmentPizzaDetailsBinding.calculate(pizzaFromRefill: Pizza? = null) {
        val pizza = pizzaFromRefill ?: generatePizza()
        fragmentPizzaDetailsSummarySurfaceValue.text =
            pizza.surface.roundToPlaces(2).getSurfaceFormat(root.context)
        fragmentPizzaDetailsSummaryPricePerUnitValue.text =
            pizza.pricePerUnit.roundToPlaces(2).getCurrencyFormat(root.context)
        fragmentPizzaDetailsSummaryPricePerSliceValue.text =
            pizza.pricePerSlice.roundToPlaces(2).getCurrencyFormat(root.context)
        fragmentPizzaDetailsSummaryPricePerConsumerValue.text =
            pizza.pricePerConsumer.roundToPlaces(2).getCurrencyFormat(root.context)
    }

    private fun InputView.calculateAfterEdit() {
        editText?.doAfterTextChanged {
            binding.calculate()
        }
    }

    private fun InputView.secureEmptyValue(
        defValue: String,
        formatTo2Decimal: Boolean = false,
        maxValue: Number? = null
    ) {
        parser = { text, hasFocus ->
            when {
                hasFocus && text.isZero() -> ""
                text.isEmpty() || text.isZero() -> {
                    defValue
                }
                else -> when (maxValue) {
                    is Int -> text.toIntOrNull()?.coerceAtMost(maxValue)?.toString() ?: text
                    is Double -> text.doubleValue?.coerceAtMost(maxValue)?.toString() ?: text
                    else -> text
                }
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
                popBackStack()
            }
            !isNameValid -> {
                fragmentPizzaDetailsPizzaNameInput.editText?.requestFocus()
            }
            !isDiameterValid -> {
                fragmentPizzaDetailsPizzaDiameterInput.editText?.requestFocus()
            }
            else -> {
                fragmentPizzaDetailsPizzaPriceInput.editText?.requestFocus()
            }
        }
    }

    private fun FragmentPizzaDetailsBinding.refill(pizza: Pizza) {
        fragmentPizzaDetailsPizzaNameInput.text = pizza.name
        fragmentPizzaDetailsPizzaDiameterInput.text = pizza.diameter.roundToPlaces(2)
        fragmentPizzaDetailsPizzaPriceInput.text = pizza.price.roundToPlaces(2)
        fragmentPizzaDetailsPizzaSlicesNumberInput.text = pizza.slices.toString()
        fragmentPizzaDetailsPizzaConsumersNumberInput.text = pizza.consumersNumber.toString()
        calculate(pizza)
        viewModel.wasEdited = false
    }
}
