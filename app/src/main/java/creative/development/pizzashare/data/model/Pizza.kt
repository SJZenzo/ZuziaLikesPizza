package creative.development.pizzashare.data.model

import creative.development.pizzashare.consts.Consts
import kotlin.math.pow

data class Pizza constructor(
    val name: String,
    val diameter: Float,
    val price: Float,
    val slices: Int,
    val consumersNumber: Int
) {

    init {
        if (diameter < 0 || price < 0 || slices < 0 || consumersNumber < 0) {
            throw IllegalArgumentException()
        }
    }

    val surface get() = Math.PI * (diameter / 2).pow(2)
    val pricePerUnit get() = if (proportionSurface > 0) price / proportionSurface else 0.0
    val pricePerSlice get() = if (slices > 0) price / slices.toDouble() else 0.0
    val pricePerConsumer
        get() = if (consumersNumber > 0) price / consumersNumber.toDouble() else 0.0

    private val proportionSurface
        get() = Math.PI * (diameter * Consts.PIZZA_SURFACE_PROPORTION / 2).pow(2)
}
