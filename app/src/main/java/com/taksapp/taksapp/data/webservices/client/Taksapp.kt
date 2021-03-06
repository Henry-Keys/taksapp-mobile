package com.taksapp.taksapp.data.webservices.client

import com.taksapp.taksapp.data.webservices.client.httpclients.HttpClient
import com.taksapp.taksapp.data.webservices.client.jsonconverters.JsonConverter
import com.taksapp.taksapp.data.webservices.client.resources.drivers.DriversResource
import com.taksapp.taksapp.data.webservices.client.resources.fares.FaresResource
import com.taksapp.taksapp.data.webservices.client.resources.places.PlacesResource
import com.taksapp.taksapp.data.webservices.client.resources.riders.RidersResource
import com.taksapp.taksapp.data.webservices.client.resources.routes.RoutesResource
import com.taksapp.taksapp.data.webservices.client.resources.users.UsersResource

enum class Environment {
    DEVELOPMENT,
    STAGING,
    PRODUCTION,
}

interface ConfigurationProvider {
    val client: HttpClient
    val jsonConverter: JsonConverter
    val sessionStore: SessionStore
}

class Taksapp(
    val environment: Environment,
    override val sessionStore: SessionStore,
    override val client: HttpClient,
    override val jsonConverter: JsonConverter) : ConfigurationProvider {

    class Builder {
        private lateinit var environment: Environment
        private lateinit var sessionExpiryListener: SessionExpiryListener
        private lateinit var sessionStore: SessionStore
        private lateinit var client: HttpClient
        private lateinit var jsonConverter: JsonConverter

        fun environment(environment: Environment): Builder {
            this.environment = environment
            return this
        }

        fun sessionExpiredCallback(callback: SessionExpiryListener): Builder {
            this.sessionExpiryListener = callback
            return this
        }

        fun sessionStore(store: SessionStore): Builder {
            this.sessionStore = store
            return this
        }

        fun client(client: HttpClient): Builder {
            this.client = client
            return this
        }

        fun jsonConverter(jsonConverter: JsonConverter): Builder {
            this.jsonConverter = jsonConverter
            return this
        }

        fun build(): Taksapp {
            return Taksapp(
                environment = this.environment,
                sessionStore = this.sessionStore,
                client = this.client,
                jsonConverter = this.jsonConverter
            )
        }
    }

    val users = UsersResource(this, store = this.sessionStore)
    val places = PlacesResource(this)
    val routes = RoutesResource(this)
    val fares = FaresResource(this)
    val riders = RidersResource(this)
    val drivers = DriversResource(this)
}