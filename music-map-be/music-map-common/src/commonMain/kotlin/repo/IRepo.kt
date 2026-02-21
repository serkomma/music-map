package com.serkomma.musicmap.common.repo

interface IRepo<T, ID> {
    suspend fun create(request: IDBRequest<T>): IDBOneResponse<T>
    suspend fun read(request: IDBIdRequest<ID>): IDBOneResponse<T>
    suspend fun update(request: IDBRequest<T>): IDBOneResponse<T>
    suspend fun delete(request: IDBIdRequest<ID>): IDBOneResponse<T>
    suspend fun search(request: IDBFilterRequest): IDBManyResponse<T>
    companion object {
        fun <T, ID> none() = object : IRepo<T, ID> {
            override suspend fun create(request: IDBRequest<T>): IDBOneResponse<T> {
                error("Must not be used")
            }

            override suspend fun read(request: IDBIdRequest<ID>): IDBOneResponse<T> {
                error("Must not be used")
            }

            override suspend fun update(request: IDBRequest<T>): IDBOneResponse<T> {
                error("Must not be used")
            }

            override suspend fun delete(request: IDBIdRequest<ID>): IDBOneResponse<T> {
                error("Must not be used")
            }

            override suspend fun search(request: IDBFilterRequest): IDBManyResponse<T> {
                error("Must not be used")
            }
        }
    }
}