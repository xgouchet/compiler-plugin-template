package com.droidcon.kcp.kotlin.fir

import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.expression.ExpressionCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirCallChecker
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension

class SafeCodeExtension(session: FirSession, messageCollector: MessageCollector) :
    FirAdditionalCheckersExtension(session) {

    override val expressionCheckers: ExpressionCheckers = object : ExpressionCheckers() {
        override val callCheckers: Set<FirCallChecker> = setOf(
            CheckCallChecker(session, messageCollector)
        )
    }

}
