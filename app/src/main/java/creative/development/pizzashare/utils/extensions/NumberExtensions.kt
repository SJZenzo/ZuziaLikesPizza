package creative.development.pizzashare.utils.extensions

fun Number.roundToPlaces(places: Int): String {
    return String.format("%.${places}f", this)
}

fun Int?.coerceRangeOrDef(range: IntRange, def: Int) =
    this?.coerceAtLeast(range.first)?.coerceAtMost(range.last) ?: def

fun Float?.coerceRangeOrDef(range: ClosedFloatingPointRange<Float>, def: Float) =
    this?.coerceAtLeast(range.start)?.coerceAtMost(range.endInclusive) ?: def
