package org.breizhcamp.bihan.it

import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.client.RestTestClient
import org.testcontainers.junit.jupiter.Testcontainers

@Import(ItContainers::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@Testcontainers
@AutoConfigureRestTestClient
abstract class AbstractItTest {

    fun createClient(webClient: RestTestClient, apiKey: String): RestTestClient =
        webClient.mutate()
            .defaultHeader("Authorization", "ApiKey $apiKey")
            .build()


}
