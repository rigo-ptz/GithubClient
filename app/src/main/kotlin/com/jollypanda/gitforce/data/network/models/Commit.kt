package com.jollypanda.gitforce.data.network.models

import com.google.gson.annotations.SerializedName
import com.jollypanda.gitforce.utils.formatToList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * @author Yamushev Igor
 * @since  28.06.17
 */
open class Commit : RealmObject() {

    @SerializedName("sha")
    @PrimaryKey
    var sha: String = ""

    @SerializedName("commit")
    var commitInfo: CommitInfo? = null

    fun toWrapper(branch: String) =
            CommitWrapper(sha = sha,
                          commitInfo = commitInfo?.toWrapper(),
                          branchName = branch)
}

open class CommitInfo : RealmObject() {

    @SerializedName("url")
    var url: String = ""

    @SerializedName("author")
    var author: Author? = null

    @SerializedName("message")
    var message: String? = null

    fun toWrapper() =
            CommitInfoWrapper(url = url,
                              author = author?.toWrapper(),
                              message = message)
}

open class Author : RealmObject() {

    @SerializedName("name")
    var name: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("date")
    var date: Date? = null

    fun toWrapper() =
            AuthorWrapper(name = name,
                          email = email,
                          date = formatToList(date))
}

open class CommitWrapper(@PrimaryKey var sha: String? = null,
                         var commitInfo: CommitInfoWrapper? = null,
                         var branchName: String? = null) : RealmObject()

open class CommitInfoWrapper(var url: String? = null,
                             var author: AuthorWrapper? = null,
                             var message: String? = null) : RealmObject()

open class AuthorWrapper(var name: String? = null,
                         var email: String? = null,
                         var date: String? = null) : RealmObject()
