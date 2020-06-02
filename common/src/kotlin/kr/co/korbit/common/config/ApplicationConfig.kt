/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kr.co.korbit.common.config


/**
 * Represents an application config node
 */
interface ApplicationConfig {
    /**
     * Get config property with [path] or fail
     * @throws ApplicationConfigurationException
     */
    fun property(path: String): ApplicationConfigValue

    /**
     * Get config property value for [path] or return `null`
     */
    fun propertyOrNull(path: String): ApplicationConfigValue?

    /**
     * Get config child node or fail
     * @throws ApplicationConfigurationException
     */
    fun config(path: String): ApplicationConfig

    /**
     * Get a list of child nodes for [path] or fail
     * @throws ApplicationConfigurationException
     */
    fun configList(path: String): List<ApplicationConfig>
}

/**
 * Represents an application config value
 */
interface ApplicationConfigValue {
    /**
     * Get property string value
     */
    fun getString(): String

    /**
     * Get property list value
     */
    fun getList(): List<String>
}

/**
 * Thrown when an application is misconfigured
 */
class ApplicationConfigurationException(message: String) : Exception(message)
