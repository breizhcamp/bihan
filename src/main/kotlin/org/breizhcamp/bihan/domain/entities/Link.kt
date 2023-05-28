package org.breizhcamp.bihan.domain.entities

import java.time.Instant

data class Link(
    val id: String,
    val url: String,
    val expirationDate: Instant?,
) {
    fun isExpired(): Boolean {
        return expirationDate?.isBefore(Instant.now()) ?: false
    }
}
