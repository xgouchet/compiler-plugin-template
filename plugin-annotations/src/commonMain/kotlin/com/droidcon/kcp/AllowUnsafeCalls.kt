package com.droidcon.kcp

@Retention(AnnotationRetention.SOURCE)
@Target(allowedTargets = [AnnotationTarget.EXPRESSION, AnnotationTarget.CLASS, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.FUNCTION])
public annotation class AllowUnsafeCalls()
