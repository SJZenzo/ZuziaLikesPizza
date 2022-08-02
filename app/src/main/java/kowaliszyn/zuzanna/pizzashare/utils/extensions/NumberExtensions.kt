package kowaliszyn.zuzanna.pizzashare.utils.extensions

fun Number.roundToPlaces(places: Int): String {
    return String.format("%.${places}f", this)
}
