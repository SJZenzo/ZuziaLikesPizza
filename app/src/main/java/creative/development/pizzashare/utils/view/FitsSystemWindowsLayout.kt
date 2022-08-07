package creative.development.pizzashare.utils.view

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.FrameLayout

/**
 * Layout with fitsSystemWindows flag and without system top padding
 */
class FitsSystemWindowsLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        fitsSystemWindows = true
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets? {
        return super.onApplyWindowInsets(insets).apply {
            setPadding(paddingLeft, 0, paddingRight, paddingBottom)
        }
    }
}
