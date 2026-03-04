package com.serkomma.musicmap.common.exceptions

import com.serkomma.musicmap.common.models.EntityLock

open class RepoConcurrencyException(expectedLock: EntityLock, actualLock: EntityLock?): RepoException(
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)