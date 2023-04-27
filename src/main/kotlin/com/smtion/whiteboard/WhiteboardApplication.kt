package com.smtion.whiteboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WhiteboardApplication

fun main(args: Array<String>) {
	runApplication<WhiteboardApplication>(*args)
}
