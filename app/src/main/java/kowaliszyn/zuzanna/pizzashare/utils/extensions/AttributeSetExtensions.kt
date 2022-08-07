package kowaliszyn.zuzanna.pizzashare.utils.extensions

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes

fun AttributeSet?.getFromStyleable(
    context: Context,
    @StyleableRes styleableResId: IntArray,
    @AttrRes defStyleAttr: Int?,
    @StyleRes defStyleRes: Int?,
    styleableBlock: (TypedArray?) -> Unit
) {
    this?.let {
        context.theme.obtainStyledAttributes(
            this,
            styleableResId,
            defStyleAttr ?: 0,
            defStyleRes ?: 0
        ).let { typedArray ->
            styleableBlock(typedArray)
            typedArray.recycle()
        }
    } ?: styleableBlock(null)
}
