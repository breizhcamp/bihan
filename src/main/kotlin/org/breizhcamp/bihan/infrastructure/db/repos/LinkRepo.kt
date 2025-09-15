package org.breizhcamp.bihan.infrastructure.db.repos

import org.breizhcamp.bihan.infrastructure.db.model.LinkDB
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface LinkRepo: JpaRepository<LinkDB, String> {

    fun findByLinkId(linkId: String): LinkDB?

    fun deleteByExpirationDateBefore(expirationDate: Instant): Long
}
