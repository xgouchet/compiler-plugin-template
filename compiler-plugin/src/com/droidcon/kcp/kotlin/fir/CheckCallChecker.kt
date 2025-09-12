package com.droidcon.kcp.kotlin.fir

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.SourceElementPositioningStrategies
import org.jetbrains.kotlin.diagnostics.error0
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirCallChecker
import org.jetbrains.kotlin.fir.expressions.FirCall
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.expressions.impl.FirFunctionCallImpl
import org.jetbrains.kotlin.fir.expressions.toResolvedCallableReference
import org.jetbrains.kotlin.fir.packageFqName
import org.jetbrains.kotlin.fir.references.resolved
import org.jetbrains.kotlin.psi.KtExpression
import kotlin.math.exp

class CheckCallChecker(
    val session: FirSession,
    val messageCollector: MessageCollector
) : FirCallChecker(MppCheckerKind.Common) {

    override fun check(
        expression: FirCall,
        context: CheckerContext,
        reporter: DiagnosticReporter
    ) {
        if (expression is FirFunctionCall){
            val resolved = expression.toResolvedCallableReference(session)?.resolved
            if (resolved != null) {
                val packageFqName = resolved.resolvedSymbol.packageFqName()
                if (packageFqName.toString() == "kotlin" && resolved.name.toString() == "check")
//                messageCollector.report(
//                    CompilerMessageSeverity.ERROR,
//                    "Calling kotlin.check() will throw an exception, please don't do that!",
//                )
                reporter.reportOn(
                    expression.source,
                    CompilationErrors.CHECK_CALL,
                    context
                )
            }
        }
    }
}