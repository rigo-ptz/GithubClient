package com.jollypanda.gitforce.ui.presentation

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpView
import com.jollypanda.gitforce.GitforceApp
import com.jollypanda.gitforce.business.interactors.AccountInteractor
import javax.inject.Inject

/**
 * @author Yamushev Igor
 * @since  20.06.17
 */
@InjectViewState
class MainPresenter : BasePresenter<MainView>() {

    @Inject
    lateinit var accountInteractor: AccountInteractor

    init {
        GitforceApp.appComponent.injectTo(this)
    }

    fun logout() {
        accountInteractor.clearAccountData()
        viewState.goToLogin()
    }
}

interface MainView : MvpView {
    fun goToLogin()
}