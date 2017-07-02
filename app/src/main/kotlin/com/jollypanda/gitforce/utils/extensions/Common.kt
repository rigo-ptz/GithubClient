package com.jollypanda.gitforce.utils.extensions

import com.jollypanda.gitforce.data.network.models.GithubError
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Yamushev Igor
 * @since  15.06.17
 */

fun retrofit2.HttpException.getDetailedInfo(): String {
    val response = this.response()
    val converter = GsonConverterFactory.create().responseBodyConverter(GithubError::class.java,
                                                                        null,
                                                                        null)
    val error = converter?.convert(response.errorBody()) as GithubError
    return error.msg
}
