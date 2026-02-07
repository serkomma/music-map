package com.serkomma.musicmap.ktor

import com.serkomma.models.CardCreateResponse
import com.serkomma.models.CardReadResponse
import com.serkomma.musicmap.app.ktor.configureRouting
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class V1StubApiTest {
    @Test
    fun create(){
        testApplication {
            application { configureRouting() }
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val response = client.post("/v1/card/create") {
                contentType(ContentType.Application.Json)
                setBody("{\n" +
                        "  \"requestType\" : \"create\",\n" +
                        "  \"debug\" : {\n" +
                        "    \"mode\" : \"stub\",\n" +
                        "    \"stub\" : \"success\"\n" +
                        "  },\n" +
                        "  \"card\" : {\n" +
                        "    \"title\" : \"title1\",\n" +
                        "    \"description\" : \"description1\",\n" +
                        "    \"message\" : \"message\",\n" +
                        "    \"genre\" : \"rock\",\n" +
                        "    \"streaming\" : [ {\n" +
                        "      \"service\" : \"SPOTIFY\",\n" +
                        "      \"link\" : \"https://spotify.com/title\"\n" +
                        "    } ],\n" +
                        "    \"place\" : {\n" +
                        "      \"latitude\" : 20.02,\n" +
                        "      \"longitude\" : 40.04\n" +
                        "    },\n" +
                        "    \"visibility\" : \"public\"\n" +
                        "  }\n" +
                        "}")
            }
            val responseObject = response.body<CardCreateResponse>()
            assertEquals(200, response.status.value)
            assertEquals("Muse", responseObject.card?.title)
        }
    }

    @Test
    fun read(){
        testApplication {
            application { configureRouting() }
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val response = client.post("/v1/card/read") {
                contentType(ContentType.Application.Json)
                setBody("{\n" +
                        "  \"requestType\" : \"read\",\n" +
                        "  \"debug\" : {\n" +
                        "    \"mode\" : \"stub\",\n" +
                        "    \"stub\" : \"success\"\n" +
                        "  },\n" +
                        "  \"card\" : {\n" +
                        "    \"id\" : 1\n" +
                        "  }\n" +
                        "}")
            }
            val responseObject = response.body<CardReadResponse>()
            assertEquals(200, response.status.value)
            assertEquals("Muse", responseObject.card?.title)
        }
    }
}