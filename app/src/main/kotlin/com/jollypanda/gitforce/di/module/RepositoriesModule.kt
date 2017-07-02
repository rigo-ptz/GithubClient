package com.jollypanda.gitforce.di.module

import com.jollypanda.gitforce.data.repositories.AccountRepository
import com.jollypanda.gitforce.data.repositories.RepoRepository
import com.jollypanda.gitforce.data.repositories.ReposListRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Yamushev Igor
 * @since  14.06.17
 */
@Module
class RepositoriesModule {

    @Provides
    @Singleton
    fun provideAccountRepository() = AccountRepository()

    @Provides
    @Singleton
    fun provideReposListRepository() = ReposListRepository()

    @Provides
    @Singleton
    fun provideBranchesRepository() = RepoRepository()
}