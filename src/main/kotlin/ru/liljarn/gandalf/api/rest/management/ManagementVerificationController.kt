package ru.liljarn.gandalf.api.rest.management

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import ru.liljarn.gandalf.support.reflection.ManagementApi

private const val MANAGEMENT_TOKEN = "MANAGEMENT_TOKEN"

@ManagementApi
@RestController
@RequestMapping("/api/v1/management/verification")
class ManagementVerificationController {

    @PostMapping
    fun verifyManager(@RequestBody token: ManagementToken) {
        if (token.token != MANAGEMENT_TOKEN) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong token")
        }
    }
}

data class ManagementToken(
    val token: String,
)
