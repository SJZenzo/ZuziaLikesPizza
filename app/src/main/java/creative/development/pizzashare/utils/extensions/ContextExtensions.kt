package creative.development.pizzashare.utils.extensions

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import creative.development.pizzashare.R

fun Context.showDialog(
    title: String,
    content: String? = null,
    items: List<String>? = null,
    approveButtonText: String = getString(R.string.dialog_accept_button_default_text),
    denyButtonText: String = getString(R.string.dialog_deny_button_default_text),
    extraButtonText: String? = null,
    onDenyAction: (() -> Unit)? = null,
    onExtraAction: (() -> Unit)? = null,
    onApproveAction: ((Int) -> Unit)?
): AlertDialog = MaterialAlertDialogBuilder(this, R.style.Dialog)
    .apply {
        setTitle(title.parseHtml())
        content?.let {
            setMessage(content.parseHtml())
        }
        items?.let {
            setSingleChoiceItems(items.toTypedArray(), 0) { dialog, which ->
                onApproveAction?.invoke(which)
            }
        }
        setNegativeButton(denyButtonText) { dialog, _ ->
            dialog.dismiss()
            onDenyAction?.invoke()
        }
        extraButtonText?.let {
            setNeutralButton(extraButtonText) { dialog, _ ->
                dialog.dismiss()
                onExtraAction?.invoke()
            }
        }
        setPositiveButton(approveButtonText) { dialog, _ ->
            dialog.dismiss()
            onApproveAction?.invoke(-1)
        }
    }
    .show()

fun Context.hideKeyboard(windowToken: IBinder) {
    val inputManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}
