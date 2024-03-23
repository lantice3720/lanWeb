package kr.kro.lanthanide.lanweb

import kr.kro.lanthanide.lanweb.data.DBManager
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class LanWebApplication

fun main(args: Array<String>) {
    runApplication<LanWebApplication>(*args)
}
