package kowaliszyn.zuzanna.pizzashare.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kowaliszyn.zuzanna.pizzashare.manager.PizzaManager
import kowaliszyn.zuzanna.pizzashare.manager.impl.PizzaManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {

    @Binds
    @Singleton
    abstract fun bindAnalyticsService(pizzaManager: PizzaManagerImpl): PizzaManager
}
