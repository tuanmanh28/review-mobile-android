package io.github.tuanmanh28.learn.lesson01.demo

import io.github.tuanmanh28.learn.core.Demo

/**
 * 1. Nullable type
 *
 * Kotlin: "can be null" is part of the TYPE. String and String? are two different types.
 * At runtime (JVM), String? is still just a plain reference that may be null —
 * the compiler checks it for you at compile time; there is no wrapper object.
 *
 * Swift: Optional<Wrapped> is a real enum: .none / .some(value).
 */
object NullSafetyDemo : Demo {
    override val id = "D01"
    override val title = "Nullable type"
    override val counterpart = "Optional"

    data class Profile(val nickname: String?)
    data class Account(val profile: Profile?)

    override fun run(): List<String> = buildList {
        val appName: String = "ReviewMobile"
        // val broken: String = null        // ❌ Compile error: Null can not be a value of a non-null type String
        val tag: String? = null
        add("appName.length = ${appName.length}   // String: call it directly, no check needed")

        // ?.  safe call: if null, the whole expression is null
        add("tag?.length = ${tag?.length}")

        // ?:  elvis: default value when the left side is null
        add("tag?.length ?: 0 = ${tag?.length ?: 0}")

        // Safe-call chain — stops at the first null link
        val account: Account? = Account(Profile(nickname = null))
        add("account?.profile?.nickname ?: \"Guest\" = ${account?.profile?.nickname ?: "Guest"}")

        // Smart cast: after the check, the compiler knows the type is non-null
        val email: String? = "user@example.com"
        if (email != null) {
            add("smart cast: email.length = ${email.length}  // no more ?. needed")
        }

        // let: runs the block only when non-null
        email?.let { add("let → sending mail to $it") }

        // !!: "I'm sure it's not null" — if you're wrong, it crashes
        try {
            @Suppress("ALWAYS_NULL")
            add("${tag!!.length}")
        } catch (e: NullPointerException) {
            add("tag!!.length → NullPointerException  ⚠️ avoid !! in real code")
        }

        // Under the hood on the JVM: Int is a primitive, Int? has to be boxed into java.lang.Integer
        val views: Int = 1500
        val maybeViews: Int? = views
        add("Int  → ${views.javaClass.name}")
        add("Int? → ${(maybeViews as Any).javaClass.name}   // boxing: costs an extra object on the heap")
    }
}
