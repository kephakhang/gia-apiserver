package kr.co.korbit.gia.config.swagger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.plugin.core.PluginRegistry
import org.springframework.stereotype.Component
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.schema.contexts.ModelContext
import springfox.documentation.spi.service.*
import springfox.documentation.spi.service.contexts.*
import springfox.documentation.spring.web.SpringGroupingStrategy
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager
import springfox.documentation.spring.web.scanners.ApiListingScanningContext
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors


@Component
class DocumentationPluginsManagerReplaced : DocumentationPluginsManager() {
    @Autowired
    @Qualifier("documentationPluginRegistry")
    private val documentationPlugins: PluginRegistry<DocumentationPlugin, DocumentationType>? = null

    @Autowired
    @Qualifier("apiListingBuilderPluginRegistry")
    private val apiListingPlugins: PluginRegistry<ApiListingBuilderPlugin, DocumentationType>? = null

    @Autowired
    @Qualifier("parameterBuilderPluginRegistry")
    private val parameterPlugins: PluginRegistry<ParameterBuilderPlugin, DocumentationType>? = null

    @Autowired
    @Qualifier("expandedParameterBuilderPluginRegistry")
    private val parameterExpanderPlugins: PluginRegistry<ExpandedParameterBuilderPlugin, DocumentationType>? = null

    @Autowired
    @Qualifier("operationBuilderPluginRegistry")
    private val operationBuilderPlugins: PluginRegistry<OperationBuilderPlugin, DocumentationType>? = null

    @Autowired
    @Qualifier("resourceGroupingStrategyRegistry")
    private val resourceGroupingStrategies: PluginRegistry<ResourceGroupingStrategy, DocumentationType>? = null

    @Autowired
    @Qualifier("operationModelsProviderPluginRegistry")
    private val operationModelsProviders: PluginRegistry<OperationModelsProviderPlugin, DocumentationType>? = null

    @Autowired
    @Qualifier("defaultsProviderPluginRegistry")
    private val defaultsProviders: PluginRegistry<DefaultsProviderPlugin, DocumentationType>? = null

    @Autowired
    @Qualifier("pathDecoratorRegistry")
    private val pathDecorators: PluginRegistry<PathDecorator, DocumentationContext>? = null

    @Autowired
    @Qualifier("apiListingScannerPluginRegistry")
    private val apiListingScanners: PluginRegistry<ApiListingScannerPlugin, DocumentationType>? = null

    @Throws(IllegalStateException::class)
    override fun documentationPlugins(): Iterable<DocumentationPlugin> {
        val plugins = documentationPlugins!!.plugins
        DuplicateGroupsDetector.ensureNoDuplicateGroups(plugins)
        return if (plugins.isEmpty()) {
            setOf(defaultDocumentationPlugin())
        } else plugins
    }

    override fun parameter(parameterContext: ParameterContext): Parameter {
        for (each in parameterPlugins!!.getPluginsFor(parameterContext.documentationType)) {
            each.apply(parameterContext)
        }
        return parameterContext.parameterBuilder().build()
    }

    override fun expandParameter(context: ParameterExpansionContext): Parameter {
        for (each in parameterExpanderPlugins!!.getPluginsFor(context.documentationType)) {
            each.apply(context)
        }
        return context.parameterBuilder.build()
    }

    override fun operation(operationContext: OperationContext): Operation {
        for (each in operationBuilderPlugins!!.getPluginsFor(operationContext.documentationType)) {
            each.apply(operationContext)
        }
        return operationContext.operationBuilder().build()
    }

    override fun apiListing(context: ApiListingContext): ApiListing {
        for (each in apiListingPlugins!!.getPluginsFor(context.documentationType)) {
            each.apply(context)
        }
        return context.apiListingBuilder().build()
    }

    override fun modelContexts(context: RequestMappingContext): Set<ModelContext> {
        val documentationType = context.documentationContext.documentationType
        for (each in operationModelsProviders!!.getPluginsFor(documentationType)) {
            each.apply(context)
        }
        return context.operationModelsBuilder().build()
    }

    override fun resourceGroupingStrategy(documentationType: DocumentationType): ResourceGroupingStrategy {
        return resourceGroupingStrategies!!.getPluginOrDefaultFor(documentationType, SpringGroupingStrategy())
    }

    private fun defaultDocumentationPlugin(): DocumentationPlugin {
        return Docket(DocumentationType.SWAGGER_2)
    }

    fun createContextBuilder(
        documentationType: DocumentationType,
        defaultConfiguration: DefaultsProviderPlugin
    ): DocumentationContextBuilder {
        return defaultsProviders!!.getPluginOrDefaultFor(documentationType, defaultConfiguration)
            .create(documentationType)
            .withResourceGroupingStrategy(resourceGroupingStrategy(documentationType))
    }

    override fun decorator(context: PathContext): Function<String, String>? {
        return Function { input: String ->
            val decorators: MutableList<Any>? =
                pathDecorators!!.getPluginsFor(context.documentationContext()).stream()
                    .map { each: PathDecorator -> each.decorator(context) }
                    .collect(Collectors.toList<Any>())
            var decorated: String = input
            if (decorators != null) {
                for (decorator: Any in decorators) {
                    decorated = decorator.apply{ decorated } as String
                }
            }
            decorated
        }
    }

    override fun additionalListings(context: ApiListingScanningContext): Collection<ApiDescription> {
        val documentationType = context.documentationContext.documentationType
        val additional: MutableList<ApiDescription> = ArrayList()
        for (each in apiListingScanners!!.getPluginsFor(documentationType)) {
            additional.addAll(each.apply(context.documentationContext))
        }
        return additional
    }
}

