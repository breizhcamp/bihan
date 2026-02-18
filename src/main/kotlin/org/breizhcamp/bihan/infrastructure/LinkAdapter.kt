package org.breizhcamp.bihan.infrastructure

import org.breizhcamp.bihan.domain.entities.Link
import org.breizhcamp.bihan.domain.use_cases.ports.LinkPort
import org.breizhcamp.bihan.infrastructure.db.model.LinkDB
import org.breizhcamp.bihan.infrastructure.db.repos.LinkRepo
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.time.Instant

@Component
class LinkAdapter(
    private val linkRepo: LinkRepo,
    private val cacheManager: CacheManager,
): LinkPort {

    @Cacheable("links", key = "#id")
    override fun get(id: String): Link? {
        return linkRepo.findByLinkId(id)?.toLink()
    }

    @Transactional
    override fun add(url: String, expirationDate: Instant?, id: String?): Link {
        val dbId = sha256sum((expirationDate?.toString() ?: "") + "|$url")

        val linkDB = linkRepo.findByIdOrNull(dbId)?.let {
            return it.toLink()
        } ?: LinkDB(dbId, makeLinkId(id), url, expirationDate)

        val res = linkRepo.save(linkDB).toLink()
        cacheManager.getCache("links")?.put(res.id, res)
        return res
    }

    @Transactional
    @CacheEvict(cacheNames = ["links"], key = "#id")
    override fun delete(id: String): Boolean {
        return linkRepo.deleteByLinkId(id) > 0
    }

    @CacheEvict(cacheNames = ["links"], allEntries = true)
    @Transactional
    override fun purge(): Long {
        return linkRepo.deleteByExpirationDateBefore(Instant.now()) ?: 0
    }

    private fun makeLinkId(id: String?): String {
        id?.let { if (!linkRepo.existsByLinkId(it)) return it }

        for (i in 0..1000) {
            val genId = (0..5).map { (('a'..'z') + ('A'..'Z')).random() }.joinToString("")
            if (!linkRepo.existsByLinkId(genId)) {
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
