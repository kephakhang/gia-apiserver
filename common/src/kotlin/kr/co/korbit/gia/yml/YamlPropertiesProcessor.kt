package kr.co.korbit.gia.yml

import org.springframework.beans.factory.config.YamlProcessor
import org.springframework.core.CollectionFactory
import org.springframework.core.io.Resource
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

class YamlPropertiesProcessor(resource: Resource) : YamlProcessor() {
    @Throws(IOException::class)
    fun createProperties(): Properties {
        val result = CollectionFactory.createStringAdaptingProperties()
        process { properties, map -> result.putAll(properties) }
        return result
    }

    init {
        if (!resource.exists()) {
            throw FileNotFoundException()
        }
        setResources(resource)
    }
}