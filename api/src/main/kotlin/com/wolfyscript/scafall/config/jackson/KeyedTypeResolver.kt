package com.wolfyscript.scafall.config.jackson

import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.jsontype.NamedType
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder
import com.wolfyscript.scafall.identifier.Keyed

@Deprecated("Should not be necessary to use anymore", level = DeprecationLevel.ERROR)
class KeyedTypeResolver : StdTypeResolverBuilder() {
    override fun buildTypeSerializer(
        config: SerializationConfig,
        baseType: JavaType,
        subtypes: Collection<NamedType>
    ): TypeSerializer? {
        return if (useForType(baseType)) super.buildTypeSerializer(config, baseType, subtypes) else null
    }

    override fun buildTypeDeserializer(
        config: DeserializationConfig,
        baseType: JavaType,
        subtypes: Collection<NamedType>
    ): TypeDeserializer? {
        return if (useForType(baseType)) super.buildTypeDeserializer(config, baseType, subtypes) else null
    }

    fun useForType(t: JavaType): Boolean {
        return t.isTypeOrSubTypeOf(Keyed::class.java)
    }
}
