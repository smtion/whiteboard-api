package com.smtion.whiteboard.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class WhiteboardService(
    private val channelManager: ChannelManager,
) {

    fun draw(code: String, data: String) =
        channelManager.getSink(code).tryEmitNext(data)

    fun close(code: String): Mono<Void> =
        channelManager.complete(code).let { Mono.empty() }

}