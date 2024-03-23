package kr.kro.lanthanide.lanweb.data

import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import io.netty.resolver.DefaultAddressResolverGroup
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.logging.log4j.LogManager
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.Disposable
import reactor.netty.http.client.HttpClient

object DBManager {

    private val logger = LogManager.getLogger(this)

    private var sslContext : SslContext = SslContextBuilder.forClient()
        .trustManager(InsecureTrustManagerFactory.INSTANCE)
        .build()
    private var client: WebClient = WebClient.builder()
        .clientConnector(ReactorClientHttpConnector(
            HttpClient.create()
                .secure {t -> t.sslContext(sslContext)}
                .resolver(DefaultAddressResolverGroup.INSTANCE)
        ))
        .build()
    private lateinit var nookWorkshopPostList: DBAllDocs
    private lateinit var lastSequence: String
    private lateinit var disposable: Disposable
    init {
//        client = HttpClient.newBuilder()
//            .authenticator(object : Authenticator() {
//                override fun getPasswordAuthentication(): PasswordAuthentication {
//                    return PasswordAuthentication(config.dbUser, config.dbPassword.toCharArray())
//                }
//            })
//            .build()

        CoroutineScope(Dispatchers.Default).launch {
            setupUpdateSubscriber()
        }
    }
    suspend fun getNookWorkshopPostList(): String {
        if (!::nookWorkshopPostList.isInitialized) updateNookWorkshopPostList()

        return Json.encodeToString(nookWorkshopPostList)
    }

    private suspend fun updateNookWorkshopPostList() {
        logger.info("updating")
        val response = withContext(Dispatchers.IO) {
            client.get()
                .uri(config.dbURI + "/nook_workshop/_all_docs")
                .headers {headers -> headers.setBasicAuth(config.dbUser, config.dbPassword)}
                .retrieve()
                .bodyToMono(String::class.java)
                .block()
            }

        nookWorkshopPostList = response?.let { Json.decodeFromString<DBAllDocs>(it) }!!
        println(disposable.isDisposed)
    }

    private suspend fun setupUpdateSubscriber() {
        disposable = withContext(Dispatchers.IO) {
            client.get()
                .uri(config.dbURI + "/nook_workshop/_changes?feed=continuous&since=now&timeout=60000")
                .headers {headers -> headers.setBasicAuth(config.dbUser, config.dbPassword)}
                .retrieve()
                .bodyToFlux(String::class.java)
                .subscribe(::updateSubscriber)
        }
        println("bb")

        delay(60000)
        setupUpdateSubscriber()
//        val jsonResponse = Json.decodeFromString<DBChangeResult>(response)
//        lastSequence = jsonResponse.seq
//        println("last seq: $lastSequence")
    }

    private fun updateSubscriber(value: String?) {
        if (disposable.isDisposed) CoroutineScope(Dispatchers.Default).launch { setupUpdateSubscriber() }
        println(value)
    }
}

