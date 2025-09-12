package com.droidcon.kcp.kotlin.utils

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector

fun MessageCollector.warn(message: String) {
    report(
        severity = CompilerMessageSeverity.WARNING,
        message = message,
        location = null
    )
}