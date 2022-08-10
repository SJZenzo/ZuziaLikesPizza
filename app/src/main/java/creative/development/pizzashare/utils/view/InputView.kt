package creative.development.pizzashare.utils.view

import android.content.Context
import android.content.res.TypedArray
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import creative.development.pizzashare.R
import creative.development.pizzashare.databinding.ViewInputBinding
import creative.development.pizzashare.utils.extensions.getBooleanOrDef
import creative.development.pizzashare.utils.extensions.getFromStyleable
import creative.development.pizzashare.utils.extensions.getIntOrDef
import creative.development.pizzashare.utils.extensions.getStringOrDef

/**
 * InputView based on Material3 TextInputLayout with method to show errors as icon and optional
 *  tooltip, in the feature probably this view will be able to show other icons and tooltips not
 *  only the errors
 */
class InputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LazyView<ViewInputBinding>(context, attrs, defStyleAttr) {

    private data class FormattedText(var value: String, var wasApply: Boolean)

    override val defaultLazyHeight: Int
        get() = resources.getDimensionPixelOffset(R.dimen.input_lazy_height)

    override val layoutResId: Int
        get() = R.layout.view_input

    var unformattedText: String = ""
        private set

    var text: String
        get() = editText?.text?.toString() ?: ""
        set(v) {
            editText?.setText(v)
        }

    val editText get() = if (isInflated) binding.inputView.editText else null

    var isValidate: Boolean = true
        private set

    var parser: ((text: String, hasFocus: Boolean) -> String)? = null
    var validator: ((text: String) -> Boolean)? = null

    var label: String by Updatable.UpdatableProperty.lateInit()
    var inputType: Int by Updatable.UpdatableProperty.lateInit()
    var imeOptions: Int by Updatable.UpdatableProperty.lateInit()
    var readonly: Boolean by Updatable.UpdatableProperty.lateInit()
    var required: Boolean by Updatable.UpdatableProperty.lateInit()
    var multiline: Boolean by Updatable.UpdatableProperty.lateInit()
    var format: String? by Updatable.UpdatableProperty.lateInit()

    private var isErrorShown: Boolean by Updatable.UpdatableProperty(false)
    private var onStateChangeListeners: MutableList<(isValid: Boolean) -> Unit> = mutableListOf()
    private val formattedText = FormattedText("", true)
    private var initialText: String = ""

    init {
        attrs.getFromStyleable(
            context,
            R.styleable.InputView,
            defStyleAttr,
            0,
            ::initAttrs
        )
    }

    override fun onBind(view: View) = ViewInputBinding.bind(view)

    override fun ViewInputBinding.subscribe() {
        editText?.apply {
            doAfterTextChanged { editable ->
                if (formattedText.wasApply && formattedText.value != editable?.toString()) {
                    unformattedText = editable?.toString() ?: ""
                    if (!hasFocus()) {
                        formattedText.value = format?.let { format ->
                            format.format(unformattedText)
                        } ?: unformattedText
                        formattedText.wasApply = false
                        setText(formattedText.value)
                    }
                    if (!isValidate) validate()
                } else {
                    formattedText.wasApply = true
                }
            }
            setText(initialText)
            setOnFocusChangeListener { _, hasFocus ->
                var newText = unformattedText
                parser?.invoke(unformattedText, hasFocus)?.let { text ->
                    newText = text
                }
                if (!hasFocus) {
                    validate(newText)
                }
                setText(newText)
            }
        }
    }

    override fun ViewInputBinding.update() {
        root.changHeightByMultilineParameter(multiline)
        inputView.changHeightByMultilineParameter(multiline)
        val backgroundColor = ContextCompat.getColor(
            context,
            if (readonly) R.color.background
            else R.color.white
        )
        val errorBackgroundColor = ContextCompat.getColor(
            context,
            if (readonly) R.color.background
            else R.color.error_background
        )
        editText?.apply {
            inputType = this@InputView.inputType.let {
                if (multiline) (it or InputType.TYPE_TEXT_FLAG_MULTI_LINE) else it
            }
            imeOptions = this@InputView.imeOptions
            isEnabled = !readonly
        }
        inputView.apply {
            hint = label
            boxBackgroundColor = if (isErrorShown) errorBackgroundColor else backgroundColor
        }
        binding.inputViewErrorIcon.isVisible = isErrorShown
    }

    fun addOnStateChangeListener(onStateChangeListener: (isValid: Boolean) -> Unit) {
        onStateChangeListeners.add(onStateChangeListener)
    }

    fun removeOnStateChangeListener(onStateChangeListener: (isValid: Boolean) -> Unit) {
        onStateChangeListeners.remove(onStateChangeListener)
    }

    fun showError() {
        isErrorShown = true
    }

    fun hideError() {
        isErrorShown = false
    }

    fun validate(validationText: String = unformattedText) {
        val validatorResult = validator?.invoke(validationText) ?: true
        isValidate = validatorResult && (!required || validationText.isNotEmpty())
        onStateChangeListeners.forEach { onStateChangeListener ->
            onStateChangeListener.invoke(isValidate)
        }
        if (isValidate) hideError()
        else showError()
    }

    private fun View.changHeightByMultilineParameter(isMultiline: Boolean) {
        val newHeight =
            if (isMultiline) LayoutParams.MATCH_PARENT
            else LayoutParams.WRAP_CONTENT
        layoutParams = layoutParams?.apply {
            height = newHeight
        } ?: LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, newHeight)
    }

    private fun initAttrs(typedArray: TypedArray?) {
        initialText = typedArray.getStringOrDef(R.styleable.InputView_android_text, "")
        label = typedArray.getStringOrDef(R.styleable.InputView_android_label, "")
        inputType = typedArray.getIntOrDef(
            R.styleable.InputView_android_inputType,
            InputType.TYPE_CLASS_TEXT
        )
        imeOptions = typedArray.getIntOrDef(
            R.styleable.InputView_android_imeOptions,
            EditorInfo.IME_NULL
        )
        readonly = typedArray.getBooleanOrDef(R.styleable.InputView_readonly, false)
        required = typedArray.getBooleanOrDef(R.styleable.InputView_android_required, false)
        multiline = typedArray.getBooleanOrDef(R.styleable.InputView_multiline, false)
        format = typedArray?.getString(R.styleable.InputView_format)
    }
}
