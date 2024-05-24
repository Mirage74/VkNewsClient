package com.balex.fbnewsclient.presentation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.balex.fbnewsclient.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
@ApplicationScope
class ViewModelFactory @Inject constructor(
    private val viewModelProviders: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelProviders[modelClass]?.get() as T
    }
}

