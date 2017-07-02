package com.jollypanda.gitforce.ui.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearSnapHelper
import com.arellomobile.mvp.presenter.InjectPresenter
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapter.Type
import com.jollypanda.gitforce.BR
import com.jollypanda.gitforce.R
import com.jollypanda.gitforce.data.network.models.Branch
import com.jollypanda.gitforce.data.network.models.CommitWrapper
import com.jollypanda.gitforce.databinding.ActivityRepoBinding
import com.jollypanda.gitforce.databinding.ItemBranchBinding
import com.jollypanda.gitforce.databinding.ItemRepoBinding
import com.jollypanda.gitforce.ui.presentation.RepoPresenter
import com.jollypanda.gitforce.ui.presentation.RepoView
import com.jollypanda.gitforce.utils.Constants
import com.jollypanda.gitforce.utils.SCREEN_STATE
import com.jollypanda.gitforce.utils.StringArg
import kotlinx.android.synthetic.main.activity_repo.*
import kotlinx.android.synthetic.main.view_empty_repos_list.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_repo_content.*


/**
 * @author Yamushev Igor
 * @since  22.06.17
 */
class RepoActivity : BaseActivity<ActivityRepoBinding>(), RepoView {

    @InjectPresenter
    lateinit var repoPresenter: RepoPresenter

    companion object {
        fun getIntent(context: Context, repoName: String) = Intent(context, RepoActivity::class.java).apply {
            putExtra("repoName", repoName)
        }
    }

    var repoName: String? by StringArg()

    override fun getLayout() = R.layout.activity_repo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.presenter = repoPresenter
        initToolbar()
        initBranchRecycler()
        initLoadCommitsAndBranches()
        btnRetry.setOnClickListener { initLoadCommitsAndBranches() }
        repoPresenter.shouldShowHint.set(getSharedPreferences(Constants.SP, 0)
                                                 .getBoolean(Constants.SHOULD_SHOW_BRANCH_HINT, true))
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = repoName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initBranchRecycler() {
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rvBranches)
        rvBranches.onFlingListener = snapHelper
    }

    private fun initLoadCommitsAndBranches() {
        repoName?.let {
            repoPresenter.loadBranches(it, this)
        } ?: showError(getString(R.string.error_empty_repo_name))
    }

    override fun showLoadedBranches(branches: List<Branch>) {
        repoPresenter.screenState.set(SCREEN_STATE.HAS_CONTENT)
        LastAdapter(branches, BR.item)
                .map<Branch>(Type<ItemBranchBinding>(R.layout.item_branch)
                                     .onClick {
                                         getSharedPreferences(Constants.SP, 0)
                                                 .edit()
                                                 .putBoolean(Constants.SHOULD_SHOW_BRANCH_HINT, false)
                                                 .apply()
                                         repoPresenter.shouldShowHint.set(false)
                                         repoPresenter.loadCommitsFor(repoName!!, it.binding.item.name!!, this)
                                     })
                .into(rvBranches)
    }

    override fun showLoadedCommits(commits: ArrayList<CommitWrapper>) {
        repoPresenter.screenState.set(SCREEN_STATE.HAS_CONTENT)
        LastAdapter(commits, BR.item)
                .map<CommitWrapper>(Type<ItemRepoBinding>(R.layout.item_commit))
                .into(rvCommits)
    }

    override fun showError(msg: String?) {
        tvErrorDescription.text = msg ?: getString(R.string.error)
        repoPresenter.screenState.set(SCREEN_STATE.HAS_ERROR)
    }

    override fun showInternetError() {
        tvErrorDescription.text = getString(R.string.error_chech_internet)
        repoPresenter.screenState.set(SCREEN_STATE.HAS_ERROR)
    }

    override fun onBranchesListIsEmpty() {
        tvEmptyMessage.text = getString(R.string.error_branch_list_is_empty)
        repoPresenter.screenState.set(SCREEN_STATE.CONTENT_IS_EMPTY)
    }

    override fun onCommitsListIsEmpty() {
        tvEmptyMessage.text = getString(R.string.error_commits_list_is_empty)
        repoPresenter.screenState.set(SCREEN_STATE.CONTENT_IS_EMPTY)
    }
}