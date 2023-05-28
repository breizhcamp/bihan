package org.breizhcamp.bihan.infrastructure

import org.breizhcamp.bihan.domain.entities.Link
import org.breizhcamp.bihan.domain.use_cases.ports.LinkPort
import org.breizhcamp.bihan.infrastructure.db.model.LinkDB
import org.breizhcamp.bihan.infrastructure.db.repos.LinkRepo
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.time.Instant

@Component
class LinkAdapter(
    private val linkRepo: LinkRepo
): LinkPort {

    @Cacheable("links")
    override fun getLink(id: String): Link? {
        return linkRepo.findByLinkId(id)?.toLink()
    }

    @Transactional
    override fun addLink(url: String, expirationDate: Instant?, id: String?): Link {
        val dbId = sha256sum((expirationDate?.toString() ?: "") + "|$url")

        val linkDB = linkRepo.findByIdOrNull(dbId)?.let {
            return it.toLink()
        } ?: LinkDB(dbId, makeLinkId(id), url, expirationDate)

        return linkRepo.save(linkDB).toLink()
    }

    @CacheEvict(cacheNames = ["links"], allEntries = true)
    @Transactional
    override fun purgeLinks() {
        linkRepo.deleteByExpirationDateBefore(Instant.now())
    }

    private fun makeLinkId(id: String?): String {
        id?.let { if (linkRepo.findByLinkId(it) == null) return it }

        for (i in 0..1000) {
            val genId = (0..5).map { (('a'..'z') + ('A'..'Z')).random() }.joinToString("")
            if (linkRepo.findByLinkId(genId) == null) {
                return genId
            }
        }

        throw IllegalStateException("Unable to generate a link id after 1000 tries")
    }

    private fun sha256sum(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}

private fun LinkDB.toLink() = Link(linkId, url, expirationDate)
