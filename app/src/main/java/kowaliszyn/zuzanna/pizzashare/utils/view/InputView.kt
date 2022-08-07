package kowaliszyn.zuzanna.pizzashare.utils.view

import android.content.Context
import android.content.res.TypedArray
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import kowaliszyn.zuzanna.pizzashare.R
import kowaliszyn.zuzanna.pizzashare.databinding.ViewInputBinding
import kowaliszyn.zuzanna.pizzashare.utils.extensions.getBooleanOrDef
import kowaliszyn.zuzanna.pizzashare.utils.extensions.getColorOrDef
import kowaliszyn.zuzanna.pizzashare.utils.extensions.getFromStyleable
import kowaliszyn.zuzanna.pizzashare.utils.extensions.getStringOrDef

/**
 * InputView based on Material3 TextInputLayout with method to show errors as icon and optional
 *  tooltip, in the feature probably this view will be able to show other icons and tooltips not
 *  only the errors
 */
class InputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), Updatable {

    private var binding = ViewInputBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var text: String
        get() = editText.text.toString()
        set(v) {
            editText.setText(v)
        }

    val editText get() = binding.viewInput

    var isValidate: Boolean = true

    var parser: ((text: String, hasFocus: Boolean) -> String)? = null
    var validator: ((text: String) -> Boolean)? = null

    var label: String by Updatable.UpdatableProperty.lateInit()
    var inputType: Int by Updatable.UpdatableProperty.lateInit()
    var imeOptions: Int by Updatable.UpdatableProperty.lateInit()
    var readonly: Boolean by Updatable.UpdatableProperty.lateInit()
    var required: Boolean by Updatable.UpdatableProperty.lateInit()

    private var initialText: String by Updatable.UpdatableProperty.lateInit()
    var onStateChangeListener: ((isValid: Boolean) -> Unit)? = null

    init {
        attrs.getFromStyleable(
            context,
            R.styleable.InputView,
            defStyleAttr,
            0,
            ::initAttrs
        )
        binding.subscribe()
        update()
    }

    override fun update() {
        val backgroundColor = ContextCompat.getColor(
            context,
            if (readonly) R.color.background
            else R.color.input_background_color
        )
        editText.apply {
            setText(initialText)
            inputType = this@InputView.inputType
            imeOptions = this@InputView.imeOptions
            isEnabled = !readonly
        }
        with(binding) {
            viewInputLabel.text = label
            viewInputContainer.setCardBackgroundColor(backgroundColor)
            viewInputLabelBackground.setBackgroundColor(backgroundColor)
        }
    }

    fun showError() {
        isValidate = false
        binding.viewInputErrorIcon.visibility = View.VISIBLE
    }

    fun hideError() {
        binding.viewInputErrorIcon.visibility = View.GONE
    }

    fun validate() {
        val validatorResult = validator?.invoke(text) ?: true
        isValidate = validatorResult && (!required || text.isNotEmpty())
        onStateChangeListener?.invoke(isValidate)
        if (isValidate) hideError()
        else showError()
    }

    private fun ViewInputBinding.subscribe() {
        editText.apply {
            setText(initialText)
            doAfterTextChanged {
                if (!isValidate) validate()
            }
            setOnFocusChangeListener { _, hasFocus ->
                parser?.invoke(this@InputView.text, hasFocus)?.let { text ->
                    editText.setText(text)
                }
                if (!hasFocus) {
                    validate()
                }
            }
        }
    }

    private fun initAttrs(typedArray: TypedArray?) {
        initialText = typedArray.getStringOrDef(R.styleable.InputView_android_text, "")
        label = typedArray.getStringOrDef(R.styleable.InputView_android_label, "")
        inputType = typedArray.getColorOrDef(
            R.styleable.InputView_android_inputType,
            InputType.TYPE_CLASS_TEXT
        )
        imeOptions = typedArray.getColorOrDef(
            R.styleable.InputView_android_imeOptions,
            EditorInfo.IME_NULL
        )
        readonly = typedArray.getBooleanOrDef(R.styleable.InputView_readonly, false)
        required = typedArray.getBooleanOrDef(R.styleable.InputView_android_required, false)
    }
}
