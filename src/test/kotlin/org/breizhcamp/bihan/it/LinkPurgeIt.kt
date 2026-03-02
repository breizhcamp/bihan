package org.breizhcamp.bihan.it

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.web.servlet.client.RestTestClient
import java.time.ZonedDateTime

class LinkPurgeIt: AbstractItTest() {

    @Test
    fun `should create, redirect, remove link`(
            @Autowired webClient: RestTestClient, @Value("\${bihan.api-key}") apiKey: String,
            @Value ("\${bihan.base-redirect}") baseRedirect: String,
    ) {
        val adminClient = createClient(webClient, apiKey)
        val now = ZonedDateTime.now()

        //Create Link with late expiration date to be sure it is not purged before test end
        adminClient.post()
            .uri("/api/links")
            .body(mapOf("id" to "noPurge", "url" to "https://www.breizhcamp.org/exist", "expirationDate" to now.plusSeconds(30)))
            .exchange()
            .expectStatus().isCreated()

        // Create Link with past expiration date to be sure it is purged
        adminClient.post()
            .uri("/api/links")
            .body(mapOf("id" to "purge", "url" to "https://www.breizhcamp.org/noExists", "expirationDate" to now.minusSeconds(30)))
            .exchange()
            .expectStatus().isCreated()

        // Launch a purge
        adminClient.delete()
            .uri("/api/links")
            .exchange()
            .expectStatus().isNoContent()

        //Check redirect of the still exising link
        webClient.get()
            .uri("/noPurge")
            .exchange()
            .expectStatus().is3xxRedirection
            .expectHeader().valueEquals("Location", "https://www.breizhcamp.org/exist")

        //Check redirect of the purged link
        webClient.get()
            .uri("/purge")
            .exchange()
            .expectStatus().is3xxRedirection
            .expectHeader().valueEquals("Location", baseRedirect)
    }

}