package kr.co.korbit.gia.yml

import java.io.IOException


class YamlPropertySourceFactory : org.springframework.core.io.support.PropertySourceFactory {
    @Throws(IOException::class)
    override fun createPropertySource(
        name: String?,
        resource: org.springframework.core.io.support.EncodedResource
    ): org.springframework.core.env.PropertySource<*> {
        val filename: String = resource.getResource().getFilename().toString()
        if (filename.endsWith(YML_FILE_EXTENSION)) {
            return name?.let { YamlResourcePropertySource(it, resource) }
                ?: YamlResourcePropertySource(
                    getNameForResource(resource.getResource()),
                    resource
                )
        }
        return if (name != null) org.springframework.core.io.support.ResourcePropertySource(
            name,
            resource
        ) else org.springframework.core.io.support.ResourcePropertySource(resource)
    }

    private fun getNameForResource(resource: org.springframework.core.io.Resource): String {
        var name: String = resource.getDescription()
        if (!org.springframework.util.StringUtils.hasText(name)) {
            name = resource.javaClass.getSimpleName() + "@" + System.identityHashCode(resource)
        }
        return name
    }

    companion object {
        private const val YML_FILE_EXTENSION = ".yml"
    }
}