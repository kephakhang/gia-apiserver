package kr.co.korbit.gia.kafka

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

class KafkaEventService(private val topic: String, private val kafkaProducer: KafkaProducer<String, Any>) {
    //var counter : AtomicLong = AtomicLong(0)
    suspend fun CoroutineScope.sendEvent(event: KafkaEvent) =
        kafkaProducer.dispatch(ProducerRecord<String, Any>(topic, "korbit", event))

    fun send(event: KafkaEvent) {
        GlobalScope.launch {
            sendEvent(event)
        }
    }
}