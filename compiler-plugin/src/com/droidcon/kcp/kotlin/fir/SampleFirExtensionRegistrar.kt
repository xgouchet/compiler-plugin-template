package com.droidcon.kcp.kotlin.fir

import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class SampleFirExtensionRegistrar(
    val messageCollector: MessageCollector
) : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +FirAdditionalCheckersExtension.Factory {
            SafeCodeExtension(it, messageCollector)
        }
    }
}