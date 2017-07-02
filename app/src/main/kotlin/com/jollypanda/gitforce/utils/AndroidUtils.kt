package com.jollypanda.gitforce.utils

import com.jollypanda.gitforce.ui.android.activities.BaseActivity
import java.io.Serializable
import kotlin.reflect.KProperty

/**
 * @author Yamushev Igor
 * @since  12.06.17
 */

class SerializableArg<out S : Serializable> {
    operator fun getValue(thisRef: BaseActivity<*>, property: KProperty<*>): S? {
        return thisRef.intent.getSerializableExtra(property.name) as? S
    }

    operator fun setValue(thisRef: BaseActivity<*>, property: KProperty<*>, value: Boolean?) {

    }
}

class BooleanArg(val default: Boolean) {

    operator fun getValue(thisRef: BaseActivity<*>, property: KProperty<*>): Boolean {
        return thisRef.intent.getBooleanExtra(property.name, default)
    }

    operator fun setValue(thisRef: BaseActivity<*>, property: KProperty<*>, value: Boolean?) {

    }
}

class StringArg(val default: String? = null) {

    operator fun getValue(thisRef: BaseActivity<*>, property: KProperty<*>): String? {
        return thisRef.intent.getStringExtra(property.name) ?: default
    }

    operator fun setValue(thisRef: BaseActivity<*>, property: KProperty<*>, value: String?) {

    }
}

class LongArg(val default: Long) {

    operator fun getValue(thisRef: BaseActivity<*>, property: KProperty<*>): Long {
        return thisRef.intent.getLongExtra(property.name, default)
    }

    operator fun setValue(thisRef: BaseActivity<*>, property: KProperty<*>, value: Long?) {

    }
}

enum class SCREEN_STATE {
    HAS_CONTENT,
    CONTENT_IS_EMPTY,
    IS_LOADING,
    HAS_ERROR
}
