package creative.development.pizzashare.di

import creative.development.pizzashare.manager.LazyViewManager
import creative.development.pizzashare.manager.PizzaManager
import creative.development.pizzashare.manager.impl.LazyViewManagerImpl
import creative.development.pizzashare.manager.impl.PizzaManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {

    @Binds
    @Singleton
    abstract fun bindPizzaManager(pizzaManager: PizzaManagerImpl): PizzaManager

    @Binds
    @Singleton
    abstract fun bindLazyViewManager(pizzaManager: LazyViewManagerImpl): LazyViewManager
}
