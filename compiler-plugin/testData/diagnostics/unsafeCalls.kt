// RUN_PIPELINE_TILL: FRONTEND

import com.droidcon.kcp.AllowUnsafeCalls

fun testCheck() {
    val s = (3 * 7 * 9)
    <!CHECK_CALL!>check(s % 11 == 0)<!>
}

@AllowUnsafeCalls
fun testAllowed() {
    val s = (208 + 195)
    check(s % 13 == 0)
}
