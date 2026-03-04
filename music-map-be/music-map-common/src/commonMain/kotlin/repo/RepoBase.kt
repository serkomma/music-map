package com.serkomma.musicmap.common.repo

import com.serkomma.musicmap.common.helpers.errorSystem

abstract class RepoBase<T, ID> : IRepo<T, ID> {

    protected suspend fun tryOneMethod(block: suspend () -> IDBOneResponse<T>) = try {
        block()
    } catch (e: Throwable) {
        DBOneResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryManyMethod(block: suspend () -> IDBManyResponse<T>) = try {
        block()
    } catch (e: Throwable) {
        DbManyResponseErr(errorSystem("methodException", e = e))
    }

}