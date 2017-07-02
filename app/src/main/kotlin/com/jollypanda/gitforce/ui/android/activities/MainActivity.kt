package com.jollypanda.gitforce.ui.android.activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jollypanda.gitforce.R
import com.jollypanda.gitforce.data.network.models.UserResponse
import com.jollypanda.gitforce.databinding.ActivityMainBinding
import com.jollypanda.gitforce.databinding.ViewNavHeaderBinding
import com.jollypanda.gitforce.ui.android.fragment.ReposListFragment
import com.jollypanda.gitforce.ui.presentation.MainPresenter
import com.jollypanda.gitforce.ui.presentation.MainView
import com.jollypanda.gitforce.utils.SerializableArg
import com.jollypanda.gitforce.utils.extensions.alert
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Yamushev Igor
 * @since  19.06.17
 */
class MainActivity : BaseActivity<ActivityMainBinding>(), MainView {

    companion object {
        fun getIntent(context: Context, user: UserResponse) =
                Intent(context, MainActivity::class.java).apply {
                    putExtra("user", user)
                }
    }

    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    lateinit private var toggle: ActionBarDrawerToggle

    val user by SerializableArg<UserResponse>()

    override fun getLayout() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        initDrawer()

        putFragment(ReposListFragment.newInstance(), ReposListFragment.TAG)
    }

    private fun initDrawer() {
        toggle = ActionBarDrawerToggle(this, dlDrawerMain, toolbar, R.string.title_main, R.string.title_main)
        dlDrawerMain.addDrawerListener(toggle)
        toggle.syncState()
        nvMain.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menuReposList -> {
                    supportActionBar?.title = getString(R.string.title_main)
                }
                R.id.menuGists -> {
                    supportActionBar?.title = getString(R.string.title_gists)
                    alert(this@MainActivity, getString(R.string.stub))
                }
                R.id.menuFolowers -> {
                    supportActionBar?.title = getString(R.string.title_folowers)
                    alert(this@MainActivity, getString(R.string.stub))
                }
                R.id.menuLogout ->  {
                    supportActionBar?.title = getString(R.string.title_main)
                    mainPresenter.logout()
                }
            }
            it.isChecked = true
            dlDrawerMain.closeDrawers()
            return@setNavigationItemSelectedListener true
        }
        nvMain.menu.getItem(0).isChecked = true

        fillUserInfo()
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(menuItem))
            return true
        return super.onOptionsItemSelected(menuItem)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.title_main)
    }

    private fun fillUserInfo() {
        val headerBinding = ViewNavHeaderBinding.bind(binding.nvMain.getHeaderView(0))
        headerBinding.user = user
    }

    override fun goToLogin() {
        startActivity(LoginActivity.getIntent(this))
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentById(R.id.container)

        if (dlDrawerMain.isDrawerOpen(GravityCompat.START)) {
            dlDrawerMain.closeDrawers()
        } else {
            if (supportFragmentManager.backStackEntryCount <= 1)
                finish()
            else
                super.onBackPressed()
        }

    }

}