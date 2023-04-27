package com.smtion.whiteboard.config

import com.smtion.whiteboard.service.ChannelManager
import com.smtion.whiteboard.handler.HostHandler
import com.smtion.whiteboard.handler.MemberHandler
import com.smtion.whiteboard.service.WhiteboardService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.HandlerResult
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.WebSocketService
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebSession
import org.springframework.web.util.UriTemplate
import reactor.core.publisher.Mono

@Configuration
class WebSocketConfig {

    @Bean
    fun webSocketMapping(
        channelManager: ChannelManager,
        whiteboardService: WhiteboardService,
    ): HandlerMapping {
        val map = mapOf(
            "/ws/host/{code}" to HostHandler(channelManager, whiteboardService),
            "/ws/member/{code}" to MemberHandler(channelManager, whiteboardService),
        )
        val order = 10

        return SimpleUrlHandlerMapping(map, order)
    }

    @Bean
    fun handlerAdapter(): WebSocketHandlerAdapter =
        object : WebSocketHandlerAdapter(webSocketService()) {
            override fun handle(exchange: ServerWebExchange, handler: Any): Mono<HandlerResult> {
                val path = exchange.request.path.toString()

                when (path.split("/")[1]) {
                    "ws" -> {
                        exchange.session.subscribe { session: WebSession ->
                            val urlTemplate = UriTemplate("{code}")
                            val params = urlTemplate.match(exchange.getAttribute<String>(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString())
                            session.attributes["code"] = params["code"]
                        }
                    }
                }

                return super.handle(exchange, handler)
            }
        }

    @Bean
    fun webSocketService(): WebSocketService {
        val webSocketService = HandshakeWebSocketService()
        webSocketService.setSessionAttributePredicate { it in listOf("code") }

        return webSocketService
    }
}