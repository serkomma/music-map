package com.serkomma.musicmap.common.repo

import com.serkomma.musicmap.common.models.MusicError

sealed interface IDBManyResponse<T>

data class DbManyResponseOk<T>(
    val data: List<T>
): IDBManyResponse<T>

@Suppress("unused")
data class DbManyResponseErr<T>(
    val errors: List<MusicError> = emptyList()
): IDBManyResponse<T> {
    constructor(err: MusicError): this(listOf(err))
}