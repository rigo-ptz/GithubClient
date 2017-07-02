package com.jollypanda.gitforce.data.repositories

import com.jollypanda.gitforce.GitforceApp
import com.jollypanda.gitforce.data.network.GithubApi
import com.jollypanda.gitforce.data.network.models.Branch
import com.jollypanda.gitforce.data.network.models.CommitWrapper
import com.vicpin.krealmextensions.deleteAll
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.saveAll
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Yamushev Igor
 * @since  22.06.17
 */
class RepoRepository : IBranchesRepository {

    @Inject
    lateinit var api: GithubApi

    @Inject
    lateinit var accountRepository: AccountRepository

    init {
        GitforceApp.appComponent.injectTo(this)
    }

    override fun getSavedBranches() = Branch().queryAll()

    override fun loadBranches(repo: String): Single<ArrayList<Branch>> =
            api.getBranchesList(accountRepository.getSavedUser()!!.login!!, repo)
                    .observeOn(Schedulers.computation())
                    .doOnSuccess {
                        if (it.isNotEmpty()) {
                            Branch().deleteAll()
                            it.saveAll()
                        }
                    }

    override fun getSavedCommits(branch: String): List<CommitWrapper>? =
            CommitWrapper().query { it.equalTo("branchName", branch) }

    override fun loadCommits(repo: String, branch: String): Single<ArrayList<CommitWrapper>> =
            api.getCommitsListFor(accountRepository.getSavedUser()!!.login!!, repo, branch)
                    .observeOn(Schedulers.computation())
                    .map { ArrayList(it.map { it.toWrapper(branch) }) }
                    .doOnSuccess {
                        if (!it.isEmpty())
                            it.saveAll()
                    }

}

interface IBranchesRepository {
    fun getSavedBranches(): List<Branch>
    fun loadBranches(repo: String): Single<ArrayList<Branch>>
    fun getSavedCommits(branch: String): List<CommitWrapper>?
    fun loadCommits(repo: String, branch: String): Single<ArrayList<CommitWrapper>>
}