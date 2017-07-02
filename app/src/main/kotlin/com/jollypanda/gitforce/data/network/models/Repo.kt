package com.jollypanda.gitforce.data.network.models

import com.google.gson.annotations.SerializedName
import com.jollypanda.gitforce.utils.formatToList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * @author Yamushev Igor
 * @since  22.06.17
 */
open class Repo : RealmObject() {

    @SerializedName("id")
    @PrimaryKey
    var id: Long = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("language")
    var language: String? = null

    @SerializedName("forks_count")
    var forksCount: Int = 0

    @SerializedName("watchers_count")
    var watchersCount: Int = 0

    @SerializedName("updated_at")
    var lastUpdateTime: Date? = null

    fun toWrapper() =
            RepoWrapper(name = name,
                        description = description,
                        language = language,
                        forksCount = forksCount.toString(),
                        watchersCount = watchersCount.toString(),
                        lastUpdateTime = formatToList(lastUpdateTime))
}

data class RepoWrapper(var name: String?,
                       var description: String?,
                       var language: String?,
                       var forksCount: String?,
                       var watchersCount: String?,
                       var lastUpdateTime: String?)