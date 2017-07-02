package com.jollypanda.gitforce.utils.extensions

import android.util.Log
import com.jollypanda.gitforce.BuildConfig
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Yamushev Igor
 * @since  15.06.17
 */
val onNextStub: (Any) -> Unit = {}
val onCompleteStub: () -> Unit = {}
val onErrorStub: (Throwable) -> Unit = {
    Log.e("RX_ERROR", "On error not implemented")
    if(BuildConfig.DEBUG)
        it.printStackTrace()
}


fun <T : Any> Single<T>.defaultSubscribe(scheduler: Scheduler = Schedulers.io(),
                                         onSuccess: (T) -> Unit = onNextStub,
                                         onError: (Throwable) -> Unit = onErrorStub) =
        observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(scheduler)
                .subscribe(onSuccess, onError)