package org.breizhcamp.bihan

import org.breizhcamp.bihan.config.BihanConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableConfigurationProperties(BihanConfig::class)
@EnableCaching
@EnableScheduling
class BihanApplication

fun main(args: Array<String>) {
	runApplication<BihanApplication>(*args)
}
