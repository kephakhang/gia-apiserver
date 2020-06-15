package kr.co.korbit.gia.config.swagger

import com.fasterxml.classmate.ResolvedType
import com.fasterxml.classmate.TypeResolver
import com.fasterxml.classmate.types.ResolvedArrayType
import com.fasterxml.classmate.types.ResolvedObjectType
import com.fasterxml.classmate.types.ResolvedPrimitiveType
import org.springframework.plugin.core.PluginRegistry
import org.springframework.stereotype.Component
import springfox.documentation.schema.*
import springfox.documentation.schema.Collections
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.schema.EnumTypeDeterminer
import springfox.documentation.spi.schema.TypeNameProviderPlugin
import springfox.documentation.spi.schema.contexts.ModelContext
import java.lang.reflect.Type
import java.util.*


@Component
class TypeNameExtractorReplaced(
    private val typeResolver: TypeResolver,
    private val typeNameProviders: PluginRegistry<TypeNameProviderPlugin, DocumentationType>,
    private val enumTypeDeterminer: EnumTypeDeterminer
) :
    TypeNameExtractor(typeResolver, typeNameProviders, enumTypeDeterminer) {
    override fun typeName(context: ModelContext): String {
        val type = asResolved(context.type)
        return if (Collections.isContainerType(type)) {
            Collections.containerType(type)
        } else innerTypeName(type, context)
    }

    private fun asResolved(type: Type): ResolvedType {
        return typeResolver.resolve(type)
    }

    private fun genericTypeName(
        resolvedType: ResolvedType,
        context: ModelContext
    ): String {
        val erasedType = resolvedType.erasedType
        val namingStrategy = context.genericNamingStrategy
        val nameContext =
            ModelNameContext(resolvedType.erasedType, context.documentationType)
        val simpleName =
            Optional.ofNullable(Types.typeNameFor(erasedType))
                .orElse(typeName(nameContext))
        val sb = StringBuilder(String.format("%s%s", simpleName, namingStrategy.openGeneric))
        var first = true
        for (index in erasedType.typeParameters.indices) {
            val typeParam = resolvedType.typeParameters[index]
            if (first) {
                sb.append(innerTypeName(typeParam, context))
                first = false
            } else {
                sb.append(
                    String.format(
                        "%s%s", namingStrategy.typeListDelimiter,
                        innerTypeName(typeParam, context)
                    )
                )
            }
        }
        sb.append(namingStrategy.closeGeneric)
        return sb.toString()
    }

    private fun innerTypeName(type: ResolvedType, context: ModelContext): String {
        return if (type.typeParameters.size > 0 && type.erasedType.typeParameters.size > 0) {
            genericTypeName(type, context)
        } else simpleTypeName(type, context)
    }

    private fun simpleTypeName(type: ResolvedType, context: ModelContext): String {
        val erasedType = type.erasedType
        if (type is ResolvedPrimitiveType) {
            return Types.typeNameFor(erasedType)
        } else if (enumTypeDeterminer.isEnum(erasedType)) {
            return "string"
        } else if (type is ResolvedArrayType) {
            val namingStrategy = context.genericNamingStrategy
            return String.format(
                "Array%s%s%s", namingStrategy.openGeneric,
                simpleTypeName(type.getArrayElementType(), context), namingStrategy.closeGeneric
            )
        } else if (type is ResolvedObjectType) {
            val typeName = Types.typeNameFor(erasedType)
            if (typeName != null) {
                return typeName
            }
        }
        return typeName(ModelNameContext(type.erasedType, context.documentationType))
    }

    private fun typeName(context: ModelNameContext): String {
        val selected =
            typeNameProviders.getPluginOrDefaultFor(context.documentationType, DefaultTypeNameProvider())
        return selected.nameFor(context.type)
    }

}