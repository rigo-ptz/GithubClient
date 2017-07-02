package com.jollypanda.gitforce.ui.android.fragment

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapter.Type
import com.jollypanda.gitforce.BR
import com.jollypanda.gitforce.R
import com.jollypanda.gitforce.data.network.models.RepoWrapper
import com.jollypanda.gitforce.databinding.FragmentReposListBinding
import com.jollypanda.gitforce.databinding.ItemRepoBinding
import com.jollypanda.gitforce.ui.android.activities.RepoActivity
import com.jollypanda.gitforce.ui.presentation.ReposListPresenter
import com.jollypanda.gitforce.ui.presentation.ReposListView
import com.jollypanda.gitforce.utils.SCREEN_STATE
import kotlinx.android.synthetic.main.fragment_repos_list.*
import kotlinx.android.synthetic.main.view_error.*

/**
 * @author Yamushev Igor
 * @since  21.06.17
 */
class ReposListFragment : BaseFragment<FragmentReposListBinding>(), ReposListView {

    companion object {
        val TAG = "reposListFragment"

        fun newInstance() = ReposListFragment()
    }

    @InjectPresenter
    lateinit var reposListPresenter: ReposListPresenter

    override fun getLayout() = R.layout.fragment_repos_list

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.presenter = reposListPresenter
        initLoad()
        btnRetry.setOnClickListener { reposListPresenter.loadReposList(this) }
        srlReposList.setOnRefreshListener {
            reposListPresenter.loadReposList(this)
        }
    }

    private fun initLoad() {
        val saved = reposListPresenter.getSavedReposList()
        if (saved != null && saved.isNotEmpty()) {
            fillRecycler(saved)
            reposListPresenter.screenState.set(SCREEN_STATE.HAS_CONTENT)
        } else {
            reposListPresenter.loadReposList(this)
        }
    }

    private fun fillRecycler(list: List<RepoWrapper>) {
        LastAdapter(list, BR.item)
                .map<RepoWrapper>(Type<ItemRepoBinding>(R.layout.item_repo)
                                   .onClick {
                                       startActivity(RepoActivity.getIntent(activity,
                                                                            it.binding.tvRepoName.text.toString()))
                                   }
                          )
                .into(rvReposList)
    }

    override fun onReposListLoaded(repositories: ArrayList<RepoWrapper>) {
        fillRecycler(repositories)
        srlReposList.isRefreshing = false
        reposListPresenter.screenState.set(SCREEN_STATE.HAS_CONTENT)
    }

    override fun showError(msg: String?) {
        srlReposList.isRefreshing = false
        tvErrorDescription.text = getString(R.string.error)
        reposListPresenter.screenState.set(SCREEN_STATE.HAS_ERROR)
    }

    override fun showInternetError() {
        srlReposList.isRefreshing = false
        tvErrorDescription.text = getString(R.string.error_chech_internet)
        reposListPresenter.screenState.set(SCREEN_STATE.HAS_ERROR)
    }

    override fun onReposListIsEmpty() {
        reposListPresenter.screenState.set(SCREEN_STATE.CONTENT_IS_EMPTY)
    }

}