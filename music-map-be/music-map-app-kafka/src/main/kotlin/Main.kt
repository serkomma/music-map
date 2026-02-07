package com.serkomma.musicmap.app.kafka

fun main() {
    val config = AppKafkaConfig()
    val consumer = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()))
    consumer.start()
}