package com.taksapp.taksapp.data.webservices.client.resources.riders

import com.taksapp.taksapp.data.webservices.client.ConfigurationProvider
import com.taksapp.taksapp.data.webservices.client.Response
import com.taksapp.taksapp.data.webservices.client.resources.common.ErrorResponseBody
import com.taksapp.taksapp.data.webservices.client.resources.common.LocationResponseBody
import com.taksapp.taksapp.data.webservices.client.resources.common.TaxiRequestResponseBody

enum class TaxiRequestApiError {
    RIDER_HAS_NO_REGISTERED_DEVICE,
    NO_AVAILABLE_DRIVERS,
    EXISTING_ACTIVE_TAXI_REQUEST,
    SERVER_ERROR
}

enum class CancellationApiError {
    NOT_FOUND,
    SERVER_ERROR
}

enum class TaxiRequestFetchApiError {
    NOT_FOUND,
    SERVER_ERROR
}

data class TaxiRequestRequestBody(val origin: LocationResponseBody, val destination: LocationResponseBody)

class RidersTaxiRequestsResource(private val configurationProvider: ConfigurationProvider) {
    fun create(body: TaxiRequestRequestBody): Response<TaxiRequestResponseBody, TaxiRequestApiError> {
        val httpClient = configurationProvider.client
        val jsonConverter = configurationProvider.jsonConverter

        val jsonBody = jsonConverter.toJson(body)

        val response = httpClient.post("api/v1/riders/me/taxi-requests", jsonBody)
        return if (response.isSuccessful) {
            val responseBody =
                jsonConverter.fromJson(response.body?.source!!, TaxiRequestResponseBody::class)
            Response.success(responseBody)
        } else {
            if (response.code in 400..499) {
                val errorBody = jsonConverter
                    .fromJson(response.body?.source!!, ErrorResponseBody::class)
                    .errors[0]
                when (errorBody.code) {
                    "noRegisteredDevice" -> Response.failure(TaxiRequestApiError.RIDER_HAS_NO_REGISTERED_DEVICE)
                    "noAvailableDrivers" -> Response.failure(TaxiRequestApiError.NO_AVAILABLE_DRIVERS)
                    "taxiRequestActive" -> Response.failure(TaxiRequestApiError.EXISTING_ACTIVE_TAXI_REQUEST)
                    else -> Response.failure(TaxiRequestApiError.SERVER_ERROR)
                }
            } else {
                Response.failure(TaxiRequestApiError.SERVER_ERROR)
            }
        }
    }

    fun cancelCurrent(): Response<Nothing, CancellationApiError> {
        val httpClient = configurationProvider.client
        val jsonConverter = configurationProvider.jsonConverter

        val response = httpClient.patch("api/v1/riders/me/taxi-requests/current/cancel")
        return if (response.isSuccessful) {
            Response.success(null)
        } else {
            if (response.code in 400..499) {
                val errorBody = jsonConverter
                    .fromJson(response.body?.source!!, ErrorResponseBody::class)
                    .errors[0]
                when (errorBody.code) {
                    "taxiRequestNotFound" -> Response.failure(CancellationApiError.NOT_FOUND)
                    else -> Response.failure(CancellationApiError.SERVER_ERROR)
                }
            } else {
                Response.failure(CancellationApiError.SERVER_ERROR)
            }
        }
    }

    fun getCurrent(): Response<TaxiRequestResponseBody, TaxiRequestFetchApiError> {
        val httpClient = configurationProvider.client
        val jsonConverter = configurationProvider.jsonConverter

        val response = httpClient.get("api/v1/riders/me/taxi-requests/current")
        return if (response.isSuccessful) {
            val taxiRequestResponseBody = jsonConverter.fromJson(
                response.body?.source!!, TaxiRequestResponseBody::class
            )
            Response.success(taxiRequestResponseBody)
        } else {
            if (response.code in 400..499) {
                val errorBody = jsonConverter
                    .fromJson(response.body?.source!!, ErrorResponseBody::class)
                    .errors[0]
                when (errorBody.code) {
                    "taxiRequestNotFound" -> Response.failure(TaxiRequestFetchApiError.NOT_FOUND)
                    else -> Response.failure(TaxiRequestFetchApiError.SERVER_ERROR)
                }
            } else {
                Response.failure(TaxiRequestFetchApiError.SERVER_ERROR)
            }
        }
    }

    fun getById(id: String): Response<TaxiRequestResponseBody, TaxiRequestFetchApiError> {
        val httpClient = configurationProvider.client
        val jsonConverter = configurationProvider.jsonConverter

        val response = httpClient.get("api/v1/riders/me/taxi-requests/$id")
        return if (response.isSuccessful) {
            val taxiRequestResponseBody = jsonConverter.fromJson(
                response.body?.source!!, TaxiRequestResponseBody::class
            )
            Response.success(taxiRequestResponseBody)
        } else {
            if (response.code in 400..499) {
                val errorBody = jsonConverter
                    .fromJson(response.body?.source!!, ErrorResponseBody::class)
                    .errors[0]
                when (errorBody.code) {
                    "taxiRequestNotFound" -> Response.failure(TaxiRequestFetchApiError.NOT_FOUND)
                    else -> Response.failure(TaxiRequestFetchApiError.SERVER_ERROR)
                }
            } else {
                Response.failure(TaxiRequestFetchApiError.SERVER_ERROR)
            }
        }
    }
}