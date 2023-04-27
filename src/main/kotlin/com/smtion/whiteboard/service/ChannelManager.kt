package com.smtion.whiteboard.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Sinks

@Service
class ChannelManager {

    private val sinkMap = mutableMapOf<String, Sinks.Many<String>>()

    private fun getOrCreate(key: String): Sinks.Many<String> {
        if (!sinkMap.containsKey(key)) {
            sinkMap[key] = Sinks.many().replay().latest()
            println(">> Create a sink for $key")
        }

        return sinkMap[key]!!
    }

    fun init(key: String) = this.getOrCreate(key)

    fun getSink(key: String) = this.getOrCreate(key)

    fun getFlux(key: String) = this.getOrCreate(key).asFlux()

    fun hasSink(key: String) = sinkMap.containsKey(key)

    fun complete(key: String) {
        sinkMap[key]?.tryEmitComplete()
        sinkMap.remove(key)
    }
}