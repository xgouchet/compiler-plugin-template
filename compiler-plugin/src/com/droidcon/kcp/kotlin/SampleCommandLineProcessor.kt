package com.droidcon.kcp.kotlin

import com.droidcon.kcp.BuildConfig
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

@Suppress("unused") // Used via reflection.
class SampleCommandLineProcessor : CommandLineProcessor {
    override val pluginId: String = BuildConfig.KOTLIN_PLUGIN_ID

    override val pluginOptions: Collection<CliOption> = listOf(
        CliOption(OPTION_COMPILE_CHECK_AS_ERRORS, "boolean", "Whether compile time checks should raise errors")
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when (option.optionName) {
            OPTION_COMPILE_CHECK_AS_ERRORS ->
                configuration.put(ARG_COMPILE_CHECK_AS_ERRORS, value.toBoolean())

            else ->
                error("Unexpected config option: '${option.optionName}'")
        }
    }

    companion object {
        private const val OPTION_COMPILE_CHECK_AS_ERRORS = "compileTimeChecksAsErrors"
        val ARG_COMPILE_CHECK_AS_ERRORS = CompilerConfigurationKey<Boolean>(OPTION_COMPILE_CHECK_AS_ERRORS)
    }
}
