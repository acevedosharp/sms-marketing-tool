package com.acevedosharp.smsmarketingtool.services

import com.acevedosharp.smsmarketingtool.GlobalHelpers
import com.acevedosharp.smsmarketingtool.GlobalHelpers.toJsonStringJackson
import com.acevedosharp.smsmarketingtool.controllers.SmsController
import com.fasterxml.jackson.databind.ObjectMapper
import org.asynchttpclient.AsyncHttpClient
import org.asynchttpclient.Dsl
import org.asynchttpclient.Param
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SmsSenderService {
    @Value("\${cellvoz.accountNumber}")
    private lateinit var accountNumber: String

    @Value("\${cellvoz.accountPassword}")
    private lateinit var password: String

    @Value("\${cellvoz.apiKey}")
    private lateinit var apiKey: String

    private val httpClient: AsyncHttpClient = Dsl.asyncHttpClient()
    private lateinit var token: String

    @Scheduled(fixedRate = 72_000_000L)
    fun scheduledTokenUpdate() {
        loginAndUpdateToken()
    }

    private data class LoginBody(
        val account: String,
        val password: String
    )

    private fun loginAndUpdateToken() {
        val loginRequestObject = LoginBody(
            accountNumber,
            password
        )

        val future = Dsl.asyncHttpClient()
            .preparePost("https://api.cellvoz.co/v2/auth/login")
            .addHeader("Content-Type", "application/json")
            .setBody(loginRequestObject.toJsonStringJackson())
            .execute()

        val response = future.get()

        val mapper = ObjectMapper()
        val newToken = mapper.readTree(response.responseBody)
            .get("token").asText()

        token = newToken
    }

    fun executeCampaign(message: String, targets: List<String>): List<SmsController.SmsSendStatus> {
        val futures = targets.map { target: String ->
            return@map httpClient
                .prepareGet("https://api.cellvoz.co/v2/sms/single")
                .addQueryParams(
                    listOf(
                        Param("apiKey", apiKey),
                        Param("account", accountNumber),
                        Param("password", password),
                        Param("message", GlobalHelpers.encodeForGsm7(message)),
                        Param("number", "57${target}"),
                        Param("type", "1")
                    )
                )
                .execute()
        }
        while (true) {
            if (futures.all { future -> future.isDone })
                break
        }

        val responses = futures.map { future -> future.get() }

        val mapper = ObjectMapper()
        val sendStatuses = responses.map { response ->
            val jsonNode = mapper.readTree(response.responseBody)
            val number = jsonNode.get("number").asText()
            val isSuccessful = jsonNode.get("success").asBoolean()
            val jsonMessage = jsonNode.get("message").asText()

            return@map SmsController.SmsSendStatus(
                number,
                isSuccessful,
                jsonMessage
            )
        }

        return sendStatuses
    }
}