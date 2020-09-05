package com.astutify.mvi.demo.domain.model

sealed class Response<T>(
    private val body: T? = null,
    private val error: PepperException? = null
) {

    private fun isSuccessFull() = error == null

    fun <R> fold(onSuccessful: (T) -> R, onError: (PepperException) -> R): R {
        return if (isSuccessFull()) {
            onSuccessful.invoke(body!!)
        } else {
            onError.invoke(error!!)
        }
    }

    fun mapSuccessFull(onSuccessful: (T) -> T): Response<T> {
        return if (isSuccessFull()) {
            val bodyTransformed = onSuccessful.invoke(body!!)
            ResponseSuccessful(body = bodyTransformed)
        } else {
            this
        }
    }
}

class ResponseSuccessful<T>(body: T) : Response<T>(body = body)
class ResponseError<T>(error: PepperException) : Response<T>(error = error)
