package org.breizhcamp.bihan.application.cron

import org.breizhcamp.bihan.domain.use_cases.PurgeLinks
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class PurgeLinksCron(
    private val purgeLinks: PurgeLinks,
) {

    @Scheduled(fixedDelay = 60*60, timeUnit = TimeUnit.SECONDS, initialDelay = 10)
    fun purge() = purgeLinks.purge()

}
