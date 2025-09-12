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
import org.jetbrains.kotlin.resolve.extensions.ExtraImportsProviderExtension

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

        FirExtensionRegistrarAdapter.registerExtension(
            SampleFirExtensionRegistrar(messageCollector)
        )
        IrGenerationExtension.registerExtension(
            SampleIrGenerationExtension(messageCollector)
        )
        ExtraImportsProviderExtension.registerExtension(
            KotlinImportExtension()
        )
    }
}
