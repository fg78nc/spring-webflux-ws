package com.fg7.reactivewebkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveWebKotlinApplication

fun main(args: Array<String>) {
    runApplication<ReactiveWebKotlinApplication>(*args)
}
