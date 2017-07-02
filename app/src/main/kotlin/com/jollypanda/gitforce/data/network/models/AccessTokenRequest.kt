package com.jollypanda.gitforce.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * @author Yamushev Igor
 * @since  14.06.17
 */
data class AccessTokenRequest(@SerializedName("client_id")
                              val clientId: String,
                              @SerializedName("client_secret")
                              val clientSecret: String,
                              @SerializedName("code")
                              val code: String)