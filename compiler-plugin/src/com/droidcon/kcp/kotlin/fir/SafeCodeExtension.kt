package com.droidcon.kcp.kotlin.fir

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.LanguageVersionSettingsCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.DeclarationCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.expression.ExpressionCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirFunctionCallChecker
import org.jetbrains.kotlin.fir.analysis.checkers.type.TypeCheckers
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension

class SafeCodeExtension(session: FirSession, val compileCheckAsErrors: Boolean) :
    FirAdditionalCheckersExtension(session) {

    override val declarationCheckers: DeclarationCheckers
        get() = super.declarationCheckers

    override val expressionCheckers: ExpressionCheckers = object : ExpressionCheckers() {
        override val functionCallCheckers: Set<FirFunctionCallChecker> = setOf(
            UnsafeCallChecker(session, compileCheckAsErrors)
        )
    }

    override val typeCheckers: TypeCheckers
        get() = super.typeCheckers

    override val languageVersionSettingsCheckers: LanguageVersionSettingsCheckers
        get() = super.languageVersionSettingsCheckers
}
