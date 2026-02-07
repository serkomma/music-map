package com.serkomma.musicmap.app.kafka

import com.serkomma.models.IRequest
import com.serkomma.models.IResponse
import com.serkomma.musicmap.api.apiRequestDeserialize
import com.serkomma.musicmap.api.apiResponseSerialize
import com.serkomma.musicmap.api.mappers.fromTransport
import com.serkomma.musicmap.api.mappers.toTransport
import com.serkomma.musicmap.common.MusicContext

class ConsumerStrategyV1 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: MusicContext): String {
        val response: IResponse = source.toTransport()
        return apiResponseSerialize(response)
    }

    override fun deserialize(value: String, target: MusicContext) {
        val request: IRequest = apiRequestDeserialize(value)
        target.fromTransport(request)
    }
}