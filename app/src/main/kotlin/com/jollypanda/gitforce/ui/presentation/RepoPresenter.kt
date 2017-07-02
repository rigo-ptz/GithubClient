package com.jollypanda.gitforce.ui.presentation

import android.databinding.ObservableBoolean
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpView
import com.google.gson.JsonParseException
import com.jollypanda.gitforce.GitforceApp
import com.jollypanda.gitforce.business.interactors.RepoInteractor
import com.jollypanda.gitforce.data.network.models.Branch
import com.jollypanda.gitforce.data.network.models.CommitWrapper
import com.jollypanda.gitforce.utils.SCREEN_STATE
import com.jollypanda.gitforce.utils.extensions.defaultSubscribe
import com.jollypanda.gitforce.utils.extensions.getDetailedInfo
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import java.io.IOException
import javax.inject.Inject

/**
 * @author Yamushev Igor
 * @since  22.06.17
 */
@InjectViewState
class RepoPresenter : BasePresenter<RepoView>() {

    @Inject
    lateinit var repoInteractor: RepoInteractor

    val shouldShowHint = ObservableBoolean(true)

    init {
        GitforceApp.appComponent.injectTo(this)
    }

    fun getSavedBranches() = repoInteractor.getSavedBranches()

    fun loadBranches(repoName: String, lifecycleProvider: LifecycleProvider<ActivityEvent>) {
        screenState.set(SCREEN_STATE.IS_LOADING)
        repoInteractor.loadBranches(repoName)
                .bindUntilEvent(lifecycleProvider, ActivityEvent.STOP)
                .defaultSubscribe(onSuccess = this::branchesResponseSuccess,
                                  onError = this::onResponseError)
    }

    private fun branchesResponseSuccess(repos: ArrayList<Branch>) {
        if (repos.isEmpty())
            viewState.onBranchesListIsEmpty()
        else
            viewState.showLoadedBranches(repos)
    }

    private fun onResponseError(thr: Throwable) {
        when (thr) {
            is retrofit2.HttpException -> viewState.showError(thr.getDetailedInfo())
            is IOException -> viewState.showInternetError()
            is JsonParseException -> viewState.showError()
            is IllegalStateException -> viewState.showError()
            else -> viewState.showError()
        }
    }

    fun getSavedCommits(branch: String) = repoInteractor.getSavedCommits(branch)

    fun loadCommitsFor(repo: String, branch: String, lifecycleProvider: LifecycleProvider<ActivityEvent>) {
        screenState.set(SCREEN_STATE.IS_LOADING)
        repoInteractor.loadCommits(repo, branch)
                .bindUntilEvent(lifecycleProvider, ActivityEvent.STOP)
                .defaultSubscribe(onSuccess = this::commitsResponseSuccess,
                                  onError = this::onResponseError)
    }


    private fun commitsResponseSuccess(commits: ArrayList<CommitWrapper>) {
        if (commits.isEmpty())
            viewState.onCommitsListIsEmpty()
        else
            viewState.showLoadedCommits(commits)
    }


}

interface RepoView : MvpView {
    fun onBranchesListIsEmpty()
    fun showLoadedBranches(branches: List<Branch>)
    fun showError(msg: String? = null)
    fun showInternetError()
    fun showLoadedCommits(commits: ArrayList<CommitWrapper>)
    fun onCommitsListIsEmpty()
}