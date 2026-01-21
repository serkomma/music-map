package ru.otus.otuskotlin.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test

class Test1{

    fun fibonacci(n: Int): Long {
        return when {
            n <= 2 -> n.toLong()
            else -> fibonacci(n - 1) + fibonacci(n - 2)
        }
    }

    @Test
    fun test(): Unit {
        val start = System.currentTimeMillis()
        val result1 = fibonacci(46)
        val result2 = fibonacci(46)
        val end = System.currentTimeMillis()
        println("result: $result1, result2: $result2, ${end - start} ms")
    }

    @Test
    fun test2() = runBlocking {
        val start = System.currentTimeMillis()
        val result1Future = CoroutineScope(Dispatchers.Default).async {
            fibonacci(46)
        }
        val result2Future = CoroutineScope(Dispatchers.Default).async {
            fibonacci(46)
        }
        val result1 = result1Future.await()
        val result2 = result2Future.await()
        val end = System.currentTimeMillis()
        println("result: $result1, result2: $result2, ${end - start} ms")
    }

    @Test
    fun test3() = runBlocking {
        var count = 0
        val start = System.currentTimeMillis()
        val coroutineList: MutableList<Deferred<Long>> = mutableListOf()
        for (i in 1..10){
            coroutineList.add(async(Dispatchers.Default + Fibonacci(46)) { // Передача значения через контекст
                count += 1
                fibonacci(this.coroutineContext[Fibonacci]?.n ?: 0)
            })
        }
        val results = coroutineList.awaitAll()
        val end = System.currentTimeMillis()
        println("results: ${results.joinToString()}, ${end - start} ms, count: $count")
    }

    @Test
    fun test4() = runBlocking {
        var count = 0
        val normCount: AtomicInteger = AtomicInteger(0)
        val start = System.currentTimeMillis()
        val coroutineList: MutableList<Job> = mutableListOf()
        for (i in 1..100000){
            coroutineList.add(launch(Dispatchers.Default) {
                count += 1
                normCount.getAndIncrement()
            })
        }
        val results = coroutineList.joinAll()
        val end = System.currentTimeMillis()
        println("${end - start} ms, count: $count, normCount: $normCount")
    }
}

data class Fibonacci(val n: Int): AbstractCoroutineContextElement(Companion) {
    companion object : CoroutineContext.Key<Fibonacci>
}