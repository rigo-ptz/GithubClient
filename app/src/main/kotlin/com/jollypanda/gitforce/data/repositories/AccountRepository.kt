package com.jollypanda.gitforce.data.repositories

import android.content.Context
import com.jollypanda.gitforce.GitforceApp
import com.jollypanda.gitforce.data.network.GithubAccountApi
import com.jollypanda.gitforce.data.network.GithubApi
import com.jollypanda.gitforce.data.network.models.AccessTokenRequest
import com.jollypanda.gitforce.data.network.models.AccessTokenResponse
import com.jollypanda.gitforce.data.network.models.UserResponse
import com.jollypanda.gitforce.utils.Constants
import com.jollypanda.gitforce.utils.Optional
import com.vicpin.krealmextensions.deleteAll
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.save
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Yamushev Igor
 * @since  14.06.17
 */
class AccountRepository : IAccountRepository {

    @Inject
    lateinit var accApi: GithubAccountApi

    @Inject
    lateinit var api: GithubApi

    @Inject
    lateinit var context: Context

    init {
        GitforceApp.appComponent.injectTo(this)
    }

    override fun loadAccessToken(request: AccessTokenRequest): Single<Optional<String?>> {
        val resp = AccessTokenResponse().queryFirst()
        if (resp != null && resp.accessToken != null) {
            return Single.just(Optional<String?>(resp.accessToken))
        } else {
            return loadTokenFromNet(request).map { Optional<String?>(it) }
        }
    }


    private fun  loadTokenFromNet(request: AccessTokenRequest) =
            accApi.getAccessTokenBody(request)
                    .observeOn(Schedulers.computation())
                    .doOnSuccess {
                        it.save()
                    }
                    .map { it.accessToken }

    override fun getSavedToken() =
            AccessTokenResponse().queryFirst()?.accessToken

    override fun loadUser(): Single<UserResponse> {
        val resp = getSavedUser()
        if (resp != null) {
            return Single.just(resp)
        } else {
            return loadUserFromNet()
        }
    }

    override fun getSavedUser() =
            UserResponse().queryFirst()

    private fun loadUserFromNet() =
            api.getCurrentUser()
                    .observeOn(Schedulers.computation())
                    .doOnSuccess {
                        it.save()
                    }

    override fun clearAccountData() {
        AccessTokenResponse().deleteAll()
        UserResponse().deleteAll()
        context.getSharedPreferences(Constants.SP, 0).edit().remove(Constants.CODE).apply()
    }

    override fun saveCode(code: String) {
        context.getSharedPreferences(Constants.SP, 0).edit().putString(Constants.CODE, code).apply()
    }

    override fun getSavedCode(): String? =
            context.getSharedPreferences(Constants.SP, 0).getString(Constants.CODE, null)

}

interface IAccountRepository {
    fun loadAccessToken(request: AccessTokenRequest): Single<Optional<String?>>
    fun getSavedToken(): String?
    fun loadUser(): Single<UserResponse>
    fun getSavedUser(): UserResponse?
    fun saveCode(code: String)
    fun getSavedCode(): String?
    fun clearAccountData()
}