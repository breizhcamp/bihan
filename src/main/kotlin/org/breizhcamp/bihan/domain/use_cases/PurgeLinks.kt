package org.breizhcamp.bihan.domain.use_cases

import org.breizhcamp.bihan.domain.use_cases.ports.LinkPort
import org.springframework.stereotype.Service

@Service
class PurgeLinks(
    private val linkPort: LinkPort,
) {

    fun purgeLinks() = linkPort.purgeLinks()

}