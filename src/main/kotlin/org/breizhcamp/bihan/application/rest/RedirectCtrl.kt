package org.breizhcamp.bihan.application.rest

import org.breizhcamp.bihan.domain.use_cases.GetLink
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class RedirectCtrl(
    private val getLink: GetLink
) {

    @GetMapping("/{id}")
    fun redirect(@PathVariable id: String): String {
        return "redirect:${getLink.get(id)}"
    }

}