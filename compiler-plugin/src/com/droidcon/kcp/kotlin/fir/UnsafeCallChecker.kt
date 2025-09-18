package com.droidcon.kcp.kotlin.fir

import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirFunctionCallChecker
import org.jetbrains.kotlin.fir.declarations.hasAnnotation
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.expressions.toResolvedCallableReference
import org.jetbrains.kotlin.fir.packageFqName
import org.jetbrains.kotlin.fir.references.resolved
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class UnsafeCallChecker(
    val session: FirSession,
    val compileTimeChecksAsErrors: Boolean = false
) : FirFunctionCallChecker(MppCheckerKind.Common) {

    context(context: CheckerContext, reporter: DiagnosticReporter)
    override fun check(expression: FirFunctionCall) {
        val isAllowed = context.annotationContainers.any {
            it.hasAnnotation(allowUnsafeAnnotationClass, session)
        }
        if (isAllowed) {
            return
        }
        val resolved = expression.toResolvedCallableReference(session)?.resolved
        if (resolved != null) {
            val packageFqName = resolved.resolvedSymbol.packageFqName()
            val functionFqName = "$packageFqName.${resolved.name}"
            val diagnostic = if (compileTimeChecksAsErrors) {
                unsafeKotlinMethodsErrors[functionFqName]
            } else {
                unsafeKotlinMethodsWarnings[functionFqName]
            }
            if (diagnostic != null) {
                reporter.reportOn(expression.source, diagnostic)
            }
        }
    }

    companion object {
        val allowUnsafeAnnotationClass = ClassId(
            packageFqName = FqName.fromSegments(listOf("com", "droidcon", "kcp")),
            topLevelName = Name.identifier("AllowUnsafeCalls")
        )

        val unsafeKotlinMethodsErrors = mapOf(
            "kotlin.check" to CompilationDiagnosticErrors.CHECK_CALL,
            "kotlin.checkNotNull" to CompilationDiagnosticErrors.CHECK_NOT_NULL_CALL,
            "kotlin.require" to CompilationDiagnosticErrors.REQUIRE_CALL,
            "kotlin.requireNotNull" to CompilationDiagnosticErrors.REQUIRE_NOT_NULL_CALL,
        )
        val unsafeKotlinMethodsWarnings = mapOf(
            "kotlin.check" to CompilationDiagnosticWarnings.CHECK_CALL,
            "kotlin.checkNotNull" to CompilationDiagnosticWarnings.CHECK_NOT_NULL_CALL,
            "kotlin.require" to CompilationDiagnosticWarnings.REQUIRE_CALL,
            "kotlin.requireNotNull" to CompilationDiagnosticWarnings.REQUIRE_NOT_NULL_CALL,
        )
    }
}