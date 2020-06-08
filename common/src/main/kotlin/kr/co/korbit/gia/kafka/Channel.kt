package kr.co.korbit.gia.kafka

import com.google.gson.internal.LinkedTreeMap
import java.util.LinkedHashMap

enum class Channel(val value: String, val no: Int) {
    PUSH("push", 0)
}

typealias ChannelList = LinkedTreeMap<String, Any>