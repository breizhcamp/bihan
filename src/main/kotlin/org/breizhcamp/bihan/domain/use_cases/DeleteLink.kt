package org.breizhcamp.bihan.domain.use_cases

import io.github.oshai.kotlinlogging.KotlinLogging
import org.breizhcamp.bihan.domain.use_cases.ports.LinkPort
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class DeleteLink(
    private val linkPort: LinkPort,
) {

    fun delete(id: String): Boolean {
        logger.info { "Deleting link [$id]" }
        return linkPort.delete(id)
    }
}
