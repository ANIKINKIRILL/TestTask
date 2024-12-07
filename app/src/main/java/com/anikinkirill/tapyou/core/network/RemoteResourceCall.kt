package com.anikinkirill.tapyou.core.network

import com.anikinkirill.tapyou.R
import com.anikinkirill.tapyou.domain.Resource
import com.anikinkirill.tapyou.domain.Text
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class RemoteResourceCall<T>(
    private val callDelegate: Call<T>,
) : Call<Resource<T>> {

    override fun clone(): Call<Resource<T>> {
        return RemoteResourceCall(callDelegate.clone())
    }

    override fun execute(): Response<Resource<T>> {
        throw UnsupportedOperationException("There is no execute call for this api")
    }

    override fun isExecuted(): Boolean {
        return callDelegate.isExecuted
    }

    override fun cancel() {
        return callDelegate.cancel()
    }

    override fun isCanceled(): Boolean {
        return callDelegate.isCanceled
    }

    override fun request(): Request {
        return callDelegate.request()
    }

    override fun timeout(): Timeout {
        return callDelegate.timeout()
    }

    override fun enqueue(callback: Callback<Resource<T>>) {
        callDelegate.enqueue(
            object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    onResponseResource(response, callback)
                }

                override fun onFailure(call: Call<T>, throwable: Throwable) {
                    failureResource(callback)
                }
            }
        )
    }

    private fun onResponseResource(
        response: Response<T>,
        callback: Callback<Resource<T>>,
    ) {
        when (response.code()) {
            in 200 .. 208 -> {
                val body = response.body()
                if (body != null) {
                    callback.onResponse(
                        this,
                        Response.success(Resource.Success(body))
                    )
                } else {
                    callback.onResponse(
                        this,
                        Response.success(Resource.SuccessWithNoResponseBody())
                    )
                }
            }
            else -> {
                callback.onResponse(
                    this,
                    Response.success(
                        Resource.Error(message = Text.res(R.string.network_error))
                    )
                )
            }
        }
    }

    private fun failureResource(
        callback: Callback<Resource<T>>,
    ) {
        callback.onResponse(
            this,
            Response.success(
                Resource.Error(message = Text.res(R.string.network_error))
            ),
        )
    }
}