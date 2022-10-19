package creative.development.pizzashare.utils.extensions

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import creative.development.pizzashare.R

fun Context.showDeleteDialog(
    title: String,
    content: String,
    approveButtonText: String,
    archiveButtonText: String = getString(R.string.dialog_remove_pizzas_list_archive_button_text),
    denyButtonText: String = getString(R.string.dialog_deny_button_default_text),
    onDenyAction: (() -> Unit)? = null,
    onArchiveAction: (() -> Unit)? = null,
    onApproveAction: (() -> Unit)?
): AlertDialog = MaterialAlertDialogBuilder(this, R.style.Dialog)
    .apply {
        setTitle(title.parseHtml())
        setMessage(content.parseHtml())
        setNegativeButton(denyButtonText) { dialog, _ ->
            dialog.dismiss()
            onDenyAction?.invoke()
        }
        setNeutralButton(archiveButtonText) { dialog, _ ->
            dialog.dismiss()
            onArchiveAction?.invoke()
        }
        setPositiveButton(approveButtonText) { dialog, _ ->
            dialog.dismiss()
            onApproveAction?.invoke()
        }
    }
    .show()

fun Context.showExitDialog(
    title: String,
    content: String,
    approveButtonText: String,
    denyButtonText: String = getString(R.string.dialog_deny_button_default_text),
    onDenyAction: (() -> Unit)? = null,
    onApproveAction: (() -> Unit)?
): AlertDialog = MaterialAlertDialogBuilder(this, R.style.Dialog)
    .apply {
        setTitle(title.parseHtml())
        setMessage(content.parseHtml())
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
