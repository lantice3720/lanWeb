package kr.kro.lanthanide.lanweb.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

val config = Json.decodeFromString<ConfigJson>(File("/config.json").readText())
@Serializable
data class ConfigJson(val dbURI: String, val dbUser: String, val dbPassword: String)

@Serializable
data class DBWelcome(val couchdb: String)