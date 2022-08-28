package creative.development.pizzashare.ui.base

import androidx.lifecycle.ViewModel
import creative.development.pizzashare.utils.EventLiveData

abstract class BaseActivityViewModel : ViewModel() {

    val changeLoaderVisibilityEvent = EventLiveData<Boolean>()

    fun showLoader() {
        changeLoaderVisibilityEvent.value = true
    }

    fun hideLoader() {
        changeLoaderVisibilityEvent.value = false
    }
}
