package org.breizhcamp.bihan.application.rest

import org.breizhcamp.bihan.application.dto.AddLinkReq
import org.breizhcamp.bihan.application.dto.AddLinkRes
import org.breizhcamp.bihan.config.BihanConfig
import org.breizhcamp.bihan.domain.use_cases.AddLink
import org.breizhcamp.bihan.domain.use_cases.DeleteLink
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.io.FileNotFoundException

@RestController
class AdminCtrl(
    private val config: BihanConfig,
    private val addLink: AddLink,
    private val deleteLink: DeleteLink,
) {

    @PostMapping("/api/links")
    fun add(@RequestBody link: AddLinkReq, @RequestHeader("Authorization") auth: String?): AddLinkRes {
        if (auth != "ApiKey ${config.apiKey}") throw FileNotFoundException()
        return AddLinkRes(addLink.add(link.url, link.expirationDate?.toInstant(), link.id))
    }

    @DeleteMapping("/api/links/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: String, @RequestHeader("Authorization") auth: String?) {
        if (auth != "ApiKey ${config.apiKey}") throw FileNotFoundException()
        if (!deleteLink.delete(id)) throw FileNotFoundException()
    }

    @ExceptionHandler(FileNotFoundException::class) @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleFNFE() {}
}
