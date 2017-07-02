package com.jollypanda.gitforce.data.network.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

/**
 * @author Yamushev Igor
 * @since  15.06.17
 */
open class UserResponse() : RealmObject(), Serializable {

    @SerializedName("id")
    @PrimaryKey
    var id: Long = 0

    @SerializedName("login")
    var login: String? = null

    @SerializedName("avatar_url")
    var avatar: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("company")
    var company: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("bio")
    var bio: String? = null

    @SerializedName("public_repos")
    var publicReposCount: Int = 0

    @SerializedName("total_private_repos")
    var privateReposCount: Int = 0

}