package com.anikinkirill.tapyou.core.network

import com.anikinkirill.tapyou.domain.Resource
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

internal class RemoteResourceCallAdapter<T>(
    private val successType: Type,
) : CallAdapter<T, Call<Resource<T>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<Resource<T>> = RemoteResourceCall(call)
}
