package com.jollypanda.gitforce.ui.presentation

import android.databinding.ObservableField
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.jollypanda.gitforce.utils.SCREEN_STATE

/**
 * @author Yamushev Igor
 * @since  20.06.17
 */
open class BasePresenter<T : MvpView> : MvpPresenter<T>() {

    val screenState = ObservableField<SCREEN_STATE>(SCREEN_STATE.IS_LOADING)

}