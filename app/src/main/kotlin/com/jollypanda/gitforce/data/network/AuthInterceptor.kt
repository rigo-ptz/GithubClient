package com.jollypanda.gitforce.data.network

import android.content.SharedPreferences
import com.jollypanda.gitforce.GitforceApp
import com.jollypanda.gitforce.data.network.models.AccessTokenResponse
import com.vicpin.krealmextensions.deleteAll
import com.vicpin.krealmextensions.queryFirst
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * @author Yamushev Igor
 * @since  12.06.17
 */
class AuthInterceptor : Interceptor {

    @Inject
    lateinit var sp: SharedPreferences

    init {
        GitforceApp.appComponent.injectTo(this)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().newBuilder()
                .apply {
                    val token = AccessTokenResponse().queryFirst()?.accessToken
                    token?.let {
                        addHeader("Authorization", "token $it")
                    }
                }.build())
                .apply {
                    if (code() == 401)
                        AccessTokenResponse().deleteAll()
                }

    }
}