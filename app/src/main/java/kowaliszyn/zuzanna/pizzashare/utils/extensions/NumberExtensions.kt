package kowaliszyn.zuzanna.pizzashare.utils.extensions

import kowaliszyn.zuzanna.pizzashare.config.Config

fun Float.toMillimeters() = this * Config.MILLIMETERS_PER_UNIT
fun Float.toAppUnit() = this / Config.MILLIMETERS_PER_UNIT
fun Double.toMillimeters() = this * Config.MILLIMETERS_PER_UNIT
fun Double.toAppUnit() = this / Config.MILLIMETERS_PER_UNIT

fun Number.roundToPlaces(places: Int): String {
    return String.format("%.${places}f", this)
}
