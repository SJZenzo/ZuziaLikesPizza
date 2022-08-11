package creative.development.pizzashare.di

import creative.development.pizzashare.utils.TouchFocusCleaner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object UtilsModule {

    @Provides
    fun provideTouchFocusCleaner(): TouchFocusCleaner {
        return TouchFocusCleaner()
    }
}
