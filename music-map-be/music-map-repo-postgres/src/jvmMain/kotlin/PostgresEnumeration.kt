package com.serkomma.musicmap.repo.postgres

import org.jetbrains.exposed.v1.core.Table
import org.postgresql.util.PGobject

inline fun <reified T : Enum<T>> Table.postgresEnumeration(
    columnName: String,
    postgresEnumName: String
) = customEnumeration(columnName, postgresEnumName,
    { value -> enumValueOf<T>(value as String) },
    { PGEnum(postgresEnumName, it) })

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}
