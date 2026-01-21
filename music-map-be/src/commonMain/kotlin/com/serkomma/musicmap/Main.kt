package com.serkomma.musicmap

import com.serkomma.musicmap.common.models.MusicCommand

fun main(args: Array<String>) {
    MusicCommand.OFFERS.name.let { println("Dependency check: $it\n") }
    println("Language version: ${getVersion()}")
//    readln()
}

expect fun getVersion(): String
