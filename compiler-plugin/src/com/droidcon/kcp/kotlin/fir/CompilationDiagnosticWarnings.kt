package com.droidcon.kcp.kotlin.fir

import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.SourceElementPositioningStrategies
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory
import org.jetbrains.kotlin.diagnostics.rendering.RootDiagnosticRendererFactory
import org.jetbrains.kotlin.diagnostics.warning0
import org.jetbrains.kotlin.psi.KtExpression

object CompilationDiagnosticWarnings : BaseDiagnosticRendererFactory() {

    val CHECK_CALL by warning0<KtExpression>()
    val CHECK_NOT_NULL_CALL by warning0<KtExpression>()

    val REQUIRE_CALL by warning0<KtExpression>()
    val REQUIRE_NOT_NULL_CALL by warning0<KtExpression>()

    override val MAP: KtDiagnosticFactoryToRendererMap = KtDiagnosticFactoryToRendererMap("SampleErrors").apply {
        put(CHECK_CALL, "check() can make your application crash and must be avoided")
        put(CHECK_NOT_NULL_CALL, "checkNotNull() can make your application crash and must be avoided")
        put(REQUIRE_CALL, "require() can make your application crash and must be avoided")
        put(REQUIRE_NOT_NULL_CALL, "requireNotNull() can make your application crash and must be avoided")
    }

    init {
        RootDiagnosticRendererFactory.registerFactory(this)
    }
}