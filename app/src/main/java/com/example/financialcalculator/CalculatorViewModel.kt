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
import kotlin.math.abs

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
        .filterNot { it.isWhitespace() || it == ',' }
        .toBigDecimalOrNull()
        .let {
            it?.setScale(6, RoundingMode.HALF_UP)
        }
        .also { Log.d("parsed", this) }
}

fun BigDecimal.toFormatPlainString(): String? {
    val df = DecimalFormat("#,###.######")
    val customSymbol = DecimalFormatSymbols()
    customSymbol.groupingSeparator = ' '
    df.decimalFormatSymbols = customSymbol
    return df.format(this)
}
fun String.toFormat() : String {
    val str : String = if (this.length >= 2 && this[0] == '0' && this[1] != '.') this[0] + "." + this.substring(1, this.lastIndex)
    else this
    return str
}

fun String.isEqualZeroAsNumber() : Boolean = (this.toNumber() != null && this.toNumber()!!.compareTo(
    BigDecimal.ZERO) == 0).also {
    Log.d("isEqualZero", this)
}