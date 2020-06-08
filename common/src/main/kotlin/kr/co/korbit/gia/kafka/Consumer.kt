package kr.co.korbit.gia.kafka

import kr.co.korbit.exception.stackTraceString
import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.jpa.common.PushMsg
import kr.co.korbit.gia.util.ClosableJob
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.errors.WakeupException
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

private val logger = KotlinLogging.logger {}

class Consumer<K, V>(private val consumer: KafkaConsumer<K, V>, val topic: String) : ClosableJob {
    private val closed: AtomicBoolean = AtomicBoolean(false)
    private var finished = CountDownLatch(1)

    init {
        consumer.subscribe(listOf(topic))
    }

    override fun run() {
        try {
            while (!closed.get()) {
                try {
                    val records = consumer.poll(Duration.of(1000, ChronoUnit.MILLIS))
                    for (record in records) {
                        logger.debug(Env.message("app.kafka.consumer.record") ,record.topic(), record.partition(), record.offset(), record.key(), record.value())
                        logger.debug("record.value() : " + record.value().toString())

                        if( (Env.branch == "master" && record.key().toString() != Env.korbit) || record.value() is Throwable ) { // DeSerialization 오류시 Exception Return from consumer
                            logger.warn(Env.message("app.kafka.consumer.unknownRecord"),record.value() as Throwable)
                            Thread.sleep(1000)
                            continue
                        }

                        when (topic) {
                            Channel.PUSH.value -> {
                                val push: PushMsg = record.value() as PushMsg
                                
                            }
                        }
                    }
                    if (!records.isEmpty) {
                        consumer.commitAsync { offsets, exception ->
                            if (exception != null) {
                                logger.error(exception){ Env.message("app.kafka.consumer.commit.fail") + offsets }
                            } else {
                                logger.debug(Env.message("app.kafka.consumer.commit.success"), offsets)
                            }
                        }
                    }
                } catch(e: Throwable) {
                    logger.error(Env.message("app.kafka.consumer.unknownRecord") + e.stackTraceString)
                    Thread.sleep(1000)
                }
            }
            logger.info { Env.message("app.kafka.consumer.finish") }
        } catch (e: Throwable) {
            when (e) {
                is WakeupException -> logger.info { Env.message("app.kafka.consumer.wakeup") }
                else -> logger.error(e) { Env.message("app.kafka.consumer.pollingFail") }
            }
        } finally {
            logger.info { Env.message("app.kafka.consumer.commit.sync") }
            consumer.commitSync()
            consumer.close()
            finished.countDown()
            logger.info { Env.message("app.kafka.consumer.closed") }
        }
    }

    override fun close() {
        logger.info { Env.message("app.kafka.consumer.jobClose") }
        closed.set(true)
        consumer.wakeup()
        finished.await(3000, TimeUnit.MILLISECONDS)
        logger.info { Env.message("app.kafka.consumer.jobClosed") }
    }
}


fun <K, V> buildConsumer(topic: String): Consumer<K, V> {
    val consumerConfig = Env.appConfig.config("app.kafka.consumer")
    val consumerProps = Properties().apply {
        this[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = consumerConfig.property("bootstrap.servers").getString().split(",")
        this[ConsumerConfig.CLIENT_ID_CONFIG] = topic + "-" + consumerConfig.property("client.id").getString()
        this[ConsumerConfig.GROUP_ID_CONFIG] = consumerConfig.property("group.id").getString()
        this[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = consumerConfig.property("key.deserializer").getString()
        this[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = consumerConfig.property("value.deserializer").getString()
        this[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "latest"
//        this["ssl.truststore.location"] = consumerConfig.property("ssl.truststore.location").getString()
//        this["ssl.truststore.password"] = consumerConfig.property("ssl.truststore.password").getString()
//        this["ssl.keystore.location"] = consumerConfig.property("ssl.keystore.location").getString()
//        this["ssl.keystore.password"] = consumerConfig.property("ssl.keystore.password").getString()
//        this["ssl.key.password"] = consumerConfig.property("ssl.key.password").getString()
//        this["security.protocol"] = consumerConfig.property("security.protocol").getString()
//        this["ssl.endpoint.identification.algorithm"] = consumerConfig.property("ssl.endpoint.identification.algorithm").getString()
    }
    return Consumer(KafkaConsumer(consumerProps), topic)
}