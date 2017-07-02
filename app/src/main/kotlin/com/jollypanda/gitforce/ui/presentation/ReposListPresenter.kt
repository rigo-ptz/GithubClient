package com.jollypanda.gitforce.ui.presentation

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpView
import com.google.gson.JsonParseException
import com.jollypanda.gitforce.GitforceApp
import com.jollypanda.gitforce.business.interactors.ReposListInteractor
import com.jollypanda.gitforce.data.network.models.RepoWrapper
import com.jollypanda.gitforce.utils.SCREEN_STATE
import com.jollypanda.gitforce.utils.extensions.defaultSubscribe
import com.jollypanda.gitforce.utils.extensions.getDetailedInfo
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import java.io.IOException
import javax.inject.Inject

/**
 * @author Yamushev Igor
 * @since  21.06.17
 */
@InjectViewState
class ReposListPresenter : BasePresenter<ReposListView>() {

    @Inject
    lateinit var reposListInteractor: ReposListInteractor

    init {
        GitforceApp.appComponent.injectTo(this)
    }

    fun getSavedReposList() = reposListInteractor.getSavedRepos()

    fun loadReposList(lyfecycle: LifecycleProvider<FragmentEvent>) {
        screenState.set(SCREEN_STATE.IS_LOADING)
        reposListInteractor.loadReposList()
                .bindUntilEvent(lyfecycle, FragmentEvent.STOP)
                .defaultSubscribe(onSuccess = this::reposResponseSuccess,
                                  onError = this::reposResponseError)
    }

    private fun reposResponseSuccess(repos: ArrayList<RepoWrapper>) {
        if (repos.isEmpty())
            viewState.onReposListIsEmpty()
        else
            viewState.onReposListLoaded(repos)
    }

    private fun reposResponseError(thr: Throwable) {
        when (thr) {
            is retrofit2.HttpException -> viewState.showError(thr.getDetailedInfo())
            is IOException -> viewState.showInternetError()
            is JsonParseException -> viewState.showError()
            is IllegalStateException -> viewState.showError()
            else -> viewState.showError()
        }
    }
}

interface ReposListView : MvpView {
    fun showError(msg: String? = null)
    fun showInternetError()
    fun onReposListLoaded(repositories: ArrayList<RepoWrapper>)
    fun onReposListIsEmpty()
}