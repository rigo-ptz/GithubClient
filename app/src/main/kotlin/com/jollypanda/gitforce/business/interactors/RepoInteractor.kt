package com.jollypanda.gitforce.business.interactors

import com.jollypanda.gitforce.GitforceApp
import com.jollypanda.gitforce.data.network.models.Branch
import com.jollypanda.gitforce.data.network.models.CommitWrapper
import com.jollypanda.gitforce.data.repositories.RepoRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author Yamushev Igor
 * @since  22.06.17
 */
class RepoInteractor : IRepoInteractor {

    @Inject
    lateinit var repoRepository: RepoRepository

    init {
        GitforceApp.appComponent.injectTo(this)
    }

    override fun getSavedBranches(): List<Branch> = repoRepository.getSavedBranches()

    override fun loadBranches(repo: String): Single<ArrayList<Branch>> =
            repoRepository.loadBranches(repo)

    override fun getSavedCommits(branch: String): List<CommitWrapper>? = repoRepository.getSavedCommits(branch)

    override fun loadCommits(repo: String, branch: String): Single<ArrayList<CommitWrapper>> =
            repoRepository.loadCommits(repo, branch)

}

interface IRepoInteractor {
    fun getSavedBranches(): List<Branch>
    fun loadBranches(repo: String): Single<ArrayList<Branch>>
    fun getSavedCommits(branch: String): List<CommitWrapper>?
    fun loadCommits(repo: String, branch: String): Single<ArrayList<CommitWrapper>>
}