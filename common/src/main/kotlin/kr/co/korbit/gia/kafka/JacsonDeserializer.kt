package kr.co.korbit.gia.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import kr.co.korbit.exception.stackTraceString
import kr.co.korbit.gia.jpa.common.PushMsg
import mu.KotlinLogging
import org.apache.kafka.common.serialization.Deserializer
import java.nio.charset.StandardCharsets

private val logger = KotlinLogging.logger {}

class JacksonDeserializer(
    private val objectMapper: ObjectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerModule(KotlinModule())
) : Deserializer<Any?> {


    override fun deserialize(topic: String, data: ByteArray): Any {
        try {

            when (topic) {
                Channel.PUSH.value -> {
                    val pushMsg: PushMsg = objectMapper.readValue<PushMsg>(data)
                    logger.debug { "kafka data : " + String(data, StandardCharsets.UTF_8) }
                    return pushMsg
                }
                else -> throw Exception("unknown topic data format")
            }

        } catch (e: Throwable) {
            logger.warn{ "deserializing JSON message error : " + e.stackTraceString}
            return e
        }
    }
}