package com.droidcon.kcp.kotlin.ir

import com.droidcon.kcp.kotlin.utils.warn
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.util.isAnnotationWithEqualFqName
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.ir.visitors.IrVisitorVoid
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class LogMethodCallVisitor(
    val pluginContext: IrPluginContext,
    val messageCollector: MessageCollector
) : IrVisitorVoid() {

    val typeNullableAny = pluginContext.irBuiltIns.anyNType
    val typeUnit = pluginContext.irBuiltIns.unitType

    val funPrintln = pluginContext.referenceFunctions(
        CallableId(
            packageName = FqName("kotlin.io"),
            callableName = Name.identifier("println"),
        )
    )
        .single {
            val parameters = it.owner.valueParameters
            parameters.size == 1 && parameters[0].type == typeNullableAny
        }

    override fun visitElement(element: IrElement) {
        element.acceptChildren(this, null)
    }

    override fun visitFunction(declaration: IrFunction) {
        super.visitFunction(declaration)

        val isAnnotated = declaration.annotations.any {
            it.isAnnotationWithEqualFqName(ANNOTATION_FQNAME)
        }

        if (isAnnotated) {
            messageCollector.warn(
                "LogMethodCallVisitor: visitFunction ${declaration.name}"
            )
            declaration.body = declaration.body?.let { wrapFunction(declaration, it) }
        }
    }

    private fun wrapFunction(
        function: IrFunction,
        body: IrBody
    ): IrBlockBody {
        val blockBody = DeclarationIrBuilder(pluginContext, function.symbol).irBlockBody {
            +logFunStart(function)

            for (statement in body.statements) +statement
            if (function.returnType == typeUnit) {
                +logReturn(function)
            }
        }
        blockBody.transform(DebugLogReturnTransformer(function), null)
        return blockBody
    }

    inner class DebugLogReturnTransformer(
        private val function: IrFunction,
    ) : IrElementTransformerVoidWithContext() {
        override fun visitReturn(expression: IrReturn): IrExpression {
            if (expression.returnTargetSymbol != function.symbol) return super.visitReturn(expression)

            return DeclarationIrBuilder(pluginContext, function.symbol).irBlock {
                val result = irTemporary(expression.value)
                +logReturn(function, irGet(result))
                +expression.apply {
                    value = irGet(result)
                }
            }
        }
    }

    private fun IrBuilderWithScope.logFunStart(
        function: IrFunction
    ): IrCall {
        val concat = irConcat()
        concat.addArgument(irString("⇢ ${function.name}("))
        for ((index, valueParameter) in function.valueParameters.withIndex()) {
            if (index > 0) concat.addArgument(irString(", "))
            concat.addArgument(irString("${valueParameter.name}="))
            concat.addArgument(irGet(valueParameter))
        }
        concat.addArgument(irString(")"))

        return irCall(funPrintln).also { call ->
            call.putValueArgument(0, concat)
        }
    }

    private fun IrBuilderWithScope.logReturn(
        function: IrFunction,
        result: IrExpression? = null
    ): IrCall {
        val concat = irConcat()
        concat.addArgument(irString("⇠ ${function.name}"))

        if (result != null) {
            concat.addArgument(irString(" = "))
            concat.addArgument(result)
        }

        return irCall(funPrintln).also { call ->
            call.putValueArgument(0, concat)
        }
    }

    companion object {
        val ANNOTATION_FQNAME = FqName("com.droidcon.kcp.LogMethod")
    }
}