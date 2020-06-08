package kr.co.korbit.gia

import BackgroundJob
import kr.co.korbit.gia.config.*
import kr.co.korbit.gia.env.Env
import kr.co.korbit.gia.interceptor.SecurityInterceptor
import kr.co.korbit.gia.kafka.Channel
import kr.co.korbit.gia.kafka.KafkaEventService
import kr.co.korbit.gia.kafka.buildConsumer
import kr.co.korbit.gia.kafka.buildProducer
import kr.co.korbit.gia.util.UnifiedArgumentResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.ApplicationPidFileWriter
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.servlet.handler.MappedInterceptor
import java.util.*
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@SpringBootApplication
@Import(value = [JpaKorbitConfig::class, JpaKorbitApiConfig::class, JpaTerraConfig::class, SwaggerConfig::class, WebMvcConfig::class])
class Application {
    companion object {

        val consumerJobs: ArrayList<BackgroundJob> = ArrayList<BackgroundJob>()


        @Autowired
        var securityInterceptor: SecurityInterceptor? = null



        @Bean
        fun myMappedInterceptor(): MappedInterceptor? {
            return MappedInterceptor(
                arrayOf("/**"), arrayOf("/resource/**"),
                Application.securityInterceptor
            )
        }

        @JvmStatic fun main(args: Array<String>) {
            TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"))
            runApplication<Application>(*args)
            val app = SpringApplication(Application::class.java)
            app.addListeners(ApplicationPidFileWriter())
            app.run(*args)
        }

        @PostConstruct
        fun started() {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
            var applicable = Env.appConfig.config("app.deployment").property("applicable").toString()
            if (System.getenv("APPLICABLE") != null) {
                applicable = System.getenv("APPLICABLE")
            }
            Env.branch = Env.appConfig.config("app.deployment").property("branch").toString()

            val topic = Env.appConfig.config("app.kafka.consumer").property("topic").getString()
            val kafkaEventProducer = buildProducer<String, Any>()
            val kafkaEventService = KafkaEventService(topic, kafkaEventProducer)


            // Appicable="false" 이면 Consumer 를 띄우지 않는다.
            if (applicable.equals("true")) {
                for (channel in Channel.values()) {
                    val topic: String = channel.value
                    val name = "Kafka-Gia-Consumer-" + topic
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

