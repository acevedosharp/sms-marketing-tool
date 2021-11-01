package com.acevedosharp.smsmarketingtool.services.sms

import com.acevedosharp.smsmarketingtool.controllers.SmsController

interface ISmsSenderService {
    fun executeCampaign(message: String, targets: List<String>): List<SmsController.SmsSendStatus>
}