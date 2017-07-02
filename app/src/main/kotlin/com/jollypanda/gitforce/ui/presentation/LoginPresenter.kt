package com.jollypanda.gitforce.ui.presentation

import android.content.Context
import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.google.gson.JsonParseException
import com.jollypanda.gitforce.GitforceApp
import com.jollypanda.gitforce.R
import com.jollypanda.gitforce.business.interactors.AccountInteractor
import com.jollypanda.gitforce.data.network.models.AccessTokenRequest
import com.jollypanda.gitforce.data.network.models.UserResponse
import com.jollypanda.gitforce.utils.Optional
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
 * @since  13.06.17
 */
@InjectViewState
class LoginPresenter : BasePresenter<LoginView>() {

    @Inject
    lateinit var accountInteractor: AccountInteractor

    init {
        GitforceApp.appComponent.injectTo(this)
    }

    fun getInitiateUrl(context: Context): Uri.Builder = Uri.Builder()
            .scheme("https")
            .authority("github.com")
            .appendPath("login")
            .appendPath("oauth")
            .appendPath("authorize")
            .appendQueryParameter("client_id", context.getString(R.string.client_id))
            .appendQueryParameter("redirect_uri", context.getString(R.string.redirect_uri))
            .appendQueryParameter("scope", "user, public_repo")
            .appendQueryParameter("state", "useyourforceluke")


    fun getAccessToken(context: Context, code: String) {
        if (getSavedToken() == null) {
            screenState.set(SCREEN_STATE.IS_LOADING)
            accountInteractor.loadToken(AccessTokenRequest(context.getString(R.string.client_id),
                                                           context.getString(R.string.client_secret),
                                                           code))
                    .bindUntilEvent(context as LifecycleProvider<ActivityEvent>, ActivityEvent.STOP)
                    .defaultSubscribe(onSuccess = this::tokenResponseSuccess,
                                      onError = this::tokenResponseError)
        }
    }

    fun getSavedToken() = accountInteractor.getSavedToken()

    private fun tokenResponseSuccess(token: Optional<String?>) {
        token.value?.let {
            viewState.canLoadUserAfterToken()
        }
    }

    private fun tokenResponseError(thr: Throwable) {
        when (thr) {
            is retrofit2.HttpException -> viewState.showError(thr.getDetailedInfo())
            is IOException -> viewState.showInternetError()
            is JsonParseException -> viewState.showError()
            is IllegalStateException -> viewState.showError()
            else -> viewState.showError()
        }
    }

    fun getSavedUser() = accountInteractor.getSavedUser()

    fun loadUser(lyfecycle: LifecycleProvider<ActivityEvent>) {
        screenState.set(SCREEN_STATE.IS_LOADING)
        accountInteractor.loadUser()
                .bindUntilEvent(lyfecycle, ActivityEvent.STOP)
                .defaultSubscribe(onSuccess = this::userResponseSuccess,
                                  onError = this::userResponseError)
    }

    private fun userResponseError(thr: Throwable) {
        when (thr) {
            is retrofit2.HttpException -> viewState.showUserError(thr.getDetailedInfo())
            is IOException -> viewState.showInternetError()
            is JsonParseException -> viewState.showError()
            is IllegalStateException -> viewState.showError()
            else -> viewState.showError()
        }
    }

    private fun userResponseSuccess(user: UserResponse) {
        viewState.onUserLoaded(user)
    }

    fun clearAccountData() {
        accountInteractor.accountRepository.clearAccountData()
    }

    fun getSavedCode() = accountInteractor.accountRepository.getSavedCode()

    fun saveCode(code: String) {
        accountInteractor.saveCode(code)
    }

}


@StateStrategyType(SkipStrategy::class)
interface LoginView : MvpView {
    fun showError(msg: String? = null)
    fun showUserError(msg: String? = null)
    fun showInternetError()
    fun onUserLoaded(user: UserResponse)
    fun canLoadUserAfterToken()
}