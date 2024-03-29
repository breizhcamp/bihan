package org.breizhcamp.bihan.application.cron

import mu.KotlinLogging
import org.breizhcamp.bihan.domain.use_cases.PurgeLinks
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}

@Component
class PurgeLinksCron(
    private val purgeLinks: PurgeLinks,
) {

    @Scheduled(fixedDelay = 60*60, timeUnit = TimeUnit.SECONDS, initialDelay = 10)
    fun purge() {
        logger.info { "Purging links" }
        purgeLinks.purgeLinks()
        logger.info { "Purging links done" }
    }

}