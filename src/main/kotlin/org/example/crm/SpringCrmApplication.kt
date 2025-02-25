package org.example.crm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringCrmApplication

fun main(args: Array<String>) {
    runApplication<SpringCrmApplication>(*args)
}
