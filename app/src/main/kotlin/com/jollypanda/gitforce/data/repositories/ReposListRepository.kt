package com.jollypanda.gitforce.data.repositories

import android.content.Context
import com.jollypanda.gitforce.GitforceApp
import com.jollypanda.gitforce.data.network.GithubApi
import com.jollypanda.gitforce.data.network.models.Repo
import com.jollypanda.gitforce.data.network.models.RepoWrapper
import com.vicpin.krealmextensions.deleteAll
import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.saveAll
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Yamushev Igor
 * @since  22.06.17
 */
class ReposListRepository : IReposListRepository {

    @Inject
    lateinit var api: GithubApi

    @Inject
    lateinit var context: Context

    init {
        GitforceApp.appComponent.injectTo(this)
    }

    override fun loadReposList(): Single<ArrayList<RepoWrapper>> =
            api.getCurrentUserRepos("updated", "desc")
                    .observeOn(Schedulers.computation())
                    .doOnSuccess {
                        removeSavedReposList()
                        if (it.isNotEmpty())
                            it.saveAll()
                    }
                    .map { repos ->
                        ArrayList(repos.map { it.toWrapper() })
                    }

    override fun removeSavedReposList() {
        Repo().deleteAll()
    }

    override fun getSavedRepos() = Repo()
            .queryAll()
            .map { it.toWrapper() }
}

interface IReposListRepository {
    fun getSavedRepos(): List<RepoWrapper>
    fun loadReposList(): Single<ArrayList<RepoWrapper>>
    fun removeSavedReposList()
}