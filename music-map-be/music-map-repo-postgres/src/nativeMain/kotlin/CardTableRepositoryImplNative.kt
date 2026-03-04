package com.serkomma.musicmap.repo.postgres

import io.github.smyrgeorge.sqlx4k.QueryExecutor

// Реализация генерируется в actual через ksp
// Заставить ksp генерировать классы в директории nativeMain не удалось, поэтому через дополнительный класс
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object CardTableRepositoryImplNative : CardTableRepository {
    override suspend fun findOneById(
        context: QueryExecutor,
        id: Long
    ): Result<CardTable?>

    override suspend fun findAll(context: QueryExecutor): Result<List<CardTable>>

    override suspend fun findOneByIdAndDelete(
        context: QueryExecutor,
        id: Long
    ): Result<CardTable?>

    override suspend fun findAllByMatch(
        context: QueryExecutor,
        searchString: String
    ): Result<List<CardTable>>

    override suspend fun deleteAll(context: QueryExecutor): Result<Long>

    override suspend fun insert(
        context: QueryExecutor,
        entity: CardTable
    ): Result<CardTable>

    override suspend fun update(
        context: QueryExecutor,
        entity: CardTable
    ): Result<CardTable>

    override suspend fun delete(
        context: QueryExecutor,
        entity: CardTable
    ): Result<Unit>

    override suspend fun save(
        context: QueryExecutor,
        entity: CardTable
    ): Result<CardTable>

    override suspend fun batchInsert(
        context: QueryExecutor,
        entities: Iterable<CardTable>
    ): Result<List<CardTable>>

    override suspend fun batchUpdate(
        context: QueryExecutor,
        entities: Iterable<CardTable>
    ): Result<List<CardTable>>
}