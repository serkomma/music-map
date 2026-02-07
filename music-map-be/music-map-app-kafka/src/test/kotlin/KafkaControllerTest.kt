import com.serkomma.models.CardCreateObject
import com.serkomma.models.CardCreateRequest
import com.serkomma.models.CardCreateResponse
import com.serkomma.models.CardDebug
import com.serkomma.models.CardRequestDebugMode
import com.serkomma.models.CardRequestDebugStubs
import com.serkomma.models.CardVisibility
import com.serkomma.musicmap.api.apiRequestSerialize
import com.serkomma.musicmap.api.apiResponseDeserialize
import com.serkomma.musicmap.app.kafka.AppKafkaConfig
import com.serkomma.musicmap.app.kafka.AppKafkaConsumer
import com.serkomma.musicmap.app.kafka.ConsumerStrategyV1
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Collections
import kotlin.collections.set
import kotlin.test.Test
import kotlin.test.assertEquals

class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiRequestSerialize(
                        CardCreateRequest(
                            card = CardCreateObject(
                                title = "Muse",
                                description = "Good",
                                visibility = CardVisibility.PRIVATE,
                            ),
                            debug = CardDebug(
                                mode = CardRequestDebugMode.STUB,
                                stub = CardRequestDebugStubs.SUCCESS,
                            ),
                        ),
                    )
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiResponseDeserialize<CardCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("Muse", result.card?.title)
    }

    companion object {
        const val PARTITION = 0
    }
}