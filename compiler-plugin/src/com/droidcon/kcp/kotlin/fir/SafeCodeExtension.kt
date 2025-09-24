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

    override val typeCheckers: TypeCheckers = TypeCheckers.EMPTY

    override val declarationCheckers: DeclarationCheckers = DeclarationCheckers.EMPTY

    override val expressionCheckers: ExpressionCheckers =
        object : ExpressionCheckers() {
            override val functionCallCheckers: Set<FirFunctionCallChecker> = setOf(
                UnsafeCallChecker(session, compileCheckAsErrors)
            )
        }


    override val languageVersionSettingsCheckers: LanguageVersionSettingsCheckers
        get() = super.languageVersionSettingsCheckers
}
