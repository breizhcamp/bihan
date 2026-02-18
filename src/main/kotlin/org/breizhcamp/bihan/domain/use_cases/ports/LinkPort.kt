package org.breizhcamp.bihan.domain.use_cases.ports

import org.breizhcamp.bihan.domain.entities.Link
import java.time.Instant

interface LinkPort {

    fun get(id: String): Link?
    fun add(url: String, expirationDate: Instant?, id: String?): Link
    fun delete(id: String): Boolean

    fun purge(): Long
}
