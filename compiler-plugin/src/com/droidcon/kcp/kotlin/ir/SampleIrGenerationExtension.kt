package com.droidcon.kcp.kotlin.ir

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid

class SampleIrGenerationExtension(
    private val messageCollector: MessageCollector,
) : IrGenerationExtension {

    override fun generate(
        moduleFragment: IrModuleFragment,
        pluginContext: IrPluginContext
    ) {
        messageCollector.report(CompilerMessageSeverity.WARNING, "generate")

        val transformers = listOf(
            LogMethodCallVisitor(pluginContext, messageCollector)
        )

        for (transformer in transformers) {
            messageCollector.report(
                CompilerMessageSeverity.WARNING,
                "apply transformer ${transformer.javaClass.simpleName}"
            )
            moduleFragment.acceptChildrenVoid(transformer)
        }
    }

}