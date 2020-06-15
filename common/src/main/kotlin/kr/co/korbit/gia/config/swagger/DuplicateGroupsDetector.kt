package kr.co.korbit.gia.config.swagger

import springfox.documentation.spi.service.DocumentationPlugin
import java.util.*
import java.util.stream.Collectors
import java.util.stream.StreamSupport
import kotlin.collections.LinkedHashMap


class DuplicateGroupsDetector private constructor() {
    companion object {
        @Throws(IllegalStateException::class)
        fun ensureNoDuplicateGroups(allPlugins: List<DocumentationPlugin?>) {
            val plugins: Map<String, List<DocumentationPlugin>> =
                allPlugins.stream()
                    .collect(
                        Collectors.groupingBy(
                            { input: DocumentationPlugin ->
                                Optional.ofNullable(
                                    input.groupName
                                ).orElse("default")
                            } as ((DocumentationPlugin?) -> String)?,
                            { LinkedHashMap() },
                            Collectors.toList()
                        )
                    )
            val duplicateGroups: Iterable<String> = plugins.entries.stream()
                .filter { input: Map.Entry<String, List<DocumentationPlugin>> -> input.value.size > 1 }
                .map { input -> input.key }
                .collect(Collectors.toList())
            check(StreamSupport.stream(duplicateGroups.spliterator(), false).count() <= 0) {
                String.format(
                    "Multiple Dockets with the same group name are not supported. "
                            + "The following duplicate groups were discovered. %s",
                    java.lang.String.join(",", duplicateGroups)
                )
            }
        }
    }
}