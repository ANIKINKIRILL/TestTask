package com.anikinkirill.tapyou.core.network

import okhttp3.Interceptor
import java.util.concurrent.TimeUnit
import okhttp3.Response
import okhttp3.Request

class TimeoutInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        var connectionTimeout = chain.connectTimeoutMillis()
        var readTimeout = chain.readTimeoutMillis()
        var writeTimeout = chain.writeTimeoutMillis()

        val connectionFromRequest: String? = request.header(CONNECTION_TIMEOUT)
        val readFromRequest: String? = request.header(READ_TIMEOUT)
        val writeFromRequest: String? = request.header(WRITE_TIMEOUT)

        if (!connectionFromRequest.isNullOrEmpty()) {
            connectionTimeout = Integer.valueOf(connectionFromRequest)
        }
        if (!readFromRequest.isNullOrEmpty()) {
            readTimeout = Integer.valueOf(readFromRequest)
        }
        if (!writeFromRequest.isNullOrEmpty()) {
            writeTimeout = Integer.valueOf(writeFromRequest)
        }

        return buildResponse(
            request = request,
            chain = chain,
            connectionTimeout = connectionTimeout,
            readTimeout = readTimeout,
            writeTimeout = writeTimeout,
        )
    }

    private fun buildResponse(
        request: Request,
        chain: Interceptor.Chain,
        connectionTimeout: Int,
        readTimeout: Int,
        writeTimeout: Int
    ): Response {
        return chain
            .withConnectTimeout(connectionTimeout, TimeUnit.SECONDS)
            .withReadTimeout(readTimeout, TimeUnit.SECONDS)
            .withWriteTimeout(writeTimeout, TimeUnit.SECONDS)
            .proceed(request)
    }

    companion object {
        const val CONNECTION_TIMEOUT = "CONNECTION_TIMEOUT"
        const val READ_TIMEOUT = "READ_TIMEOUT"
        const val WRITE_TIMEOUT = "WRITE_TIMEOUT"
    }
}