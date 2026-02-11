package com.serkomma.musicmap.app.kafka

import com.serkomma.musicmap.app.common.IAppSettings
import com.serkomma.musicmap.biz.MusicCardProcessor
import com.serkomma.musicmap.common.MusicCorSettings
import com.serkomma.musicmap.libs.logging.common.LoggerProvider
import loggerKermit

class AppKafkaConfig(
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val kafkaTopicInV1: String = KAFKA_TOPIC_IN_V1,
    val kafkaTopicOutV1: String = KAFKA_TOPIC_OUT_V1,
    override val corSettings: MusicCorSettings = MusicCorSettings(
        loggerProvider = LoggerProvider { loggerKermit(it) }
    ),
    override val processor: MusicCardProcessor = MusicCardProcessor(corSettings),
): IAppSettings {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_V1_VAR = "KAFKA_TOPIC_IN_V1"
        const val KAFKA_TOPIC_OUT_V1_VAR = "KAFKA_TOPIC_OUT_V1"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_VAR) ?: "").split("\\s*[,; ]\\s*") }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "musicapp" }
        val KAFKA_TOPIC_IN_V1 by lazy { System.getenv(KAFKA_TOPIC_IN_V1_VAR) ?: "musicapp-card-v1-in" }
        val KAFKA_TOPIC_OUT_V1 by lazy { System.getenv(KAFKA_TOPIC_OUT_V1_VAR) ?: "musicapp-card-v1-out" }
    }
}