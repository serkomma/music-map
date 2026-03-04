package com.serkomma.musicmap.repo.common
/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class RepoInitialized<T, R>(
    val repo: IRepoInitializable<T, R>,
    initObjects: Collection<T> = emptyList(),
) : IRepoInitializable<T, R> by repo {
    @Suppress("unused")
    val initializedObjects: List<T> = save(initObjects).toList()
}