package com.jollypanda.gitforce.data.network

import com.jollypanda.gitforce.data.network.models.Branch
import com.jollypanda.gitforce.data.network.models.Commit
import com.jollypanda.gitforce.data.network.models.Repo
import com.jollypanda.gitforce.data.network.models.UserResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Yamushev Igor
 * @since  11.06.17
 */
interface GithubApi {

    @GET("user")
    fun getCurrentUser(): Single<UserResponse>

    @GET("user/repos")
    fun getCurrentUserRepos(@Query("sort") sortType: String?,
                            @Query("direction") sortOrder: String?): Single<ArrayList<Repo>>

    @GET("repos/{owner}/{repo}/branches")
    fun getBranchesList(@Path("owner") owner: String,
                        @Path("repo") repo: String): Single<ArrayList<Branch>>

    @GET("repos/{owner}/{repo}/commits")
    fun getCommitsListFor(@Path("owner") owner: String,
                          @Path("repo") repo: String,
                          @Query("sha") branch: String): Single<ArrayList<Commit>>

}