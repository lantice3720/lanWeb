package kr.kro.lanthanide.lanweb.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

val config = Json.decodeFromString<ConfigJson>(File("./config.json").readText())
@Serializable
data class ConfigJson(val dbURI: String, val dbUser: String, val dbPassword: String)

@Serializable
data class DBAllDocs(val total_rows: Int, val offset: Int, val rows: List<DBAllDocsRow>)

@Serializable
data class DBAllDocsRow(val id: String, val key: String, val value: DBRev)

@Serializable
data class DBChanges(val results: List<DBChangeResult>, val last_seq: String, val pending: String)

@Serializable
data class DBChangeResult(val seq: String, val id: String, val changes: List<DBRev>)

@Serializable
data class DBRev(val rev: String)