package kr.kro.lanthanide.lanweb

import kr.kro.lanthanide.lanweb.data.getPostList
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class RestController {
    @GetMapping("/hello")
    fun main(): Flux<String> {
            return Flux.just("Hello", "World!")
    }

    @GetMapping("/api/blog/post_list")
    fun get(): Flux<String> {
        return Flux.just(getPostList())
    }
}
