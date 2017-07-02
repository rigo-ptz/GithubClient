package com.jollypanda.gitforce.di.module

import android.content.Context
import com.jollypanda.gitforce.utils.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Yamushev Igor
 * @since  10.06.17
 */
@Module
class AppModule(val context: Context) {

    @Provides
    @Singleton
    fun provideAppContext() = context

    @Provides
    @Singleton
    fun provideSharedPrefs(context: Context) = context.getSharedPreferences(Constants.SP, 0)

}