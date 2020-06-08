package kr.co.korbit.gia.kafka

import java.time.Instant

data class KafkaEvent(val type: Int , val timestamp: Instant, val data: Any)
