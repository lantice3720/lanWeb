package kr.kro.lanthanide.lanweb

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.*
import java.util.stream.Stream

@RestController
class RestController {
    @GetMapping("/")
    fun main(): Flux<String> {
        return Flux.just("Hello", "World!")
    }

    @GetMapping("stream")
    fun stream(): Flux<Map<String, Int>> {
        val stream = Stream.iterate(0) {i -> i+1}
        Flux.interval(Duration.ofSeconds(1))
        return (Flux.fromStream(stream).zipWith(Flux.interval(Duration.ofSeconds(1)))
            .map {i -> Collections.singletonMap("value", i.t1)})
    }

}
