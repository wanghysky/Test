package com.why.lib_base.livedata

import android.os.SystemClock
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.why.lib_base.BuildConfig
import com.why.lib_base.util.LogUtil

/**
 *
 * @author why
 * @date 2022/11/14 10:48
 */
internal abstract class SafeObserver<T>(private val alias: String, var errorBlock: (throwable: Throwable) -> Unit = {}) :
    Observer<T> {
    override fun onChanged(t: T?) {
        val startTime = SystemClock.elapsedRealtime()
        try {
            changed(t)
            val endTime = SystemClock.elapsedRealtime()
            LogUtil.d( "$alias time: ${endTime - startTime}" )
        } catch (throwable: Throwable) {
            errorBlock(throwable)
            LogUtil.e("","$throwable + $alias: onChanged error" )
            if (BuildConfig.DEBUG) {
                throw throwable
            }
        }
    }

    abstract fun changed(t: T?)
}

open class SafeMutableLiveData<T>(val alias: String = "default_live_data_alias", val errorBlock: (throwable: Throwable) -> Unit = {}) : LiveData<T>() {
    companion object {
        const val TAG = "SafeMutableLiveData"
    }

    private val mObserverMap by lazy {
        HashMap<Observer<in T>, SafeObserver<T>>()
    }

    public override fun setValue(value: T?) {
        try {
            super.setValue(value)
        } catch (throwable: Throwable) {
            errorBlock(throwable)
            LogUtil.e("$throwable + $alias: setValue error" )
        }
    }


    public override fun postValue(value: T) {
        super.postValue(value)
    }

    @Deprecated("Deprecated， plz use observe(LifecycleOwner, String, Observer<T>)")
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, object : SafeObserver<T>("$alias->default_observer_alias", errorBlock) {
            override fun changed(t: T?) {
                observer.onChanged(t)
            }
        })
    }

    fun observe(owner: LifecycleOwner, observerAlias: String, observer: Observer<T>) {
        super.observe(owner, object : SafeObserver<T>("$alias->$observerAlias", errorBlock) {
            override fun changed(t: T?) {
                observer.onChanged(t)
            }
        })
    }


    fun observeForever(observerAlias: String, observer: Observer<T>) {
        val safeObserver = object : SafeObserver<T>("$alias->${observerAlias}_observeForever", errorBlock) {
            override fun changed(t: T?) {
                observer.onChanged(t)
            }
        }
        super.observeForever(safeObserver)
        mObserverMap[observer] = safeObserver
    }

    override fun removeObserver(observer: Observer<in T>) {
        val safeObserver = mObserverMap[observer]
        if (safeObserver != null) {
            super.removeObserver(safeObserver)
            mObserverMap.remove(observer)
        } else {
            super.removeObserver(observer)
        }
    }
}

// value非空的LiveData，需传入默认值
class NonNullLiveData<T>(
    private val initValue: T,
    alias: String,
    errorBlock: (throwable: Throwable) -> Unit = {}
) : SafeMutableLiveData<T>(alias, errorBlock) {
    override fun getValue(): T {
        return super.getValue() ?: initValue
    }
}

/**
 * notify with old value
 */
fun SafeMutableLiveData<*>.notify() {
    this.value = this.value
}

/**
 * notify if [newValue] not equals old value
 */
fun <T> SafeMutableLiveData<T>.notifyIfChanged(newValue: T?) {
    if (newValue == null && this is NonNullLiveData<T>) {
        return
    }
    if (this.value != newValue) {
        this.value = newValue
    }
}