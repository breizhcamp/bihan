package org.breizhcamp.bihan.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bihan")
data class BihanConfig(
    // if no id or invalid id provided, redirect to this url
    val baseRedirect: String
)
