package org.breizhcamp.bihan.application.rest

import org.breizhcamp.bihan.application.dto.AddLinkReq
import org.breizhcamp.bihan.application.dto.AddLinkRes
import org.breizhcamp.bihan.config.BihanConfig
import org.breizhcamp.bihan.domain.use_cases.AddLink
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.io.FileNotFoundException

@RestController
class AdminCtrl(
    private val config: BihanConfig,
    private val addLink: AddLink,
) {

    @PostMapping("/api/links")
    fun add(@RequestBody link: AddLinkReq, @RequestHeader("Authorization") auth: String?): AddLinkRes {
        if (auth != "ApiKey ${config.apiKey}") throw FileNotFoundException()
        return AddLinkRes(addLink.add(link.url, link.expirationDate?.toInstant(), link.id))
    }

    @ExceptionHandler(FileNotFoundException::class) @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleFNFE() {}
}