package com.astutify.mvi.demo.data

import com.astutify.mvi.demo.domain.model.*
import io.reactivex.SingleTransformer
import java.net.ConnectException

fun <T : ToDomain<D>, D> Iterable<T>.toDomain(): List<D> {
    return this.map { it.toDomain() }
}

fun <A : ToDomain<R>, I : Iterable<A>, R, G : Iterable<R>> mapListResponse(): SingleTransformer<retrofit2.Response<I>, Response<G>> {
    return SingleTransformer { response ->
        response.map {
            if (it.isSuccessful) {
                ResponseSuccessful(
                    body = it.body()!!.toDomain() as G
                )
            } else {
                ResponseError<G>(
                    error = mapResponseError(
                        it
                    )
                )
            }
        }.onErrorReturn {
            ResponseError(
                error = mapResponseException(
                    it
                )
            )
        }
    }
}

fun mapResponseException(exception: Throwable) =
    when (exception) {
        is ConnectException -> PepperException.NetworkError
        else -> PepperException.UnknownError
    }

fun <T> mapResponseError(response: retrofit2.Response<T>) =
    when (response.code()) {
        400 -> PepperException.BadRequest
        401 -> PepperException.Unauthorized
        403 -> PepperException.Forbidden
        404 -> PepperException.NotFound
        else -> PepperException.UnknownError
    }
