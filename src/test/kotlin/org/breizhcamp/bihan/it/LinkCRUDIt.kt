package org.breizhcamp.bihan.it

import org.breizhcamp.bihan.application.dto.AddLinkRes
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody

class LinkCRUDIt: AbstractItTest() {

    @Test
    fun `should create, redirect, remove link`(
            @Autowired webClient: RestTestClient, @Value("\${bihan.api-key}") apiKey: String,
            @Value ("\${bihan.base-redirect}") baseRedirect: String,
    ) {
        val adminClient = createClient(webClient, apiKey)

        //Create Link
        val linkId = adminClient.post()
            .uri("/api/links")
            .body(mapOf("url" to "https://www.breizhcamp.org/redirect"))
            .exchange()
            .expectStatus().isCreated()
            .expectBody<AddLinkRes>()
            .returnResult()
            .responseBody!!
            .id

        //Check redirect
        webClient.get()
            .uri("/$linkId")
            .exchange()
            .expectStatus().is3xxRedirection
            .expectHeader().valueEquals("Location", "https://www.breizhcamp.org/redirect")

        //Delete Link
        adminClient.delete()
            .uri("/api/links/{id}", linkId)
            .exchange()
            .expectStatus().isNoContent()

        //Check redirect is not available anymore
        webClient.get()
            .uri("/$linkId")
            .exchange()
            .expectStatus().is3xxRedirection
            .expectHeader().valueEquals("Location", baseRedirect)
    }

}