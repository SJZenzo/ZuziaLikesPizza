package creative.development.pizzashare.utils

import android.view.MotionEvent
import android.view.View
import creative.development.pizzashare.utils.extensions.hideKeyboard
import kotlin.math.abs

/**
 * Instance of this class is helper for cleaning focus on touch screen
 * outside the current focus input
 */
class TouchFocusCleaner {

    // X coordinate of the point on screen where user started touching
    private var startX = 0f

    // Y coordinate of the point on screen where user started touching
    private var startY = 0f

    fun clearFocusOnTouchOutside(
        event: MotionEvent,
        currentFocus: View
    ) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_UP -> {
                currentFocus.let { view ->
                    val touchCoordinates = IntArray(2)
                    view.getLocationOnScreen(touchCoordinates)
                    val x: Float = event.rawX + view.left - touchCoordinates[0]
                    val y: Float = event.rawY + view.top - touchCoordinates[1]
                    if (x < view.left || x >= view.right || y < view.top || y > view.bottom) {
                        // If not scrolling lose focus and hide keyboard
                        if (abs(startX - event.x) < SCROLL_THRESHOLD ||
                            abs(startY - event.y) < SCROLL_THRESHOLD
                        ) {
                            view.clearFocus()
                            if (view.rootView.isFocusable) {
                                view.rootView.requestFocus()
                            }
                            view.context.hideKeyboard(view.windowToken)
                        }
                    }
                }
            }
            else -> Unit
        }
    }

    companion object {
        // The difference between start point and end point (user finished touching), after
        // which we can infer the user is scrolling not clicking.
        private const val SCROLL_THRESHOLD = 2f
    }
}
