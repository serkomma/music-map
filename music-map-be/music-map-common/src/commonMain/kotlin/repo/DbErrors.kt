package com.serkomma.musicmap.common.repo

import com.serkomma.musicmap.common.exceptions.RepoConcurrencyException
import com.serkomma.musicmap.common.models.EntityLock
import com.serkomma.musicmap.common.models.MusicError

const val ERROR_GROUP_REPO = "repo"

fun <T>errorNotFound(id: Any) = DBOneResponseErr<T>(
    MusicError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: $id is not Found",
    )
)

fun <T>errorEmptyId() = DBOneResponseErr<T>(
    MusicError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun <T> errorRepoConcurrency(data: T, id: Any, oldLock: EntityLock, newLock: EntityLock) =
    DBOneResponseErrWithData(
        data = data,
        err = MusicError(
            code = "$ERROR_GROUP_REPO-concurrency",
            group = ERROR_GROUP_REPO,
            field = "lock",
            message = "The object with ID $id has been changed concurrently by another user or process",
            exception = RepoConcurrencyException(oldLock, newLock)
        )
    )
fun <T>errorNotCreated(title: Any): DBOneResponseErr<T> {
    println("not-created: $title")
    return DBOneResponseErr<T>(
        MusicError(
            code = "$ERROR_GROUP_REPO-not-created",
            group = ERROR_GROUP_REPO,
            field = "id",
            message = "Object with title: $title was not created",
        )
    )
}