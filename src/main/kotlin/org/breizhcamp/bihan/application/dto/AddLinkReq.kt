package org.breizhcamp.bihan.application.dto

import java.time.ZonedDateTime

data class AddLinkReq(
    val url: String,
    val expirationDate: ZonedDateTime?
)
