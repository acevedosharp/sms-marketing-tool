package com.acevedosharp.smsmarketingtool

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

internal class GlobalHelpersTest {
    @Test
    fun encodeForGsm7_negativeNSegments() {
        assertThrows<IllegalStateException> {
            GlobalHelpers.encodeForGsm7("str", -1)
        }
    }

    @Test
    fun encodeForGsm7_segmentLimiting() {
        val nSegments = 3
        val result = GlobalHelpers.encodeForGsm7("s".repeat(1000), nSegments)
        assertEquals("s".repeat(3*160), result)
    }
}