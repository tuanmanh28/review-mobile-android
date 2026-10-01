package io.github.tuanmanh28.learn.lesson01.demo

import io.github.tuanmanh28.learn.core.Demo

/**
 * 2. Extension function
 *
 * An extension does NOT modify the original class. The compiler turns it into a static function,
 * with the receiver passed in as the first parameter:
 *
 *   fun String.isAdUnitId(): Boolean      ──►   public static boolean isAdUnitId(String $this$isAdUnitId)
 *
 * Consequence: the extension is chosen by the DECLARED TYPE (static dispatch), not the actual runtime type.
 * View the bytecode: Tools ▸ Kotlin ▸ Show Kotlin Bytecode ▸ Decompile.
 */
object ExtensionDemo : Demo {
    override val id = "D02"
    override val title = "Extension function"
    override val counterpart = "extension"

    fun String.isAdUnitId(): Boolean = matches(Regex("""ca-app-pub-\d{16}/\d{10}"""))

    // Extension property: no backing field, just a getter
    val Double.asUsd: String get() = "$" + ((this * 100).toLong() / 100.0)

    // Extension on a nullable type — can be called even when the receiver is null
    fun String?.orDash(): String = if (this.isNullOrBlank()) "-" else this

    open class AdFormat
    class Banner : AdFormat()

    fun AdFormat.label() = "AdFormat"
    fun Banner.label() = "Banner"

    class Rewarded {
        fun label() = "member"   // a member always wins over an extension with the same signature
    }
    @Suppress("EXTENSION_SHADOWED_BY_MEMBER")
    fun Rewarded.label() = "extension"

    override fun run(): List<String> = buildList {
        add("\"ca-app-pub-3940256099942544/6300978111\".isAdUnitId() = ${"ca-app-pub-3940256099942544/6300978111".isAdUnitId()}")
        add("\"abc\".isAdUnitId() = ${"abc".isAdUnitId()}")
        add("1.2345.asUsd = ${1.2345.asUsd}")

        val missing: String? = null
        add("missing.orDash() = ${missing.orDash()}   // still callable with a null receiver")

        val format: AdFormat = Banner()          // declared type: AdFormat, actual type: Banner
        add("(format: AdFormat = Banner()).label() = ${format.label()}   // static dispatch!")
        add("Banner().label() = ${Banner().label()}")
        add("Rewarded().label() = ${Rewarded().label()}   // member wins")
    }
}
