package kowaliszyn.zuzanna.pizzashare.utils.view

import android.content.Context
import android.content.res.TypedArray
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import kowaliszyn.zuzanna.pizzashare.R
import kowaliszyn.zuzanna.pizzashare.databinding.ViewInputBinding
import kowaliszyn.zuzanna.pizzashare.utils.extensions.getBooleanOrDef
import kowaliszyn.zuzanna.pizzashare.utils.extensions.getFromStyleable
import kowaliszyn.zuzanna.pizzashare.utils.extensions.getIntOrDef
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
) : LazyView<ViewInputBinding>(context, attrs, defStyleAttr) {

    override val defaultLazyHeight: Int
        get() = resources.getDimensionPixelOffset(R.dimen.input_lazy_height)

    override val layoutResId: Int
        get() = R.layout.view_input

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

    private var initialText: String by Updatable.UpdatableProperty.lateInit()
    private var isErrorShown: Boolean by Updatable.UpdatableProperty(false)

    private var onStateChangeListeners: MutableList<(isValid: Boolean) -> Unit> = mutableListOf()

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
            setText(initialText)
            doAfterTextChanged {
                if (!isValidate) validate()
            }
            setOnFocusChangeListener { _, hasFocus ->
                parser?.invoke(this@InputView.text, hasFocus)?.let { text ->
                    setText(text)
                }
                if (!hasFocus) {
                    validate()
                }
            }
        }
    }

    override fun ViewInputBinding.update() {
        val backgroundColor = ContextCompat.getColor(
            context,
            if (readonly) R.color.background
            else R.color.input_background_color
        )
        val errorBackgroundColor = ContextCompat.getColor(
            context,
            if (readonly) R.color.background
            else R.color.input_error_background
        )
        editText?.apply {
            inputType = this@InputView.inputType
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

    fun removeOnInflatedListener(onStateChangeListener: (isValid: Boolean) -> Unit) {
        onStateChangeListeners.remove(onStateChangeListener)
    }

    fun showError() {
        isErrorShown = true
    }

    fun hideError() {
        isErrorShown = false
    }

    fun validate() {
        val validatorResult = validator?.invoke(text) ?: true
        isValidate = validatorResult && (!required || text.isNotEmpty())
        onStateChangeListeners.forEach { onStateChangeListener ->
            onStateChangeListener.invoke(isValidate)
        }
        if (isValidate) hideError()
        else showError()
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
    }
}
