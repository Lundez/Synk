package com.londogard.synk

import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>): Unit = Synk().subcommands(ServerCommands(), ClientCommands()).main(args)