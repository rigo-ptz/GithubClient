package com.jollypanda.gitforce.data.network.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * @author Yamushev Igor
 * @since  14.06.17
 */
open class AccessTokenResponse() : RealmObject() {

    @PrimaryKey
    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("token_type")
    var tokenType: String? = null

    @SerializedName("scope")
    var scope: String? = null


    constructor(accessToken: String, tokenType: String, scope: String) : this() {
        this.accessToken = accessToken
        this.tokenType = tokenType
        this.scope = scope
    }
}