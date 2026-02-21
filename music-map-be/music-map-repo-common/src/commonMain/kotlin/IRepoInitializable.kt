package com.serkomma.musicmap.repo.common

import com.serkomma.musicmap.common.repo.IRepo

interface IRepoInitializable<T, ID>: IRepo<T, ID> {
    fun save(data: Collection<T>): Collection<T>
}