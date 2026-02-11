package com.serkomma.musicmap.app.kafka

data class InputOutputTopics(
    /**
     * Топик входящих в приложение сообщений
     */
    val input: String,
    /**
     * Топик для исходящих из приложения сообщений
     */
    val output: String,
)
