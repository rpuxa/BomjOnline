package ru.rpuxa.bomjonline

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rx.Observable
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <T> Call<T>.await() = suspendCoroutine<T> {
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>?, t: Throwable) {
            it.resumeWithException(t)
        }

        override fun onResponse(call: Call<T>?, response: Response<T>) {
            it.resume(response.body())
        }
    })
}

fun <T> MutableLiveData(value: T) =
    androidx.lifecycle.MutableLiveData<T>().apply { this.value = value }

var <T> MutableLiveData<T>.postValue: T
    @Deprecated("getter not supported")
    get() {
        throw UnsupportedOperationException()
    }
    set(value) {
        postValue(value)
    }

inline fun <reified V : ViewModel> FragmentActivity.getViewModel() = ViewModelProviders.of(this).get(V::class.java)

fun <T> LiveData<T>.observe(owner: LifecycleOwner, block: (T) -> Unit) {
    observe(owner, Observer { block(it) })
}

fun <T> LiveData<T>.toObservable(owner: LifecycleOwner): Observable<T> {
    return Observable.create { subscriber ->
        observe(owner) { value ->
            subscriber.onNext(value)
        }
    }
}

fun <T> LiveData<T>.toObservableForever(): Observable<T> {
    return Observable.create { subscriber ->
        observeForever { value ->
            subscriber.onNext(value)
        }
    }
}

val <T> LiveData<T>.nnValue get() = value!!

fun setVisibility(visibility: Int, vararg views: View) {
    for (v in views)
        v.visibility = visibility
}