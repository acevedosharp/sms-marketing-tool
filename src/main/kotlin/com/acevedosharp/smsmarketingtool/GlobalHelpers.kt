package com.acevedosharp.smsmarketingtool

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import kotlin.math.min

object GlobalHelpers {
    private val gsm7Mappings = mapOf(
        'á' to 'a',
        'é' to 'e',
        'í' to 'i',
        'ó' to 'o',
        'ú' to 'u',

        'Á' to 'A',
        'É' to 'E',
        'Í' to 'I',
        'Ó' to 'O',
        'Ú' to 'U'
    )

    fun encodeForGsm7(str: String, nSegments: Int = 0): String {
        val encodedString = String(
            str.map { char ->
                return@map gsm7Mappings[char] ?: char
            }.toCharArray()
        )


        if (nSegments > 0)
            return encodedString.substring(0, min(nSegments*160, str.length))
        else if (nSegments == 0)
            return encodedString
        else
            throw IllegalStateException("Illegal number of segments for SMS message")
    }

    fun Any.toJsonStringJackson(): String {
        val ow: ObjectWriter = ObjectMapper().writer().withDefaultPrettyPrinter()
        return ow.writeValueAsString(this)
    }
}