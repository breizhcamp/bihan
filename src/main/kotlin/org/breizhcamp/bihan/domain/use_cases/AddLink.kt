package org.breizhcamp.bihan.domain.use_cases

import io.github.oshai.kotlinlogging.KotlinLogging
import org.breizhcamp.bihan.domain.use_cases.ports.LinkPort
import org.springframework.stereotype.Service
import java.time.Instant

private val logger = KotlinLogging.logger {}

@Service
class AddLink(
    private val linkPort: LinkPort,
) {

    fun add(url: String, expirationDate: Instant?, id: String?): String {
        val createdId = linkPort.addLink(url, expirationDate, id).id
        logger.info { "Adding link [${createdId}] to $url" }
        return createdId
    }

}
