package com.jollypanda.gitforce.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * @author Yamushev Igor
 * @since  15.06.17
 */
data class GithubError(@SerializedName("message")
                       var msg: String,
                       @SerializedName("errors")
                       var errors: ArrayList<ListedError>)

data class ListedError(@SerializedName("resource")
                       var resource: String,
                       @SerializedName("field")
                       var field: String,
                       @SerializedName("code")
                       var code: String)