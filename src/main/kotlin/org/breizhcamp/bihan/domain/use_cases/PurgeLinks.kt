package org.breizhcamp.bihan.domain.use_cases

import io.github.oshai.kotlinlogging.KotlinLogging
import org.breizhcamp.bihan.domain.use_cases.ports.LinkPort
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class PurgeLinks(
    private val linkPort: LinkPort,
) {

    fun purge() {
        logger.info { "Purging links" }
        val nbPurge = linkPort.purge()
        logger.info { "Purging [${nbPurge}] links done" }
    }

}