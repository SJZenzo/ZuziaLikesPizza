package creative.development.pizzashare.utils.extensions

fun Number.roundToPlaces(places: Int): String {
    return String.format("%.${places}f", this)
}
