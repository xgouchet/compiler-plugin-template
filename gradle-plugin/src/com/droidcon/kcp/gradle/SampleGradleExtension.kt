package com.droidcon.kcp.gradle

import org.gradle.api.model.ObjectFactory

open class SampleGradleExtension(objectFactory: ObjectFactory) {
    var compileTimeChecksAsErrors : Boolean = false
}