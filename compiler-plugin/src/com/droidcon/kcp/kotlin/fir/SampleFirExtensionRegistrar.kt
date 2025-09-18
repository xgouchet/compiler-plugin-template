package com.droidcon.kcp.kotlin.fir

import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class SampleFirExtensionRegistrar(val compileCheckAsErrors: Boolean) : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        FirAdditionalCheckersExtension.Factory { SafeCodeExtension(it, compileCheckAsErrors) }.unaryPlus()
    }
}