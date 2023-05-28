package org.breizhcamp.bihan.domain.use_cases

import org.breizhcamp.bihan.domain.use_cases.ports.LinkPort
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class AddLink(
    private val linkPort: LinkPort,
) {

    fun add(url: String, expirationDate: Instant?, id: String?): String = linkPort.addLink(url, expirationDate, id).id

}