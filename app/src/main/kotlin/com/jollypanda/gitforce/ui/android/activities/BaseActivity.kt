package com.jollypanda.gitforce.ui.android.activities

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.CheckResult
import android.support.annotation.NonNull
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.MvpAppCompatFragment
import com.jollypanda.gitforce.R
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * @author Yamushev Igor
 * @since  12.06.17
 */
abstract class BaseActivity<B : ViewDataBinding> : MvpAppCompatActivity(), LifecycleProvider<ActivityEvent> {

    abstract fun getLayout(): Int

    lateinit var binding: B

    private val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<B>(this, getLayout())
        lifecycleSubject.onNext(ActivityEvent.CREATE)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(ActivityEvent.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(ActivityEvent.RESUME)
    }

    @CallSuper
    override fun onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE)
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP)
        super.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY)
        super.onDestroy()
    }

    override fun lifecycle(): Observable<ActivityEvent> {
        return lifecycleSubject
    }

    @NonNull
    @CheckResult
    override fun <T> bindUntilEvent(@NonNull event: ActivityEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event)
    }

    @NonNull
    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem) =
            when (menuItem.itemId) {
                android.R.id.home -> {
                    finish()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(menuItem)
                }
            }

    protected fun addFragment(fragment: MvpAppCompatFragment, tag: String) {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }

    protected fun putFragment(fragment: MvpAppCompatFragment, tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragment, tag)
                    .addToBackStack(tag)
                    .commit()
        }
    }
}