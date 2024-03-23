package kr.kro.lanthanide.lanweb

import kr.kro.lanthanide.lanweb.data.DBManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class RestController {
    @GetMapping("/hello")
    fun main(): Flux<String> {
            return Flux.just("Hello", "World!")
    }

    @GetMapping("/api/blog/post_list")
    suspend fun get(): ResponseEntity<String> {
        return ResponseEntity(DBManager.getNookWorkshopPostList(), HttpStatus.OK)
    }
}
