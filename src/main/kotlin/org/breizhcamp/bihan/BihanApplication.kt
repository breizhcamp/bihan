package org.breizhcamp.bihan

import org.breizhcamp.bihan.config.BihanConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(BihanConfig::class)
class BihanApplication

fun main(args: Array<String>) {
	runApplication<BihanApplication>(*args)
}
