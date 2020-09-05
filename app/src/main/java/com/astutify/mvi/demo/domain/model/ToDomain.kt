package com.astutify.mvi.demo.domain.model

interface ToDomain<T> {
    fun toDomain(): T
}
