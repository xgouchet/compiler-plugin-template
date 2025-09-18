package com.droidcon.kcp.kotlin

import com.droidcon.kcp.kotlin.fir.SampleFirExtensionRegistrar
import com.droidcon.kcp.kotlin.ir.SampleIrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter

class SampleCompilerPluginRegistrar : CompilerPluginRegistrar() {
    override val supportsK2: Boolean
        get() = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val messageCollector = configuration.get(
            CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY,
            MessageCollector.NONE
        )
        messageCollector.report(
            CompilerMessageSeverity.WARNING,
            "SampleCompilerPluginRegistrar:registering extensions"
        )
        val compileCheckAsErrors = configuration.get(SampleCommandLineProcessor.ARG_COMPILE_CHECK_AS_ERRORS) ?: false
        FirExtensionRegistrarAdapter.registerExtension(SampleFirExtensionRegistrar(compileCheckAsErrors))
        IrGenerationExtension.registerExtension(SampleIrGenerationExtension(messageCollector))
    }
}
