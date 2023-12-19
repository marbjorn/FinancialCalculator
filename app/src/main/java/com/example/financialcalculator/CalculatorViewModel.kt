package com.example.financialcalculator

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.lang.Exception
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.function.BiFunction
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

enum class Operation() {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE
}

class CalculatorViewModel : ViewModel() {
    val num1 = mutableStateOf("0")
    val num2 = mutableStateOf("0")
    val result = mutableStateOf("0")


    fun doOperation(operation : Operation, num1 : String , num2 : String) : String? {
        val number1 = num1.toNumber()
        val number2 = num2.toNumber()

        Log.d("NUM1", number1.toString())
        Log.d("NUM2", number2.toString())

        if (number1 == null
            || number2 == null
            || !validateNumRange(number1)
            || !validateNumRange(number2)) {
            return null;
        }
        val result = when(operation) {
            Operation.ADD -> number1 + number2
            Operation.SUBTRACT -> number1 - number2
            Operation.MULTIPLY -> number1 * number2
            Operation.DIVIDE -> {
                try {
                    number1.div(number2)
                }
                catch (_ : Exception) {
                    return null
                }
            }
        }

        return result.toFormatPlainString()
    }

    fun validateNumRange(num : BigDecimal) : Boolean {
        return (
                num.abs() <= BigDecimal(1000000000000))
    }

    fun validateNumRange(numStr : String) : Boolean {
        val num = numStr.toNumber()
        return (num != null &&
                num.abs() <= BigDecimal(1000000000000))
    }
}

fun String.toNumber() : BigDecimal? {
    return this
        .filterNot { it.isWhitespace() }
        .replace(',', '.')
        .toBigDecimalOrNull()
        .let {
            it?.setScale(6, RoundingMode.HALF_UP)
        }
        .also { Log.d("parsed", this) }
}

fun format(initial : String) : String {
    if (initial.isEmpty()) return initial
    val initialProcessed = initial.filterNot { it.isWhitespace() }
    var str = ""
    var divider : Char? = null
    if (initial.contains('.') && initial.contains(','))
    {
        divider = initial[min(initial.indexOfFirst { it == '.' }, initial.indexOfFirst { it == ',' })]
    }
    else if (initial.contains('.'))
    {
        divider = '.'
    }
    else if (initial.contains(','))
    {
        divider = ','
    }

    val acceptableChars = setOf('-', '.',',')
    val possibleDividers = setOf('.', ',')
    for (i in initialProcessed.indices) {
        if (initialProcessed[i] == '-' && i != 0) continue
        if (divider != null) {
            if (divider != initialProcessed[i] && possibleDividers.contains(initialProcessed[i])) continue
            if (divider == initialProcessed[i] && str.contains(initialProcessed[i])) continue
        }
        if (divider == null && possibleDividers.contains(initialProcessed[i])) continue
        if (!initialProcessed[i].isDigit() && !acceptableChars.contains(initialProcessed[i])) continue
        str += initialProcessed[i]
    }

    var resultStr = ""
    var dividerPos : Int = str.length
    if (initial.contains('.'))
    {
        dividerPos = str.indexOfFirst { it == '.' }
    }
    else if (initial.contains(','))
    {
        dividerPos = str.indexOfFirst { it == ',' }
    }
    var lastSpaceIndex : Int = dividerPos
    for (ch in (dividerPos-1) downTo 0) {
        if (ch + 4 == lastSpaceIndex && str[ch] != '-') {
            resultStr += " "
            lastSpaceIndex = ch + 1
        }
        resultStr += str[ch]
    }
    resultStr = resultStr.reversed()
    if (dividerPos > -1) resultStr += str.subSequence(startIndex = dividerPos, endIndex = str.length)
    return resultStr
}

fun BigDecimal.toFormatPlainString(): String? {
    return format(this.toPlainString())
}

fun String.isEqualZeroAsNumber() : Boolean = (this.toNumber() != null && this.toNumber()!!.compareTo(
    BigDecimal.ZERO) == 0).also {
    Log.d("isEqualZero", this)
}