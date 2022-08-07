package kowaliszyn.zuzanna.pizzashare.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.math.pow

@Parcelize
data class Pizza constructor(
    val name: String,
    val diameter: Float,
    val price: Float,
    val slices: Int,
    val consumersNumber: Int
) : Parcelable {

    init {
        if (diameter < 0 || price < 0 || consumersNumber < 0) {
            throw IllegalArgumentException()
        }
    }

    val surface get() = Math.PI * (diameter / 2).pow(2)
    val pricePerUnit get() = if (surface > 0) price / surface else 0.0
    val pricePerConsumer
        get() = if (consumersNumber > 0) price / consumersNumber.toDouble() else 0.0
}
