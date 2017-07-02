package com.jollypanda.gitforce.data.network

import com.jollypanda.gitforce.data.network.models.AccessTokenRequest
import com.jollypanda.gitforce.data.network.models.AccessTokenResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author Yamushev Igor
 * @since  19.06.17
 */
interface GithubAccountApi {

    @FormUrlEncoded
    @POST("login/oauth/access_token")
    fun getAccessToken(@Field("client_id") clientId: String,
                       @Field("client_secret") clientSecret: String,
                       @Field("code") code: String): Single<AccessTokenResponse>

    @POST("login/oauth/access_token")
    fun getAccessTokenBody(@Body req: AccessTokenRequest): Single<AccessTokenResponse>

}