package com.jollypanda.gitforce.di.component

import com.jollypanda.gitforce.business.interactors.AccountInteractor
import com.jollypanda.gitforce.business.interactors.RepoInteractor
import com.jollypanda.gitforce.business.interactors.ReposListInteractor
import com.jollypanda.gitforce.data.network.AuthInterceptor
import com.jollypanda.gitforce.data.repositories.AccountRepository
import com.jollypanda.gitforce.data.repositories.RepoRepository
import com.jollypanda.gitforce.data.repositories.ReposListRepository
import com.jollypanda.gitforce.di.module.AppModule
import com.jollypanda.gitforce.di.module.InteractorsModule
import com.jollypanda.gitforce.di.module.NetworkModule
import com.jollypanda.gitforce.di.module.RepositoriesModule
import com.jollypanda.gitforce.ui.presentation.LoginPresenter
import com.jollypanda.gitforce.ui.presentation.MainPresenter
import com.jollypanda.gitforce.ui.presentation.RepoPresenter
import com.jollypanda.gitforce.ui.presentation.ReposListPresenter
import dagger.Component
import javax.inject.Singleton

/**
 * @author Yamushev Igor
 * @since  10.06.17
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class,
                             RepositoriesModule::class, InteractorsModule::class))
interface AppComponent {
    fun injectTo(authInterceptor: AuthInterceptor)
    fun injectTo(loginPresenter: LoginPresenter)
    fun injectTo(accountRepository: AccountRepository)
    fun injectTo(accountInteractor: AccountInteractor)
    fun injectTo(mainPresenter: MainPresenter)
    fun injectTo(reposListPresenter: ReposListPresenter)
    fun injectTo(reposListInteractor: ReposListInteractor)
    fun injectTo(reposListRepository: ReposListRepository)
    fun injectTo(repoPresenter: RepoPresenter)
    fun injectTo(repoInteractor: RepoInteractor)
    fun injectTo(repoRepository: RepoRepository)
}