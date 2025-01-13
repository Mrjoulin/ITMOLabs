package com.joulin.dragonservice.controllers

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/swagger-ui")
class SwaggerUiController {
    @GetMapping
    fun swaggerUi(request: HttpServletRequest): String {
        return "redirect:/swagger-ui/index.html"
    }
}
