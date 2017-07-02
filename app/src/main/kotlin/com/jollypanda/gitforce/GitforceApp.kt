package com.jollypanda.gitforce

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.jollypanda.gitforce.di.component.AppComponent
import com.jollypanda.gitforce.di.component.DaggerAppComponent
import com.jollypanda.gitforce.di.module.AppModule
import com.jollypanda.gitforce.di.module.NetworkModule
import io.realm.Realm

/**
 * @author Yamushev Igor
 * @since  09.06.17
 */
class GitforceApp : MultiDexApplication() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger()
        Realm.init(this)
    }

    private fun initDagger(): AppComponent {
        return DaggerAppComponent.builder()
                .networkModule(NetworkModule(BuildConfig.BASE_URL))
                .appModule(AppModule(this))
                .build()
    }
}