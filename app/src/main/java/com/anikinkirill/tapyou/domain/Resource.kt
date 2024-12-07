package com.anikinkirill.tapyou.domain

sealed class Resource<T>(
    val data: T? = null,
    val message: Text? = null,
    val status: Status? = null
) {

    class Success<T>(data: T) : Resource<T>(data, status = Status.SUCCESS)
    class SuccessWithNoResponseBody<T> : Resource<T>(data = null, status = Status.SUCCESS)
    class Error<T>(data: T? = null, message: Text) : Resource<T>(data, message, status = Status.ERROR)

    sealed class Status {
        data object SUCCESS : Status()
        data object ERROR : Status()
    }

    companion object {

        inline fun <T> Resource<T>.onSuccess(action: (value: T) -> Unit): Resource<T> {
            if (this is Success && this.data != null) action(data)
            return this
        }

        inline fun <T> Resource<T>.onError(
            action: (message: Text) -> Unit
        ): Resource<T> {
            if (this is Error && this.message != null) action(message)
            return this
        }
    }

}