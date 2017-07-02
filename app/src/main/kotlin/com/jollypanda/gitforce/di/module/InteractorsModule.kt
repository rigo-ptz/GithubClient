package com.jollypanda.gitforce.di.module

import com.jollypanda.gitforce.business.interactors.AccountInteractor
import com.jollypanda.gitforce.business.interactors.RepoInteractor
import com.jollypanda.gitforce.business.interactors.ReposListInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Yamushev Igor
 * @since  15.06.17
 */
@Module
class InteractorsModule {

    @Provides
    @Singleton
    fun provideAccountInteractor() = AccountInteractor()

    @Provides
    @Singleton
    fun provideReposListInteractor() = ReposListInteractor()

    @Provides
    @Singleton
    fun provideRepoInteractor() = RepoInteractor()
}