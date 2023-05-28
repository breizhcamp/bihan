package org.breizhcamp.bihan.infrastructure.db.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity @Table(name = "link")
data class LinkDB(
    @Id
    val id: String,
    val linkId: String,
    val url: String,
    val expirationDate: Instant?,
)