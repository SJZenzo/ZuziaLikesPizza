package creative.development.pizzashare.utils.extensions

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.text.Spanned
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import creative.development.pizzashare.R

fun Context.showConfirmDialog(
    title: String,
    content: Spanned,
    approveButtonText: String,
    denyButtonText: String = getString(R.string.dialog_deny_button_default_text),
    onDenyAction: (() -> Unit)? = null,
    onApproveAction: (() -> Unit)?,
): AlertDialog = MaterialAlertDialogBuilder(this, R.style.Dialog)
    .apply {
        setTitle(title)
        setMessage(content)
        setNegativeButton(denyButtonText) { dialog, _ ->
            dialog.dismiss()
            onDenyAction?.invoke()
        }
        setPositiveButton(approveButtonText) { dialog, _ ->
            dialog.dismiss()
            onApproveAction?.invoke()
        }
    }
    .show()

fun Context.hideKeyboard(windowToken: IBinder) {
    val inputManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}
