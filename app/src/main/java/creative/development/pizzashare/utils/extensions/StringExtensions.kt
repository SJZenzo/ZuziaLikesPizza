package creative.development.pizzashare.utils.extensions

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import creative.development.pizzashare.R

fun String.getCurrencyFormat(context: Context) =
    context.getString(R.string.format_currency, this)

fun String.getLengthFormat(context: Context) =
    context.getString(R.string.format_length, this)

fun String.getSurfaceFormat(context: Context) =
    context.getString(R.string.format_surface, this)

val String.doubleValue
    get() = replace(",", ".")
        .replace(" ", "")
        .toDoubleOrNull()

fun String.isZero() = doubleValue == 0.0

fun String.parseHtml(): Spanned =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(
            this,
            Html.FROM_HTML_MODE_COMPACT
        )
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }
