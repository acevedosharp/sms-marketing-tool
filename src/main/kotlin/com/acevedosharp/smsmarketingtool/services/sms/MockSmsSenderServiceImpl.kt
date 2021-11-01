package com.acevedosharp.smsmarketingtool.services.sms

import com.acevedosharp.smsmarketingtool.controllers.SmsController
import kotlin.random.Random

class MockSmsSenderServiceImpl(private val mode: MockSmsSenderModes) : ISmsSenderService {
    override fun executeCampaign(message: String, targets: List<String>): List<SmsController.SmsSendStatus> {
        return when (mode) {
            MockSmsSenderModes.SUCCEED_ALL -> {
                targets.map { target ->
                    return@map SmsController.SmsSendStatus(
                        target,
                        true,
                        "Ok"
                    )
                }
            }
            MockSmsSenderModes.FAIL_ALL -> {
                targets.map { target ->
                    return@map SmsController.SmsSendStatus(
                        target,
                        false,
                        "Nope"
                    )
                }
            }
            MockSmsSenderModes.RANDOM -> {
                targets.map { target ->
                    val isSuccessful = Random.nextBoolean()
                    return@map SmsController.SmsSendStatus(
                        target,
                        isSuccessful,
                        if (isSuccessful) "Ok" else "Nope"
                    )
                }
            }
        }
    }
}