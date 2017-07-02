package com.jollypanda.gitforce.ui.android.activities

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jollypanda.gitforce.BuildConfig
import com.jollypanda.gitforce.R
import com.jollypanda.gitforce.data.network.models.UserResponse
import com.jollypanda.gitforce.databinding.ActivityLoginBinding
import com.jollypanda.gitforce.ui.presentation.LoginPresenter
import com.jollypanda.gitforce.ui.presentation.LoginView
import com.jollypanda.gitforce.utils.SCREEN_STATE
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.view_error.view.*

/**
 * @author Yamushev Igor
 * @since  12.06.17
 */
class LoginActivity : BaseActivity<ActivityLoginBinding>(), LoginView {

    companion object {
        fun getIntent(context: Context) = Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    @InjectPresenter
    lateinit var loginPresenter: LoginPresenter

    override fun getLayout() = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        binding.presenter = loginPresenter
        vError.btnRetry.setOnClickListener { loginPresenter.clearAccountData(); initLoading() }
        initLoading()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.title_login)
    }

    private fun initLoading() {
        if (loginPresenter.getSavedToken() != null) {
            if (BuildConfig.DEBUG)
                Log.e("TOKEN", loginPresenter.getSavedToken())
            loginPresenter.getSavedUser()?.let { startMain(it) } ?: loginPresenter.loadUser(this)
        } else {
            loginPresenter.getSavedCode()?.apply {
                loginPresenter.getAccessToken(this@LoginActivity, this)
            } ?: initWebView()
        }
    }

    override fun canLoadUserAfterToken() {
        loginPresenter.loadUser(this)
    }

    private fun initWebView() {
        loginPresenter.screenState.set(SCREEN_STATE.IS_LOADING)

        wvLogin.settings.javaScriptEnabled = true
        wvLogin.settings.saveFormData = false
        // Everything is so buggy in this world
        // https://forums.xamarin.com/discussion/38746/android-webview-authentication-bug
        wvLogin.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        tryToDeletePrevCreditneils()

        wvLogin.setWebViewClient(object : WebViewClient() {

            @SuppressWarnings("deprecation")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?) =
                    handleUrl(Uri.parse(url))

            // 1 https://github.com/login/oauth/authorize?client_id=25d40aadf3d5b0c62b73&redirect_uri=https%3A%2F%2Fgitforce.im%2Fcallback&scope=user%2C+public_repo&state=useyourforceluke
            // 2 https://gitforce.im/callback?code=50de2e5bb31acb701a97&state=useyourforceluke

            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) =
                    handleUrl(request.url)

            private fun handleUrl(uri: Uri): Boolean {
                if (uri.toString().contains("https://github.com/login"))
                    loginPresenter.screenState.set(SCREEN_STATE.HAS_CONTENT)
                val code = uri.getQueryParameter("code")
                if (uri.getQueryParameter("state")?.equals("useyourforceluke") ?: false
                        && code != null) {
                    loginPresenter.saveCode(code)
                    loginPresenter.getAccessToken(this@LoginActivity, code)
                    return true
                }
                return false
            }

        })

        wvLogin.loadUrl(loginPresenter.getInitiateUrl(this).toString())
    }

    private fun tryToDeletePrevCreditneils() {
        wvLogin.clearCache(true)
        wvLogin.clearHistory()
        wvLogin.clearFormData()
        wvLogin.clearMatches()
        val cm = CookieManager.getInstance()
        cm.removeAllCookie()
        cm.removeSessionCookie()
        WebViewDatabase.getInstance(this).clearHttpAuthUsernamePassword()
        WebViewDatabase.getInstance(this).clearUsernamePassword()
        WebViewDatabase.getInstance(this).clearFormData()
    }

    override fun showError(msg: String?) {
        vError.tvErrorDescription.text = getString(R.string.error)
        loginPresenter.screenState.set(SCREEN_STATE.HAS_ERROR)
    }

    override fun showInternetError() {
        vError.tvErrorDescription.text = getString(R.string.error_chech_internet)
        loginPresenter.screenState.set(SCREEN_STATE.HAS_ERROR)
    }

    override fun showUserError(msg: String?) {
        loginPresenter.screenState.set(SCREEN_STATE.HAS_ERROR)
        msg?.let {
            vError.tvErrorDescription.text = it
        }
    }

    override fun onUserLoaded(user: UserResponse) {
        startMain(user)
    }

    private fun startMain(user: UserResponse) {
        startActivity(MainActivity.getIntent(this, user))
    }

}