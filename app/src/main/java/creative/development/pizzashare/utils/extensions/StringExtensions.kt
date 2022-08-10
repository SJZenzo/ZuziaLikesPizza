package creative.development.pizzashare.utils.extensions

import android.content.Context
import creative.development.pizzashare.R

fun String.getCurrencyFormat(context: Context) =
    context.getString(R.string.format_currency).format(this)

fun String.getLengthFormat(context: Context) =
    context.getString(R.string.format_length).format(this)

fun String.getSurfaceFormat(context: Context) =
    context.getString(R.string.format_surface).format(this)

val String.doubleValue
    get() = replace(",", ".")
        .replace(" ", "")
        .toDoubleOrNull()

fun String.isZero() = doubleValue == 0.0
