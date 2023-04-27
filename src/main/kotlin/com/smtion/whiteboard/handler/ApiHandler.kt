package com.smtion.whiteboard.handler

import com.smtion.whiteboard.service.ChannelManager
import com.smtion.whiteboard.service.WhiteboardService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class ApiHandler(
    private val whiteboardService: WhiteboardService,
    private val channelManager: ChannelManager,
) {

    fun checkCode(req: ServerRequest): Mono<ServerResponse> =
        channelManager.hasSink(req.pathVariable("code"))
            .let { ok().body(it.toMono()) }

    fun close(req: ServerRequest): Mono<ServerResponse> =
        ok().body(whiteboardService.close(req.pathVariable("code")))
}