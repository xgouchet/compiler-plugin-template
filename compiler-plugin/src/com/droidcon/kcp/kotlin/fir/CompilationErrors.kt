package com.droidcon.kcp.kotlin.fir

import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.SourceElementPositioningStrategies
import org.jetbrains.kotlin.diagnostics.error0
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory
import org.jetbrains.kotlin.diagnostics.rendering.RootDiagnosticRendererFactory
import org.jetbrains.kotlin.psi.KtExpression

object CompilationErrors: BaseDiagnosticRendererFactory()  {

    val CHECK_CALL by error0<KtExpression>(SourceElementPositioningStrategies.WHOLE_ELEMENT)

    override val MAP: KtDiagnosticFactoryToRendererMap = KtDiagnosticFactoryToRendererMap("Droidcon").apply {
        put(CHECK_CALL, "check() can make your application crash and must be avoided")
    }

    init {
        RootDiagnosticRendererFactory.registerFactory(this)
    }
}