package org.breizhcamp.bihan.domain.use_cases.ports

import org.breizhcamp.bihan.domain.entities.Link
import java.time.Instant

interface LinkPort {

    fun getLink(id: String): Link?

    fun addLink(url: String, expirationDate: Instant?): Link

    fun purgeLinks()
}