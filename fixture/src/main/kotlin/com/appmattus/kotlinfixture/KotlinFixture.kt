/*
 * Copyright 2020 Appmattus Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("MatchingDeclarationName")

package com.appmattus.kotlinfixture

import com.appmattus.kotlinfixture.config.Configuration
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

/**
 * Used to generate random values and sequences for a given types.
 */
class Fixture @JvmOverloads constructor(val fixtureConfiguration: Configuration = ConfigurationBuilder().build()) {

    /**
     * Create an instance of [T] with the option to override any [configuration] specifically for this instance. If a
     * [range] is provided then a value from that is chosen.
     */
    inline operator fun <reified T : Any?> invoke(
        range: Iterable<T> = emptyList(),
        noinline configuration: ConfigurationBuilder.() -> Unit = {}
    ): T {
        val rangeShuffled = range.shuffled()
        return if (rangeShuffled.isNotEmpty()) {
            rangeShuffled.first()
        } else {
            @Suppress("DEPRECATION_ERROR")
            create(typeOf<T>(), ConfigurationBuilder(fixtureConfiguration).apply(configuration).build()) as T
        }
    }

    /**
     * Create a sequence of instances of [T] with the option to override any [configuration] for all instances
     * generated by this sequence.
     */
    inline fun <reified T : Any?> asSequence(
        noinline configuration: ConfigurationBuilder.() -> Unit = {}
    ): Sequence<T> {
        val type = typeOf<T>()
        val builtConfiguration = ConfigurationBuilder(fixtureConfiguration).apply(configuration).build()
        return sequence {
            while (true) {
                @Suppress("DEPRECATION_ERROR")
                yield(create(type, builtConfiguration) as T)
            }
        }
    }

    /**
     * Create an instance of [clazz] with the option to override any [configuration] specifically for this instance.
     */
    @JvmOverloads
    fun create(clazz: Class<*>, configuration: Configuration = fixtureConfiguration): Any? {
        @Suppress("DEPRECATION_ERROR")
        return create(clazz.kotlin, configuration)
    }

    @Deprecated("Use the fixture<Class>() function", level = DeprecationLevel.ERROR)
    fun create(clazz: KClass<*>, configuration: Configuration = fixtureConfiguration): Any? {
        @Suppress("DEPRECATION_ERROR")
        return create(clazz.starProjectedType, configuration)
    }

    @Deprecated("Use the fixture<Class>() function", level = DeprecationLevel.ERROR)
    fun create(type: KType, configuration: Configuration = fixtureConfiguration): Any? {
        val result = ContextImpl(configuration).resolve(type)
        if (result is Unresolved) {
            throw UnsupportedOperationException("Unable to handle $type\n$result")
        }
        return result
    }

    /**
     * Generate a new [Fixture] with overridden configuration.
     */
    fun new(configuration: ConfigurationBuilder.() -> Unit = {}): Fixture {
        return Fixture(ConfigurationBuilder(fixtureConfiguration).apply(configuration).build())
    }
}

/**
 * Create a [Fixture] with [configuration]
 */
fun kotlinFixture(configuration: ConfigurationBuilder.() -> Unit = {}) =
    Fixture(ConfigurationBuilder().apply(configuration).build())

inline fun <reified T> fixture(
    range: Iterable<T> = emptyList(),
    noinline configuration: ConfigurationBuilder.() -> Unit = {}
) = kotlinFixture().invoke(range, configuration)
