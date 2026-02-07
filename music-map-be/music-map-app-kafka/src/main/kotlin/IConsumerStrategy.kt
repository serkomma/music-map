package com.serkomma.musicmap.app.kafka

import com.serkomma.musicmap.common.MusicContext

interface IConsumerStrategy {
    /**
     * Топики, для которых применяется стратегия
     */
    fun topics(config: AppKafkaConfig): InputOutputTopics
    /**
     * Сериализатор для версии API
     */
    fun serialize(source: MusicContext): String
    /**
     * Десериализатор для версии API
     */
    fun deserialize(value: String, target: MusicContext)
}