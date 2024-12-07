package com.anikinkirill.tapyou.core.network

import com.anikinkirill.tapyou.domain.Resource
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class RemoteResourceCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Call::class.java != getRawType(returnType)) return null

        check(returnType is ParameterizedType)

        val responseType = getParameterUpperBound(0, returnType)

        if (getRawType(responseType) != Resource::class.java) return null

        check(responseType is ParameterizedType)

        val successType = getParameterUpperBound(0, responseType)

        return RemoteResourceCallAdapter<Any>(successType)
    }
}
