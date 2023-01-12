package tech.figure.validationoracle.client.test

/**
 * Helpers for Kotest-driven tests.
 *
 * Derived from [provenance-io/loan-package-contracts](https://github.com/provenance-io/loan-package-contracts/blob/dd1ed4fe4b47265472465f64df21c1c154b89bdf/contract/src/test/kotlin/io/provenance/scope/loan/test/KotestHelpers.kt).
 */

import io.kotest.property.Arb
import io.kotest.property.arbitrary.Codepoint
import io.kotest.property.arbitrary.alphanumeric
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Generators of [Arb]itrary instances of classes not defined in the metadata asset model, like primitives or Java classes.
 */
internal object PrimitiveArbs {
    /* Primitives */
    val anyNonEmptyString: Arb<String> = Arb.string().filter { it.isNotBlank() }
    val anyBlankString: Arb<String> = Arb.string().filter { it.isBlank() }
    val anyNonUuidString: Arb<String> = Arb.string().filterNot { it.length == 36 }
    val anyValidUli: Arb<String> = Arb.string(minSize = 23, maxSize = 45, codepoints = Codepoint.alphanumeric())
    val anyNonUliString: Arb<String> = Arb.string().filterNot { it.length in 23..45 }
    val anyDoubleString: Arb<String> = Arb.double().filterNot { double ->
        /** Simple hack to avoid inapplicable edge cases without delving into `arbitrary {}` construction */
        double in listOf(Double.NEGATIVE_INFINITY, Double.NaN, Double.POSITIVE_INFINITY)
    }.toSimpleString()
}

/** Based on [this StackOverflow answer](https://stackoverflow.com/a/25307973). */
internal fun Arb<Double>.toSimpleString(): Arb<String> = map { double ->
    DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH)).apply {
        maximumFractionDigits = 340
    }.format(double)
}
