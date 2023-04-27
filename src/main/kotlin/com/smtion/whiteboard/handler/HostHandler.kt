package com.smtion.whiteboard.handler

import com.smtion.whiteboard.service.ChannelManager
import com.smtion.whiteboard.service.WhiteboardService
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class HostHandler(
    private val channelManager: ChannelManager,
    private val whiteboardService: WhiteboardService,
) : WebSocketHandler {

    override fun handle(session: WebSocketSession): Mono<Void> {
        val code = session.attributes["code"] as String
        channelManager.init(code)
        println("[$code] Created")

        // Receive a message from client
        session.receive()
            .subscribe(
                { whiteboardService.draw(code, it.payloadAsText) },
                { error: Throwable -> println("Error: $error") },
                { println("[$code] WebSocketSession is closed") }
            )

        // Send message to client
        return session.send(channelManager.getFlux(code).map { data ->
            session.textMessage(data)
        })
    }
}
