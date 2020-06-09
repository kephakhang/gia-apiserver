package kr.co.korbit.gia

import BackgroundJob
import kr.co.korbit.gia.config.*
import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.kafka.Channel
import kr.co.korbit.gia.kafka.KafkaEventService
import kr.co.korbit.gia.kafka.buildConsumer
import kr.co.korbit.gia.kafka.buildProducer
import kr.co.korbit.gia.util.Firebase
import org.apache.kafka.clients.producer.KafkaProducer
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.ApplicationPidFileWriter
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Import
import java.util.*
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@SpringBootApplication
@Import(value = [JpaKorbitConfig::class, JpaKorbitApiConfig::class, JpaTerraConfig::class, SwaggerConfig::class, WebMvcConfig::class])
class Application {
    companion object {

        val consumerJobs: ArrayList<BackgroundJob> = ArrayList<BackgroundJob>()
        val kafkaEventServices: ArrayList<KafkaEventService> = ArrayList<KafkaEventService>()
        lateinit var app: SpringApplication
        lateinit var appContext: ApplicationContext


        @JvmStatic fun main(args: Array<String>) {
            TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"))
            Env.branch = Env.appConfig.config("app.deployment").property("branch").toString()
            appContext = runApplication<Application>(*args)
        }

        fun initFirebase(context: ApplicationContext) {
            Firebase.geInstance(context)
        }

        @PostConstruct
        fun started() {
            var applicable = Env.appConfig.config("app.deployment").property("applicable").toString()
            if (System.getenv("APPLICABLE") != null) {
                applicable = System.getenv("APPLICABLE")
            }

            // Appicable="false" 이면 Consumer 를 띄우지 않는다.
            if (applicable.equals("true")) {

                initFirebase(appContext)

                val topics: List<String> = Env.appConfig.config("app.kafka.consumer").property("topics").getList()
                val kafkaEventProducer: KafkaProducer<String, Any> = buildProducer<String, Any>()
                for(topic: String in topics) {
                    val kafkaEventService = KafkaEventService(topic, kafkaEventProducer)
                    kafkaEventServices.add(kafkaEventService)
                }

                for (channel in Channel.values()) {
                    val topic: String = channel.value
                    val name = "Gia-Kafka-Consumer-" + topic
                    val job = buildConsumer<String, Any>(topic)
                    val consumerJob = BackgroundJob(job, name)
                    Application.consumerJobs.add(consumerJob)
                    consumerJob.run()
                }
            }
        }

        @PreDestroy
        fun onExit() {

            for(x in 1 .. Application.consumerJobs.size) {
                Application.consumerJobs[x-1].close()
            }
        }
    }
}

