// WITH_STDLIB

import com.droidcon.kcp.LogMethod

fun box(): String {
    val result = logMePlease(42, "Lorem ipsum dolor sit amet…")
    return "OK"
}

@LogMethod
fun logMePlease(i: Int, s: String): String? {
    return if (i >= 0 && i < s.length) {
        "[" + s.get(i) + "]"
    } else {
        null
    }
}
