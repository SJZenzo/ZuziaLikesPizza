package kowaliszyn.zuzanna.pizzashare.utils.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf

inline fun <reified A : Activity> Activity.startActivity(
    extras: Bundle = bundleOf(),
    finishCurrent: Boolean = false
) {
    startActivity(Intent(this, A::class.java), extras)
    if (finishCurrent) finish()
}
