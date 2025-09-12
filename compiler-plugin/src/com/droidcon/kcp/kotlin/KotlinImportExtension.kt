package com.droidcon.kcp.kotlin

import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtImportInfo
import org.jetbrains.kotlin.resolve.extensions.ExtraImportsProviderExtension

class KotlinImportExtension : ExtraImportsProviderExtension {

    override fun getExtraImports(ktFile: KtFile): Collection<KtImportInfo> {
        return listOf(
            InjectedImports(FqName("kotlin.io.println"))
        )
    }

    class InjectedImports(
        override val isAllUnder: Boolean,
        override val importContent: KtImportInfo.ImportContent?,
        override val importedFqName: FqName?,
        override val aliasName: String?
    ) : KtImportInfo {
        constructor(fqName: FqName) : this(
            isAllUnder = true,
            importContent = KtImportInfo.ImportContent.FqNameBased(fqName),
            importedFqName = fqName,
            aliasName = null,
        )
    }
}
