package com.jollypanda.gitforce.business.interactors

import com.jollypanda.gitforce.GitforceApp
import com.jollypanda.gitforce.data.network.models.RepoWrapper
import com.jollypanda.gitforce.data.repositories.ReposListRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author Yamushev Igor
 * @since  22.06.17
 */
class ReposListInteractor : IReposListInteractor {

    @Inject
    lateinit var reposListRepository: ReposListRepository

    init {
        GitforceApp.appComponent.injectTo(this)
    }

    override fun loadReposList(): Single<ArrayList<RepoWrapper>> = reposListRepository.loadReposList()

    override fun removeSavedReposList() = reposListRepository.removeSavedReposList()

    override fun  getSavedRepos() = reposListRepository.getSavedRepos()

}

interface IReposListInteractor {
    fun loadReposList(): Single<ArrayList<RepoWrapper>>
    fun removeSavedReposList()
    fun getSavedRepos(): List<RepoWrapper>
}