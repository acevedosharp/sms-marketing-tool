package com.acevedosharp.smsmarketingtool.controllers

import com.acevedosharp.smsmarketingtool.services.sms.ISmsSenderService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class SmsController(private val smsSenderService: ISmsSenderService) {

    class SmsCampaign(
        val message: String,
        val targets: List<String>
    )

    class SmsSendStatus(
        val target: String,
        val wasSent: Boolean,
        val message: String
    ) {
        override fun toString(): String {
            return "SmsSendStatus(target='$target', wasSent=$wasSent, message='$message')"
        }
    }

    @PostMapping
    @ResponseBody
    fun createCampaign(@RequestBody smsCampaign: SmsCampaign): List<SmsSendStatus> {
        return smsSenderService.executeCampaign(
            message = smsCampaign.message,
            targets = smsCampaign.targets
        )
    }

    @GetMapping
    fun getIndex(): String {
        return "index.html"
    }
}