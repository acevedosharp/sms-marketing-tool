package com.acevedosharp.smsmarketingtool.controllers

import com.acevedosharp.smsmarketingtool.services.sms.MockSmsSenderModes
import com.acevedosharp.smsmarketingtool.services.sms.MockSmsSenderServiceImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class SmsControllerTest {

    private val n1 = "3022175285"
    private val n2 = "3001231234"

    @Test
    fun createCampaignAllSuccessful_succeedAll() {
        val controller = SmsController(
            MockSmsSenderServiceImpl(MockSmsSenderModes.SUCCEED_ALL)
        )

        val response = controller.createCampaign(
            SmsController.SmsCampaign(
                "Hi",
                listOf(n1, n2)
            )
        )

        assertTrue(
            response.all { r -> r.wasSent },
            "Not all responses succeeded"
        )
    }

    @Test
    fun createCampaignAllSuccessful_failAll() {
        val controller = SmsController(
            MockSmsSenderServiceImpl(MockSmsSenderModes.FAIL_ALL)
        )

        val response = controller.createCampaign(
            SmsController.SmsCampaign(
                "Hi",
                listOf(n1, n2)
            )
        )

        assertTrue(
            response.none { r -> r.wasSent },
            "Not all responses failed"
        )
    }
}