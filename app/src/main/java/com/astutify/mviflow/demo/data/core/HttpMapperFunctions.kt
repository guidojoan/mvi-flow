package com.astutify.mviflow.demo.data.core

import com.astutify.mviflow.demo.domain.model.PepperException
import com.astutify.mviflow.demo.domain.model.Response
import com.astutify.mviflow.demo.domain.model.ResponseError
import com.astutify.mviflow.demo.domain.model.ResponseSuccessful
import com.astutify.mviflow.demo.domain.model.ToDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.net.ConnectException

fun <T : ToDomain<D>, D> Iterable<T>.toDomain(): List<D> {
    return this.map { it.toDomain() }
}

fun <A : ToDomain<R>, I : Iterable<A>, R, G : Iterable<R>> mapListResponse(retrofitResponse: Flow<retrofit2.Response<I>>): Flow<Response<G>> {
    return retrofitResponse.map { response ->
        if (response.isSuccessful) {
            ResponseSuccessful(
                body = response.body()!!.toDomain() as G
            )
        } else {
            ResponseError<G>(
                error = mapResponseError(
                    response
                )
            )
        }
    }.catch {
        emit(ResponseError(error = mapResponseException(
            it
        )
        ))
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
