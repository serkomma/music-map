package com.serkomma.musicmap.common.repo

import com.serkomma.musicmap.common.models.MusicError

sealed interface IDBOneResponse<T>

data class DBOneResponseOk<T>(
    val data: T
): IDBOneResponse<T>

data class DBOneResponseErr<T>(
    val errors: List<MusicError> = emptyList()
): IDBOneResponse<T> {
    constructor(err: MusicError): this(listOf(err))
}

data class DBOneResponseErrWithData<T>(
    val data: T,
    val errors: List<MusicError> = emptyList()
): IDBOneResponse<T> {
    constructor(data: T, err: MusicError): this(data, listOf(err))
}