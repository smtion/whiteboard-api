package com.smtion.whiteboard.router

import com.smtion.whiteboard.handler.ApiHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class ApiRouter(
    private val apiHandler: ApiHandler
) {

    @Bean
    fun apiRoutes() = nest(path("/channels"),
        router {
            GET("{code}", apiHandler::checkCode)
            DELETE("{code}", apiHandler::close)
        }
    )
}