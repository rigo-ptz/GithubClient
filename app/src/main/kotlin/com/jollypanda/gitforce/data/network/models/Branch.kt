package com.jollypanda.gitforce.data.network.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * @author Yamushev Igor
 * @since  22.06.17
 */
open class Branch : RealmObject() {

    @SerializedName("name")
    @PrimaryKey
    var name: String = ""

}