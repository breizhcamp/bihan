package org.breizhcamp.bihan.application.rest

import org.breizhcamp.bihan.config.BihanConfig
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeCtrl(
    private val bihanConfig: BihanConfig
) {

    @GetMapping("/")
    fun home(): String = "redirect:${bihanConfig.baseRedirect}"

}