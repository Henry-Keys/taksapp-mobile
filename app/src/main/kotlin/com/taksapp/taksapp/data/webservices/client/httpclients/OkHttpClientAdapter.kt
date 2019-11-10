package com.taksapp.taksapp.data.webservices.client.httpclients

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
class OkHttpClientAdapter(override val host: String, timeout: Duration) : HttpClient {
    private val httpClient = OkHttpClient.Builder()
            .connectTimeout(10, unit = TimeUnit.SECONDS)
            .writeTimeout(timeout.toLongMilliseconds(), unit = TimeUnit.MILLISECONDS)
            .readTimeout(timeout.toLongMilliseconds(), unit = TimeUnit.MILLISECONDS)
            .build()

    init {
        require(host.isNotBlank())
        require(host.endsWith("/"))
    }

    override fun get(url: String): HttpResponse {
        val request = Request.Builder()
            .url("$host$url")
            .get()
            .build()

        val response = httpClient.newCall(request).execute()

        return HttpResponse(
            code = response.code,
            body = HttpResponse.HttpResponseBody(response.body?.byteStream())
        )
    }

    override fun post(url: String, body: String): HttpResponse {
        val request = Request.Builder()
            .url("$host$url")
            .post(body.toRequestBody("application/json; charset=utf-8".toMediaType()))
            .build()

        val response = httpClient.newCall(request).execute()

        return HttpResponse(
            code = response.code,
            body = HttpResponse.HttpResponseBody(response.body?.byteStream())
        )
    }

    override fun post(url: String, formDataBody: Map<String, String>): HttpResponse {
        val bodyBuilder = FormBody.Builder()

        for (entry in formDataBody.entries) {
            bodyBuilder.add(entry.key, entry.value)
        }

        val request = Request.Builder()
            .url("$host$url")
            .post(bodyBuilder.build())
            .build()
        val response = httpClient.newCall(request).execute()

        return HttpResponse(
            code = response.code,
            body = HttpResponse.HttpResponseBody(response.body?.byteStream())
        )
    }
}