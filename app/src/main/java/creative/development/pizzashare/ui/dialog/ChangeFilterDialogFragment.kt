package creative.development.pizzashare.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import creative.development.pizzashare.R

class ChangeFilterDialogFragment : BottomSheetDialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // listItems = arrayOf("Item 1", "Item 2", "Item 3")
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.dialog_change_list_view_header)
                .setSingleChoiceItems(R.array.dialog_change_list_view_choice, -1) { dialog, id ->
                    costan()
                }
                .setPositiveButton(R.string.dialog_change_list_view_aprrove_choice,
                    DialogInterface.OnClickListener { dialog, id ->
                        costam()
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun costam() {

    }
    fun costan() {

    }
}