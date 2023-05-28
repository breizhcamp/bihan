package org.breizhcamp.bihan.domain.use_cases

import org.breizhcamp.bihan.config.BihanConfig
import org.breizhcamp.bihan.domain.use_cases.ports.LinkPort
import org.springframework.stereotype.Service

@Service
class GetLink(
    private val bihanConfig: BihanConfig,
    private val linkPort: LinkPort,
) {

    fun get(id: String): String {
        val link = linkPort.getLink(id) ?: return bihanConfig.baseRedirect
        if (link.isExpired()) return bihanConfig.baseRedirect

        return link.url
    }

}
