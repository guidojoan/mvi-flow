package com.astutify.mvi.demo.domain.model

sealed class PepperException : Throwable() {
    object BadRequest : PepperException()
    object NotFound : PepperException()
    object Forbidden : PepperException()
    object Unauthorized : PepperException()
    object UnknownError : PepperException()
    object NetworkError : PepperException()
}
