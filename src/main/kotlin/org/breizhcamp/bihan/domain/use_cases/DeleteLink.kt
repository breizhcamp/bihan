package org.breizhcamp.bihan.domain.use_cases

import org.breizhcamp.bihan.domain.use_cases.ports.LinkPort
import org.springframework.stereotype.Service

@Service
class DeleteLink(
    private val linkPort: LinkPort,
) {

    fun delete(id: String): Boolean = linkPort.delete(id)
}
